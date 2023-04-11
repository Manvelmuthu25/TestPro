package org.chennaimetrorail.appv1.travelcard;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.ChangepassowordModal;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcab.TravelCabs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    EditText new_password_edtt, confirm_password_edtt, old_password_edtt;
    Button submit_password_btn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    /*Storage for Login details*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    String TAG = "ForgotPassword";
    TextView back_user_register;
    View view;
    String version,menuitemstatuscode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int_chk = new Internet_connection_checking(getActivity());
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.travalcard_changepasword, container, false);

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
        back_user_register = (TextView) view.findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        /*Forget Layout...................*/
        old_password_edtt = view.findViewById(R.id.old_password_edtt);
        new_password_edtt = (EditText) view.findViewById(R.id.new_password_edtt);
        confirm_password_edtt = (EditText) view.findViewById(R.id.confirm_password_edtt);
        submit_password_btn = (Button) view.findViewById(R.id.submit_password_btn);
        menuitemstatuscode = pref.getString("menuitemstatuscode", null);
        submit_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    /*Call Validate parameters method..*/

                    validateChangePassword();


                }
            }
        });

        back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(menuitemstatuscode.equalsIgnoreCase("1")) {
                    hidekeyboard();
                    editor.putString("menuitemstatuscode", "1");
                    editor.apply();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                    fragmentTransaction.commit();
                }else if(menuitemstatuscode.equalsIgnoreCase("2")){
                    hidekeyboard();
                    editor.putString("menuitemstatuscode", "2");
                    editor.apply();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                    fragmentTransaction.commit();
                }else {
                    hidekeyboard();
                    editor.putString("menuitemstatuscode", "1");
                    editor.apply();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                    fragmentTransaction.commit();
                }

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {


                    if(menuitemstatuscode.equalsIgnoreCase("1")) {

                        hidekeyboard();
                        editor.putString("menuitemstatuscode", "1");
                        editor.apply();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();

                    }else if(menuitemstatuscode.equalsIgnoreCase("2")){

                        hidekeyboard();
                        editor.putString("menuitemstatuscode", "2");
                        editor.apply();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                        fragmentTransaction.commit();

                    }else {
                        hidekeyboard();
                        editor.putString("menuitemstatuscode", "1");
                        editor.apply();
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();

                    }


                    return true;
                }
                return false;
            }


        });

        return view;

    }


    private void validatePasswords() {

        if (old_password_edtt.getText().toString().equals(pref.getString("password", null))) {

            if (new_password_edtt.getText().toString().equals(confirm_password_edtt.getText().toString())) {
                try {
                    ChangePasswordAttempt(pref.getString("registeredmobile", null),old_password_edtt.getText().toString(), confirm_password_edtt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "New passwords doesn't match", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Old passwords doesn't match", Toast.LENGTH_LONG).show();

        }

    }

  /*  {
        "MobileNumber" : "9600676567",
"OldPassword" : "Test@123",
"NewPassword":"Test@1234",
  "secretcode": "$3cr3t",
  "appv": "2.0.0"
}
    }
*/

    //Change password layout...
    private void ChangePasswordAttempt(final String forget_phNum, final String Old_Password,final String changed_password) throws Exception {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
       // Call<ChangepassowordModal> call = apiService.getChangeForgetPassword(forget_phNum, changed_password, Constant.strApiKey, version);
        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(changed_password, key);
        String encryptedText1 = Encryption.encryptData(Old_Password, key);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        JsonObject values = new JsonObject();
        values.addProperty("MobileNumber",forget_phNum);
        values.addProperty("OldPassword",encryptedText1);
        values.addProperty("NewPassword", encryptedText);
        values.addProperty("secretcode",Constant.strApiKey);
        values.addProperty("tokenid", refreshedToken);
        values.addProperty("appv","ANDROID|"+version);
        Log.e(TAG, "getNewsstatus1232" + values);

        String token = pref.getString("QR_JWTTOKEN","");

        Log.e("token", " = "+token);

        Call<ChangepassowordModal> call = apiService.getChangeForgetPassword( "Bearer "+token,values);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);

        call.enqueue(new Callback<ChangepassowordModal>() {
            @Override
            public void onResponse(Call<ChangepassowordModal> call, Response<ChangepassowordModal> response) {
                Log.d(TAG,call.request().url().toString());

                int statusCode = response.code();
                Log.d(TAG, "statusCode" + statusCode);
                if (statusCode == 200) {
                    if(menuitemstatuscode.equalsIgnoreCase("1")) {
                        loader.dismiss();
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_LONG).show();
                        editor.putString("password", changed_password);
                        editor.putString(" OldPassword",Old_Password);
                        editor.putString("menuitemstatuscode", "1");
                        editor.apply();
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();
                    }else if(menuitemstatuscode.equalsIgnoreCase("2")){
                        loader.dismiss();
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_LONG).show();
                        editor.putString("password", changed_password);
                        editor.putString(" OldPassword",Old_Password);
                        editor.putString("menuitemstatuscode", "2");
                        editor.apply();
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCabs());
                        fragmentTransaction.commit();
                    }else {
                        loader.dismiss();
                        Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_LONG).show();
                        editor.putString("password", changed_password);
                        editor.putString(" OldPassword",Old_Password);
                        editor.putString("menuitemstatuscode", "1");
                        editor.apply();
                        fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                        fragmentTransaction.commit();
                    }


                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getActivity(), "Password must be strong", Toast.LENGTH_LONG).show();

                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChangepassowordModal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                loader.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });

    }


    /*Check Login Details Hear...!*/
    private void validateChangePassword() {

        if (TextUtils.isEmpty(old_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter old password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(old_password_edtt.getText().toString()) && !isPasswordValid(old_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter valid password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(new_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(new_password_edtt.getText().toString()) && !isPasswordValid(new_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(confirm_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter confirm password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(confirm_password_edtt.getText().toString()) && !isPasswordValid(confirm_password_edtt.getText().toString())) {
            Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else {
            validatePasswords();

        }
    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}