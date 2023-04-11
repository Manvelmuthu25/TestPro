package org.chennaimetrorail.appv1;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.activity.Travelcardbalance;
import org.chennaimetrorail.appv1.modal.Nfcmodel;
import org.chennaimetrorail.appv1.modal.Travelcardtagreader;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

        public class Nfctestfile extends AppCompatActivity {

            private static final String TAG = "MainActivity";
            LinearLayout image_view_back;
            ImageView back;
            TextView nfc_name, nfc_phonenumber;
            SharedPreferences pref;
            String version;
            String cardNumber = "1100910792";
            String cardType = "";

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_nfctestfile);

                pref = getApplication().getSharedPreferences("LoginDetails", 0);
                image_view_back = (LinearLayout) findViewById(R.id.image_view_back);

                back = (ImageView) findViewById(R.id.back);

                nfc_name = findViewById(R.id.nfc_name);
                nfc_phonenumber = findViewById(R.id.nfc_phonenumber);






                        // nfc_name.setText(cardNumber);
                        //  nfc_phonenumber.setText("Display");
                        Travelcardphonenumber(cardNumber);

            }



            private void Travelcardphonenumber(String cardNumber) {
                Log.d(TAG, "tesst" + cardNumber);
                /// Toast.makeText(Travelcardbalance.this, "Inside the Travelcardphone function", Toast.LENGTH_LONG).show();
                //nfc_phonenumber.setText("Function");
                final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);


        Call<Nfcmodel> call = apiService.getUserProfileBasedCardNumber(
               cardNumber);

        call.enqueue(new Callback<Nfcmodel>() {
            @Override
            public void onResponse(Call<Nfcmodel> call, Response<Nfcmodel> response) {
                int statusCode = response.code();
                //   nfc_phonenumber.setText("response");
                if (statusCode == 200) {
                    nfc_phonenumber.setText("resp 200");
                    String name = response.body().getProfilename() +" "+ response.body().getMobilenumber();
                    Toast.makeText(Nfctestfile.this, name, Toast.LENGTH_LONG).show();

                    if (response.body().getProfilename() != null) {
                        nfc_name.setText(response.body().getProfilename());
                    }
                    if (response.body().getMobilenumber() != null) {
                        nfc_phonenumber.setText(response.body().getMobilenumber());
                    }
                } else if (statusCode == 400) {
                    nfc_phonenumber.setText("resp 400");
                    Toast.makeText(Nfctestfile.this, "Unable to retrieve User information", Toast.LENGTH_LONG).show();

                } else {
                    nfc_phonenumber.setText("resp " + statusCode);
                    Toast.makeText(Nfctestfile.this, "Error: " + statusCode + " Unable to retrieve User information", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Nfcmodel> call, Throwable t) {
                nfc_phonenumber.setText("onFailure");
                Toast.makeText(Nfctestfile.this, "Unable to retrieve the User Details", Toast.LENGTH_LONG).show();

            }
        });
    }

}