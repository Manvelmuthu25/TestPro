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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.billdesk.sdk.PaymentOptions;
import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Cardvalidate;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.PaymentStatusModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TransactionListModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalCardlistModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravelcardHistorymodal;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcard.travelcardadapter.TravelCardhistoryAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by 102525 on 2/3/2018.
 */

public class NormalRechargeActivity extends Activity implements View.OnClickListener {

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    TextView rc_cardNumber_txt, rc_cardNikname_txt, rc_viewallhistrory, card_balance, phonenumber, name_list, ason_date;
    EditText rc_amount_edttext;
    Switch sw1;
    Button rc_submitnormal_btn, rc_ed_100, rc_ed_200, rc_ed_500;
    TextView back_user_register;
    List<TransactionListModal> transactionListModal_array;
    SharedPreferences pref;
    RecyclerView recy_transaction_history;
    String TAG = "NormalRechargeActivity";
    String finaldate;
    String card_id;
    TextView error_note;
    String strPGMsg;
    String version;

    public static String SLASH_MM_DD_YYYY = "dd-MMM-yyyy";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.travelcard_normalrecharge);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        FontStyle fontStyle = new FontStyle();
        ;
        fontStyle.Changeview(NormalRechargeActivity.this);
        int_chk = new Internet_connection_checking(this);
        pref = getApplication().getSharedPreferences("LoginDetails", 0);
        rc_viewallhistrory = (TextView) findViewById(R.id.rc_viewallhistrory);
        recy_transaction_history = (RecyclerView) findViewById(R.id.recy_transaction_htory);
        recy_transaction_history.setLayoutManager(new LinearLayoutManager(NormalRechargeActivity.this));
        back_user_register = findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        rc_amount_edttext = (EditText) findViewById(R.id.rc_amount_edttext);
        rc_cardNikname_txt = (TextView) findViewById(R.id.rc_cardNikname_txt);
        rc_cardNikname_txt.setTypeface(fontStyle.helveticabold_CE);
        rc_cardNumber_txt = (TextView) findViewById(R.id.rc_cardNumber_txt);
        rc_cardNumber_txt.setTypeface(fontStyle.helveticabold_CE);
        error_note = (TextView) findViewById(R.id.error_note);
        rc_submitnormal_btn = (Button) findViewById(R.id.rc_submitnormal_btn);
        rc_submitnormal_btn.setTypeface(fontStyle.helveticabold_CE);
        rc_ed_100 = (Button) findViewById(R.id.rc_ed_100);
        rc_ed_200 = (Button) findViewById(R.id.rc_ed_200);
        rc_ed_500 = (Button) findViewById(R.id.rc_ed_500);
        TextView rct_name = findViewById(R.id.rct_name);
        card_balance = (TextView) findViewById(R.id.card_balance);
        // phonenumber=(TextView)findViewById(R.id.phonenumber);
