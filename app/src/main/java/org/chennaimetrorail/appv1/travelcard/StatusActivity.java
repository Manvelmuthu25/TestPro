package org.chennaimetrorail.appv1.travelcard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;

public class StatusActivity extends Activity {
	TextView status,recharge_status,recharge_number,recahrge_payment,back_user_register;
	ImageView payment_statusimg;
	SharedPreferences pref;
	String mStatus[];

	LinearLayout instruction,notetext;
	Button done_btn;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_status);
		FontStyle fontStyle = new FontStyle();;
		fontStyle.Changeview(StatusActivity.this);
		pref = getApplication().getSharedPreferences("LoginDetails", 0);
		status = (TextView) findViewById(R.id.payment_status);
		status.setTypeface(fontStyle.helveticabold_CE);
		payment_statusimg = (ImageView)findViewById(R.id.payment_statusimg);
		recharge_status = (TextView) findViewById(R.id.recharge_status);
		recharge_number = (TextView) findViewById(R.id.recharge_number);
		recahrge_payment = (TextView) findViewById(R.id.recahrge_payment);
		recahrge_payment.setTypeface(fontStyle.helveticabold_CE);
		back_user_register=(TextView)findViewById(R.id.back_user_register) ;
		back_user_register.setTypeface(fontStyle.helveticabold_CE);
		instruction = (LinearLayout)findViewById(R.id.instruction);
		notetext = (LinearLayout)findViewById(R.id.notetext);
		done_btn = findViewById(R.id.done_btn);
		done_btn.setTypeface(fontStyle.helveticabold_CE);
		TextView instruc_txt = findViewById(R.id.instruc_txt);
		instruc_txt.setTypeface(fontStyle.helveticabold_CE);
		Bundle bundle = this.getIntent().getExtras();



		mStatus = bundle.getString("status").toString().split("\\|");
		TextView tandc = findViewById(R.id.tandc);
		tandc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.addCategory(Intent.CATEGORY_BROWSABLE);
				i.setData(Uri.parse("http://chennaimetrorail.org/cmrl-apps-terms"));
				startActivity(i);
			}
		});
		tandc.setMovementMethod(LinkMovementMethod.getInstance());
	//	mStatus = "CHENMETRO|1100421174|NUR26329169209|814317197461|1.00|UR2|508944|03|INR|RDDIRECT|NA|NA|00000000.00|23-05-2018 17:08:04|0300|NA|1100421174|jaibala1993@gmail.com|8148535076|AMOB|M180500000027|NA|NA|NA|PGS10001-Success|E820F61CF02BB3E166D8EBF2D253E1A7699BAEF6EC74A7D44B2785B98BE6AF94".split("\\|");


        String[] array = bundle.getString("status").toString().split("\\|", -1);
		String arr = array[4].replaceFirst("^0+(?!$)", "");
        Log.d("Check_Status","tn"+array[1]+"c.n"+array[16]+"status"+array[14]);

		back_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		if(array[14].equals("0300")){
           // PaymentStatusUpdatetoServer(array[1],array[16],"Success");
			status.setText("Payment Success");
			status.setTextColor(Color.parseColor("#FF73A757"));
			recharge_status.setText("Your card has been recharged successfully");
			recharge_number.setText("Transaction No."+array[2]);
			recahrge_payment.setText("₹"+arr);
			payment_statusimg.setImageResource(R.drawable.ic_successpay);
			instruction.setVisibility(View.VISIBLE);
			notetext.setVisibility(View.VISIBLE);
        }else if(array[14].equals("0399")) {
           // PaymentStatusUpdatetoServer(array[1],array[16],"Failed");
			status.setText("Payment Failed");
			recharge_status.setText("We were unable to process your payment please check with you bank and initiate again.");
			recharge_number.setText("Transaction No."+array[2]);
			recahrge_payment.setText("₹"+arr);
			payment_statusimg.setImageResource(R.drawable.ic_failedpay);
			instruction.setVisibility(View.GONE);
			notetext.setVisibility(View.GONE);

        }else if(array[14].equals("NA")){
           // PaymentStatusUpdatetoServer(array[1],array[16],"Cancel Transaction");
			status.setText("Cancel Transaction");
			recharge_status.setText("We were unable to process your payment please check with you bank and initiate again.");
			recharge_number.setText("Transaction No."+array[2]);
			recahrge_payment.setText("₹"+arr);
			payment_statusimg.setImageResource(R.drawable.ic_failedpay);
			instruction.setVisibility(View.GONE);
			notetext.setVisibility(View.GONE);
        }else if(array[14].equals("0002")){
            //PaymentStatusUpdatetoServer(array[1],array[16],"Pending Transaction");
			status.setText("Pending Transaction");
			recharge_status.setText("We were unable to process your payment please check with you bank and initiate again.");
			recharge_number.setText("Transaction No."+array[2]);
			recahrge_payment.setText("₹"+arr);
			payment_statusimg.setImageResource(R.drawable.ic_failedpay);
			instruction.setVisibility(View.GONE);
			notetext.setVisibility(View.GONE);
        }else if(array[14].equals("0001")){
            //PaymentStatusUpdatetoServer(array[1],array[16],"Cancel Transaction");
			status.setText("Cancel Transaction");
			recharge_status.setText("We were unable to process your payment please check with you bank and initiate again.");
			recharge_number.setText("Transaction No."+array[2]);
			recahrge_payment.setText("₹"+arr);
			payment_statusimg.setImageResource(R.drawable.ic_failedpay);
			instruction.setVisibility(View.GONE);
			notetext.setVisibility(View.GONE);
        }


	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}
	/*
	public void PaymentStatusUpdatetoServer(String transaction_number,String card_number,String status) {


		ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
		Call<TransactionStatus> call = apiService.UpdateTransactionStatus(pref.getString("username", null), pref.getString("password", null),transaction_number,card_number,status,pref.getString("securitycode",null));		// Set up progress before call
		final ProgressDialog progressDoalog;
		progressDoalog = new ProgressDialog(this);
		progressDoalog.setMax(100);
		progressDoalog.setMessage("Loading....");
		progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDoalog.setCanceledOnTouchOutside(false);
		// show it
		progressDoalog.show();

		call.enqueue(new Callback<TransactionStatus>() {
			@Override
			public void onResponse(Call<TransactionStatus> call, Response<TransactionStatus> response) {
				int statusCode = response.code();
				Log.e("statusCode200", "" );
				if (statusCode == 200) {
					Log.e("StringMSG", "" + response.body().getMsg());
					progressDoalog.dismiss();
					Toast.makeText(StatusActivity.this, "Transaction Completed!", Toast.LENGTH_LONG).show();

				} else if (statusCode == 400) {
					progressDoalog.dismiss();
					Toast.makeText(StatusActivity.this, "Getting Error please Try again!", Toast.LENGTH_LONG).show();
				} else {
					progressDoalog.dismiss();
					Toast.makeText(StatusActivity.this, "Getting Networking Error please Try again!", Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onFailure(Call<TransactionStatus> call, Throwable t) {
				// Log error here since request failed
				Log.e("error", t.toString());
				progressDoalog.dismiss();
				Toast.makeText(StatusActivity.this, "Getting Networking Error please Try again!", Toast.LENGTH_LONG).show();
			}
		});


	}
*/


}
