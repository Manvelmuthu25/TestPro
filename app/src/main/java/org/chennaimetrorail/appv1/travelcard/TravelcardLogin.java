package org.chennaimetrorail.appv1.travelcard;


import static android.Manifest.permission.READ_PHONE_STATE;
import static android.support.v4.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.ViewPagerAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Login;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Tips;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcab.Booking;
import org.chennaimetrorail.appv1.travelcab.FreeDiscountRide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 1/20/2018.
 */

public class TravelcardLogin extends Fragment implements View.OnClickListener {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    EditText user_name, pass_word;
    Button login_btn;
    TextView forget_password_btn, create_account_btn;
    /*Storage for Login details*/
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    LinearLayout login_layout, mpin_layout;
    TextView welcome_txt, forget_mPin, mpin_create_account;
    PinEntryEditText m_pin_entry;
    Button m_pin_login_btn;
    View view;
    ViewPagerAdapter viewPagerAdapter;
    String TAG = "TravelcardLogin";
    private int dotscount;
    private ImageView[] dots;
    String imei;
    TelephonyManager tm;
    String version, menuitemstatuscode,stingIMei, window;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();


        int_chk = new Internet_connection_checking(getActivity());

        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.travelcard_login, container, false);
        FontStyle fontStyle = new FontStyle();
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fontStyle.Changeview(getActivity(), view);

        TextView title_tx = view.findViewById(R.id.title_tx);
        title_tx.setTypeface(fontStyle.helveticabold_CE);
        TextView login_txt = view.findViewById(R.id.login_txt);
        login_txt.setTypeface(fontStyle.helveticabold_CE);
        TextView tips_txt = view.findViewById(R.id.tips_txt);
        tips_txt.setTypeface(fontStyle.helveticabold_CE);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setTitle("");
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.GONE);
        home.toolbar_img.setVisibility(View.GONE);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
     /*   TextView toolbar_text = (TextView) home.toolbar.findViewById(R.id.title_tx);
        toolbar_text.setText("");
        ImageView toolbar_img = (ImageView) home.toolbar.findViewById(R.id.imageView);
        toolbar_img.setVisibility(View.GONE);*/

        /*Login Layout...................*/

        login_layout = (LinearLayout) view.findViewById(R.id.login_layout);
        user_name = (EditText) view.findViewById(R.id.user_name);
        pass_word = (EditText) view.findViewById(R.id.pass_word);
        login_btn = (Button) view.findViewById(R.id.login_btn);
        forget_password_btn = (TextView) view.findViewById(R.id.forget_password);
        create_account_btn = (TextView) view.findViewById(R.id.create_account);
        login_btn.setOnClickListener(this);
        login_btn.setTypeface(fontStyle.helveticabold_CE);
        forget_password_btn.setOnClickListener(this);
        create_account_btn.setOnClickListener(this);

