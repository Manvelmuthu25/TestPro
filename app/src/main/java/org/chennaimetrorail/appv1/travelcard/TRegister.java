package org.chennaimetrorail.appv1.travelcard;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.NothingSelectedSpinnerAdapter;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PhoneVerifyModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.RegisterModal;
//import org.chennaimetrorail.appv1.readsms.SmsReceiver;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.RegistrationVerify;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 1/24/2018.
 */

public class TRegister extends Fragment {

    String TAG = "TRegister";

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    EditText user_name, pass_word;

    /*Storage for Login details*/
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    Calendar myCalendar;
    String gender_selectedId;
    RadioGroup gender_selected;
    EditText name_edt, dob_edt, phone_edt, email_edt, password_edt, confirm_password_edt,get_otp;
    Button reg_submit_btn;
    /*OTP Dialog...*/
    Button otp_verify_btn;
    TextView otp_status_txt, otp_send_to, resend_otp, otp_cancel_btn;
    View view;
    String version;
    CheckBox termsC_check;
    private RadioButton radioButton;
    private String android_id;
    private NothingSelectedSpinnerAdapter toast;

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "check radio button value" + android_id);
        int_chk = new Internet_connection_checking(getActivity());
        myCalendar = Calendar.getInstance();
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.travelcard_register, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setTitle("");
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

        TextView back_user_register = (TextView) view.findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
            }
        });
        termsC_check = view.findViewById(R.id.termsC_check);
        name_edt = (EditText) view.findViewById(R.id.name_edt);
        dob_edt = (EditText) view.findViewById(R.id.dob_edt);
        gender_selected = (RadioGroup) view.findViewById(R.id.gender_selected);
        phone_edt = (EditText) view.findViewById(R.id.phone_edt);
        email_edt = (EditText) view.findViewById(R.id.email_edt);
        password_edt = (EditText) view.findViewById(R.id.password_edt);
        confirm_password_edt = (EditText) view.findViewById(R.id.confirm_password_edt);
        reg_submit_btn = (Button) view.findViewById(R.id.reg_submit_btn);
        reg_submit_btn.setTypeface(fontStyle.helveticabold_CE);
        TextView termsandc = view.findViewById(R.id.termsandc);
        termsandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setData(Uri.parse("http://chennaimetrorail.org/cmrl-apps-terms"));
                startActivity(i);
            }
        });
        reg_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });

        // find the radiobutton by returned id

        gender_selected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioButton = (RadioButton) getActivity().findViewById(checkedId);

                gender_selectedId = radioButton.getText().toString();

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dob_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, 2000, 00, 01).show();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    hidekeyboard();
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });

        return view;

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob_edt.setText(sdf.format(myCalendar.getTime()));
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
    private void validateRegister() {

        // Reset errors.
        name_edt.setError(null);
        dob_edt.setError(null);
        phone_edt.setError(null);
        email_edt.setError(null);
        password_edt.setError(null);
        confirm_password_edt.setError(null);

        final String selected_genderValue;
        boolean cancel = false;
        View focusView = null;
        if (gender_selectedId == null) {
            Toast.makeText(getActivity(), "Select gender", Toast.LENGTH_LONG).show();
        }
        // Check for a valid UserName.
        if (TextUtils.isEmpty(name_edt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter your name", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(name_edt.getText().toString()) && !isname(name_edt.getText().toString())) {
            Toast.makeText(getActivity(), "Set 3 characters minimum", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(dob_edt.getText().toString())) {
            Toast.makeText(getActivity(), "Select date of birth", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phone_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter mobile number", Toast.LENGTH_LONG).show();
        }// Check for a valid password, if the user entered one.
        else if (!TextUtils.isEmpty(phone_edt.getText().toString()) && !isphone(phone_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter valid mobile number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter your email", Toast.LENGTH_LONG).show();
        }
        // Check for a valid password, if the user entered one.
        else if (!TextUtils.isEmpty(email_edt.getText().toString()) && !isEmail(email_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(password_edt.getText().toString()) && !isPasswordValid(password_edt.getText().toString())) {

//            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
//                    "Password should be minimum 8 characters" +
//                            " and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG);
//            ViewGroup group = (ViewGroup) toast.getView();
//            TextView messageTextView = (TextView) group.getChildAt(0);
//            messageTextView.setTextSize(15);
//            toast.show();

//
//            Toast toast = Toast.makeText(getActivity(), "Password should be minimum 8 characters" +
//                    " and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_SHORT);
//            LinearLayout toastLayout = (LinearLayout) toast.getView();
//            TextView toastTV = (TextView) toastLayout.getChildAt(0);
//            toastTV.setTextSize(30);
//            toast.show();

            Toast toast = Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
          //  Toast.makeText(getActivity(), "Password should be minimum 8 characters" + " and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(confirm_password_edt.getText().toString())) {
            Toast.makeText(getActivity(), "Enter confirm password", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(confirm_password_edt.getText().toString()) && !isPasswordValid(confirm_password_edt.getText().toString())) {

//            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG);
//            ViewGroup group = (ViewGroup) toast.getView();
//            TextView messageTextView = (TextView) group.getChildAt(0);
//            messageTextView.setTextSize(15);
//            toast.show();
            Toast toast = Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            //Toast.makeText(getActivity(), "Password should be minimum 8 characters and should contain 1 upper, 1 lower, 1 numeric and 1 special character.", Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isEmpty(password_edt.getText().toString()) && !TextUtils.isEmpty(confirm_password_edt.getText().toString()) && !isPasswordValid(password_edt.getText().toString(), confirm_password_edt.getText().toString())) {

            Toast.makeText(getActivity(), "Passwords doesn't match", Toast.LENGTH_LONG).show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            /* Call Login Process To Server*/
            if (!termsC_check.isChecked()) {
                Toast.makeText(getActivity(), "Please agree the terms and conditions", Toast.LENGTH_LONG).show();

            } else {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    AttemptToOTP(phone_edt.getText().toString());


                }

            }

        }
    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password, String confirm_password) {
        //TODO: Replace this with your own logic
        //  return password.equals(confirm_password);
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(confirm_password);
        return matcher.matches();
    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        // return password.length() > 7;
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
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

    private boolean isname(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }

    private void AttemptToOTP(final String reg_phNum) {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        //Old
        //  Call<PhoneVerifyModal> call = apiService.getRegisterVerify(reg_phNum, Constant.strApiKey, version);

        JsonObject values = new JsonObject();
        values.addProperty("RegisteredMobilnumber",reg_phNum);
        values.addProperty("secretcode","$3cr3t");
        values.addProperty("appv","ANDROID|"+version);

        Call<RegistrationVerify> call = apiService.RegisterVerify(values);

//        Call<RegistrationVerify> call = apiService.getRegisterVerify(reg_phNum, Constant.strApiKey,"ANDROID|"+ version);

        // Set up progress before call
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        call.enqueue(new Callback<RegistrationVerify>() {
            @Override
            public void onResponse(Call<RegistrationVerify> call, Response<RegistrationVerify> response) {

                Log.d(TAG,call.request().url().toString());
                int statusCode = response.code();
                Log.d(TAG, "Scode==" + statusCode);
                if (statusCode == 200) {
                    Log.d(TAG, "Scode12==" + statusCode);
                    loader.dismiss();
                    OTPdialogs(reg_phNum);
                    Log.d(TAG, "otp==" + ""+response.body().getStatus());

                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getActivity(), "Mobile number already registered", Toast.LENGTH_LONG).show();

                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationVerify> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });

    }


 /*   {
        "Name" : "Test",
            "DOB" : "03/07/2020",
            "Gender":"Male",
            "RegisteredMobile":"9600676567",
            "Email":"test@gmail.com",
            "Password":"Test@123",
            "DeviceId":"4589",
            "OTP":"",
            "secretcode": "$3cr3t",
            "appv": "2.0.0"
    }*/


    private void AttemptoRegister( String name, String dob, String gender, String reg_mobNumber, String email, String password, String pinEntry) throws Exception {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        //Old
        // Call<RegisterModal> call = apiService.getRegisterUser(name, dob, gender, reg_mobNumber, email, password, device_id, Constant.strApiKey, version);
        String text = "  Hello World  ";
        String key =  "44 02 d7 16 87 b6 bc 2c 10 89 c3 34 9f dc 19 fb 3d fb ba 88 24 af fb 76 76 e1 33 79 26 cd d6 02";
        String encryptedText = Encryption.encryptData(password, key);
        String decryptedText = Encryption.decryptData(encryptedText, key);


        Log.d("encode",""+encryptedText);


        JsonObject values = new JsonObject();
        values.addProperty("Name",name);
        values.addProperty("DOB",dob);
        values.addProperty("Gender", gender);
        values.addProperty("RegisteredMobile",reg_mobNumber);
        values.addProperty("Email",email);
        values.addProperty("Password",encryptedText);
        //values.addProperty("DeviceId",device_id+"123");
        values.addProperty("OTP",pinEntry);
        values.addProperty("secretcode",Constant.strApiKey);
        values.addProperty("appv","ANDROID|"+version);

        Log.d(TAG, "RegisterUserScode13" + values);
        Call<RegisterModal> call = apiService.getRegisterUser(values);

        // Set up progress before call
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);

        call.enqueue(new Callback<RegisterModal>() {
            @Override
            public void onResponse(Call<RegisterModal> call, Response<RegisterModal> response) {
                int statusCode = response.code();
                Log.d(TAG, "RegisterUserScode==" + statusCode);
                Log.d(TAG, "Respionse-> " + new Gson().toJson(response.body()));
                Log.d(TAG, "Respionse-> " + new Gson().toJson(response.errorBody()));



                if (statusCode == 200) {
                    Log.d(TAG, "Register" + statusCode);
                    loader.dismiss();
                    // Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    //  Log.d(TAG, "Statusmeg1==" + response.body().getStatus() + "otp=" + response.body().getMsg());
                    fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                    fragmentTransaction.commit();

                } else if (statusCode == 400) {
                    loader.dismiss();
                    if (response.body() != null) {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();Log.d(TAG, "Statusmege==" + response.body().getMsg());
                    } else if (response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());

                            String errorMsg = jObjError.getString("msg");
                            String statusMsg = jObjError.getString("status");
                            Log.d(TAG, "Error Msg " + errorMsg);
                            Log.d(TAG, "Error Status " + statusMsg);

                            Toast.makeText(getContext(), errorMsg,
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Something went wrong... Please try again...", Toast.LENGTH_LONG).show();
                        }
//                        Toast.makeText(getActivity(), response.errorBody(), Toast.LENGTH_LONG).show();Log.d(TAG, "Statusmege==" + response.errorBody().getMsg());
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();Log.d(TAG, "Statusmege==" + "Unabled to read the response for Bad Request");
                    }

                    // Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterModal> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });
    }

    //OTP Dialog Box
    public void OTPdialogs(String phone_txt) {


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.travelcard_otp_dialogbox);
        dialog.setCancelable(false);
        isShown = true;
        final PinEntryEditText pinEntry = (PinEntryEditText) dialog.findViewById(R.id.otp_txt_pin_entry);

        otp_verify_btn = (Button) dialog.findViewById(R.id.otp_verify_btn);
        otp_cancel_btn = (TextView) dialog.findViewById(R.id.otp_cancel_btn);
        otp_status_txt = (TextView) dialog.findViewById(R.id.status_txt);
        otp_send_to = (TextView) dialog.findViewById(R.id.send_to);
        resend_otp = (TextView) dialog.findViewById(R.id.resend_otp);

        StringBuilder phone_number_txt = new StringBuilder(phone_txt);
        for (int i = 0; i < 6; i++) {
            phone_number_txt.setCharAt(i, '*');
        }
        otp_send_to.setText("We have sent an OTP to " + phone_number_txt + ". Please enter it to continue");
       /* SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                pinEntry.setText(messageText);
            }
        });*/
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    AttemptToOTP(phone_edt.getText().toString());

                }

                dialog.dismiss();
            }
        });
        otp_cancel_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });

        otp_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (pinEntry != null) {
                    if (pinEntry.getText().toString().length()==6) {
                        try {
                            AttemptoRegister(name_edt.getText().toString(), dob_edt.getText().toString(), gender_selectedId, phone_edt.getText().toString(), email_edt.getText().toString(), confirm_password_edt.getText().toString(),pinEntry.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //AttemptoRegister(name_edt.getText().toString(),get_otp,dob_edt.getText().toString(),get_otp, gender_selectedId, phone_edt.getText().toString(), email_edt.getText().toString(), confirm_password_edt.getText().toString(), android_id);

                        dialog.dismiss();

                    } else if (pinEntry.getText().toString().equals("")) {
                        //AttemptoRegister(name_edt.getText().toString(),dob_edt.getText().toString(), gender_selectedId, phone_edt.getText().toString(), email_edt.getText().toString(), confirm_password_edt.getText().toString(), android_id);


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

        dialog.show();
    }

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}