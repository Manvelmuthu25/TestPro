package org.chennaimetrorail.appv1.travelcab;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.modal.book_a_cab.Bookcab_modals;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcard.ChangePassword;
import org.chennaimetrorail.appv1.travelcard.TravelcardLogin;
import org.chennaimetrorail.appv1.travelcard.travelcardadapter.TravalcardListAdapter;


import java.security.PublicKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.support.constraint.Constraints.TAG;


public class TravelCabs extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    LinearLayout toolbar_menu;
    TextView menu_textview;
    private Boolean exit = false;
    private boolean isDebuggable = true;

    String version, menuitemstatuscode;

    String token, url;

    private MTWebview webView;
    private ViewGroup viewGroup;
    public static final int FASTEST_MIN_LOCATION_TIME = 1000 * 1;// 15 seconds
    public static final int MIN_LOCATION_TIME = 1000 * 1;// 30 seconds
    public static final int DEFAULT_LOCATION_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public String[] REQUIRED_ALL_PERMISSIONS;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();
        int_chk = new Internet_connection_checking(getActivity());

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_cabs, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FontStyle fontStyle = new FontStyle();

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fontStyle.Changeview(getActivity(), view);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

        webView = (MTWebview) view.findViewById(R.id.webview);
        menuitemstatuscode = pref.getString("menuitemstatuscode", null);
        Connection = int_chk.checkInternetConnection();
        /*Check Internet Connection Connect Or Not*/
        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {

            /*Call Validate login parameters method..*/
            // Toast.makeText(getContext(),menuitemstatuscode, Toast.LENGTH_LONG).show();

           /* Log.e("statusCode1", "" + pref.getString("username", null));
            Log.e("statusCode2", "" + pref.getString("login_name", null));
            Log.e("statusCode3", "" + pref.getString("gender", null));
            Log.e("statusCode4", "" + pref.getString("registeredmobile", null));
            Log.e("statusCode5", "" + pref.getString("dob", null));
*/

            viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            initView();
            book(pref.getString("username", null), pref.getString("password", null), pref.getString("securitycode", null));
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();



        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {



                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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




        menu_textview = (TextView) view.findViewById(R.id.menu_textview);
        menu_textview.setText("Book Your Ride");
        menu_textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        menu_textview.setTypeface(fontStyle.helveticabold_CE);
        toolbar_menu = (LinearLayout) view.findViewById(R.id.toolbar_menu);
        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        return view;

    }


    private void book(String username, String password, String securitycode) {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Cechjald", "FireRefreshedtoken: " + refreshedToken);
      //  Call<Bookcab_modals> call = apiService.getbookcab(username, password, Constant.strApiKey, refreshedToken, version);

        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",password);
        values.addProperty("secretcode", Constant.strApiKey);
        values.addProperty("tokenid",refreshedToken);
        values.addProperty("appv","ANDROID|"+version);
        Call<Bookcab_modals> call = apiService.getbookcab(values);

        call.enqueue(new Callback<Bookcab_modals>() {

            @Override
            public void onResponse(Call<Bookcab_modals> call, retrofit2.Response<Bookcab_modals> response) {
                int statusCode = response.code();

                if (statusCode == 200) {

                    if (response.isSuccessful()) {
                        Log.d("bookcab", "" + statusCode);
                        token = response.body().getJwttoken();

                        url = response.body().getUrl();

                        Log.d("bookkkkcab1", "" + url);
                        Log.d("bookkkkcab2", "" + token);

                        checkAllPermissionsAndGPSAlert();


                    }
                    // mSwipeRefreshLayout.setRefreshing(false);

                } else if (statusCode == 400) {

                    //Toast.makeText(getActivity(), "No Records Founded..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Bookcab_modals> call, Throwable t) {

            }
        });
    }




    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.cabpopup, null);
        FontStyle fontStyle = new FontStyle();
        ;
        fontStyle.Changeview(getActivity(), popupView);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        TextView t_sign_out = (TextView) popupView.findViewById(R.id.t_sign_out);
        TextView t_change_password = (TextView) popupView.findViewById(R.id.change_passwordtxt);

        t_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_place, new ChangePassword());
                fragmentTransaction.commit();
                popupWindow.dismiss();
            }
        });
        t_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
                popupWindow.dismiss();
                editor.putString("securitycode", null);
                editor.putString("username", null);
                editor.putString("password", null);
                editor.putString("login_name", null);
                editor.putString("dob", null);
                editor.putString("gender", null);
                editor.putString("registeredmobile", null);
                editor.putString("email", null);
                editor.putString("m_pinLoginState", "no");
                editor.putString("menuitemstatuscode", "2");
                editor.apply();
            }
        });
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }


    private void initView() {


        activateWebContentDebugFeature(webView);


        REQUIRED_ALL_PERMISSIONS = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getActivity().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
    }


    private void loadUrl() {

       //Direct cab link call Hard code  URL
       // String wburl = "https://transit.nuvizzards.com/deliverit/transitrider/?companyCode=cmrlqa2#/";//Direct cab link call iTech URL


        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Authorization", "Bearer " + token);
        //Direct cab link call API URL only
        webView.loadUrl(url, extraHeaders);
        Log.d("botokentoken", "" + url + extraHeaders);

        //Direct cab link call Hard code  URL

        /*webView.loadUrl(wburl,extraHeaders);
        Log.d("botokentoken", "" +wburl+ extraHeaders); */
    }

    public void activateWebContentDebugFeature(WebView webview){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isDebuggable) {
                if (0 != getActivity().getApplicationInfo().flags && 0 != ApplicationInfo.FLAG_DEBUGGABLE) {
                    webview.setWebContentsDebuggingEnabled(true);
                }else {
                    Toast.makeText(getActivity(),"Please make sure USB debugging is enabled for your device!",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

/*
    @Override
    public void onResume() {
        super.onResume();

        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
//        checkAllPermissionsAndGPSAlert();
    }*/

    private boolean isGpsRequired() {
        LocationManager locManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean gpsSwitchedOn = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return (!gpsSwitchedOn);
    }


    private void checkAllPermissionsAndGPSAlert() {
        //Asking for Mandatory Permissions
        if (isGpsRequired()) {
            initGoogleAPIClient();//Init Google API Client//Enable GPS POPUP show
            checkPermissions();//Check Permission//Enable GPS POPUP show


        } else {
            loadUrl();
        }
    }


    //Enable GPS POPUP show

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else
            showSettingDialog();

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        updateGPSStatus("GPS is Enabled in your device");
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;


                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        break;

                    case RESULT_CANCELED:

                       /* LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        // Check if enabled and if not send user to the GPS settings
                        if (!enabled) {
                            Toast.makeText(getContext(), "Please turn on the location to book a cab.", Toast.LENGTH_LONG).show();
                        }*/
                        Log.e("Settings", "Result Cancel");
                        updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS

        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_gps, Snackbar.LENGTH_LONG).setAction("Action", null).show();

           /* final Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_gps, Snackbar.LENGTH_INDEFINITE);

            snackBar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();
*/
            //snackBar.setActionTextColor(R.color.colorPrimary);
           // Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_gps, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
           // Toast.makeText(getContext(), "Please turn on the location to book a cab.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            getContext().unregisterReceiver(gpsLocationReceiver);
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    updateGPSStatus("GPS is Enabled in your device");

                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");

                }

            }


        }
    };

    //Method to update GPS status text
    private void updateGPSStatus(String status) {



        book(pref.getString("username", null), pref.getString("password", null), pref.getString("securitycode", null));

    }


    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                        Toast.makeText(getContext(), "Location Permission denied.", Toast.LENGTH_LONG).show();
                    } else
                        showSettingDialog();


                } else {
                    updateGPSStatus("Location Permission denied.");
                    Toast.makeText(getContext(), "Location Permission denied.", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }




}
