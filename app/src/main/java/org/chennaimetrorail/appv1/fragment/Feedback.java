package org.chennaimetrorail.appv1.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.NothingSelectedSpinnerAdapter;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.database.DatabaseHandler;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeedbackResponse;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Feedback extends Fragment {

    String TAG = "FeedbackError";

    List<StationList> station_list = new ArrayList<StationList>();
    DatabaseHandler db;
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Spinner station_listspinner;
    Dialog dialog;
    String dialog_msgs;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    EditText fb_username, fb_email, fb_ph_number, fb_msgbox;
    Button fb_submit;
    String version;
    RatingBar rateing_txt1, rateing_txt2, rateing_txt3, rateing_txt4, rateing_txt5, rateing_txt6, rateing_txt7, rateing_txt8, rateing_txt9, rateing_txt10;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    View view;
    public static Retrofit getResponsee() {

        Retrofit retrofit = null;
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.feedback, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        station_listspinner = view.findViewById(R.id.fb_station_name);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

        fb_username = view.findViewById(R.id.fb_username);
        fb_email = view.findViewById(R.id.fb_email);
        fb_ph_number = view.findViewById(R.id.fb_ph_number);
        fb_msgbox = view.findViewById(R.id.fb_msgbox);
        fb_submit = view.findViewById(R.id.fb_submit);
        rateing_txt1 = view.findViewById(R.id.rateing_txt1);
        rateing_txt2 = view.findViewById(R.id.rateing_txt2);
        rateing_txt3 = view.findViewById(R.id.rateing_txt3);
        rateing_txt4 = view.findViewById(R.id.rateing_txt4);
        rateing_txt5 = view.findViewById(R.id.rateing_txt5);
        rateing_txt6 = view.findViewById(R.id.rateing_txt6);
        rateing_txt7 = view.findViewById(R.id.rateing_txt7);
        rateing_txt8 = view.findViewById(R.id.rateing_txt8);
        rateing_txt9 = view.findViewById(R.id.rateing_txt9);
        rateing_txt10 = view.findViewById(R.id.rateing_txt10);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Feedback");

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();

        if(pref.getString("login_name", null)!=null&&pref.getString("registeredmobile", null)!=null&& pref.getString("email", null)!=null){
            fb_ph_number.setText(pref.getString("registeredmobile", null));
            fb_email.setText(pref.getString("email", null));
            fb_username.setText(pref.getString("login_name",null));
        }else {
            fb_email.setText("");
            fb_ph_number.setText("");
            fb_username.setText("");
        }

        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();
        db = new DatabaseHandler(getActivity());
        station_list = db.getAllStationdetails();

        if (station_list.size() > 0) {
            Collections.sort(station_list, new Comparator<StationList>() {
                @Override
                public int compare(final StationList stationList, final StationList t1) {
                    return stationList.getStation_LongName().compareTo(t1.getStation_LongName());
                }
            });
        }
        String[] spinnerArray = new String[station_list.size()];
        final HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
        if (spinnerArray != null) {

            for (int i = 0; i < station_list.size(); i++) {
                spinnerMap.put(i, station_list.get(i).getStation_Id());
                spinnerArray[i] = station_list.get(i).getStation_LongName();
            }
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);


        station_listspinner.setAdapter(spinnerArrayAdapter);
        station_listspinner.setAdapter(new NothingSelectedSpinnerAdapter(spinnerArrayAdapter, R.layout.nothing_selected_station, getActivity()));


        fb_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    validateFeedback();
                }

            }
        });

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hidekeyboard();
                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    hidekeyboard();
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

    /*Send Feedback Details to server*/
    private void SendFeedbackDetails(String name, String email, String cont_no, String r1, String r2, String r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String stationame, String msg) {


        final ApiInterface apiService = Feedback.getResponsee().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*Send Parameters to the Api*/
        Call<FeedbackResponse> call = apiService.sendFeedback(name, email, cont_no, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, stationame, msg, Constant.strApiKey, version);
        Log.d(TAG, "postValues" + name + email + cont_no + r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9 + r10 + stationame + msg + Constant.strApiKey + version);

        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {

                Log.d(TAG, call.request().url().toString());
                int statusCode = response.code();

                Log.d(TAG, "statusCodedd" + statusCode);

                if (statusCode == 200) {
                    loader.dismiss();
                    hidekeyboard();
                    Toast.makeText(getActivity(), "Thanks for your feedback", Toast.LENGTH_LONG).show();
                    Home home = new Home();
                    home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
                    home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
                    home.toolbrtxt_layout.setVisibility(View.GONE);
                    home.toolbar_img.setVisibility(View.VISIBLE);
                    home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "feedbackException" + t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*Check Login Details Hear...!*/
    private void validateFeedback() {


        // Check for a valid UserName.
        if (TextUtils.isEmpty(fb_username.getText().toString())) {
            Toast.makeText(getContext(), "Enter your name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(fb_email.getText().toString())) {
            Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(fb_email.getText().toString()) && !isEmail(fb_email.getText().toString())) {
            Toast.makeText(getContext(), "Enter valid email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(fb_ph_number.getText().toString())) {
            Toast.makeText(getContext(), "Enter your mobile number", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(fb_ph_number.getText().toString()) && !isphone(fb_ph_number.getText().toString())) {
            Toast.makeText(getContext(), "Enter valid mobile number", Toast.LENGTH_LONG).show();
        } else if (rateing_txt1.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate experience in train", Toast.LENGTH_LONG).show();
        } else if (station_listspinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select station", Toast.LENGTH_LONG).show();
        } else if (rateing_txt2.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate upkeep of station", Toast.LENGTH_LONG).show();
        } else if (rateing_txt3.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate rest room facilities", Toast.LENGTH_LONG).show();
        } else if (rateing_txt4.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate security services", Toast.LENGTH_LONG).show();
        } else if (rateing_txt5.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate ticketing experience", Toast.LENGTH_LONG).show();
        } else if (rateing_txt6.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate parking facilities", Toast.LENGTH_LONG).show();
        } else if (rateing_txt7.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate staff friendliness", Toast.LENGTH_LONG).show();
        } else if (rateing_txt8.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate facilities at station", Toast.LENGTH_LONG).show();
        } else if (rateing_txt9.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate safety", Toast.LENGTH_LONG).show();
        } else if (rateing_txt10.getRating() == 0) {
            Toast.makeText(getContext(), "Please rate total experience in CMRL", Toast.LENGTH_LONG).show();
        } else {

            SendFeedbackDetails(fb_username.getText().toString(),
                    fb_email.getText().toString(),
                    fb_ph_number.getText().toString(),
                    String.valueOf(rateing_txt1.getRating()),
                    String.valueOf(rateing_txt2.getRating()),
                    String.valueOf(rateing_txt3.getRating()),
                    String.valueOf(rateing_txt4.getRating()),
                    String.valueOf(rateing_txt5.getRating()),
                    String.valueOf(rateing_txt6.getRating()),
                    String.valueOf(rateing_txt7.getRating()),
                    String.valueOf(rateing_txt8.getRating()),
                    String.valueOf(rateing_txt9.getRating()),
                    String.valueOf(rateing_txt10.getRating()),
                    String.valueOf(station_listspinner.getSelectedItem()),
                    fb_msgbox.getText().toString());

        }
    }

    /*Password Validate Method*/
    private boolean isphone(String ph) {
        //TODO: Replace this with your own logic
        return ph.length() > 9;
    }

    public boolean isEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