//
//        TelephonyManager telephonyManager =(TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
//        if (  ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE) !=PackageManager.PERMISSION_GRANTED){
//
//            return;
//        }
//        String stingIMei = telephonyManager.getImei();
//        Toast toast= Toast.makeText(getContext(), "Your string here", Toast.LENGTH_SHORT);
//        toast.setText(stingIMei);


        /*m-Pin Layout...................*/

        mpin_layout = (LinearLayout) view.findViewById(R.id.m_pin_layout);
        welcome_txt = (TextView) view.findViewById(R.id.welcome_txt);
        forget_mPin = (TextView) view.findViewById(R.id.forget_mPin);
        mpin_create_account = (TextView) view.findViewById(R.id.mpin_create_account);
        m_pin_entry = (PinEntryEditText) view.findViewById(R.id.m_pin_entry);
        m_pin_login_btn = (Button) view.findViewById(R.id.m_pin_login_btn);
        m_pin_login_btn.setOnClickListener(this);
        forget_mPin.setOnClickListener(this);
        mpin_create_account.setOnClickListener(this);


        menuitemstatuscode = pref.getString("menuitemstatuscode", null);
        //Toast.makeText(getContext(),menuitemstatuscode,Toast.LENGTH_LONG).show();

        /*Slider image view..*/

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.sliderDots);

        Connection = int_chk.checkInternetConnection();
        /*Check Internet Connection Connect Or Not*/
        if (!Connection) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            /*Call Validate login parameters method..*/

            Tips();

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //MpinCheck();
        try {
            LoginCheck();
        } catch (Exception e) {
            e.printStackTrace();
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

        m_pin_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (m_pin_entry != null) {
                    if (m_pin_entry.getText().toString().equals(pref.getString("m_pin", null))) {

                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        IBinder binder = v.getWindowToken();
                        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        editor.putString("m_pinLoginState", "yes");
                        editor.apply();

                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        if (menuitemstatuscode.equalsIgnoreCase("1")) {
                            fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                            fragmentTransaction.commit();
                        } else if (menuitemstatuscode.equalsIgnoreCase("2")) {

//                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                            fragmentTransaction.replace(R.id.fragment_place, new Booking());
                            fragmentTransaction.commit();
                        }
                    } else {

                        Toast.makeText(getActivity(), "Please Enter the Correct m-Pin", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
        if (m_pin_entry != null) {
            m_pin_entry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (m_pin_entry.getText().toString().equals(pref.getString("m_pin", null))) {

                     /*   Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();*/
                        editor.putString("m_pinLoginState", "yes");
                        editor.apply();
                        mpinLoginAction(pref.getString("username", null), pref.getString("password", null));
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    } else {

                        Toast.makeText(getActivity(), "Please Enter the Correct m-Pin", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        return view;

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            /*m_pin Layout..............*/
            case R.id.login_btn:
                /*InterNet Checking Class create Object Here*/

                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    /*Call Validate login parameters method..*/
                    try {
                        validateLogin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                break;
            case R.id.create_account:

                fragmentTransaction.replace(R.id.fragment_place, new TRegister());
                fragmentTransaction.commit();

                break;
            case R.id.forget_password:
                editor.putString("m_pinLoginState", "no");
                editor.apply();
                fragmentTransaction.replace(R.id.fragment_place, new ForgotPassword());
                fragmentTransaction.commit();

                break;


            /*m_pin Layout..............*/
            case R.id.m_pin_login_btn:


                break;

            case R.id.forget_mPin:

                editor.putString("m_pin", null);
                editor.apply();
                MpinCheck();
                break;

            case R.id.mpin_create_account:
                fragmentTransaction.replace(R.id.fragment_place, new TRegister());
                fragmentTransaction.commit();
                break;

        }


    }


    /*Check Login Details Hear...!*/
    private void validateLogin() throws Exception {

        // Reset errors.
        user_name.setError(null);
        pass_word.setError(null);

        // Store values at the time of the login attempt.
        String username = user_name.getText().toString();
        String password = pass_word.getText().toString();


        pref.getString("device_id", null);




           String device_id = Settings.Secure.getString(this.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);




        //  Log.d("Cechjald2", "FireRefreshedtoken: " + android_id);
        //  editor.putString("token_id", refreshedToken);
        //  editor.putString("device_id", android_id);
        //  editor.commit();

     //   ActivityCompat.requestPermissions(getActivity(),new String[]{READ_PHONE_STATE},PackageManager.PERMISSION_GRANTED);


        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(username)) {

            Toast.makeText(getActivity(), "Enter your mobile number", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(username) && !isphoneValid(username)) {

            Toast.makeText(getActivity(), "Incorrect mobile number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_LONG).show();
        }
        // Check for a valid password, if the user entered one.
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {

            Toast.makeText(getActivity(), "Incorrect password", Toast.LENGTH_LONG).show();
        } else {
            /* Call Login Process To Server*/
            LoginAction(username, password, device_id);

        }
    }

    private Object getSystemService(String telephonyService) {

        return null;
    }

//
//    private void getSystemService(String telephonyService) {
//        if (telephonyService(READ_PHONE_STATE)) {
//
//            String simSerialNo = "";
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//                SubscriptionManager subsManager = (SubscriptionManager) getContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//
//                if (ActivityCompat.checkSelfPermission(getContext(), READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                List<SubscriptionInfo> subsList = subsManager.getActiveSubscriptionInfoList();
//
//                if (subsList!=null) {
//                    for (SubscriptionInfo subsInfo : subsList) {
//                        if (subsInfo != null) {
//                            simSerialNo  = subsInfo.getIccId();
//                        }
//                    }
//                }
//            } else {
//                TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
//                simSerialNo = tMgr.getSimSerialNumber();
//            }
//        }
//    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    private boolean isphoneValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 9;
    }

    /*  {
   {
     "username": "9600676567",
     "userpassword": "Test@123",
     "secretcode": "$3cr3t",
     "tokenid": "",
     "appv": "2.0.0"
   }
           }
   */
    /*Attempt To LOGIN Process to Server........*/
    private void LoginAction(final String username, final String password ,final String device_id) throws Exception {
        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);
        Log.d("encode",""+encryptedText);
        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
      // final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Cechjald", "FireRefreshedtoken: " + refreshedToken);
        /*Send Parameters to the Api*/

      //old
     //  Call<Login> call = apiService.getT_LoginDetails(username, password, Constant.strApiKey,refreshedToken,version);
        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",encryptedText);
        values.addProperty("secretcode", Constant.strApiKey);
        values.addProperty("tokenid",refreshedToken);
        values.addProperty("DeviceType","ANDROID");
        values.addProperty("DeviceId", device_id);
        values.addProperty("appv","ANDROID|"+version);
        //eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..pot6XOYi-SLtjB_O.tKBQ5oQ-rB1DnuopAoNKucqx07lkI14Cg3aBTrqNEauteDmboIE2ERDy4DehYaeE_9ZAtkWncVNudsoPAaz1ol2Xap70w-0hOTeNJYftAzqnGzM5MgaCYIyIpW1Ez6-JujqlR89FNUVhRpJEiDcFSESrWfoGHfCR.KuzHnEl8F4f-Qq2iwPz_1w
        Log.e("freeridefre",values.toString() );
        Call<Login> call = apiService.getT_LoginDetails(values);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int statusCode = response.code();
                Log.d("loginurl", call.request().url().toString());
                Log.d(TAG,call.request().url().toString());
                Log.d(TAG, "LoginResponse" + username + password);
                /*Get Login Good Response...*/

                Log.d(TAG, "status12"+response);
                if (statusCode == 200) {
                    Log.d(TAG, "status1234"+statusCode);
                    Log.d(TAG, "LoginRespons_status" + username + password);

                    if (response.body().getStatus().equalsIgnoreCase("Failed")) {

                    } else {
                        if(menuitemstatuscode != null) {
                            if (menuitemstatuscode.equalsIgnoreCase("1")) {
                                hidekeyboard();
                                editor.putString("securitycode", response.body().getSecuritycode());
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("device_id", device_id);
                                editor.putString("dob", response.body().getLoginDetails().getDob());
                                editor.putString("login_name", response.body().getLoginDetails().getUsername());
                                editor.putString("gender", response.body().getLoginDetails().getGender());
                                editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                                editor.putString("email", response.body().getLoginDetails().getEmail());
                                editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                                editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                                editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                                editor.putString("m_pin", null);
                                editor.apply();

                                Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                                Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());

                                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                    ((Home) getActivity()).freeridevisibilityItem();

                                    Log.d("SecondIf1","1");
                                } else if (pref.getString("username", null) != null) {

                                    Log.d("SecondIf1","22");

                                    ((Home) getActivity()).visibilityItem();

                                } else {
                                    Log.d("SecondIf2","3");
                                    ((Home) getActivity()).hideItem();
                                }
                                fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                                fragmentTransaction.commit();
                            } else if (menuitemstatuscode.equalsIgnoreCase("2")) {

                                hidekeyboard();
                                editor.putString("securitycode", response.body().getSecuritycode());
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("device_id", device_id);
                                editor.putString("dob", response.body().getLoginDetails().getDob());
                                editor.putString("login_name", response.body().getLoginDetails().getUsername());
                                editor.putString("gender", response.body().getLoginDetails().getGender());
                                editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                                editor.putString("email", response.body().getLoginDetails().getEmail());
                                editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                                editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                                editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                                editor.putString("m_pin", null);
                                editor.apply();
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());
                                Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                                Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());

                                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                    ((Home) getActivity()).freeridevisibilityItem();
                                    Log.d("SecondIf1","1");
                                } else if (pref.getString("username", null) != null) {
                                    Log.d("SecondIf1","22");
                                    ((Home) getActivity()).visibilityItem();
                                } else {
                                    Log.d("SecondIf101","3");
                                    ((Home) getActivity()).hideItem();
                                }
                                fragmentTransaction.replace(R.id.fragment_place, new Booking());
                                fragmentTransaction.commit();
                            } else if (menuitemstatuscode.equalsIgnoreCase("3")) {

                                hidekeyboard();
                                editor.putString("securitycode", response.body().getSecuritycode());
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("device_id", device_id);
                                editor.putString("dob", response.body().getLoginDetails().getDob());
                                editor.putString("login_name", response.body().getLoginDetails().getUsername());
                                editor.putString("gender", response.body().getLoginDetails().getGender());
                                editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                                editor.putString("email", response.body().getLoginDetails().getEmail());
                                editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                                editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                                editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                                editor.putString("m_pin", null);
                                editor.apply();
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());
                                Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                                Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());
//
                                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                    ((Home) getActivity()).freeridevisibilityItem();
                                    Log.d("SecondIf1","1");
                                } else if (pref.getString("username", null) != null) {
                                    Log.d("SecondIf1","22");
                                    ((Home) getActivity()).visibilityItem();
                                } else {
                                    Log.d("SecondIf100","3");
                                    ((Home) getActivity()).hideItem();
                                }

                                fragmentTransaction.replace(R.id.fragment_place, new Booking());
                                fragmentTransaction.commit();

                        }else if (menuitemstatuscode.equalsIgnoreCase("4")) {

                            hidekeyboard();
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("device_id", device_id);
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                                editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                                editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                            editor.putString("m_pin", null);


                            editor.apply();
                            Log.d(TAG, "LoginResponse" + response.body().getStatus());
                            Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                            Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());

                                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                    ((Home) getActivity()).freeridevisibilityItem();
                                    Log.d("SecondIf1","1");
                                } else if (pref.getString("username", null) != null) {
                                    Log.d("SecondIf1","22");
                                    ((Home) getActivity()).visibilityItem();
                                } else {
                                    Log.d("SecondIf","3");
                                    ((Home) getActivity()).hideItem();
                                }



