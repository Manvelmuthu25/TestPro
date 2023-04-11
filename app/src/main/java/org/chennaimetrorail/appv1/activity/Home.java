package org.chennaimetrorail.appv1.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.nextgen.microsdlib.MicroSdHandler;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

import org.chennaimetrorail.appv1.SingarChennaiCard;
import org.chennaimetrorail.appv1.SingleShotLocationProvider;
import org.chennaimetrorail.appv1.fragment.Singarachennaicard;
import org.chennaimetrorail.appv1.fragment.Sugarbox;
import org.chennaimetrorail.appv1.travelcab.Booking;
import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FloatingWidgetService;
import org.chennaimetrorail.appv1.GPSTracker;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.database.DatabaseHandler;
import org.chennaimetrorail.appv1.fragment.ContactUs;
import org.chennaimetrorail.appv1.fragment.CulturalCenter;
import org.chennaimetrorail.appv1.fragment.EditMenu;
import org.chennaimetrorail.appv1.fragment.FacilitesforDisabled;
import org.chennaimetrorail.appv1.fragment.FeaderService;
import org.chennaimetrorail.appv1.fragment.Feedback;
import org.chennaimetrorail.appv1.fragment.InstructiontoCommuters;
import org.chennaimetrorail.appv1.fragment.LostFound;
import org.chennaimetrorail.appv1.fragment.MetronetInformation;
import org.chennaimetrorail.appv1.fragment.NearestMetro;
import org.chennaimetrorail.appv1.fragment.NearestMetroRouteDirection;
import org.chennaimetrorail.appv1.fragment.Notifications;
import org.chennaimetrorail.appv1.fragment.OtherInfo;
import org.chennaimetrorail.appv1.fragment.StationDetails;
import org.chennaimetrorail.appv1.fragment.StationInfo;
import org.chennaimetrorail.appv1.fragment.TourGuid;
import org.chennaimetrorail.appv1.fragment.TravelPlanner;
import org.chennaimetrorail.appv1.fragment.WeatherReport;
import org.chennaimetrorail.appv1.modal.jsonmodal.CurrentNewsResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.NewsNotificationList;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Login;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcab.FreeDiscountRide;
import org.chennaimetrorail.appv1.travelcab.TravelCabs;
import org.chennaimetrorail.appv1.travelcab.freeridelivefile;
import org.chennaimetrorail.appv1.travelcard.AddTravelCard;
import org.chennaimetrorail.appv1.travelcard.ChangePassword;
import org.chennaimetrorail.appv1.travelcard.Encryption;
import org.chennaimetrorail.appv1.travelcard.ForgotPassword;
import org.chennaimetrorail.appv1.travelcard.TRegister;
import org.chennaimetrorail.appv1.travelcard.TravelCardList;
import org.chennaimetrorail.appv1.travelcard.TravelcardLogin;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;


