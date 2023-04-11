package org.chennaimetrorail.appv1.travelcard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Annotation;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.forgetotp_modal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.ChangepassowordModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PhoneVerifyModal;
//import org.chennaimetrorail.appv1.readsms.SmsReceiver;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by 102525 on 2/5/2018.
 */

public class ForgotPassword extends Fragment {
    public Dialog dialog, dialog_otp;
    public PinEntryEditText pinEntry;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    EditText forget_phonenum_edt, new_password_edtt, confirm_password_edtt;
    Button get_otp_btn, submit_password_btn;
    SharedPreferences pref;
    /*Storage for Login details*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    String dialog_msgs;
    String TAG = "ForgotPassword";
    TextView back_user_register;
    LinearLayout verifiy_layout, change_password_layout;
    /*OTP Dialog...*/
    Button otp_verify_btn;
    TextView otp_status_txt, otp_send_to, resend_otp, otp_cancel_btn;
    //SmsReceiver smsListener;
    View view;
    String version,regid,regsucees,RegistrationId,message;
    String   QRtoken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        QRtoken = pref.getString("QRjwttoken", null);

        // smsListener = new SmsReceiver();
        dialog_otp = new Dialog(getActivity());
        pinEntry = (PinEntryEditText) dialog_otp.findViewById(R.id.otp_txt_pin_entry);
        int_chk = new Internet_connection_checking(getActivity());
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.travelcard_forgetpassword, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setTitle("");
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        back_user_register = (TextView) view.findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        /*Forgot Layout...................*/
        change_password_layout = (LinearLayout) view.findViewById(R.id.change_password_layout);
        new_password_edtt = (EditText) view.findViewById(R.id.new_password_edtt);
        confirm_password_edtt = (EditText) view.findViewById(R.id.confirm_password_edtt);
        submit_password_btn = (Button) view.findViewById(R.id.submit_password_btn);

        verifiy_layout = (LinearLayout) view.findViewById(R.id.verifiy_layout);
        forget_phonenum_edt = (EditText) view.findViewById(R.id.forget_phonenum_edt);
        get_otp_btn = (Button) view.findViewById(R.id.get_otp_btn);


        Log.d(TAG, "viewdetails" + pref.getString("m_pinLoginState", null));

        if (pref.getString("m_pinLoginState", null) == "yes") {
            change_password_layout.setVisibility(View.VISIBLE);
            verifiy_layout.setVisibility(View.GONE);
            back_user_register.setText("Change Password");
        } else {
            change_password_layout.setVisibility(View.GONE);
            verifiy_layout.setVisibility(View.VISIBLE);
            back_user_register.setText("Forgot Password");
        }


        Log.d(TAG, "viewdetails" + pref.getString("m_pinLoginState", null));

