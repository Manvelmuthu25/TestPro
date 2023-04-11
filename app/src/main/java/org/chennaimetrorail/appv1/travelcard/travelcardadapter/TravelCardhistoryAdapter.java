package org.chennaimetrorail.appv1.travelcard.travelcardadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TransactionListModal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 102525 on 2/15/2018.
 */

public class TravelCardhistoryAdapter  extends RecyclerView.Adapter<TravelCardhistoryAdapter.MyViewHolder> {

    private int row_index;

    private List<TransactionListModal> alltransactionListModal_array;
    private Context context;
    private int rowLayout;
    private String TAG = "TravelCardhistoryAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView rc_t_date,rc_t_monthyear,rc_t_name,rc_t_card_no,rc_t_amount,rc_t_id,rc_t_status;
        private LinearLayout rc_t_body,rc_t_state,rc_t_color_state;

        private MyViewHolder(View view) {
            super(view);
            FontStyle fontStyle = new FontStyle();;
            fontStyle.Changeview(context,view);
            rc_t_color_state = (LinearLayout)view.findViewById(R.id.rc_t_color_state);
            rc_t_body = (LinearLayout)view.findViewById(R.id.rc_t_body);
            rc_t_state = (LinearLayout)view.findViewById(R.id.rc_t_state);
            rc_t_date = (TextView) view.findViewById(R.id.rc_t_date);
            rc_t_monthyear = (TextView) view.findViewById(R.id.rc_t_monthyear);
            rc_t_name = (TextView) view.findViewById(R.id.rc_t_name);
            rc_t_card_no = (TextView) view.findViewById(R.id.rc_t_card_no);
            rc_t_amount = (TextView) view.findViewById(R.id.rc_t_amount);
            rc_t_id = (TextView) view.findViewById(R.id.rc_t_id);
            rc_t_status = (TextView) view.findViewById(R.id.rc_t_status);
            rc_t_amount.setTypeface(fontStyle.helveticabold_CE);
            rc_t_name.setTypeface(fontStyle.helveticabold_CE);
            rc_t_date.setTypeface(fontStyle.helveticabold_CE);
            rc_t_monthyear.setTypeface(fontStyle.helveticabold_CE);
        }
    }


    public TravelCardhistoryAdapter(List<TransactionListModal> alltransactionListModal_array, int rowLayout, Context context) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.alltransactionListModal_array = alltransactionListModal_array;
    }

    @Override
    public TravelCardhistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TravelCardhistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelCardhistoryAdapter.MyViewHolder holder, final int position) {


        String date=alltransactionListModal_array.get(position).getTrans_Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date d=dateFormat.parse(date);
            holder.rc_t_date.setText(String.valueOf(d.getDate()));
            holder.rc_t_monthyear.setText(String.valueOf(new SimpleDateFormat("MMM").format(d)));
        }
        catch(Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep"+e);
        }





        holder.rc_t_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
            }
        });


        if(row_index==position){
            holder.rc_t_state.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.rc_t_state.setVisibility(View.GONE);
        }
        if (alltransactionListModal_array.get(position).getChildname() == "") {
            holder.rc_t_name.setText(alltransactionListModal_array.get(position).getCard_number());
        } else {
            holder.rc_t_name.setText(alltransactionListModal_array.get(position).getChildname()+"("+alltransactionListModal_array.get(position).getCard_number()+")");
        }


        holder.rc_t_card_no.setText(Html.fromHtml("Bank Ref.no : <b>"+alltransactionListModal_array.get(position).getTransReferenceNo()+"</b>"));
        holder.rc_t_amount.setText(String.valueOf(alltransactionListModal_array.get(position).getPrice()));
        holder.rc_t_id.setText(Html.fromHtml("Txn No.: <b>"+String.valueOf(alltransactionListModal_array.get(position).getTrans_number()+"</b>")));

        holder.rc_t_status.setText(alltransactionListModal_array.get(position).getStatus());
        if(alltransactionListModal_array.get(position).getStatus().equals("success"))
        {
            holder.rc_t_status.setTextColor(Color.parseColor("#5AD665"));
            holder.rc_t_color_state.setBackgroundColor(Color.parseColor("#5AD665"));
        }else {
            holder.rc_t_status.setTextColor(Color.parseColor("#EB5656"));
            holder.rc_t_color_state.setBackgroundColor(Color.parseColor("#EB5656"));
        }

    }

    @Override
    public int getItemCount() {
        return alltransactionListModal_array.size();
    }






}