public class Home extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 198;


    public Toolbar toolbar;
    public RelativeLayout home_relative;
    public LinearLayout toolbrtxt_layout, current_locatinlayout;
    public ImageView toolbar_img;
    public ImageView drawer_open, addmenu1_img, addmenu2_img, addmenu3_img, addmenu4_img;
    public TextView version_txt, station_inftext, rt_infotext, menu_txt, editmenu, emv_txt, transit_txt, flash_txt, addmenu1_txt, addmenu2_txt, addmenu3_txt, addmenu4_txt, cmrl_title;
    public LinearLayout rout_info, station_btn, contact_btn, emv_btn, transit_btn, w_line, addmenu1, addmenu2, addmenu3, addmenu4;
    String TAG = "Home";

    /*Activity initialize*/
    DrawerLayout drawer;
    ImageView emv_bg, transit_bg;
    Button img;
    List<NewsNotificationList> newsNotificationLists;

    /*Fragment transaction*/
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    /*microsdhandler*/
    MicroSdHandler microSdHandler;
    /*loacl storage*/
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Handler handler;
    Runnable myRunnable;
    DatabaseHandler db;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    Typeface helveticabold_CE;

    SharedPreferences pref;
    /*Weatherdetails text and icons*/
    TextView current_loc_txt, temp_txt, temp_discri, weahtetxt;
    ImageView weather_ic, weatherimg;
    String menuFragmentdirec, menuFragment;
    //GPSTracker gps;
    FloatingWidgetService floatingWidgetService;
    String version,device_id;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        /** FloatingWidgetService stopSelf
         * */
        floatingWidgetService = new FloatingWidgetService();
        floatingWidgetService.stopSelf();

        /** Font Style set action
         * */
        TypefaceCollection typeface = new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaCE-Cond.ttf")).create();
        TypefaceHelper.init(typeface);
        typeface(this);
        Typeface helveticaCE = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaCE-CondBold.ttf");
        helveticabold_CE = Typeface.create(helveticaCE, Typeface.BOLD);

        /*Initialize*/
        home_relative = (RelativeLayout) findViewById(R.id.home_relative);
        emv_btn = (LinearLayout) findViewById(R.id.emv_btn);
        transit_btn = (LinearLayout) findViewById(R.id.transit_btn);
        emv_bg = (ImageView) findViewById(R.id.emv_btn_bg);
        transit_bg = (ImageView) findViewById(R.id.transit_btn_bg);
        rout_info = (LinearLayout) findViewById(R.id.rout_info);
        station_btn = (LinearLayout) findViewById(R.id.station_btn);
        w_line = (LinearLayout) findViewById(R.id.w_line);
        addmenu1 = (LinearLayout) findViewById(R.id.addmenu1);
        addmenu2 = (LinearLayout) findViewById(R.id.addmenu2);
        addmenu3 = (LinearLayout) findViewById(R.id.addmenu3);
        addmenu4 = (LinearLayout) findViewById(R.id.addmenu4);
        addmenu1_img = (ImageView) findViewById(R.id.addmenu1_img);
        addmenu2_img = (ImageView) findViewById(R.id.addmenu2_img);
        addmenu3_img = (ImageView) findViewById(R.id.addmenu3_img);
        addmenu4_img = (ImageView) findViewById(R.id.addmenu4_img);
        weather_ic = (ImageView) findViewById(R.id.weather_ic);
        version_txt = (TextView) findViewById(R.id.version_txt);
        weahtetxt = findViewById(R.id.weahtetxt);
        weatherimg = findViewById(R.id.weatherimg);
        addmenu1_txt = (TextView) findViewById(R.id.addmenu1_txt);
        addmenu2_txt = (TextView) findViewById(R.id.addmenu2_txt);
        addmenu3_txt = (TextView) findViewById(R.id.addmenu3_txt);
        addmenu4_txt = (TextView) findViewById(R.id.addmenu4_txt);
        rt_infotext = (TextView) findViewById(R.id.rt_infotext);
        station_inftext = (TextView) findViewById(R.id.station_inftext);
        emv_txt = (TextView) findViewById(R.id.emv_txt);
        transit_txt = (TextView) findViewById(R.id.transit_txt);
        emv_txt = (TextView) findViewById(R.id.emv_txt);
        transit_txt = (TextView) findViewById(R.id.transit_txt);
        temp_txt = (TextView) findViewById(R.id.temp_txt);
        current_locatinlayout = findViewById(R.id.current_locatinlayout);
        current_loc_txt = (TextView) findViewById(R.id.current_loc_txt);
        editmenu = (TextView) findViewById(R.id.edit_menu);
        editmenu.setTypeface(helveticaCE);
        flash_txt = (TextView) findViewById(R.id.flash_txt);
        flash_txt.setSelected(true);
        temp_discri = (TextView) findViewById(R.id.temp_discri);
        cmrl_title = (TextView) findViewById(R.id.title_tx);
        //Toolbar Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbrtxt_layout = toolbar.findViewById(R.id.toolbrtxt_layout);
        toolbar_img = (ImageView) toolbar.findViewById(R.id.toolbar_img);
        //Drawer open close action
        drawer_open = (ImageView) findViewById(R.id.drawer_open);
        cmrl_title.setTypeface(null, Typeface.BOLD);
        editmenu.setTypeface(null, Typeface.BOLD);

        /*Get App Version Name*/
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            version_txt.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*Initialize DB*/
        db = new DatabaseHandler(this);
        drawer_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                } else {
                    drawer.openDrawer(Gravity.START);
                    hideKeyboard(Home.this);
                }
            }
        });


        // Define drawer layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.closeDrawer(GravityCompat.START);
        toggle.syncState();
        //Navigation view Initialize
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Create local storage
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editor.putString("emv_active", "0");
        editor.putString("transit_active", "0");
        editor.apply();


    }


    @Override
    public void onStart() {
        super.onStart();

        /*Check GPS */
        //gps = new GPSTracker(Home.this);

        rout_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelPlanner());
                fragmentTransaction.commit();

            }
        });
        station_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new StationInfo());
                fragmentTransaction.commit();
            }
        });
        editmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new EditMenu());
                fragmentTransaction.commit();

            }
        });

        /*IMportant------------EMV USING NFC Concepts-------------------------------------------------->*/
        handler = new Handler(Looper.getMainLooper());
        myRunnable = new Runnable() {
            @Override
            public void run() {

                /*editor.putString("transit_active", "0");
                editor.putString("emv_active", "0");
                editor.apply();
                microSdHandler.deactivateCard();    //Deactivate Card
                microSdHandler.closeChannel();
                emv_txt.setText(getResources().getString(R.string.enable_emv));
                emv_bg.setBackgroundResource(R.drawable.circle_blue);
                transit_txt.setText(getResources().getString(R.string.enable_transit));
                transit_bg.setBackgroundResource(R.drawable.circle_blue);*/
            }
        };

        emv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                NearestMetro nearestMetro = new NearestMetro();
                fragmentTransaction.replace(R.id.fragment_place, nearestMetro);
                fragmentTransaction.commit();

                /*IMportant-------------------------------------------------------------->*/

                /*NFC CARD ACTIVATE DE-ACTIVATE OPTION CODE*/

                /*if (preferences.getString("transit_active", "").equalsIgnoreCase("0") && preferences.getString("emv_active", "").equalsIgnoreCase("0")) {
                    editor.putString("emv_active", "1");
                    editor.apply();
                     *//*microsdhandler initialize*//*
                    microSdHandler = MicroSdHandler.getMicroSDHandler(Home.this);
                    microSdHandler.openChannel(MicroSdHandler.CARD_TYPE.EMV);
                    //activate EMV
                    if (microSdHandler.activateCard()) {
                        emv_txt.setText(getResources().getString(R.string.disabled_emv));
                        emv_bg.setBackgroundResource(R.drawable.circle_green);
                        Toast.makeText(Home.this, "EMV Card activated successfully..", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(myRunnable, SPLASH_DISPLAY_LENGTH);
                    }
                } else if (preferences.getString("emv_active", "").equalsIgnoreCase("1")) {
                    editor.putString("emv_active", "0");
                    editor.apply();
                    handler.removeCallbacks(myRunnable);
                    emv_txt.setText(getResources().getString(R.string.enable_emv));
                    emv_bg.setBackgroundResource(R.drawable.circle_blue);
                } else if (preferences.getString("transit_active", "").equalsIgnoreCase("1")) {

                    Toast.makeText(Home.this, "Deactivate Travel Card", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        int_chk = new Internet_connection_checking(this);
        transit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
                editor = pref.edit();

                if (pref.getString("username", null) != null) {

                    // int_chk = new Internet_connection_checking(this);
                    Connection = int_chk.checkInternetConnection();
                    if (!Connection) {

                        Snackbar.make(Home.this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else {
                        try {
                            LoginActionForCabs(pref.getString("username", null), pref.getString("password", null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    toolbrtxt_layout.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.GONE);
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                    home_relative.setVisibility(View.GONE);
                    editor.putString("menuitemstatuscode", "2");
                    editor.apply();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();

                }

             /*   toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ContactUs contactUs = new ContactUs();
                fragmentTransaction.replace(R.id.fragment_place, contactUs);
                fragmentTransaction.commit();*/



                /*IMportant-------------------------------------------------------------->*/
                /*NFC CARD ACTIVATE DE-ACTIVATE OPTION CODE*/


               /* if (preferences.getString("transit_active", "").equalsIgnoreCase("0") && preferences.getString("emv_active", "").equalsIgnoreCase("0")) {
                    editor.putString("transit_active", "1");
                    editor.apply();
                    *//*microsdhandler initialize*//*
                    microSdHandler = MicroSdHandler.getMicroSDHandler(Home.this);
                    Log.v("microsdHandler value", microSdHandler.toString());
                    microSdHandler.openChannel(MicroSdHandler.CARD_TYPE.TRANSIT);
                    //activate EMV
                    if (microSdHandler.activateCard()) {
                        transit_txt.setText(getResources().getString(R.string.disable_transit));
                        transit_bg.setBackgroundResource(R.drawable.circle_green);
                        Toast.makeText(Home.this, "Travel Card activated successfully..", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(myRunnable, SPLASH_DISPLAY_LENGTH);
                    }

                } else if (preferences.getString("transit_active", "").equalsIgnoreCase("1")) {
                    editor.putString("transit_active", "0");
                    editor.apply();
                    handler.removeCallbacks(myRunnable);
                    transit_txt.setText(getResources().getString(R.string.enable_transit));
                    transit_bg.setBackgroundResource(R.drawable.circle_blue);
                } else if (preferences.getString("emv_active", "").equalsIgnoreCase("1")) {
                    Toast.makeText(Home.this, "Deactivate EMV Card ", Toast.LENGTH_SHORT).show();
                }*/



            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Check Internet Connection*/
        int_chk = new Internet_connection_checking(this);
        Connection = int_chk.checkInternetConnection();

        if (!Connection) {

            Snackbar.make(this.findViewById(android.R.id.content), R.string.chekc_inconnection, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            /*gps = new GPSTracker(this);
            // check if GPS enabled
            if (gps.canGetLocation()) {
                if (gps.getLongitude() != 0.0) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    currentLocationNews(latitude, longitude);
                } else {
                    onResume();
                }
            } else {
                if (!gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    currentLocationNews(latitude, longitude);

                }
            }*/

            new SingleShotLocationProvider().requestSingleUpdate(this, new SingleShotLocationProvider.LocationCallback() {
                @Override
                public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                    double latitude = location.latitude;
                    double longitude = location.longitude;
                    currentLocationNews(latitude, longitude);
                }
            });


        }

        /*Shortcut EditLocalMenu List*/
        db.getLocalAllMenus();
        Constant.saved_menus_list.clear();
        for (int i = 0; i < Constant.local_menus_list.size(); i++) {

            if (Constant.local_menus_list.get(i).getMenu_priority().equalsIgnoreCase("1")) {
                Constant.saved_menus_list.add(Constant.local_menus_list.get(i));
                Log.d(TAG, "SavedMenus" + Constant.saved_menus_list.size() + Constant.local_menus_list.get(0).getMenu_priority());

            }

        }


        if (Constant.saved_menus_list.size() == 1) {
            addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getPackageName());
            addmenu1_img.setImageResource(icon);
            addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            addmenu2_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu2_img.setImageResource(R.drawable.ic_plus);
            addmenu2_txt.setText("Add Menu");

            addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu3_img.setImageResource(R.drawable.ic_plus);
            addmenu3_txt.setText("Add Menu");

            addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu4_img.setImageResource(R.drawable.ic_plus);
            addmenu4_txt.setText("Add Menu");

        } else if (Constant.saved_menus_list.size() == 2) {

            addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getPackageName());
            addmenu1_img.setImageResource(icon);
            addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getPackageName());
            addmenu2_img.setImageResource(icon2);
            addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu3_img.setImageResource(R.drawable.ic_plus);
            addmenu3_txt.setText("Add Menu");

            addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu4_img.setImageResource(R.drawable.ic_plus);
            addmenu4_txt.setText("Add Menu");

        } else if (Constant.saved_menus_list.size() == 3) {

            addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getPackageName());
            addmenu1_img.setImageResource(icon);
            addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getPackageName());
            addmenu2_img.setImageResource(icon2);
            addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            addmenu3_img.setBackgroundResource(R.drawable.circle_blue);
            int icon3 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(2).getMenu_icon(), "drawable", getPackageName());
            addmenu3_img.setImageResource(icon3);
            addmenu3_txt.setText(Constant.saved_menus_list.get(2).getMenu_name());

            addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu4_img.setImageResource(R.drawable.ic_plus);
            addmenu4_txt.setText("Add Menu");

        } else if (Constant.saved_menus_list.size() == 4) {
            addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getPackageName());
            addmenu1_img.setImageResource(icon);
            addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getPackageName());
            addmenu2_img.setImageResource(icon2);
            addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            addmenu3_img.setBackgroundResource(R.drawable.circle_blue);
            int icon3 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(2).getMenu_icon(), "drawable", getPackageName());
            addmenu3_img.setImageResource(icon3);
            addmenu3_txt.setText(Constant.saved_menus_list.get(2).getMenu_name());

            addmenu4_img.setBackgroundResource(R.drawable.circle_blue);
            int icon4 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(3).getMenu_icon(), "drawable", getPackageName());
            addmenu4_img.setImageResource(icon4);
            addmenu4_txt.setText(Constant.saved_menus_list.get(3).getMenu_name());

        } else {
            addmenu1_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu1_img.setImageResource(R.drawable.ic_plus);
            addmenu1_txt.setText("Add Menu");

            addmenu2_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu2_img.setImageResource(R.drawable.ic_plus);
            addmenu2_txt.setText("Add Menu");

            addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu3_img.setImageResource(R.drawable.ic_plus);
            addmenu3_txt.setText("Add Menu");

            addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            addmenu4_img.setImageResource(R.drawable.ic_plus);
            addmenu4_txt.setText("Add Menu");

        }

        addmenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    MenuClicked((String) addmenu1_txt.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        addmenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MenuClicked((String) addmenu2_txt.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addmenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MenuClicked((String) addmenu3_txt.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addmenu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MenuClicked((String) addmenu4_txt.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null) {
            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("stationDetails")) {
                getIntent().removeExtra("menuFragment");
                menuFragment = "";
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                StationDetails stationDetails = new StationDetails();
                fragmentTransaction.replace(R.id.fragment_place, stationDetails);
                fragmentTransaction.commit();
            }
        }

        menuFragmentdirec = getIntent().getStringExtra("menuFragmentdirec");
        if (menuFragmentdirec != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragmentdirec.equals("direction")) {
                getIntent().removeExtra("menuFragmentdirec");
                menuFragmentdirec = "";
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                NearestMetroRouteDirection nearestMetroRouteDirection = new NearestMetroRouteDirection();
                fragmentTransaction.replace(R.id.fragment_place, nearestMetroRouteDirection);
                fragmentTransaction.commit();
            }
        }



//        SharedPreferences preff = getSharedPreferences("LoginDetails", 0);
//        editor = preff.edit();
        pref = getApplicationContext().getSharedPreferences("LoginDetails", 0);

        editor = pref.edit();

        if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){
            Log.d("Data  " ,"" + pref.getString("AvailFreeRide",null));
            Log.d("FirstIf","1");
            freeridevisibilityItem();
        } else if (pref.getString("username", null) !=null){
            Log.d("Sec","2");
           visibilityItem();

        } else {
            Log.d("ThirdIf","3");
            hideItem();

        }


    }



    public void hideItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if (menu.findItem(R.id.nav_traval_card) != null) {
            menu.findItem(R.id.nav_traval_card).setVisible(false);
        }
        if (menu.findItem(R.id.nav_travel_NFC) != null) {
            menu.findItem(R.id.nav_travel_NFC).setVisible(false);
        }
        if (menu.findItem(R.id.nav_ticket_booking) != null) {
            menu.findItem(R.id.nav_ticket_booking).setVisible(false);
        }
        if (menu.findItem(R.id.nav_Nfctestfile_NFC) != null) {
            menu.findItem(R.id.nav_Nfctestfile_NFC).setVisible(true);

        }
        if (menu.findItem(R.id.nav_promotional_ride) != null) {
            menu.findItem(R.id.nav_promotional_ride).setVisible(false);
        }

    }

    public void visibilityItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        if (menu.findItem(R.id.nav_traval_card) != null) {
            menu.findItem(R.id.nav_traval_card).setVisible(true);
        }
        if (menu.findItem(R.id.nav_travel_NFC) != null) {
            menu.findItem(R.id.nav_travel_NFC).setVisible(true);
        }
        if (menu.findItem(R.id.nav_ticket_booking) != null) {
            menu.findItem(R.id.nav_ticket_booking).setVisible(true);
        }
        if (menu.findItem(R.id.nav_promotional_ride) != null) {
            menu.findItem(R.id.nav_promotional_ride).setVisible(true);
        }

        if (menu.findItem(R.id.nav_Nfctestfile_NFC) != null) {
            menu.findItem(R.id.nav_Nfctestfile_NFC).setVisible(false);
        }
    }





    public void freeridevisibilityItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        if (menu.findItem(R.id.nav_traval_card) != null) {
            menu.findItem(R.id.nav_traval_card).setVisible(true);
        }
        if (menu.findItem(R.id.nav_travel_NFC) != null) {
            menu.findItem(R.id.nav_travel_NFC).setVisible(true);
        }
        if (menu.findItem(R.id.nav_ticket_booking) != null) {
            menu.findItem(R.id.nav_ticket_booking).setVisible(true);
        }
        if (menu.findItem(R.id.nav_promotional_ride) != null) {
            menu.findItem(R.id.nav_promotional_ride).setVisible(true);
        }

        if (menu.findItem(R.id.nav_Nfctestfile_NFC) != null) {
            menu.findItem(R.id.nav_Nfctestfile_NFC).setVisible(false);
        }

    }



    public void onPause() {
        super.onPause();

        /*IMportant-------------------------------------------------------------->*/
        //gps.locationManager.removeUpdates((LocationListener) this);
        //requestLocationUpdates();
        // microSdHandler.deactivateCard();    //Deactivate Card
        // microSdHandler.closeChannel();

       /* editor.putString("emv_active", "0");
        editor.putString("transit_active", "0");
        editor.apply();
        emv_txt.setText(getResources().getString(R.string.enable_emv));
        emv_bg.setBackgroundResource(R.drawable.circle_blue);
        transit_txt.setText(getResources().getString(R.string.enable_transit));
        transit_bg.setBackgroundResource(R.drawable.circle_blue);*/

    }

    /*Handle navigation Drawer view item clicks here.*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();


        SharedPreferences preff = getSharedPreferences("LoginDetails", 0);
        editor = preff.edit();
        if (preff.getString("username", null) != null) {


        }

        if (id == R.id.nav_home) {
            if (getSupportFragmentManager().findFragmentById(R.id.fragment_place) != null) {
                home_relative.setVisibility(View.VISIBLE);
                toolbrtxt_layout.setVisibility(View.GONE);
                toolbar_img.setVisibility(View.VISIBLE);
                toolbar.setBackgroundResource(R.color.color_transprant);
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                toolbarsettext();
            }

        } else if (id == R.id.nav_travel_planner) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            TravelPlanner travelPlanner = new TravelPlanner();
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, travelPlanner);
            fragmentTransaction.commit();
            toolbarsettext();

        } else if (id == R.id.nav_sta_info) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StationInfo stationInfo = new StationInfo();
            fragmentTransaction.replace(R.id.fragment_place, stationInfo);
            fragmentTransaction.commit();
            toolbarsettext();



        } else if (id == R.id.nav_Singara_chennai_card) {


            Intent i = new Intent(getApplicationContext(), SingarChennaiCard.class);
            startActivity(i);

           /* fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StationInfo stationInfo = new StationInfo();
            fragmentTransaction.replace(R.id.fragment_place, stationInfo);
            fragmentTransaction.commit();*/
            toolbarsettext();






    } else if (id == R.id.nav_travel_NFC) {
           /* toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);*/
            Intent i = new Intent(getApplicationContext(), Travelcardbalance.class);
            startActivity(i);


           /* fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StationInfo stationInfo = new StationInfo();
            fragmentTransaction.replace(R.id.fragment_place, stationInfo);
            fragmentTransaction.commit();*/
            toolbarsettext();

        } else if (id == R.id.nav_Nfctestfile_NFC) {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StationInfo stationInfo = new StationInfo();
            fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
            fragmentTransaction.commit();
            toolbarsettext();

           /* SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();

            if (pref.getString("username", null) != null) {

                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    LoginAction(pref.getString("username", null), pref.getString("password", null));
                }
            } else  {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);

                editor.putString("menuitemstatuscode", "1");
                editor.apply();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();

            }
           */
                                                                     ////manivel card list menu ////
        } else if (id == R.id.nav_traval_card) {

            SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();

            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    try {
                        //  pref.getString("device_id", null);
                        // TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        // telephonyManager.getDeviceId();
                        String device_id = Settings.Secure.getString(Home.this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        LoginAction(pref.getString("username", null), pref.getString("password", null), device_id);
                        //  LoginAction(pref.getString("username", null), pref.getString("password", null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            else {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                editor.putString("menuitemstatuscode", "1");
                editor.apply();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();

            }


        } else if (id == R.id.nav_traval_cabs) {

           /* SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();

            if (pref.getString("username", null) != null) {

                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    LoginActionForCabs(pref.getString("username", null), pref.getString("password", null));
                }
            } else {*/
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            editor.putString("menuitemstatuscode", "2");
            editor.apply();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
            fragmentTransaction.commit();

            //    }
        } else if (id == R.id.nav_ticket_booking) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            editor.putString("menuitemstatuscode", "3");
            editor.apply();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new Booking());
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_promotional_ride) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            editor.putString("menuitemstatuscode", "4");
            editor.apply();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new FreeDiscountRide());
            fragmentTransaction.commit();

        }


