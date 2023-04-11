package org.chennaimetrorail.appv1.travelcard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.billdesk.sdk.PaymentOptions;
import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Cardvalidate;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PaymentStatusModal;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 2/2/2018.
 */

public class QuickRechargeActivity extends Activity implements View.OnClickListener {

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    EditText quick_rc_number, quick_rc_amount;
    Button quick_rc_submitbtn, quick_rc_btn_100, quick_rc_btn_200, quick_rc_btn_500;
    TextView back_user_register;
    SharedPreferences pref;
    String TAG = "QuickRechargeActivity";
    String finaldate;

    String version;
    public static String SLASH_MM_DD_YYYY = "dd-MMM-yyyy";
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.travelcard_quickrecharge);
        FontStyle fontStyle = new FontStyle();
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fontStyle.Changeview(QuickRechargeActivity.this);
        int_chk = new Internet_connection_checking(this);
        quick_rc_number = (EditText) findViewById(R.id.quick_rc_number);
        quick_rc_amount = (EditText) findViewById(R.id.quick_rc_amount);
        back_user_register = (TextView) findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_submitbtn = (Button) findViewById(R.id.quick_rc_submitbtn);
        quick_rc_btn_100 = (Button) findViewById(R.id.quick_rc_btn_100);
        quick_rc_btn_200 = (Button) findViewById(R.id.quick_rc_btn_200);
        quick_rc_btn_500 = (Button) findViewById(R.id.quick_rc_btn_500);
        quick_rc_submitbtn.setOnClickListener(this);
        quick_rc_btn_100.setOnClickListener(this);
        quick_rc_btn_200.setOnClickListener(this);
        quick_rc_btn_500.setOnClickListener(this);
        TextView tooltxt_title = findViewById(R.id.tooltxt_title);
        tooltxt_title.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_btn_100.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_btn_200.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_btn_500.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_submitbtn.setTypeface(fontStyle.helveticabold_CE);

        pref = getApplication().getSharedPreferences("LoginDetails", 0);


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
        Date date = null;
        try {
            date = (Date) formatter.parse(pref.getString("dob", null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("DDMMyyyy");
        try {
            Date finaldate = format.parse(String.valueOf(date));

            long difference = finaldate.getTime() - finaldate.getTime();

        }catch (Exception e) {

           // System.err.println(e.printStackTrace());
        }

       /* SimpleDateFormat newFormat = new SimpleDateFormat("DDMMyyyy");
      //  SimpleDateFormat newFormat = new SimpleDateFormat("DDMMyyyy");
        finaldate = newFormat.format(date);
        Log.d("Jackup", "test" + finaldate);
*/
        back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Connection = int_chk.checkInternetConnection();
        /*Check Internet Connection Connect Or Not*/
        if (!Connection) {

            Snackbar.make(this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_rc_submitbtn:
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    if (TextUtils.isEmpty(quick_rc_number.getText())) {

                        Toast.makeText(QuickRechargeActivity.this, "Enter card number", Toast.LENGTH_LONG).show();

                    } else if (!TextUtils.isEmpty(quick_rc_number.getText()) && !iscardValid(quick_rc_number.getText().toString())) {
                        Toast.makeText(QuickRechargeActivity.this, "Enter valid card number", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(quick_rc_amount.getText())) {

                        Toast.makeText(QuickRechargeActivity.this, "Enter amount", Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(quick_rc_amount.getText().toString()) == 0) {
                        Toast.makeText(QuickRechargeActivity.this, "Enter valid amount", Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(quick_rc_amount.getText().toString()) >=2501) {
                        Toast.makeText(QuickRechargeActivity.this, "Maximum recharge amount 2501", Toast.LENGTH_LONG).show();

                    } else {

                        ValidateCard(quick_rc_number.getText().toString());
                    }


                }

                break;
            case R.id.quick_rc_btn_100:

                quick_rc_amount.setText("100");

                break;
            case R.id.quick_rc_btn_200:

                quick_rc_amount.setText("200");
                break;
            case R.id.quick_rc_btn_500:
                quick_rc_amount.setText("500");

                break;
        }
    }

    private boolean iscardValid(String card) {
        //TODO: Replace this with your own logic
        return card.length() > 9;
    }

    private void paymentCall(String strMsg, String strToken) {

        SampleCallBack callbackObj = new SampleCallBack();

        String strEmail = pref.getString("email", null);
        String strMobile = pref.getString("registeredmobile", null);
        System.out.println("msg:- " + strMsg);
        Intent intent = new Intent(QuickRechargeActivity.this, PaymentOptions.class);
        intent.putExtra("msg", strMsg); // pg_msg
        intent.putExtra("token", strToken);
        intent.putExtra("user-email", strEmail);
        intent.putExtra("user-mobile", strMobile + finaldate);
        intent.putExtra("callback", callbackObj);
        intent.putExtra("orientation", Configuration.ORIENTATION_PORTRAIT);
        startActivity(intent);
        finish();

    }


    public void SendPaymentStatustoServer() {

        Random ran = new Random();
        final int uniqueID = ran.nextInt(6) + 5;
        String pattern = "dd/MM/yyyy hh:mm:ss a";
        final String dateInString = new SimpleDateFormat(pattern).format(new Date());

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        JsonObject values = new JsonObject();
        values.addProperty("username", pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("smart_card_number",  quick_rc_number.getText().toString());
        values.addProperty("transdate", dateInString);
        values.addProperty("price", quick_rc_amount.getText().toString());
        values.addProperty("mobilenumber", pref.getString("registeredmobile", null));
        values.addProperty("transactionsource", "AMOB");
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv", "ANDROID|" + version);
        Log.d(TAG, "LoginRe" + values);
        Call<PaymentStatusModal> call = apiService.add_Card_Transactionstatus(values);

//        Call<PaymentStatusModal> call = apiService.add_Card_Transactionstatus(pref.getString("username", null),
//        pref.getString("password", null), quick_rc_number.getText().toString(),
//        dateInString, quick_rc_amount.getText().toString(),
//        pref.getString("registeredmobile", null),
//        "AMOB", pref.getString("securitycode", null),version);

        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<PaymentStatusModal>() {
            @Override
            public void onResponse(Call<PaymentStatusModal> call, Response<PaymentStatusModal> response) {
                int statusCode = response.code();

                Log.d("manivelpayment",call.request().url().toString());
                Log.e("statusCode", "" +statusCode+ pref.getString("username", null) + pref.getString("password", null) + quick_rc_number.getText().toString() + String.valueOf(uniqueID) + dateInString + quick_rc_amount.getText().toString() + "4" + "Pending");
                if (statusCode == 200) {
                    Log.e("StringMSG", "" + response.body().getStrmsg());
                    progressDoalog.dismiss();

                    paymentCall(response.body().getStrmsg(), response.body().getTokendetails());
                    Log.e("Tokendetails", "" +response.body().getTokendetails());

                } else if (statusCode == 400) {
                    progressDoalog.dismiss();
                    Toast.makeText(QuickRechargeActivity.this, response.body().getStrmsg(), Toast.LENGTH_LONG).show();
                    Log.e("errormeg", "" +response.body().getStrmsg());

                }else {
                    progressDoalog.dismiss();
                    Toast.makeText(QuickRechargeActivity.this, response.body().getStrmsg(), Toast.LENGTH_LONG).show();
                    Log.e("errormeg", "" +response.body().getStrmsg());

                }

            }

            @Override
            public void onFailure(Call<PaymentStatusModal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                progressDoalog.dismiss();
                Toast.makeText(QuickRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


    }

    /*Attempt To LOGIN Process to Server........*/
    private void ValidateCard(final String add_cart_number) {

        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(this, "", "Loading...", true);
        /*Send Parameters to the Api*/
//        Call<Cardvalidate> call = apiService.getupdate_CardList(pref.getString("username", null),
//        pref.getString("password", null), add_cart_number, "1", pref.getString("securitycode", null),version);

        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("card_number",add_cart_number);
        values.addProperty("cardresult","1");
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<Cardvalidate> call = apiService.getupdate_CardList(values);
        call.enqueue(new Callback<Cardvalidate>() {
            @Override
            public void onResponse(Call<Cardvalidate> call, Response<Cardvalidate> response) {
                int statusCode = response.code();
                Log.d("AddCardvalidate123",call.request().url().toString());
                Log.d(TAG, "AddCardvalidate=====" + pref.getString("username", null) + pref.getString("password", null) + add_cart_number);
                if (statusCode == 200) {
                    Log.d(TAG, "statusCode111" + response.body().getReason());
                    if (response.body().getReason().equalsIgnoreCase("VALID CARD")) {
                        loader.dismiss();
                        SendPaymentStatustoServer();
                    }else if (response.body().getReason().equalsIgnoreCase("TRIP CARD")) {
                        loader.dismiss();
                        Toast.makeText(QuickRechargeActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();

                    } else {
                        loader.dismiss();
                        Toast.makeText(QuickRechargeActivity.this, "Enter valid card number", Toast.LENGTH_LONG).show();
                    }
                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(QuickRechargeActivity.this,response.body().getMsg(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Cardvalidate> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                loader.dismiss();
                Toast.makeText(QuickRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


}