//                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);
//                            if (pref.getString("username", null) != null) {
//                                ((Home) getActivity()).visibilityItem();
//                            } else {
//                                ((Home) getActivity()).hideItem();
//
//                            }
                            fragmentTransaction.replace(R.id.fragment_place, new FreeDiscountRide());
                            fragmentTransaction.commit();
                        }



                        else {
                                hidekeyboard();
                                editor.putString("securitycode", response.body().getSecuritycode());
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.putString("device_id", device_id);
                                editor.putString("dob", response.body().getLoginDetails().getDob());
                                editor.putString("login_name", response.body().getLoginDetails().getUsername());
                                editor.putString("gender", response.body().getLoginDetails().getGender());
                                editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                                editor.putString("email", response.body().getLoginDetails().getEmail());
                                editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                                editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                                editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                                editor.putString("m_pin", null);
                                editor.apply();
                                Log.d(TAG, "LoginResponse" + response.body().getStatus());
                                Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                                Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());

                                Log.d(TAG, "LoginResponse" + response.body().getStatus());

                                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);
                                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                    ((Home) getActivity()).freeridevisibilityItem();
                                    Log.d("SecondIf1","1");
                                } else if (pref.getString("username", null) != null) {
                                    Log.d("SecondIf1","22");
                                    ((Home) getActivity()).visibilityItem();
                                } else {
                                    Log.d("SecondIf49","3");
                                    ((Home) getActivity()).hideItem();
                                }

                                fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                                fragmentTransaction.commit();

                            }
                        }else{
                            hidekeyboard();
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("device_id", device_id);
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.putString("QR_JWTTOKEN", response.body().getQRjwttoken());
                            editor.putString("AvailFreeRide",response.body().getAvailFreeRide());
                            editor.putString("AvailDiscountedRide",response.body().getAvailDiscountedRide());
                            editor.putString("m_pin", null);
                            editor.apply();
                            Log.d(TAG, "LoginResponse" + response.body().getStatus());
                            Log.d(TAG, "freeride" + response.body().getAvailFreeRide());
                            Log.d(TAG, "getAvailDiscountedRide" + response.body().getAvailDiscountedRide());

                            Log.d(TAG, "LoginResponse" + response.body().getStatus());

                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                ((Home) getActivity()).freeridevisibilityItem();
                                Log.d("SecondIf1","1");
                            } else if (pref.getString("username", null) != null) {
                                Log.d("SecondIf1","22");
                                ((Home) getActivity()).visibilityItem();
                            } else {
                                Log.d("SecondIf48","3");
                                ((Home) getActivity()).hideItem();
                            }

                            fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                            fragmentTransaction.commit();
                        }
                    }

                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {
                    Log.d(TAG, "Login400" + statusCode);
                  //  loader.dismiss();
                    Gson gson = new GsonBuilder().create();
                    LoginApiError pojo = new LoginApiError();
                    try {
                        pojo = gson.fromJson(response.errorBody().string(), LoginApiError.class);
                        Toast.makeText(getActivity(), pojo.getLogin(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) { // handle failure at error parse }

                    }
                  //  Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_LONG).show();


                }else {
                 //   loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*Attempt To LOGIN Process to Server........*/
    private void mpinLoginAction(final String username, final String password) {

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Cechjald", "FireRefreshedtoken: " + refreshedToken);
        /*Send Parameters to the Api*/


       /* curl --location --request POST 'https://apidev2.chennaimetrorail.org/v2/api/Passenger/Login' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        "username": "9600676567",
                "userpassword": "Test@123",
                "secretcode": "$3cr3t",
                "tokenid": "",
                "appv": "2.0.0"
    }*/

        //Old
      //  Call<Login> call = apiService.getT_LoginDetails(username, password, Constant.strApiKey,refreshedToken,version);
        JsonObject values = new JsonObject();
        values.addProperty("username",username);
        values.addProperty("userpassword",password);
        values.addProperty("tokenid", Constant.strApiKey);
        values.addProperty("appv",version);
        values.addProperty("secretcode",refreshedToken);
        Call<Login> call = apiService.getT_LoginDetails(values);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int statusCode = response.code();
                Log.d(TAG, "LoginResponse" + username + password);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    Log.d(TAG, "LoginRespons_status" + username + password);
                    loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("Failed")) {
                        // showUpdateDialog();
                    } else {
                        if(menuitemstatuscode.equalsIgnoreCase("1")) {
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.apply();

                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                ((Home) getActivity()).freeridevisibilityItem();
                                Log.d("SecondIf1","1");
                            } else if (pref.getString("username", null) != null) {
                                Log.d("SecondIf1","22");
                                ((Home) getActivity()).visibilityItem();
                            } else {
                                Log.d("SecondIf6","3");
                                ((Home) getActivity()).hideItem();
                            }



                            fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                            fragmentTransaction.commit();
                        }

                        else if(menuitemstatuscode.equalsIgnoreCase("2")){
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.apply();
                            Log.d(TAG, "LoginResponse" + response.body().getLoginDetails().getDob());

                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                ((Home) getActivity()).freeridevisibilityItem();
                                Log.d("SecondIf1","1");
                            } else if (pref.getString("username", null) != null) {
                                Log.d("SecondIf1","22");
                                ((Home) getActivity()).visibilityItem();
                            } else {
                                Log.d("SecondIf28","3");
                                ((Home) getActivity()).hideItem();
                            }


//                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                            fragmentTransaction.replace(R.id.fragment_place, new Booking());
                            fragmentTransaction.commit();
                        }

                        else if(menuitemstatuscode.equalsIgnoreCase("4")){
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.apply();
                            Log.d(TAG, "LoginResponse" + response.body().getLoginDetails().getDob());

                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                ((Home) getActivity()).freeridevisibilityItem();
                                Log.d("SecondIf1","1");
                            } else if (pref.getString("username", null) != null) {
                                Log.d("SecondIf1","22");
                                ((Home) getActivity()).visibilityItem();
                            } else {
                                Log.d("SecondIf47","3");
                                ((Home) getActivity()).hideItem();
                            }


//                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                            fragmentTransaction.replace(R.id.fragment_place, new FreeDiscountRide());
                            fragmentTransaction.commit();
                        }

                        else {
                            editor.putString("securitycode", response.body().getSecuritycode());
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("login_name", response.body().getLoginDetails().getUsername());
                            editor.putString("dob", response.body().getLoginDetails().getDob());
                            editor.putString("gender", response.body().getLoginDetails().getGender());
                            editor.putString("registeredmobile", response.body().getLoginDetails().getRegisteredmobile());
                            editor.putString("email", response.body().getLoginDetails().getEmail());
                            editor.apply();
                            Log.d(TAG, "LoginResponse" + response.body().getLoginDetails().getDob());

                            SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);

                            if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){

                                ((Home) getActivity()).freeridevisibilityItem();
                                Log.d("SecondIf1","1");
                            } else if (pref.getString("username", null) != null) {
                                Log.d("SecondIf1","22");
                                ((Home) getActivity()).visibilityItem();
                            } else {
                                Log.d("SecondIf46","3");
                                ((Home) getActivity()).hideItem();
                            }

                            fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                            fragmentTransaction.commit();
                        }
                    }


                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {

                    loader.dismiss();
                    Toast.makeText(getActivity(), "Please Check your login details!", Toast.LENGTH_LONG).show();

                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*Attempt To LOGIN Process to Server........*/
    private void Tips() {

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        /*Send Parameters to the Api*/
        Call<Tips> call = apiService.getT_Tips(Constant.strApiKey,version);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Tips>() {
            @Override
            public void onResponse(Call<Tips> call, Response<Tips> response) {
                int statusCode = response.code();
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    Log.d(TAG, "Get_tips");

                    Constant.tips_array = response.body().getTips();
                    viewPagerAdapter = new ViewPagerAdapter(getContext());

                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = Constant.tips_array.size();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getContext());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);

                        sliderDotspanel.addView(dots[i], params);

                    }


                    dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

                }


            }

            @Override
            public void onFailure(Call<Tips> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
            }
        });
    }


    public void MpinCheck() {
        if (pref.getString("m_pin", null) != null) {
            mpin_layout.setVisibility(View.VISIBLE);
            welcome_txt.setText("Welcome! " + pref.getString("login_name", null));
            login_layout.setVisibility(View.GONE);
        } else {

            if (pref.getString("username", null) != null) {

                user_name.setText(pref.getString("username", null));
            }

            mpin_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
        }
    }

    public void LoginCheck() throws Exception {


        if (pref.getString("username", null) != null) {
            LoginAction(pref.getString("username", null), pref.getString("password", null), pref.getString("device_id", null));
        } else {
            mpin_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);

        }
    }

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {

        SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);
        if (pref.getString("username", null) != null) {
            ((Home) getActivity()).visibilityItem();
        } else {
            ((Home) getActivity()).hideItem();

        }

        super.onResume();
    }
}