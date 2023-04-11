package org.chennaimetrorail.appv1.travelcard.travelcardadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.CardlistModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalCardlistModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalcardDeletMadol;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcard.NormalRechargeActivity;
import org.chennaimetrorail.appv1.travelcard.TravelCardList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by 102525 on 2/1/2018.
 */

public class TravalcardListAdapter extends RecyclerView.Adapter<TravalcardListAdapter.MyViewHolder> {


    private int row_index;
    private List<CardlistModal> cardlistModals_array;
    private Context context;
    private int rowLayout;
    private SharedPreferences pref;
    private boolean isShown = false, Connection;
    private Internet_connection_checking int_chk;
    private Dialog dialog;
    private String dialog_msgs;
    String version;
    TravelCardList travelcardlist;
    private String TAG = "TravalcardListAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView card_shortname,card_nik_name,card_number;
        private ImageView card_delete_btn;
        private LinearLayout row_linearlayout;

        private MyViewHolder(View view) {
            super(view);

            FontStyle fontStyle = new FontStyle();
            fontStyle.Changeview(context,view);
            row_linearlayout = (LinearLayout)view.findViewById(R.id.row_linearlayout);
            card_shortname = (TextView) view.findViewById(R.id.card_shortname);
            card_nik_name = (TextView) view.findViewById(R.id.card_nik_name);
            card_number = (TextView) view.findViewById(R.id.card_number);
            card_delete_btn = (ImageView) view.findViewById(R.id.card_delete_btn);
            card_shortname.setTypeface(fontStyle.helveticabold_CE);
            card_nik_name.setTypeface(fontStyle.helveticabold_CE);

        }
    }

    public TravalcardListAdapter(List<CardlistModal> cardlistModals_array, int rowLayout, Context context, TravelCardList travelcardlist) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.cardlistModals_array = cardlistModals_array;
        this.travelcardlist =travelcardlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.card_shortname.setText(cardlistModals_array.get(position).getNik_name().substring(0, 1));
        holder.card_nik_name.setText(cardlistModals_array.get(position).getNik_name());
        holder.card_number.setText(Html.fromHtml("Card Number : <b>"+cardlistModals_array.get(position).getCard_number()+"</b>"));



        holder.row_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                Intent i = new Intent(context, NormalRechargeActivity.class);
                i.putExtra("Card_number",cardlistModals_array.get(position).getCard_number());
                i.putExtra("card_name",cardlistModals_array.get(position).getNik_name());
                i.putExtra("card_id",String.valueOf(cardlistModals_array.get(position).getCard_id()));
                Log.d("lskdl","lskdlk"+cardlistModals_array.get(position).getCard_id());
                context.startActivity(i);
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.card_shortname.setBackgroundResource(R.drawable.t_card_circle_slec_bg);
            holder.card_delete_btn.setImageResource(R.drawable.ic_delete_sel);


        }
        else
        {
            holder.card_shortname.setBackgroundResource(R.drawable.t_card_circle_bg);
            holder.card_delete_btn.setImageResource(R.drawable.ic_delete);
        }




        holder.card_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*InterNet Checking Class create Object Here*/
                int_chk = new Internet_connection_checking((Activity) context);
                Connection = int_chk.checkInternetConnection();
                     /*Check Internet Connection Connect Or Not*/
                if (!Connection) {
                    Toast.makeText(context, "This function requires internet connection.", Toast.LENGTH_LONG).show();

                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder
                            .setMessage("Do you wish to delete the travel card")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    DeleteAttempt(cardlistModals_array.get(position).getCard_number());
                                    removeAt(position);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return cardlistModals_array.size();
    }

    public void DeleteAttempt(final String card_number) {

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
             version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref = context.getSharedPreferences("LoginDetails", 0);
//        Call<TravalcardDeletMadol> call = apiService.deletT_CardList(pref.getString("username",null),
//                pref.getString("password",null),
//                card_number,pref.getString("securitycode",null),version);


        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("card_number", card_number);
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Call<TravalcardDeletMadol> call = apiService.deletT_CardList(values);
        Log.d(TAG, "deletT_CardList" + values);
        // Set up progress before call
        final ProgressDialog loader = ProgressDialog.show(context, "", "Loading...", true);


        call.enqueue(new Callback<TravalcardDeletMadol>() {
            @Override
            public void onResponse(Call<TravalcardDeletMadol> call, Response<TravalcardDeletMadol> response) {
                int statusCode = response.code();
                Log.d(TAG, "deleteAttempts_code" + statusCode );

                Log.d(TAG,call.request().url().toString());
                if (statusCode == 200) {
                    loader.dismiss();
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_LONG).show();

                    travelcardlist.GetCardListData();
                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(context, "Deleted Failed!", Toast.LENGTH_LONG).show();
                }else {
                    loader.dismiss();
                    Toast.makeText(context, "Deleted Failed!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TravalcardDeletMadol> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                loader.dismiss();
                Toast.makeText(context, "Deleted Failed!", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void removeAt(int position) {
        cardlistModals_array.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardlistModals_array.size());
    }


    //Internet Checking message popup
    public void dialogs() {

        dialog = new Dialog(context);
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


}