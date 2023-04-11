package org.chennaimetrorail.appv1.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.GPSTracker;
import org.chennaimetrorail.appv1.SingleShotLocationProvider;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.NerestStationAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.NeaerstStationList;
import org.chennaimetrorail.appv1.modal.jsonmodal.StationList;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 8/28/2017.
 */

public class NearestMetro extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    String TAG = "NearestMetro";
    RecyclerView nearestmetro_recyview;
    LinearLayout image_view_back;
    private SlidingUpPanelLayout mLayout;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    //GPSTracker gps;
    TextView show_list,near_errornote;
    LinearLayout fadin;



    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    public static NearestMetro newInstance() {
        NearestMetro fragment = new NearestMetro();
        return fragment;
    }

    public NearestMetro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nearest_metor, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        show_list = (TextView) view.findViewById(R.id.show_list);
        fadin = (LinearLayout) view.findViewById(R.id.fadin);
        nearestmetro_recyview = (RecyclerView) view.findViewById(R.id.nearestmetro_recyview);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Nearest Metro Station");

        near_errornote= view.findViewById(R.id.near_errornote);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        nearestmetro_recyview.setLayoutManager(layoutManager);
        nearestmetro_recyview.setNestedScrollingEnabled(false);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

            }
        });

        mLayout = (SlidingUpPanelLayout)view.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                show_list.setText("Hide Stations");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (String.valueOf(newState).equalsIgnoreCase("COLLAPSED")) {
                    show_list.setText("Show Stations ");

                } else {
                    show_list.setText("Hide Stations ");

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

                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.toolbrtxt_layout.setVisibility(View.GONE);
                    home.toolbar_img.setVisibility(View.VISIBLE);
                    home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

                    return true;
                }
                return false;
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        int_chk = new Internet_connection_checking(getActivity());

        Connection = int_chk.checkInternetConnection();

        if (!Connection) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            // create class object
            /*gps = new GPSTracker(getActivity());
            // check if GPS enabled
            if (gps.canGetLocation()) {
                if(gps.getLongitude()!=0.0){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    nearestMetroDetails(latitude, longitude);
                }else {
                    onResume();
                }
            } else {
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    nearestMetroDetails(latitude, longitude);
                }
            }*/

            if (getContext()!=null){
                new SingleShotLocationProvider().requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        double latitude = location.latitude;
                        double longitude = location.longitude;
                        nearestMetroDetails(latitude, longitude);
                    }
                });
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    public void nearestMetroDetails(final double lat, final double lon) {
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        Call<NeaerstStationList> call = apiService.getNearestMetro(lat, lon, Constant.strApiKey, info.versionName);

        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<NeaerstStationList>() {
            @Override
            public void onResponse(Call<NeaerstStationList> call, Response<NeaerstStationList> response) {
                int statusCode = response.code();
                Log.d(TAG, "statusCode"+statusCode);

                Log.d(TAG,call.request().url().toString());

                if (statusCode == 200) {
                        progressDoalog.dismiss();
                        List<StationList> stationList = response.body().getStation_list();
                        if(stationList.size()!=0) {
                            near_errornote.setVisibility(View.GONE);
                            mLayout.setVisibility(View.VISIBLE);
                            nearestmetro_recyview.setAdapter(new NerestStationAdapter(lat,lon,stationList, R.layout.custome_nearestmetro, getActivity()));
                            mMap.clear();
                            LatLng coordinate = new LatLng(lat, lon);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Your location").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 8));
                            //gps.stopUsingGPS();
                            //Store these lat lng values somewhere. These should be constant.
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 11);
                            mMap.animateCamera(location);
                            if (stationList.size()!=0) {
                                for (int i = 0; i < stationList.size(); i++) {
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    double lat = Double.parseDouble(stationList.get(i).getStation_latitude());
                                    double lng = Double.parseDouble(stationList.get(i).getStation_longitude());
                                    String placeName = stationList.get(i).getStation_longName();
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(placeName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                }
                            }
                        }else {
                            near_errornote.setVisibility(View.VISIBLE);
                            mLayout.setVisibility(View.GONE);
                        }


                }else if(statusCode==400){
                    progressDoalog.dismiss();
                    near_errornote.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }else {
                    progressDoalog.dismiss();
                    near_errornote.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<NeaerstStationList> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressDoalog.dismiss();
                near_errornote.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(),
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                       // Toast.makeText(getActivity(), "Location enabled by user!", Toast.LENGTH_LONG).show();
                        onResume();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(getActivity(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