//        else if (id == R.id.nav_alert_destination) {
//
//            Intent i = new Intent(Home.this, AlertDestination.class);
//            startActivity(i);
//
//        }
        else if (id == R.id.nav_feeder_service) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FeaderService feaderService = new FeaderService();
            fragmentTransaction.replace(R.id.fragment_place, feaderService);
            fragmentTransaction.commit();
            toolbarsettext();
        } else if (id == R.id.nav_nearest_metro) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            NearestMetro nearestMetro = new NearestMetro();
            fragmentTransaction.replace(R.id.fragment_place, nearestMetro);
            fragmentTransaction.commit();
            toolbarsettext();
        } else if (id == R.id.nav_tour_guid) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            TourGuid tourGuid = new TourGuid();
            fragmentTransaction.replace(R.id.fragment_place, tourGuid);
            fragmentTransaction.commit();
            toolbarsettext();
        } else if (id == R.id.nav_other_info) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            OtherInfo otherInfo = new OtherInfo();
            fragmentTransaction.replace(R.id.fragment_place, otherInfo);
            fragmentTransaction.commit();
            toolbarsettext();
        } else if (id == R.id.nav_feedback) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Feedback feedback = new Feedback();
            fragmentTransaction.replace(R.id.fragment_place, feedback);
            fragmentTransaction.commit();
            toolbarsettext();
        } else if (id == R.id.nav_sugarbox) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Sugarbox Sugarbox = new Sugarbox();
            fragmentTransaction.replace(R.id.fragment_place, Sugarbox);
            fragmentTransaction.commit();
            toolbarsettext();
        }


        drawer.closeDrawer(Gravity.START);
        return true;
    }

    /*CurrentLocation Flash News */
    public void currentLocationNews(double lat, double lon) {


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        //Old

        // Call<CurrentNewsResponse> call = apiService.getCurrentNewsResponse(Constant.os_type, lat, lon, Constant.strApiKey, info.versionName);

/*

        {
            ostype:"Android",
                    latitude:"13.051092",
                longitude:"80.211712",
                secretcode:"$3cr3t"
        }
*/

        JsonObject values = new JsonObject();
        values.addProperty("ostype",Constant.os_type);
        values.addProperty("latitude",lat);
        values.addProperty("longitude", lon);
        values.addProperty("secretcode",Constant.strApiKey);
        values.addProperty("appv","ANDROID|"+version);
        Call<CurrentNewsResponse> call = apiService.getCurrentNewsResponse(values);

        call.enqueue(new Callback<CurrentNewsResponse>() {
            @Override
            public void onResponse(Call<CurrentNewsResponse> call, Response<CurrentNewsResponse> response) {
                int statusCode = response.code();

                Log.d("getNewsstatus",call.request().url().toString());
                Log.e(TAG, "getNewsstatus" + statusCode);
                if (statusCode == 200) {

                    if (response.body().getNewsstatus().equals("success")) {
                        newsNotificationLists = response.body().getNotifications_list();

                        if (response.body().getCurrent_location() != "") {
                            //gps.stopUsingGPS();
                            w_line.setVisibility(View.VISIBLE);
                            weahtetxt.setVisibility(View.VISIBLE);
                            weatherimg.setVisibility(View.VISIBLE);
                            DecimalFormat df = new DecimalFormat("###.#");
                            String temp = response.body().getTemp();
                            temp_txt.setText(temp + " ℃");
                            temp_txt.setTypeface(helveticabold_CE);
                            temp_discri.setText(response.body().getDescription().substring(0, 1).toUpperCase() + response.body().getDescription().substring(1));
                            String uri = "@drawable/w_" + response.body().getIcon() + "";  // where myresource (without the extension) is the file
                            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imageResource);
                            weather_ic.setImageDrawable(res);
                            weather_ic.setColorFilter(ContextCompat.getColor(Home.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                            current_loc_txt.setText(response.body().getCurrent_location());
                            current_loc_txt.setTypeface(helveticabold_CE);
                            current_locatinlayout.setVisibility(View.VISIBLE);
                        } else {
                            current_locatinlayout.setVisibility(View.GONE);
                            w_line.setVisibility(View.INVISIBLE);
                            weahtetxt.setVisibility(View.INVISIBLE);
                            weatherimg.setVisibility(View.INVISIBLE);
                        }

                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < newsNotificationLists.size(); i++) {

                            if (newsNotificationLists.get(i).getMessagetext() != null) {

                                stringBuilder.append(newsNotificationLists.get(i).getMessagetext() + "  ");

                            }else {

                            }

                        }

                        flash_txt.setText(stringBuilder);



                    } else if (response.body().getNewsstatus().equals("versionupdate")) {
                        showUpdateDialog();


                    }
                } else if (statusCode == 400) {

                    Toast.makeText(Home.this, R.string.somthinnot_right, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Home.this, R.string.somthinnot_right, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CurrentNewsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "currentLocationNewsException" + t.toString());
            }
        });


    }

    /*On BackPressed Event */
    @Override
    public void onBackPressed() {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_place);
        if (f instanceof StationInfo) {
            homeviewfragment();
        } else if (f instanceof TravelcardLogin) {
            homeviewfragment();
        } else if (f instanceof ForgotPassword) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
            fragmentTransaction.commit();
        } else if (f instanceof TRegister) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
            fragmentTransaction.commit();
        } else if (f instanceof ChangePassword) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
        } else if (f instanceof AddTravelCard) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
            fragmentTransaction.commit();
        } else if (f instanceof Feedback) {
            homeviewfragment();
        } else if (exit) {
            this.finishAffinity();
        } else {
            Toast.makeText(this, R.string.press_back, Toast.LENGTH_SHORT).show();
            if (drawer.isDrawerOpen(Gravity.START)) {
                drawer.closeDrawer(Gravity.START);
            }
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

    }

    /*ShortCut Menu Click Action*/
    public void MenuClicked(String menuName) throws Exception {
        if (menuName.equalsIgnoreCase("Travel Card Recharge")) {
            SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();
            if (pref.getString("username", null) != null) {

                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    LoginAction(pref.getString("username", null), pref.getString("password", null), pref.getString("device_id", null));

                    //   LoginAction(pref.getString("username", null), pref.getString("password", null));
                }

            } else {

                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                editor.putString("menuitemstatuscode", "1");
                editor.apply();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();


            }


        } else if (menuName.equalsIgnoreCase("Feeder Services")) {

            SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();

            if (pref.getString("username", null) != null) {

                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    LoginActionForCabs(pref.getString("username", null), pref.getString("password", null));
                }
            } else {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                editor.putString("menuitemstatuscode", "2");
                editor.apply();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();

            }

        } else if (menuName.equalsIgnoreCase("Book a Cab")) {

            SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
            editor = pref.edit();
            if (pref.getString("username", null) != null) {
                int_chk = new Internet_connection_checking(this);
                Connection = int_chk.checkInternetConnection();
                if (!Connection) {
                    Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    LoginActionForCabs(pref.getString("username", null), pref.getString("password", null));
                }
            } else {
                toolbrtxt_layout.setVisibility(View.VISIBLE);
                toolbar_img.setVisibility(View.GONE);
                toolbar.setBackgroundResource(R.color.colorPrimary);
                home_relative.setVisibility(View.GONE);
                editor.putString("menuitemstatuscode", "2");
                editor.apply();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();

            }
        }
            else if (menuName.equalsIgnoreCase("Promotional Ride")) {

                SharedPreferences pref = getSharedPreferences("LoginDetails", 0);
                editor = pref.edit();
                if (pref.getString("username", null) != null) {
                    int_chk = new Internet_connection_checking(this);
                    Connection = int_chk.checkInternetConnection();
                    if (!Connection) {
                        Snackbar.make(this.findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else {
                        promotionalride(pref.getString("username", null), pref.getString("password", null));
                    }
                } else {

                    toolbrtxt_layout.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.GONE);
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                    home_relative.setVisibility(View.GONE);
                    editor.putString("menuitemstatuscode", "4");
                    editor.apply();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();

                }

        } else if (menuName.equalsIgnoreCase("MTC/Auto/Cab Services")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FeaderService feaderService = new FeaderService();
            fragmentTransaction.replace(R.id.fragment_place, feaderService);
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Metro Network Information")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MetronetInformation());
            fragmentTransaction.commit();

        } else if (menuName.equalsIgnoreCase("News Updates")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new Notifications());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Instruction for Commuters")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new InstructiontoCommuters());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Facilities for Disabled")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new FacilitesforDisabled());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Cultural Centres")) {

            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new CulturalCenter());
            fragmentTransaction.commit();

        } else if (menuName.equalsIgnoreCase("Local Weather Forecasts")) {

            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new WeatherReport());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Lost and Found")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new LostFound());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Notifications")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new Notifications());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Facilities For Disabled Persons")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new FacilitesforDisabled());
            fragmentTransaction.commit();
        } else if (menuName.equalsIgnoreCase("Contact")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            ContactUs contactUs = new ContactUs();
            fragmentTransaction.replace(R.id.fragment_place, contactUs);
            fragmentTransaction.commit();

        } else if (menuName.equalsIgnoreCase("SugarBox Entertainment")) {
            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Sugarbox Sugarbox = new Sugarbox();
            fragmentTransaction.replace(R.id.fragment_place, Sugarbox);
            fragmentTransaction.commit();
        } else {

            toolbrtxt_layout.setVisibility(View.VISIBLE);
            toolbar_img.setVisibility(View.GONE);
            toolbar.setBackgroundResource(R.color.colorPrimary);
            home_relative.setVisibility(View.GONE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new EditMenu());
            fragmentTransaction.commit();

        }
    }

    /*Attempt To LOGIN Process to Server........*/
    private void promotionalride(final String username, final String password) throws Exception {

        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);


        Log.d("encode",""+encryptedText);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (!refreshedToken.equalsIgnoreCase("")) {
            Log.d(TAG, "FireRefreshedtoken: " + refreshedToken);
        }

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(Home.this, "", "Loading...", true);

        /*Send Parameters to the Api*/
        //Old
        //  Call<Login> call = apiService.getT_LoginDetails(username, password, Constant.strApiKey, refreshedToken, version);

        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",encryptedText);
        values.addProperty("secretcode", Constant.strApiKey);
        values.addProperty("tokenid",refreshedToken);
        values.addProperty("appv","ANDROID|"+version);
        Call<Login> call = apiService.getT_LoginDetails(values);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int statusCode = response.code();

                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("Failed")) {
                        // showUpdateDialog();
                    } else {

                        editor.putString("securitycode", response.body().getSecuritycode());
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("dob", response.body().getLoginDetails().getDob());
                        editor.putString("login_name", response.body().getLoginDetails().getUsername());
                        editor.putString("gender", response.body().getLoginDetails().getGender());
                        editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                        editor.putString("email", response.body().getLoginDetails().getEmail());
                        editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                        editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                        editor.putString("m_pin", null);
                        editor.putString("menuitemstatuscode", "4");
                        editor.apply();
                        toolbrtxt_layout.setVisibility(View.VISIBLE);
                        toolbar_img.setVisibility(View.GONE);
                        toolbar.setBackgroundResource(R.color.colorPrimary);
                        home_relative.setVisibility(View.GONE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                        fragmentTransaction.replace(R.id.fragment_place, new FreeDiscountRide());
                        fragmentTransaction.commit();

                    }

                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {

                    loader.dismiss();
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
                    toolbrtxt_layout.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.GONE);
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                    home_relative.setVisibility(View.GONE);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "LoginError" + t.toString());
                loader.dismiss();
            }
        });
    }


    /*Toolbar Text*/
    public void toolbarsettext() {

        toolbrtxt_layout.setVisibility(View.GONE);
        toolbar_img.setVisibility(View.VISIBLE);
    }

    /*Attempt To LOGIN Process to Server........*/
    private void LoginAction(final String username, final String password,final String device_id ) throws Exception {

        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);


        Log.d("encode",""+encryptedText);


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (!refreshedToken.equalsIgnoreCase("")) {
            Log.d(TAG, "FireRefreshedtoken: " + refreshedToken);
        }

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(Home.this, "", "Loading...", true);
        /*Send Parameters to the Api*/
        //Old
        // Call<Login> call = apiService.getT_LoginDetails(username, password, Constant.strApiKey, refreshedToken, version);

        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",encryptedText);
        values.addProperty("secretcode", Constant.strApiKey);
        values.addProperty("tokenid",refreshedToken);
        values.addProperty("DeviceType","ANDROID");
        values.addProperty("DeviceId",device_id);
        values.addProperty("appv","ANDROID|"+version);
        Call<Login> call = apiService.getT_LoginDetails(values);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int statusCode = response.code();

                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("Failed")) {

                        //  Toast.makeText(getApplication(),"Failed 200", Toast.LENGTH_SHORT).show();
                        // showUpdateDialog();

                      /*  JsonObject values = new JsonObject();
                        values.addProperty("Name",name);
                        values.addProperty("DOB",dob);
                        values.addProperty("Gender", gender);
                        values.addProperty("RegisteredMobile",reg_mobNumber);
                        values.addProperty("Email",email);
                        values.addProperty("Password",password);
                        values.addProperty("DeviceId",device_id);
                        values.addProperty("OTP",textotp);
                        values.addProperty("secretcode",Constant.strApiKey);
                        values.addProperty("appv","ANDROID|"+version);
*/
                    } else {
                        // Toast.makeText(getApplication(),response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        editor.putString("securitycode", response.body().getSecuritycode());
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("dob", response.body().getLoginDetails().getDob());
                        editor.putString("login_name", response.body().getLoginDetails().getUsername());
                        editor.putString("gender", response.body().getLoginDetails().getGender());
                        editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                        editor.putString("email", response.body().getLoginDetails().getEmail());
                        editor.putString("m_pin", null);
                        editor.putString("QR_JWTTOKEN",response.body().getQRjwttoken());
                        editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                        editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                        editor.putString("menuitemstatuscode", "1");
                        editor.apply();
                        toolbrtxt_layout.setVisibility(View.VISIBLE);
                        toolbar_img.setVisibility(View.GONE);
                        toolbar.setBackgroundResource(R.color.colorPrimary);
                        home_relative.setVisibility(View.GONE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();

                    }
                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {
                    // Toast.makeText(getApplication(),"Failed 400", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                    editor.putString("securitycode", null);
                    editor.putString("username", null);
                    editor.putString("password", null);
                    editor.putString("login_name", null);
                    editor.putString("dob", null);
                    editor.putString("gender", null);
                    editor.putString("registeredmobile", null);
                    editor.putString("email", null);
                    editor.putString("m_pinLoginState", "no");
                    editor.putString("menuitemstatuscode", "1");
                    editor.apply();
                    toolbrtxt_layout.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.GONE);
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                    home_relative.setVisibility(View.GONE);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "LoginError" + t.toString());
                // Toast.makeText(getApplication(),"Failed", Toast.LENGTH_SHORT).show();
                //  loader.dismiss();
            }
        });
    }

    /*--------------------Booking Cabs-----------------------*/

    /*Attempt To LOGIN Process to Server........*/
    private void LoginActionForCabs(final String username, final String password) throws Exception {

        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);


        Log.d("encode",""+encryptedText);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (!refreshedToken.equalsIgnoreCase("")) {
            Log.d(TAG, "FireRefreshedtoken: " + refreshedToken);
        }

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(Home.this, "", "Loading...", true);

        /*Send Parameters to the Api*/
        //Old
        //  Call<Login> call = apiService.getT_LoginDetails(username, password, Constant.strApiKey, refreshedToken, version);

        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",encryptedText);
        values.addProperty("secretcode", Constant.strApiKey);
        values.addProperty("tokenid",refreshedToken);
        values.addProperty("appv","ANDROID|"+version);
        Call<Login> call = apiService.getT_LoginDetails(values);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int statusCode = response.code();

                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("Failed")) {
                        // showUpdateDialog();
                    } else {

                        editor.putString("securitycode", response.body().getSecuritycode());
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("dob", response.body().getLoginDetails().getDob());
                        editor.putString("login_name", response.body().getLoginDetails().getUsername());
                        editor.putString("gender", response.body().getLoginDetails().getGender());
                        editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                        editor.putString("email", response.body().getLoginDetails().getEmail());
                        editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                        editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                        editor.putString("m_pin", null);
                        editor.putString("menuitemstatuscode", "2");
                        editor.apply();
                        toolbrtxt_layout.setVisibility(View.VISIBLE);
                        toolbar_img.setVisibility(View.GONE);
                        toolbar.setBackgroundResource(R.color.colorPrimary);
                        home_relative.setVisibility(View.GONE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                        fragmentTransaction.replace(R.id.fragment_place, new Booking());
                        fragmentTransaction.commit();

                    }

                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {

                    loader.dismiss();
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
                    toolbrtxt_layout.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.GONE);
                    toolbar.setBackgroundResource(R.color.colorPrimary);
                    home_relative.setVisibility(View.GONE);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "LoginError" + t.toString());
                loader.dismiss();
            }
        });
    }


    /*Home Fragment*/
    public void homeviewfragment() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_place) != null) {
            home_relative.setVisibility(View.VISIBLE);
            toolbrtxt_layout.setVisibility(View.GONE);
            toolbar_img.setVisibility(View.VISIBLE);
            toolbar.setBackgroundResource(R.color.color_transprant);
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_place)).commit();
            toolbarsettext();
        }

    }

    /*App Update DialogBox*/
    private void showUpdateDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.update_dailog);
        dialog.setCancelable(false);
        isShown = true;
        Button ubtn_dialogs_ok = (Button) dialog.findViewById(R.id.ubtn_dialogs_ok);
        ubtn_dialogs_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.chennaimetrorail.appv1")));
                dialog.dismiss();

            }
        });

        dialog.show();

      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=org.chennaimetrorail.appv1")));
                dialog.dismiss();
            }
        }, 3000);*/


    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                                    Home.this,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
                        //Toast.makeText(Home.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        onResume();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(Home.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
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
