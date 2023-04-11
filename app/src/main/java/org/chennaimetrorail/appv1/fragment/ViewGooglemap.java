package org.chennaimetrorail.appv1.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.StationLatitudeandLongitude;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationdetailsmodel;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by 102525 on 3/20/2018.
 */

public class ViewGooglemap extends Fragment implements OnMapReadyCallback {
    String TAG = "ViewGooglemap";

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    RecyclerView wheretostaydin_recyview;
    LinearLayout image_view_back;
    TextView show_list;
    SharedPreferences pref;

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    LinearLayout btnRestaurant, btncafe, btnid;
    private GoogleMap mMap;
    private int PROXIMITY_RADIUS = 1500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.viewgoogle_map, container, false);

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            getActivity().finish();
        } else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }


        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pref = getActivity().getSharedPreferences("stationdetails", 0);


        Log.d("TAG", "Checkdetails" + Constant.stationLatitudeandLongitude.size());


        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);

        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);

        title_txt.setText(pref.getString("station_longname", null) + " Location");

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationDetails());
                fragmentTransaction.commit();
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new StationDetails());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int_chk = new Internet_connection_checking(getActivity());

        Connection = int_chk.checkInternetConnection();

        if (!Connection) {
            Log.d("Network Checking", "Oops!\\nThere was a problem connecting to the internet. Please check your connection");
        } else {

            mMap = googleMap;
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(pref.getString("station_latitude", null)), Double.valueOf(pref.getString("station_longitude", null)))).title(pref.getString("station_longname", null) + " Station").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(pref.getString("station_latitude", null)), Double.valueOf(pref.getString("station_longitude", null))), 18.0f));

            // addCircleToMap();
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = null;
            try {
                info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Constant.stationLatitudeandLongitude.clear();
            final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
            final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
            /*Send Parameters to the Api*/
            Call<Stationdetailsmodel> call = apiService.getstationdetails(pref.getString("station_id", null), Constant.strApiKey, info.versionName);

            call.enqueue(new Callback<Stationdetailsmodel>() {
                @Override
                public void onResponse(Call<Stationdetailsmodel> call, Response<Stationdetailsmodel> response) {
                    int statusCode = response.code();

                    Log.d(TAG,call.request().url().toString());

                    if (statusCode == 200) {


                        List<StationLatitudeandLongitude> stationLatitudeandLongitude = response.body().getStationLatitudeandLongitude();
                        loader.dismiss();

                        if (stationLatitudeandLongitude.size() != 0) {
                            ArrayList<LatLng> coordList = new ArrayList<LatLng>();

                            for (int i = 0; i < stationLatitudeandLongitude.size() - 1; i++) {
                                coordList.add(new LatLng(Double.parseDouble(stationLatitudeandLongitude.get(i).getLatitude()), Double.parseDouble(stationLatitudeandLongitude.get(i).getLongitude())));
                                Log.d("TAG", "arrayse" + stationLatitudeandLongitude.get(i).getLatitude());
                            }


                            Polygon polygon = mMap.addPolygon(new PolygonOptions().addAll(coordList).strokeColor(Color.parseColor("#4270C5")).strokeWidth(4).fillColor(Color.parseColor("#A6BCCF")).strokeWidth(2));


                        }


                    } else if (statusCode == 400) {
                        Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        loader.dismiss();
                    } else {
                        Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        loader.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Stationdetailsmodel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("error", t.toString());
                    loader.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }
            });


        }

    }

    private void addCircleToMap() {

        // circle settings
        int radiusM = 80;// your radius in meters
        double latitude = Double.valueOf(pref.getString("station_latitude", null));
        double longitude = Double.valueOf(pref.getString("station_longitude", null));// your center longitude
        LatLng latLng = new LatLng(latitude, longitude);

        // draw circle
        int d = 500; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.bd_options_button_click));
        c.drawCircle(d / 2, d / 2, d / 2, p);

        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

// mapView is the GoogleMap
        mMap.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(latLng, radiusM * 2, radiusM * 2).
                transparency(0.4f));
    }

}