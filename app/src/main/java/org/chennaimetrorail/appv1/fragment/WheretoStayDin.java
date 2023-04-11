package org.chennaimetrorail.appv1.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.NearbyplacesAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal.Nearbyplaces;
import org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal.Results;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/27/2017.
 */

public class WheretoStayDin extends Fragment implements OnMapReadyCallback {

    String TAG = "WheretoStayDin";

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    private SlidingUpPanelLayout mLayout;
    RecyclerView wheretostaydin_recyview;
    LinearLayout image_view_back;
    TextView show_list;
    SharedPreferences pref;

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    private GoogleMap mMap;
    private int PROXIMITY_RADIUS = 1500;
    LinearLayout btnRestaurant, btncafe, btnid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.where_to_stay_dine, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");

        } else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

       Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pref = getActivity().getSharedPreferences("stationdetails", 0);




        wheretostaydin_recyview = (RecyclerView) view.findViewById(R.id.wheretostaydin_recyview);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        show_list = (TextView) view.findViewById(R.id.show_list);

        btnRestaurant = (LinearLayout) view.findViewById(R.id.btnRestaurant);
        btncafe = (LinearLayout) view.findViewById(R.id.btncofe);
        btnid = (LinearLayout) view.findViewById(R.id.btnid);

        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);

        title_txt.setText("Places near "+pref.getString("station_longname", null));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        wheretostaydin_recyview.setLayoutManager(layoutManager);
        wheretostaydin_recyview.setNestedScrollingEnabled(false);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });




        mLayout = (SlidingUpPanelLayout)view.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "onPanelSlide, offset " + slideOffset);
                show_list.setText("Hide Places");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.d(TAG, "onPanelStateChanged " + newState);
                if (String.valueOf(newState).equalsIgnoreCase("COLLAPSED")) {
                    show_list.setText("Show Places");
                    //show_list.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_list_black, 0, R.drawable.ic_arrow_drop_down, 0);

                } else {
                    show_list.setText("Hide Places");
                    //show_list.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_list_black, 0, R.drawable.ic_arrow_drop_up, 0);

                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chk = new Internet_connection_checking(getActivity());

                Connection = int_chk.checkInternetConnection();

                if (!Connection) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                else {
                    btnRestaurant.setBackgroundResource(R.drawable.bottomblue_line);
                    btncafe.setBackgroundResource(0);
                    btnid.setBackgroundResource(0);
                    build_retrofit_and_get_response("restaurant");
                }

            }
        });

        btncafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chk = new Internet_connection_checking(getActivity());

                Connection = int_chk.checkInternetConnection();

                if (!Connection) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                else {
                    btnRestaurant.setBackgroundResource(0);
                    btncafe.setBackgroundResource(R.drawable.bottomblue_line);
                    btnid.setBackgroundResource(0);
                    build_retrofit_and_get_response("cafe");
                }
            }
        });


        btnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_chk = new Internet_connection_checking(getActivity());

                Connection = int_chk.checkInternetConnection();

                if (!Connection) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                else {
                    btnRestaurant.setBackgroundResource(0);
                    btncafe.setBackgroundResource(0);
                    btnid.setBackgroundResource(R.drawable.bottomblue_line);
                    build_retrofit_and_get_response("lodging");
                }
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
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {

            mMap = googleMap;



            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(pref.getString("station_latitude", null)),Double.valueOf(pref.getString("station_longitude", null)))).title(pref.getString("station_longname", null) + " Station").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
            build_retrofit_and_get_response("restaurant");



        }

    }

    private void build_retrofit_and_get_response(final String type) {


        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final ApiInterface apiInterface = ApiClient.getResponse().create(ApiInterface.class);
        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Call<Nearbyplaces> call = apiInterface.getNearbyPlaces(type, pref.getString("station_latitude", null) + "," + pref.getString("station_longitude", null), Constant.strApiKey,info.versionName);

        // show it
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Nearbyplaces>() {

            @Override
            public void onResponse(Call<Nearbyplaces> call, Response<Nearbyplaces> response) {
                try {
                    int statusCode = response.code();

                    Log.d("wheretostay",call.request().url().toString());
                    if(statusCode==200){
                        progressDoalog.dismiss();

                        mMap.clear();
                        Log.d("chekck", "" + response.body().getStatus());
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(pref.getString("station_latitude", null)),Double.valueOf(pref.getString("station_longitude", null)))).title(pref.getString("station_longname", null) + " Station").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));

                        List<Results> results = response.body().getResults();

                        wheretostaydin_recyview.setAdapter(new NearbyplacesAdapter(results, R.layout.custome_nearest_places, Double.valueOf(pref.getString("station_latitude", null)),Double.valueOf(pref.getString("station_longitude", null)), getActivity()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(pref.getString("station_latitude", null)),Double.valueOf(pref.getString("station_longitude", null)))));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        // This loop will go through all the results and add marker on each location.
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                            String placeName = response.body().getResults().get(i).getName();
                            String vicinity = response.body().getResults().get(i).getVicinity();
                            LatLng latLng = new LatLng(lat, lng);
                            // Adding colour to the marker
                            if(type.equalsIgnoreCase("restaurant")){
                                mMap.addMarker(new MarkerOptions().position(latLng).title(placeName + " : " + vicinity).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_resturantmarker)));

                            }else if(type.equalsIgnoreCase("cafe")){
                                mMap.addMarker(new MarkerOptions().position(latLng).title(placeName + " : " + vicinity).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cofemarker)));

                            }else {
                                mMap.addMarker(new MarkerOptions().position(latLng).title(placeName + " : " + vicinity).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dinmarker)));

                            }


                            // move map camera

                        }

                    }else {
                        progressDoalog.dismiss();
                        build_retrofit_and_get_response(type);
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                    progressDoalog.dismiss();
                    build_retrofit_and_get_response(type);
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Nearbyplaces> call, Throwable t) {
                Log.d("onFailure", t.toString());
                progressDoalog.dismiss();
                build_retrofit_and_get_response(type);
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }

        });

    }

}
