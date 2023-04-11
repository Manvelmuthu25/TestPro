package org.chennaimetrorail.appv1.travelcard;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TransactionAllListModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalcardDeletMadol;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravelcardHistorymodal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravelcardallHistorymodal;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcard.travelcardadapter.TravelCardAllhistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 2/15/2018.
 */

public class ViewAllTransactionActivity extends Activity{

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    TextView back_user_register;
    List<TransactionAllListModal> alltransactionListModal_array = new ArrayList<>();
    SharedPreferences pref;
    RecyclerView alltransaction_history;
    Spinner spinner;
    String TAG ="ViewAllTransactionActivity";

    TextView error_note;

    String version;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.travelcard_alltransaction_history);
        FontStyle fontStyle = new FontStyle();;
        fontStyle.Changeview(ViewAllTransactionActivity.this);
        int_chk = new Internet_connection_checking(this);
        pref = getApplication().getSharedPreferences("LoginDetails", 0);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        error_note = findViewById(R.id.error_note);
        alltransaction_history = (RecyclerView)findViewById(R.id.alltransaction_history);
        alltransaction_history.setLayoutManager(new LinearLayoutManager(ViewAllTransactionActivity.this));
        back_user_register = (TextView) findViewById(R.id.back_user_register);
        back_user_register.setTypeface(fontStyle.helveticabold_CE);
        TextView tooltxt_title = findViewById(R.id.tooltxt_title);
        tooltxt_title.setTypeface(fontStyle.helveticabold_CE);
// Array of choices
        String days[] = {"15Days","30Days","1Year"};

// Selection of the spinner
       spinner = (Spinner) findViewById(R.id.filter_date_spinner);

// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   R.layout.alltran_spinnertxt, days);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int sum = 0;
                 sum = (int) (id + 1);
                Log.d(TAG,"postion"+(int) (id + 1));


                Connection = int_chk.checkInternetConnection();
/*Check Internet Connection Connect Or Not*/
                if (!Connection) {

                    Snackbar.make(ViewAllTransactionActivity.this.findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {
/*Call Validate login parameters method..*/
                    GetCardtransactionHistory((int) (id + 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Error Messages
    public void dialogs() {

        dialog = new Dialog(this);
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


    public void GetCardtransactionHistory(int type){

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

//        Call<TravelcardallHistorymodal> call = apiService.getAllTransactionList(pref.getString("username",null),
//        pref.getString("password",null),String.valueOf(type),pref.getString("securitycode",null),version);
//

        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("passwd", pref.getString("password", null));
        values.addProperty("stype",String.valueOf(type));
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<TravelcardallHistorymodal> call = apiService.getAllTransactionList(values);

        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<TravelcardallHistorymodal>() {
            @Override
            public void onResponse(Call<TravelcardallHistorymodal> call, Response<TravelcardallHistorymodal> response) {
                int statusCode = response.code();

                Log.d(TAG,call.request().url().toString());
                if(statusCode == 200){

                    Log.d(TAG, "status12354"+statusCode);
                    Log.e(TAG,"status200"+response.body().getStatus()+pref.getString("username",null)+pref.getString("password",null));
                    alltransactionListModal_array = response.body().getAll_Transaction_List();
                    progressDoalog.dismiss();
                   alltransaction_history.setAdapter(new TravelCardAllhistoryAdapter(alltransactionListModal_array, R.layout.travelcard_history_custome, ViewAllTransactionActivity.this));
                    error_note.setVisibility(View.GONE);
                    alltransaction_history.setVisibility(View.VISIBLE);
                }else if(statusCode==400){
                    //Log.e(TAG,"status400"+response.body().getStatus());
                    progressDoalog.dismiss();
                    error_note.setVisibility(View.VISIBLE);
                    alltransaction_history.setVisibility(View.GONE);
                    error_note.setText("No transaction data were found");
                }else {
                   // Log.e(TAG,"statuserror"+response.body().getStatus());
                    progressDoalog.dismiss();
                    Toast.makeText(ViewAllTransactionActivity.this,R.string.somthinnot_right,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TravelcardallHistorymodal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                progressDoalog.dismiss();
                Toast.makeText(ViewAllTransactionActivity.this,R.string.somthinnot_right,Toast.LENGTH_LONG).show();
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
