package org.chennaimetrorail.appv1.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.google.android.gms.maps.GoogleMap;
import com.jsibbold.zoomage.ZoomageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FloatingWidgetService;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.GPSTracker;
import org.chennaimetrorail.appv1.SingleShotLocationProvider;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.StationDetailAdapter;
import org.chennaimetrorail.appv1.adapter.StationdetailsmapAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationdetailsmodel;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationfacilities;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationmapdetails;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 102525 on 7/12/2017.
 */

public class StationDetails extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{



    String TAG= "StationDetails";
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    Dialog dialog;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    TextView station_title, station_discription;
    SharedPreferences pref;
    FragmentManager fm;
    RecyclerView facility_recy, facilitymap_recy;
    FragmentTransaction fragmentTransaction;
    ImageView zoom_img, zoom_imgbtn, start_ride_btn, viewgooglemap_btn;
    String station_images_tmp;


    String station_image_url;
    String version;
    View view;
    //GPSTracker gps;
    Bitmap image;
    private List<Stationfacilities> stationdetailsModals_array = new ArrayList<>();


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 200;

    public static StationDetails newInstance() {
        StationDetails fragment = new StationDetails();
        return fragment;
    }

    public StationDetails() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.station_details, container, false);


        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);


        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        pref = getActivity().getSharedPreferences("stationdetails", 0);

        station_title = (TextView) view.findViewById(R.id.station_d_title);
        station_title.setText(pref.getString("station_longname", null));
        station_title.setTypeface(fontStyle.helveticabold_CE);

        facility_recy = (RecyclerView) view.findViewById(R.id.facility_recy);
        RecyclerView.LayoutManager facility_recymLayoutManager = new GridLayoutManager(getActivity(), 4);
        facility_recy.setLayoutManager(facility_recymLayoutManager);
        facility_recy.setItemAnimator(new DefaultItemAnimator());
        facilitymap_recy = (RecyclerView) view.findViewById(R.id.facilitymap_recy);
        RecyclerView.LayoutManager facilitymap_recymLayoutManager = new GridLayoutManager(getActivity(), 2);
        facilitymap_recy.setLayoutManager(facilitymap_recymLayoutManager);
        facilitymap_recy.setItemAnimator(new DefaultItemAnimator());


        int_chk = new Internet_connection_checking(getActivity());


        Connection = int_chk.checkInternetConnection();

        if (!Connection) {


            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            GetStationFacilityDetails();
        }


        zoom_img = (ImageView) view.findViewById(R.id.zoom_img);
        start_ride_btn = (ImageView) view.findViewById(R.id.start_ride_btn);
        viewgooglemap_btn = (ImageView) view.findViewById(R.id.viewgooglemap_btn);

        zoom_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Initiate Popup view **/


                int_chk = new Internet_connection_checking(getActivity());


                Connection = int_chk.checkInternetConnection();

                if (!Connection) {

                    Constant.station_image_url = "";
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    if (!Constant.station_image_url.equals("")) {

                        ShowImageview(Constant.station_image_url);
                    }

                }

            }
        });



        /*start_ride_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE );

                if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

                    new SingleShotLocationProvider().requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                            double latitude = location.latitude;
                            double longitude = location.longitude;

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + pref.getString("station_latitude", null) + "," + pref.getString("station_longitude", null) + ""));
                            startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {
                                //If the draw over permission is not available open the settings screen
                                //to grant the permission.
                                Intent intentt = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                                startActivityForResult(intentt, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                                //gps.stopUsingGPS();
                            } else {
                                //If permission is granted start floating widget service
                                startFloatingWidgetService();
                            }
                        }
                    });

                }else {
                    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(StationDetails.this)
                            .addOnConnectionFailedListener(StationDetails.this).build();
                    mGoogleApiClient.connect();
                }

                gps = new GPSTracker(getActivity());
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    if(gps.getLongitude()!=0.0){
                        gps.stopUsingGPS();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + gps.getLatitude() + "," + gps.getLongitude() + "&daddr=" + pref.getString("station_latitude", null) + "," + pref.getString("station_longitude", null) + ""));
                        startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {
                            //If the draw over permission is not available open the settings screen
                            //to grant the permission.
                            Intent intentt = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                            startActivityForResult(intentt, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                           //gps.stopUsingGPS();
                        } else {
                            //If permission is granted start floating widget service
                            startFloatingWidgetService();
                        }
                    }

                } else {
                    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(StationDetails.this)
                            .addOnConnectionFailedListener(StationDetails.this).build();
                    mGoogleApiClient.connect();

                }*//*


            }
        });*/

        viewgooglemap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.fragment_place, new ViewGooglemap());
                fragmentTransaction.commit();
            }
        });
        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Station Details");
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

        return view;

    }

    /*Attempt To GetStation Facility Process to Server........*/
    private void GetStationFacilityDetails() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Constant.stationLatitudeandLongitude.clear();
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*Send Parameters to the Api*/
        Call<Stationdetailsmodel> call = apiService.getstationdetails(pref.getString("station_id", null),
                Constant.strApiKey, version);
        Log.d("idvaluesid",""+pref.getString("station_id", null));
        Log.d("idvalueskey",""+Constant.strApiKey);
        Log.d("idvaluesver",""+version);


        call.enqueue(new Callback<Stationdetailsmodel>() {
            @Override
            public void onResponse(Call<Stationdetailsmodel> call, Response<Stationdetailsmodel> response) {
                int statusCode = response.code();

                Log.d("stationLatit",""+call.request().url().toString());

                if (statusCode == 200) {

                    if (response.body() != null) {
                        station_images_tmp = response.body().getImage_temp();
                        List<Stationfacilities> sation = response.body().getStationfacilities();
                        List<Stationmapdetails> stationmapdetails = response.body().getStationmapdetails();
                        Log.d("TAGid", "Checkdetails" + response.body().getStationfacilities());
                        Constant.stationLatitudeandLongitude = response.body().getStationLatitudeandLongitude();
                        Log.d("TAGww", "Checkdetails" + Constant.stationLatitudeandLongitude.size());
                        Constant.station_image_url = response.body().getImage();
                        Log.d("imagesss", "" +  response.body().getImage());

                        facility_recy.setAdapter(new StationDetailAdapter(sation, R.layout.station_details_custome, getActivity()));
                        facilitymap_recy.setAdapter(new StationdetailsmapAdapter(stationmapdetails, getActivity()));
                        if (station_images_tmp != "") {
                            //   loader.dismiss();
                            Picasso.with(getActivity()).load(station_images_tmp).resize(1500, 2200).into(zoom_img);


                        }
                    }
                    loader.dismiss();
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


    public void ShowImageview(String img_url) {
        dialog = new Dialog(getActivity());
        dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.image_popup);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        loader.show();


        ZoomageView myZoomageViewstation = (ZoomageView) dialog.findViewById(R.id.myZoomageViewstation);
        Glide.with(getContext())
                .load(img_url)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loader.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        loader.dismiss();
                        return false;
                    }
                })

                .into(myZoomageViewstation);


        dialog.setCancelable(false);
        isShown = true;

        ImageView cloase_btn = (ImageView) dialog.findViewById(R.id.ic_close_btn);
        cloase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        getActivity().startService(new Intent(getActivity(), FloatingWidgetService.class));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else {
                //Permission is not available then display toast
                //Toast.makeText(getActivity(), getResources().getString(R.string.draw_other_app_permission_denied), Toast.LENGTH_SHORT).show();
            }


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


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}