//        sw1=(Switch)findViewById(R.id.switch1);
        ason_date = (TextView) findViewById(R.id.ason_date);
        rct_name.setTypeface(fontStyle.helveticabold_CE);
        rc_submitnormal_btn.setOnClickListener(this);
        rc_ed_100.setOnClickListener(this);
        rc_ed_200.setOnClickListener(this);
        rc_ed_500.setOnClickListener(this);
        rc_ed_100.setTypeface(fontStyle.helveticabold_CE);
        rc_ed_200.setTypeface(fontStyle.helveticabold_CE);
        rc_ed_500.setTypeface(fontStyle.helveticabold_CE);

        //strPGMsg = "AIRMTST|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|892409133";

        TextView tooltxt_title = findViewById(R.id.tooltxt_title);
        tooltxt_title.setTypeface(fontStyle.helveticabold_CE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rc_cardNikname_txt.setText(extras.getString("card_name"));
            rc_cardNumber_txt.setText(extras.getString("Card_number"));
            card_id = extras.getString("card_id");
            ValidateCard(extras.getString("Card_number"));
            Log.d(TAG, "jdlk" + card_id);
        }

        back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        rc_viewallhistrory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NormalRechargeActivity.this, ViewAllTransactionActivity.class);
                startActivity(i);
            }
        });
        GetCardtransactionHistory();

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

        } catch (Exception e) {

            // System.err.println(e.printStackTrace());
        }
     /*   SimpleDateFormat newFormat = new SimpleDateFormat("DDMMyyyy");
        finaldate = newFormat.format(date);
        Log.d("Jackup", "test" + finaldate);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        Connection = int_chk.checkInternetConnection();
        /*Check Internet Connection Connect Or Not*/
        if (!Connection) {

            Snackbar.make(this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {

            GetCardtransactionHistory();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rc_submitnormal_btn:

                String str1;
//                if (sw1.isChecked())
//                    str1 = sw1.getTextOn().toString();
//                else
//                    str1 = sw1.getTextOff().toString();

                ///Toast.makeText(getApplicationContext(), "Switch1 -  " + str1 + " \n" + "Switch2 - " ,Toast.LENGTH_SHORT).show();


                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    if (TextUtils.isEmpty(rc_amount_edttext.getText())) {

                        Toast.makeText(NormalRechargeActivity.this, "Enter amount", Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(rc_amount_edttext.getText().toString()) == 0) {
                        Toast.makeText(NormalRechargeActivity.this, "Enter valid amount", Toast.LENGTH_LONG).show();

                    } else {
                        if (Integer.parseInt(rc_amount_edttext.getText().toString()) >= 2501) {
                            Toast.makeText(NormalRechargeActivity.this, "Maximum recharge amount 2501", Toast.LENGTH_LONG).show();

                        } else {

                            SendPaymentStatustoServer();
                        }
                    }

                }

                break;
            case R.id.rc_ed_100:

                rc_amount_edttext.setText("100");

                break;
            case R.id.rc_ed_200:

                rc_amount_edttext.setText("200");

                break;
            case R.id.rc_ed_500:

                rc_amount_edttext.setText("500");

                break;
        }
    }


    /*Attempt To LOGIN Process to Server........*/
    private void ValidateCard(final String add_cart_number) {
        Log.d(TAG, "test" + add_cart_number);
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(this, "", "Loading...", true);
        /*Send Parameters to the Api*/
        //  Call<Cardvalidate> call = apiService.getupdate_CardList(pref.getString("username", null),
        //  pref.getString("password", null),
        //  add_cart_number, "1", pref.getString("securitycode", null),version);
//
        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("card_number",add_cart_number);
        values.addProperty("cardresult","1");
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<Cardvalidate> call = apiService.getupdate_CardList(values);
        Log.d(TAG, "Cardvalidate" + values);
        call.enqueue(new Callback<Cardvalidate>() {
            @Override
            public void onResponse(Call<Cardvalidate> call, Response<Cardvalidate> response) {
                int statusCode = response.code();
                Log.d("cartbalance==", call.request().url().toString());
                Log.d(TAG, "AddCardvalidate=====" + pref.getString("username", null) + pref.getString("password", null) + add_cart_number);
                if (statusCode == 200) {
                    Log.d(TAG, "statusCode111" + response.body().getReason());
                    if (response.body().getReason().equalsIgnoreCase("VALID CARD")) {
                        loader.dismiss();
                        card_balance.setText(response.body().getBalance());
                        //phonenumber.setText(response.body().getMobilenumber());
                        ason_date.setText(response.body().getDate());
                        rc_submitnormal_btn.setClickable(true);
                        error_note.setVisibility(View.GONE);
                        //    SendPaymentStatustoServer();


                    } else if (response.body().getReason().equalsIgnoreCase("TRIP CARD")) {
                        loader.dismiss();
                        rc_submitnormal_btn.setClickable(false);
                        Toast.makeText(NormalRechargeActivity.this, "You cannot recharge trip card.Please contact Station Controller.", Toast.LENGTH_LONG).show();

                    } else {
                       loader.dismiss();
                        rc_submitnormal_btn.setText("Card Expired");
                        rc_submitnormal_btn.setClickable(false);
                        error_note.setVisibility(View.VISIBLE);
                        //Toast.makeText(NormalRechargeActivity.this, "Enter valid card number", Toast.LENGTH_LONG).show();
                    }
                } else if (statusCode == 400) {
                  /*  Intent i = new Intent(getApplicationContext(),TravelcardLogin.class);
                    startActivity(i);*/
                    rc_submitnormal_btn.setEnabled(false);
                  loader.dismiss();
                    Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Cardvalidate> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
               loader.dismiss();
                Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void SendPaymentStatustoServer() {

        Random ran = new Random();
        final int uniqueID = ran.nextInt(6) + 5;
        String pattern = "dd/MM/yyyy hh:mm:ss a";
        final String dateInString = new SimpleDateFormat(pattern).format(new Date());


        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
//        string username, string password, string smart_card_number, string transdate,
//                string price, string mobilenumber, string transactionsource, string
//        secretcode, string appv, string card_id = null

        JsonObject values = new JsonObject();
        values.addProperty("username", pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("smart_card_number",  rc_cardNumber_txt.getText().toString());
        values.addProperty("transdate", dateInString);
        values.addProperty("price",rc_amount_edttext.getText().toString());
        values.addProperty("mobilenumber", pref.getString("registeredmobile", null));
        values.addProperty("transactionsource", pref.getString("securitycode", null));
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv", "ANDROID|" + version);
        values.addProperty("card_id", card_id);

        Call<PaymentStatusModal> call = apiService.add_Card_Transactionstatus(values);
        Log.d(TAG, "PaymentStatusModal" + values);

//        Call<PaymentStatusModal> call = apiService.add_Card_Transactionstatus(pref.getString("username", null),
//        pref.getString("password", null), rc_cardNumber_txt.getText().toString(), dateInString,
//        rc_amount_edttext.getText().toString(),version,card_id, pref.getString("registeredmobile", null),
//       "AMOB",pref.getString("securitycode",null));



//        // Set up progress before call

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

                Log.d("manivel", call.request().url().toString());
                Log.d("statusCode200", "" + statusCode + pref.getString("username", null) + pref.getString("password", null) + rc_cardNumber_txt.getText().toString() + dateInString + rc_amount_edttext.getText().toString() + pref.getString("email", null) + pref.getString("registeredmobile", null) + "AMOB");
                if (statusCode == 200) {
                    Log.d("StringMSG", "" + response.body().getStrmsg());
                    Log.d("StringToken", "" + response.body().getTokendetails());
                    progressDoalog.dismiss();
                    paymentCall(response.body().getStrmsg(), response.body().getTokendetails());


                } else if (statusCode == 400) {
                   progressDoalog.dismiss();
                    Toast.makeText(NormalRechargeActivity.this,response.body().getStrmsg(), Toast.LENGTH_LONG).show();
                } else {
                    progressDoalog.dismiss();
                    Toast.makeText(NormalRechargeActivity.this, response.body().getStrmsg(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PaymentStatusModal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                progressDoalog.dismiss();
                Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


    }


    private void paymentCall(String strMsg, String strToken) {
        SampleCallBack callbackObj = new SampleCallBack(); // callback
        String strEmail = pref.getString("email", null);
        String strMobile = pref.getString("registeredmobile", null);
        System.out.println("msg:- " + strMsg);
        Log.e("magesspay","n" +strMsg);
        System.out.println("msg-------------:- " + strToken);
        Intent intent = new Intent(NormalRechargeActivity.this, PaymentOptions.class);
        intent.putExtra("msg", strMsg); // pg_msg //Live
        //intent.putExtra("msg", strPGMsg); // pg_msg //Test
        intent.putExtra("token", strToken);
        intent.putExtra("user-email", strEmail);
        intent.putExtra("user-mobile", strMobile + finaldate);
        intent.putExtra("callback", callbackObj);
        intent.putExtra("orientation", Configuration.ORIENTATION_PORTRAIT);
        intent.putExtra("MerchantID", "");
        intent.putExtra("CustomerID", "");
        intent.putExtra("SIDetails", "");
        intent.putExtra("TxnAmount", "");
        intent.putExtra("BankID", "");
        intent.putExtra("Filler2", "NA");
        intent.putExtra("Filler3", "NA");
        intent.putExtra("CurrencyType", "INR");
        intent.putExtra("ItemCode", "DIRECT");
        intent.putExtra("TypeField1", "R");
        intent.putExtra("UserID", "mercid");
        intent.putExtra("Filler4", "NA");
        intent.putExtra("Filler5", "NA");
        intent.putExtra("TypeField2", "F");
        startActivity(intent);
        finish();

    }


    public void GetCardtransactionHistory() {

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

     /*   Call<TravelcardHistorymodal> call = apiService.getTransactionListHistory(pref.getString("username", null),
      pref.getString("password", null),
       rc_cardNumber_txt.getText().toString(),
       pref.getString("securitycode", null), version);
*/
       /* string username, string password, string cardnumber, string secretcode, string appv
*/
        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("cardnumber", rc_cardNumber_txt.getText().toString());
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<TravelcardHistorymodal> call = apiService.getTransactionListHistory(values);
        Log.d(TAG, "TravelcardHistorymodal" + values);


        // Set up progress before call

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();
        call.enqueue(new Callback<TravelcardHistorymodal>() {
            @Override
            public void onResponse(Call<TravelcardHistorymodal> call, Response<TravelcardHistorymodal> response) {
                int statusCode = response.code();
                Log.d(TAG, call.request().url().toString());
                if (statusCode == 200) {
                    Log.e("statu", "" + statusCode);
                    Log.d(TAG, call.request().url().toString());
                    Log.e("statusCode200", "" + response.body().getTransaction_List());
                    transactionListModal_array = response.body().getTransaction_List();
                    progressDoalog.dismiss();
                    recy_transaction_history.setAdapter(new TravelCardhistoryAdapter(transactionListModal_array, R.layout.travelcard_history_custome, NormalRechargeActivity.this));
                } else if (statusCode == 400) {
                    Log.e("statusCode400", "" + pref.getString("username", null) + pref.getString("password", null) + rc_cardNumber_txt.getText().toString());
                    progressDoalog.dismiss();
                   // Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                } else {
                  progressDoalog.dismiss();
                   // Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<TravelcardHistorymodal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                progressDoalog.dismiss();
                //Toast.makeText(NormalRechargeActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
