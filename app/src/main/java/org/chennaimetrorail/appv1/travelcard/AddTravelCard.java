package org.chennaimetrorail.appv1.travelcard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.AddcardModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Cardvalidate;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 2/5/2018.
 */

public class AddTravelCard extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    EditText add_cart_number_edt, add_card_nikname_edt;
    Button add_card;
    SharedPreferences pref;
    /*Storage for Login details*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;

    LinearLayout toolbar_menu;
    TextView menu_textview;

    String version;
    String TAG = "AddTravelCard";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        int_chk = new Internet_connection_checking(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.travelcard_add_new, container, false);
//        Log.e(TAG, "secretcode" + pref.getString("secretcode", null));
//        Log.e(TAG, "version" + version);

        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setTitle("");
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*Login Layout...................*/

        add_cart_number_edt = (EditText) view.findViewById(R.id.add_cart_number_edt);
        add_card_nikname_edt = (EditText) view.findViewById(R.id.add_card_nikname_edt);
        add_card = (Button) view.findViewById(R.id.add_card);
        add_card.setTypeface(fontStyle.helveticabold_CE);
        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    /*Call Validate parameters method..*/
                    validateAddcard();


                }
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    hidekeyboard();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }


        });
        menu_textview = (TextView) view.findViewById(R.id.menu_textview);
        menu_textview.setTypeface(fontStyle.helveticabold_CE);
        menu_textview.setText("Add New Travel Card");
        menu_textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_left, 0, 0, 0);
        toolbar_menu = (LinearLayout) view.findViewById(R.id.toolbar_menu);
        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        menu_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                fragmentTransaction.commit();
            }
        });
        return view;

    }


    //Error Messages
    public void dialogs() {

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialogs);
        dialog.setCancelable(false);
        isShown = true;


        TextView tv_msg = (TextView) dialog.findViewById(R.id.dialog_texts);
        tv_msg.setText("" + dialog_msgs);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_dialogs_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    /*Check Login Details Hear...!*/
    private void validateAddcard() {


        // Check for a valid UserName.
        if (TextUtils.isEmpty(add_cart_number_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter card number", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(add_cart_number_edt.getText().toString()) && !isPasswordValid(add_cart_number_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter valid card number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(add_card_nikname_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter nick name", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(add_card_nikname_edt.getText().toString()) && !isNiknameValid(add_card_nikname_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Set 3 characters minimum", Toast.LENGTH_LONG).show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            /* Call Login Process To Server*/
            ValidateCard(add_cart_number_edt.getText().toString());


        }
    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /*Password Validate Method*/
    private boolean isNiknameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }

    /*Attempt To LOGIN Process to Server........*/
    private void AddCardAction(final String add_cart_number, final String add_card_nikname) {

        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);

        /*Send Parameters to the Api*/
//        Call<AddcardModal> call = apiService.getT_AddCardList(pref.getString("username", null),
//                pref.getString("password", null),
//                add_cart_number, add_card_nikname, pref.getString("securitycode", null), version);


        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("card_number",add_cart_number);
        values.addProperty("nik_name",add_card_nikname);
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<AddcardModal> call = apiService.AddCardList(values);

        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<AddcardModal>() {
            @Override
            public void onResponse(Call<AddcardModal> call, Response<AddcardModal> response) {

                Log.d("addcard", call.request().url().toString());
                int statusCode = response.code();
                Log.d(TAG, "StatusCode" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    Log.d(TAG, "StatusCodesucess" + statusCode);
                    loader.dismiss();
                    hidekeyboard();
                    Toast.makeText(getActivity(), "New card added successfully", Toast.LENGTH_LONG).show();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                    fragmentTransaction.commit();
                }
                /*Get Login Bad Response...*/
                else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getActivity(), "Entered card number is already exist", Toast.LENGTH_LONG).show();
                } else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddcardModal> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                loader.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*Attempt To LOGIN Process to Server........*/
    private void ValidateCard(final String add_cart_number) {
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*Send Parameters to the Api*/
       /* Call<Cardvalidate> call = apiService.getupdate_CardList(pref.getString("username", null),
                pref.getString("password", null),
                add_cart_number, "1", pref.getString("securitycode", null), version);*/

        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("card_number",add_cart_number);
        values.addProperty("cardresult","1");
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<Cardvalidate> call = apiService.getupdate_CardList(values);

        Log.d(TAG, "addcart" + values);
        call.enqueue(new Callback<Cardvalidate>() {
            @Override
            public void onResponse(Call<Cardvalidate> call, Response<Cardvalidate> response) {

                Log.d(TAG, call.request().url().toString());
                int statusCode = response.code();
                Log.d(TAG, "AddCardvalidate=====" + statusCode + pref.getString("username",
                        null) + pref.getString("password", null) + add_cart_number);
                if (statusCode == 200) {
                    Log.d(TAG, "statusCodesuces" + response.body().getReason());
                    if (response.body().getReason().equalsIgnoreCase("VALID CARD")) {
                        loader.dismiss();
                        AddCardAction(add_cart_number_edt.getText().toString(), add_card_nikname_edt.getText().toString());

                        Log.d(TAG, "statusCode111" +add_card_nikname_edt+add_cart_number_edt);

                    } else if (response.body().getReason().equalsIgnoreCase("TRIP CARD")) {
                        loader.dismiss();
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    } else {
                        loader.dismiss();
                        Toast.makeText(getActivity(), "Invalid card", Toast.LENGTH_LONG).show();
                    }
                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                } else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Cardvalidate> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                loader.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        TextView tv = (TextView) popupView.findViewById(R.id.t_sign_out);
        TextView t_all_history = (TextView) popupView.findViewById(R.id.t_all_history);
        TextView t_change_password = (TextView) popupView.findViewById(R.id.change_passwordtxt);
        t_all_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewAllTransactionActivity.class);
                startActivity(i);
            }
        });
        t_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_place, new ForgotPassword());
                fragmentTransaction.commit();
                popupWindow.dismiss();
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences pref = getContext().getSharedPreferences("LoginDetails", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", null);
                editor.commit();

                if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "true")){
                    Log.d("Data  " ,"" + pref.getString("AvailFreeRide",null));
                    Log.d("FirstIf","1");
                    ((Home) getActivity()).  freeridevisibilityItem();
                } else if ((pref.getString("username", null) !=null) && ( pref.getString("AvailFreeRide",null)== "false")){


                    Log.d("Sec","2");
                    ((Home) getActivity()).visibilityItem();


                } else {
                    Log.d("ThirdIf","3");
                    ((Home) getActivity()). hideItem();

                }

//                if (pref.getString("username", null) != null) {
//                    ((Home) getContext()).visibilityItem();
//                } else {
//                    ((Home) getActivity()).hideItem();
//
//                }


                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
                popupWindow.dismiss();
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

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
