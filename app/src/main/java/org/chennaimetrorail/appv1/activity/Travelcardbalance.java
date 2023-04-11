package org.chennaimetrorail.appv1.activity;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.Nfcmodel;
import org.chennaimetrorail.appv1.modal.Travelcardtagreader;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Travelcardbalance extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RelativeLayout r1, r2;
    private NfcAdapter mNfcAdapter;
    TextView card_amount, rc_viewallhistrory;
    TextView amt1, amt2, trans41, trans42, trans43, trans44, trans45, trans46, nfc_name, nfc_phonenumber;
    ImageView back;

    SharedPreferences pref;
    String version;
    PendingIntent pendingIntent;

    TextView rechargesDate1, rechargesAmt1, rechargesDate2, rechargesAmt2, Balance;

    TextView tranDate1, tranType1, tranStation1, tranAmt1, tranDate2, tranType2, tranStation2, tranAmt2, tranDate3, tranType3, tranStation3, tranAmt3, tranDate4, tranType4, tranStation4, tranAmt4, tranDate5, tranType5, tranStation5, tranAmt5, tranDate6, tranType6, tranStation6, tranAmt6, cardNo;

    LinearLayout image_view_back;

    TableRow tr1, tr2, tr3, tr4, tr5, tr6, tr7, tr8, tr9, tr10, tr11, tr12;

    RelativeLayout rechrl1, rechrl2;

    View line1, line2, line3, line4, line5;

    private enum CardType {
        IsoDep,
        FeliCa;

        public String toString() {
            switch (this) {
                case IsoDep:
                    return "android.nfc.tech.IsoDep";
                case FeliCa:
                    return "android.nfc.tech.NfcF";
                default:
                    return "Unknown";
            }
        }
    }

    String cardNumber = "";
    String cardType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_tag_reader);

        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);

        pref = getApplication().getSharedPreferences("LoginDetails", 0);
        image_view_back = (LinearLayout) findViewById(R.id.image_view_back);
        rc_viewallhistrory = (TextView) findViewById(R.id.rc_viewallhistrory);
        back = (ImageView) findViewById(R.id.back);


        card_amount = findViewById(R.id.card_amount);
        cardNo = findViewById(R.id.cardNo);

        Balance = findViewById(R.id.Balance);
        amt1 = findViewById(R.id.amt1);
        amt2 = findViewById(R.id.amt2);
        trans41 = findViewById(R.id.trans41);
        trans42 = findViewById(R.id.trans42);
        trans43 = findViewById(R.id.trans43);
        trans44 = findViewById(R.id.trans44);
        trans45 = findViewById(R.id.trans45);
        trans46 = findViewById(R.id.trans46);

        rechargesDate1 = findViewById(R.id.rechargesDate1);
        rechargesAmt1 = findViewById(R.id.rechargesAmt1);


        nfc_name = findViewById(R.id.nfc_name);
        nfc_phonenumber = findViewById(R.id.nfc_phonenumber);


        rechargesDate2 = findViewById(R.id.rechargesDate2);
        rechargesAmt2 = findViewById(R.id.rechargesAmt2);


        tranDate1 = findViewById(R.id.tranDate1);
        tranType1 = findViewById(R.id.tranType1);
        tranStation1 = findViewById(R.id.tranStation1);
        tranAmt1 = findViewById(R.id.tranAmt1);

        tranDate2 = findViewById(R.id.tranDate2);
        tranType2 = findViewById(R.id.tranType2);
        tranStation2 = findViewById(R.id.tranStation2);
        tranAmt2 = findViewById(R.id.tranAmt2);

        tranDate3 = findViewById(R.id.tranDate3);
        tranType3 = findViewById(R.id.tranType3);
        tranStation3 = findViewById(R.id.tranStation3);
        tranAmt3 = findViewById(R.id.tranAmt3);

        tranDate4 = findViewById(R.id.tranDate4);
        tranType4 = findViewById(R.id.tranType4);
        tranStation4 = findViewById(R.id.tranStation4);
        tranAmt4 = findViewById(R.id.tranAmt4);

        tranDate5 = findViewById(R.id.tranDate5);
        tranType5 = findViewById(R.id.tranType5);
        tranStation5 = findViewById(R.id.tranStation5);
        tranAmt5 = findViewById(R.id.tranAmt5);

        tranDate6 = findViewById(R.id.tranDate6);
        tranType6 = findViewById(R.id.tranType6);
        tranStation6 = findViewById(R.id.tranStation6);
        tranAmt6 = findViewById(R.id.tranAmt6);


        tr1 = findViewById(R.id.tr1);
        tr2 = findViewById(R.id.tr2);
        tr3 = findViewById(R.id.tr3);
        tr4 = findViewById(R.id.tr4);
        tr5 = findViewById(R.id.tr5);
        tr6 = findViewById(R.id.tr6);
        tr7 = findViewById(R.id.tr7);
        tr8 = findViewById(R.id.tr8);
        tr9 = findViewById(R.id.tr9);
        tr10 = findViewById(R.id.tr10);
        tr11 = findViewById(R.id.tr11);
        tr12 = findViewById(R.id.tr12);

        rechrl1 = findViewById(R.id.rechrl1);
        rechrl2 = findViewById(R.id.rechrl2);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        line5 = findViewById(R.id.line5);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (mNfcAdapter == null) {
            //  Toast.makeText(this, "The device does not support nfc", Toast.LENGTH_SHORT).show();

            Toast toast = Toast.makeText(this, "Your phone does not support NFC. To view the card balance, please use a NFC enabled phone.", Toast.LENGTH_LONG);
            TextView v = (TextView) findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();


            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            startActivity(new Intent("android.settings.NFC_SETTINGS"));
            Toast.makeText(this, "NFC is not enabled in your phone. Please enable NFC to view the card balance.", Toast.LENGTH_SHORT).show();
        }


        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
            }
        });


        rc_viewallhistrory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);


    }


    @Override
    protected void onNewIntent(Intent intent) {
        String[] tagList = new String[0];
        tranDate1.setText("0");
        tranDate2.setText("0");
        tranDate3.setText("0");
        tranDate4.setText("0");
        tranDate5.setText("0");
        tranDate6.setText("0");
        rechargesDate1.setText("0");
        rechargesDate2.setText("0");


        try {
            super.onNewIntent(intent);
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                    || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                tagList = tag.getTechList();

                if (ArrayUtils.contains(tagList, CardType.IsoDep.toString())) {

                    final ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Travel Card Reading...");
                    progress.setMessage("Please hold the card to the back of your phone");
                    progress.show();

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            progress.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 100);

                    //   card_amount.setText(Travelcardtagreader.ShowCardInformation(tag, intent));

                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.VISIBLE);


                    String[] rechargeHistory = new String[0];
                    String[] cardNumType = new String[0];

                    rechargeHistory = Travelcardtagreader.ShowCardInformation(tag, intent);

                    String cardNumber = "";
                    String cardType = "";

                    cardNumType = rechargeHistory[1].split(":");
                    cardNumber = cardNumType[0];
                    cardType = cardNumType[1];
                    //Travelcardphonenumber(cardNumber);

                    Log.d("fileContents10 ", "reHist1: " + rechargeHistory[1]);
                    Log.d("fileContents10 ", "cardNumType: " + cardNumType[0] + " //// " + cardNumType[1]);
                    Log.d("fileContents10 ", "cardType: " + cardType);
                    if (cardType.equals("Value") || cardType.equals("Trip")) {

                        tranAmt1.setText(" ");
                        tranAmt2.setText(" ");
                        tranAmt3.setText(" ");
                        tranAmt4.setText(" ");
                        tranAmt5.setText(" ");
                        tranAmt6.setText(" ");
                        if (cardType.equals("Trip")) {
                            card_amount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            Balance.setText(R.string.tripbalance);
                            amt1.setText(R.string.trips);
                            amt2.setText(R.string.trips);
                            rechargesAmt1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            rechargesAmt2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                            trans41.setText(R.string.trips);
                            trans42.setText(R.string.trips);
                            trans43.setText(R.string.trips);
                            trans44.setText(R.string.trips);
                            trans45.setText(R.string.trips);
                            trans46.setText(R.string.trips);
                            tranAmt1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tranAmt2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tranAmt3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tranAmt4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tranAmt5.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tranAmt6.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        } else {
                            card_amount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupeess, 0, 0, 0);
                            Balance.setText(R.string.amtbalance);
                            amt1.setText(R.string.amt);
                            amt2.setText(R.string.amt);
                            rechargesAmt1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            rechargesAmt2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);

                            trans41.setText(R.string.amt);
                            trans42.setText(R.string.amt);
                            trans43.setText(R.string.amt);
                            trans44.setText(R.string.amt);
                            trans45.setText(R.string.amt);
                            trans46.setText(R.string.amt);
                            tranAmt1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            tranAmt2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            tranAmt3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            tranAmt4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            tranAmt5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);
                            tranAmt6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ruppess, 0, 0, 0);

                        }
                        card_amount.setText(rechargeHistory[0]);//balance amount
                        Log.d("test_amount", "" + rechargeHistory[0]);
                        cardNo.setText(cardNumber);     //CMRL card number
                        Log.d("test_cardnumber", "" + (cardNumber));
                        //Travelcardphonenumber(cardNumber);

                        Log.d("fileContents10 ", "reHi:date: " + rechargeHistory[4]);
                        Log.d("fileContents10 ", "reHi:amt: " + rechargeHistory[5]);
                        rechargesDate1.setText(rechargeHistory[4]);//Last recharges date
                        rechargesAmt1.setText(rechargeHistory[5]);//Last recharges amount

                        if (rechargeHistory[2].equals("2")) {
                            rechargesDate2.setText(rechargeHistory[6]); //Last recharges date
                            rechargesAmt2.setText(rechargeHistory[7]); //Last recharges amount
                        } else {
                            //hide the row 2
                        }


                        // the first 7 values in the array "rechargeHistory" are related to the recharge history and the total counts.
                        // the transaction values are stored from array position 8.
                        // for each transaction there are 4 values and a maximum of 6 records will be there. So,
                        // record 1 values will be in position 8, 9, 10 & 11
                        // record 2 values will be in position 12, 13, 14 & 15
                        // record 3 values will be in position 16, 17, 18 & 19
                        // record 4 values will be in position 20, 21, 22 & 23
                        // record 5 values will be in position 24, 25, 26 & 27
                        // record 6 values will be in position 28, 29, 30 & 31
                        // but we need to show in descending order. record 6 should be on top and record 1 should be in the bottom.
                        // identify how many records are there and show that as the top most record.

                        Integer totalTranCount, arrayPosition;
                        totalTranCount = Integer.parseInt(rechargeHistory[3]);

                        if (Integer.parseInt(rechargeHistory[3]) > 0) {
                            // depending on the total count of transaction records, the top record has to be identified.
                            // if there are 4 records, then the below formula will result in 7 + ((4-1) * 4) = 19
                            // so, arrayPosition + 1 will show the 20th value in the array
                            // and so this block will display 20, 21, 22 & 23.

                            arrayPosition = 7 + ((totalTranCount - 1) * 4);

                            tranDate1.setText(rechargeHistory[arrayPosition + 1]);
                            tranType1.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt1.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation1.setText(rechargeHistory[arrayPosition + 4]);
                        }
                        if (Integer.parseInt(rechargeHistory[3]) > 1) {
                            // depending on the total count of transaction records, the top record has to be identified.
                            // if there are 4 records, then the below formula will result in 7 + ((4-2) * 4) = 15
                            // so, arrayPosition + 1 will show the 16th value in the array
                            // and so this block will display 16, 17, 18 & 19.

                            arrayPosition = 7 + ((totalTranCount - 2) * 4);

                            tranDate2.setText(rechargeHistory[arrayPosition + 1]);
                            tranType2.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt2.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation2.setText(rechargeHistory[arrayPosition + 4]);
                        }
                        if (Integer.parseInt(rechargeHistory[3]) > 2) {
                            arrayPosition = 7 + ((totalTranCount - 3) * 4);

                            tranDate3.setText(rechargeHistory[arrayPosition + 1]);
                            tranType3.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt3.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation3.setText(rechargeHistory[arrayPosition + 4]);
                        }
                        if (Integer.parseInt(rechargeHistory[3]) > 3) {
                            arrayPosition = 7 + ((totalTranCount - 4) * 4);

                            tranDate4.setText(rechargeHistory[arrayPosition + 1]);
                            tranType4.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt4.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation4.setText(rechargeHistory[arrayPosition + 4]);
                        }
                        if (Integer.parseInt(rechargeHistory[3]) > 4) {
                            arrayPosition = 7 + ((totalTranCount - 5) * 4);

                            tranDate5.setText(rechargeHistory[arrayPosition + 1]);
                            tranType5.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt5.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation5.setText(rechargeHistory[arrayPosition + 4]);
                        }
                        if (Integer.parseInt(rechargeHistory[3]) > 5) {
                            arrayPosition = 7 + ((totalTranCount - 6) * 4);

                            tranDate6.setText(rechargeHistory[arrayPosition + 1]);
                            tranType6.setText(rechargeHistory[arrayPosition + 2]);
                            tranAmt6.setText(rechargeHistory[arrayPosition + 3]);
                            tranStation6.setText(rechargeHistory[arrayPosition + 4]);
                        }


                        String strtranDate1 = tranDate1.getText().toString();
                        String strtranDate2 = tranDate2.getText().toString();
                        String strtranDate3 = tranDate3.getText().toString();
                        String strtranDate4 = tranDate4.getText().toString();
                        String strtranDate5 = tranDate5.getText().toString();
                        String strtranDate6 = tranDate6.getText().toString();

                        if (strtranDate1.equals("0")) {
                            tr1.setVisibility(View.GONE);
                            tr2.setVisibility(View.GONE);
                            line1.setVisibility(View.GONE);
                        } else {
                            tr1.setVisibility(View.VISIBLE);
                            tr2.setVisibility(View.VISIBLE);
                            line1.setVisibility(View.VISIBLE);

                        }

                        if (strtranDate2.equals("0")) {
                            tr3.setVisibility(View.GONE);
                            tr4.setVisibility(View.GONE);
                            line2.setVisibility(View.GONE);
                        } else {
                            tr3.setVisibility(View.VISIBLE);
                            tr4.setVisibility(View.VISIBLE);
                            line2.setVisibility(View.VISIBLE);

                        }

                        if (strtranDate3.equals("0")) {
                            tr5.setVisibility(View.GONE);
                            tr6.setVisibility(View.GONE);
                            line3.setVisibility(View.GONE);
                        } else {
                            tr5.setVisibility(View.VISIBLE);
                            tr6.setVisibility(View.VISIBLE);
                            line3.setVisibility(View.VISIBLE);

                        }

                        if (strtranDate4.equals("0")) {
                            tr7.setVisibility(View.GONE);
                            tr8.setVisibility(View.GONE);
                            line4.setVisibility(View.GONE);
                        } else {
                            tr7.setVisibility(View.VISIBLE);
                            tr8.setVisibility(View.VISIBLE);
                            line4.setVisibility(View.VISIBLE);

                        }

                        if (strtranDate5.equals("0")) {
                            tr9.setVisibility(View.GONE);
                            tr10.setVisibility(View.GONE);
                            line5.setVisibility(View.GONE);
                        } else {
                            tr9.setVisibility(View.VISIBLE);
                            tr10.setVisibility(View.VISIBLE);
                            line5.setVisibility(View.VISIBLE);

                        }
                        if (strtranDate6.equals("0")) {
                            tr11.setVisibility(View.GONE);
                            tr12.setVisibility(View.GONE);
                        } else {
                            tr11.setVisibility(View.VISIBLE);
                            tr12.setVisibility(View.VISIBLE);

                        }


                        String strrecharg1 = rechargesDate1.getText().toString();
                        String strrecharg2 = rechargesDate2.getText().toString();

                        if (strrecharg1.equals("")) {
                            rechrl1.setVisibility(View.GONE);

                        } else {
                            rechrl1.setVisibility(View.VISIBLE);


                        }
                        if (strrecharg2.equals("")) {
                            rechrl2.setVisibility(View.GONE);

                        } else {
                            rechrl2.setVisibility(View.VISIBLE);


                        }
                        if (strrecharg1.equals("0") || strrecharg1.equals("")) {
                            rechrl1.setVisibility(View.GONE);

                        } else {
                            rechrl1.setVisibility(View.VISIBLE);


                        }
                        if (strrecharg2.equals("0") || strrecharg2.equals("")) {
                            rechrl2.setVisibility(View.GONE);

                        } else {
                            rechrl2.setVisibility(View.VISIBLE);


                        }
                        Travelcardphonenumber(cardNumber);
                    } else {
                        //show toast as unknown card type
                        Toast.makeText(this, "This travel card is not supported.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //show toast as unknown card type
                    Toast.makeText(this, "This travel card is not supported yet.", Toast.LENGTH_SHORT).show();

                }

                //Travelcardphonenumber(cardNumber);

            }
        } catch (Exception e) {
            // handle the error response here.
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Travelcardphonenumber(String cardNumber) {

        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);


        Log.d("cardNumber ", "reHist1: " + cardNumber);
        Call<Nfcmodel> call = apiService.getUserProfileBasedCardNumber(cardNumber);
        call.enqueue(new Callback<Nfcmodel>() {
            @Override
            public void onResponse(Call<Nfcmodel> call, Response<Nfcmodel> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getProfilename() != null) {
                        nfc_name.setText(response.body().getProfilename());
                    }
                    if (response.body().getMobilenumber() != null) {
                        nfc_phonenumber.setText(response.body().getMobilenumber());
                    }
                } else if (statusCode == 400) {
                    Toast.makeText(Travelcardbalance.this, "Unable to retrieve User information", Toast.LENGTH_LONG).show();

                } else {
                    nfc_phonenumber.setText("resp " + statusCode);
                    Toast.makeText(Travelcardbalance.this, "Error: " + statusCode + " Unable to retrieve User information", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Nfcmodel> call, Throwable t) {
                nfc_phonenumber.setText("onFailure");
                Toast.makeText(Travelcardbalance.this, "Unable to retrieve the User Details", Toast.LENGTH_LONG).show();

            }
        });
    }

}