        if (pref.getString("m_pinLoginState", null) == "yes") {
            change_password_layout.setVisibility(View.VISIBLE);
            verifiy_layout.setVisibility(View.GONE);
            back_user_register.setText("Change Password");
        } else {
            change_password_layout.setVisibility(View.GONE);
            verifiy_layout.setVisibility(View.VISIBLE);
            back_user_register.setText("Forgot Password");
        }

        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    /*Call Validate parameters method..*/
                    validateForget();


                }
            }
        });

        submit_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {


                    Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
                    /*Call Validate parameters method..*/


                    try {
                        validatePasswords();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    hidekeyboard();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
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
    private void validateForget() {


        // Check for a valid UserName.
        if (TextUtils.isEmpty(forget_phonenum_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter your mobile number", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(forget_phonenum_edt.getText().toString()) && !isphoneValid(forget_phonenum_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter valid mobile number", Toast.LENGTH_LONG).show();
        } else {
            AttemptToOTP(forget_phonenum_edt.getText().toString());
        }
    }

    /*Password Validate Method*/
    private boolean isphoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() > 9;
    }


    private boolean isnewpassword(String newpassword) {
        //TODO: Replace this with your own logic
        //return newpassword.length() > 7;
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(newpassword);
        return matcher.matches();
    }

    private boolean isconfirmpassword(String confirmpassword) {
        //TODO: Replace this with your own logic
        //return confirmpassword.length() > 7;
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(confirmpassword);
        return matcher.matches();
    }


    private void validatePasswords() throws Exception {


            if (TextUtils.isEmpty(new_password_edtt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_LONG).show();


        } else if (!TextUtils.isEmpty(new_password_edtt.getText().toString()) && !isnewpassword(new_password_edtt.getText().toString())) {

            Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(confirm_password_edtt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter confirm password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(confirm_password_edtt.getText().toString()) && !isconfirmpassword(confirm_password_edtt.getText().toString())) {

            Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else {

            if (new_password_edtt.getText().toString().equals(confirm_password_edtt.getText().toString())) {

                ChangePasswordAttempt(forget_phonenum_edt.getText().toString(), confirm_password_edtt.getText().toString());
                // ChangePasswordAttemp(forget_phonenum_edt.getText().toString(), confirm_password_edtt.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Passwords doesn't match", Toast.LENGTH_LONG).show();
            }
        }

    }

    /*  {
          "RegistrationId": "48",
              "OTP": "849678",
      }
  */
    private void AttemptToOTP(final String forget_phonenum_edt) {

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        // Call<PhoneVerifyModal> call = apiService.getForgetPassword(forget_phonenum_edt, Constant.strApiKey, "ANDROID|"+version);

        JsonObject values = new JsonObject();

        values.addProperty("ForgetPassword",forget_phonenum_edt);
        values.addProperty("secretcode","$3cr3t");
        values.addProperty("appv","ANDROID|"+version);
        Call<PhoneVerifyModal> call = apiService.getForgetPassword(values);

        // final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        call.enqueue(new Callback<PhoneVerifyModal>() {
            @Override
            public void onResponse(Call<PhoneVerifyModal> call, Response<PhoneVerifyModal> response) {

                int statusCode = response.code();


                Log.d(TAG, call.request().url().toString());
                if (statusCode == 200) {

                    regsucees = response.body().getStatus();
                    regid = response.body().getRegistrationId();
                    Log.d(TAG, "registeridvalues" + response.body().getRegistrationId());

                    Log.d(TAG, "registerid=>" + forget_phonenum_edt + response.body().getRegistrationId() + response.body().getRegistrationId());

                    //loader.dismiss();
                    OTPdialogs(forget_phonenum_edt, regid);
                    Log.d(TAG, "registerid=>" + forget_phonenum_edt + response.body().getRegistrationId());

                } else if (statusCode == 400) {

                    /// Log.e("errorBody", new GsonBuilder().setLenient().create().toJson(response.errorBody()));

                    Gson gson = new GsonBuilder().create();
                    ApiError pojo = new ApiError();
                    try {
                        pojo = gson.fromJson(response.errorBody().string(), ApiError.class);
                        Toast.makeText(getActivity(), pojo.getMsg(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) { // handle failure at error parse }

                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneVerifyModal> call, Throwable t) {
                // Log error here since request failed

                Log.e(TAG, t.toString());
                //  loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void forgotPasswordValidateOTP(final String otpPin,final String regid) {

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final JsonObject values = new JsonObject();
        values.addProperty("RegistrationId",regid);
        values.addProperty("OTP",otpPin);

        Call<forgetotp_modal> call = apiService.getForgetValidateOTP(values);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        call.enqueue(new Callback<forgetotp_modal>() {
            @Override
            public void onResponse(Call<forgetotp_modal> call, Response<forgetotp_modal> response) {

                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.d(TAG, "sucees" +statusCode);

                    loader.dismiss();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "msg" +response.body().getMsg());
                    loader.dismiss();
                    change_password_layout.setVisibility(View.VISIBLE);
                    verifiy_layout.setVisibility(View.GONE);

                } else if (statusCode == 400) {
                    loader.dismiss();
                    Gson gson = new GsonBuilder().create();
                    ApiError pojo = new ApiError();
                    try {
                        pojo = gson.fromJson(response.errorBody().string(), ApiError.class);
                        Toast.makeText(getActivity(), pojo.getMsg(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) { // handle failure at error parse }

                    }

                    change_password_layout.setVisibility(View.GONE);
                    verifiy_layout.setVisibility(View.VISIBLE);
                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<forgetotp_modal> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });

    }

    //OTP Dialog Box
    public void OTPdialogs(String phone_txt, final String RegistrationId) {
        // start listening for refresh local file list in

        dialog_otp = new Dialog(getActivity());
        dialog_otp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_otp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog_otp.setContentView(R.layout.travelcard_otp_dialogbox);
        dialog_otp.setCancelable(false);
        isShown = true;
        // smsListener = new SmsReceiver();
        pinEntry = (PinEntryEditText) dialog_otp.findViewById(R.id.otp_txt_pin_entry);
        otp_verify_btn = (Button) dialog_otp.findViewById(R.id.otp_verify_btn);
        otp_cancel_btn = (TextView) dialog_otp.findViewById(R.id.otp_cancel_btn);
        otp_status_txt = (TextView) dialog_otp.findViewById(R.id.status_txt);
        otp_send_to = (TextView) dialog_otp.findViewById(R.id.send_to);
        resend_otp = (TextView) dialog_otp.findViewById(R.id.resend_otp);

        StringBuilder phone_number_txt = new StringBuilder(phone_txt);
        for (int i = 0; i < 6; i++) {
            phone_number_txt.setCharAt(i, '*');
        }
        otp_send_to.setText("We have sent an OTP to " + phone_number_txt + ". Please enter it to continue");
        Log.d(TAG, "reg1122==" + phone_number_txt);
      /*  SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                pinEntry.setText(messageText);
            }
        });
*/
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AttemptToOTP(forget_phonenum_edt.getText().toString());
                dialog_otp.dismiss();
            }
        });
        otp_cancel_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog_otp.dismiss();

            }
        });

        otp_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (pinEntry != null) {

                    if (pinEntry.getText().toString().length() == 6) {
                        //back_user_register.setText("Reset Password");

                        dialog_otp.dismiss();
                        forgotPasswordValidateOTP(pinEntry.getText().toString(),RegistrationId);

                    } else if (pinEntry.getText().toString().equals("")) {

                        Toast.makeText(getActivity(), "Enter the OTP", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                    } else {
                        Toast.makeText(getActivity(), "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                    }
                    /*pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                        @Override
                        public void onPinEntered(CharSequence str) {
                            if (str.toString().equals("1234")) {
                                Toast.makeText(Register.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "FAIL", Toast.LENGTH_SHORT).show();
                                pinEntry.setText(null);
                            }
                        }
                    });*/
                }
            }
        });

        dialog_otp.show();
    }

    //Change password layout...
    private void ChangePasswordAttempt(final String RegistrationId, final String changed_password) throws Exception {
        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(changed_password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);


        Log.d("encode",""+encryptedText);



        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        //  Call<ChangepassowordModal> call = apiService.getChangeForgetPassword(forget_phNum, changed_password, Constant.strApiKey, version);
        JsonObject values = new JsonObject();
        values.addProperty("RegistrationId",regid);
        values.addProperty("OTP",pinEntry.getText().toString());
        values.addProperty("NewPassword", encryptedText);
        values.addProperty("secretcode",Constant.strApiKey);
        values.addProperty("appv","ANDROID|"+version);
        Log.e(TAG, "getNewsstatus12" + values);
        Call<ChangepassowordModal> call = apiService.getUpdateForgetPassword( values);
        // Set up progress before call
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);

        call.enqueue(new Callback<ChangepassowordModal>() {
            @Override
            public void onResponse(Call<ChangepassowordModal> call, Response<ChangepassowordModal> response) {
                Log.d(TAG,call.request().url().toString());

                int statusCode = response.code();
                if (statusCode == 200) {
                    loader.dismiss();
                    Log.d(TAG, "statusCode200" + statusCode);
                    //            loader.dismiss();
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();
                    Toast.makeText(getActivity(), "Password reset successfully", Toast.LENGTH_LONG).show();


                } else if (statusCode == 400) {
                    loader.dismiss();
                    Gson gson = new GsonBuilder().create();
                    ApiError pojo = new ApiError();
                    try {
                        pojo = gson.fromJson(response.errorBody().string(), ApiError.class);
                        Toast.makeText(getActivity(), pojo.getMsg(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) { // handle failure at error parse }

                    }

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
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }

    //Change password layout...
//    private void ChangePasswordAttemp(final String forget_phonenum_edt, final String changed_password) {
//        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
//
//        //  Call<ChangepassowordModal> call = apiService.getChangeForgetPassword(forget_phNum, changed_password, Constant.strApiKey, version);
//        JsonObject values = new JsonObject();
//        values.addProperty("MobileNumber",forget_phonenum_edt);
//        //values.addProperty("OldPassword",Old_Password);
//        values.addProperty("NewPassword", changed_password);
//        values.addProperty("secretcode",Constant.strApiKey);
//        values.addProperty("appv","ANDROID|"+version);
//        Log.e(TAG, "getNewsstatus12" + values);
//
////        OkHttpClient client = new OkHttpClient();
////        Request request = new Request.Builder()
////                .get()
////                .addHeader("Content-type: application/json","Authorization")
////                .addHeader("Authorization" , "Bearer " +  pref.getString("QRjwttoken", null))
////                .build();
////        HashMap<String, String> headers = new HashMap<String, String>();
////        String authValue = "Bearer " + QRtoken;
////        headers.put("Authorization", authValue);
////        headers.put("Accept", "application/json; charset=UTF-8");
////        headers.put("Content-Type", "application/json; charset=UTF-8");
////        Log.v("nameValue","entered");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("Content-Type","application/json");
//        map.put("Authorization", "Bearer " +QRtoken);
//        // map.put("QRjwttoken",pref.getString("QRjwttoken", null));
//
//        Call<ChangepassowordModal> call = apiService.getChangeForgetPassword(values);
//        Log.d(TAG,"token"+pref.getString("QRjwttoken", null));
//
//        // Set up progress before call
//        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
//
//        call.enqueue(new Callback<ChangepassowordModal>() {
//            @Override
//            public void onResponse(Call<ChangepassowordModal> call, Response<ChangepassowordModal> response) {
//
//                int statusCode = response.code();
//                if (statusCode == 200) {
//                    Log.d(TAG, "statusCode200" + statusCode);
//                    loader.dismiss();
//                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
//                    fragmentTransaction.commit();
//                    Toast.makeText(getActivity(), "Password reset successfully", Toast.LENGTH_LONG).show();
//
//
//                } else if (statusCode == 400) {
//                    loader.dismiss();
//                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
//
//                }else {
//                    loader.dismiss();
//                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ChangepassowordModal> call, Throwable t) {
//                // Log error here since request failed
//                Log.e("error", t.toString());
//                loader.dismiss();
//                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
//            }
//
//
//        });
//
//
//    }

    //Error Messages
    public void dialogsstatus(String msg) {

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialogs);
        dialog.setCancelable(false);
        isShown = true;


        TextView tv_msg = (TextView) dialog.findViewById(R.id.dialog_texts);

        tv_msg.setText(msg);

        Button btn_ok = (Button) dialog.findViewById(R.id.btn_dialogs_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /*    class SmsReceiver extends BroadcastReceiver {

           private SharedPreferences preferences;

           @Override
           public void onReceive(Context context, Intent intent) {
               // TODO Auto-generated method stub

               if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                   Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                   SmsMessage[] msgs = null;
                   String msg_from;
                   if (bundle != null){
                       //---retrieve the SMS message received---
                       try{
                           Object[] pdus = (Object[]) bundle.get("pdus");
                           msgs = new SmsMessage[pdus.length];

                           for(int i=0; i<msgs.length; i++){
                               msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                               msg_from = msgs[i].getOriginatingAddress();
                               String msgBody = msgs[i].getMessageBody();
                               Log.d("jai","lskd"+msgBody);
                           }
                       }catch(Exception e){
   //                            Log.d("Exception caught",e.getMessage());
                       }
                   }
               }
           }


       }
   */
    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}