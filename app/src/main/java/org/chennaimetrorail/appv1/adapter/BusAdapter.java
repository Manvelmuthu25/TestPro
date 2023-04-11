package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederBus;

import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusAdapterHolder> {

    List<FeederBus> feederBusList = new ArrayList<FeederBus>();
    Context mcontext;

    public class BusAdapterHolder extends RecyclerView.ViewHolder {
        public TextView fe_busrout_no,fe_from_station,fe_to_station,feed_discription,fe_number_service;
        public LinearLayout rout_sub;

        public BusAdapterHolder(View view) {
            super(view);

            fe_busrout_no = (TextView) view.findViewById(R.id.fe_busrout_no);
            fe_from_station = (TextView) view.findViewById(R.id.fe_from_station);
            fe_to_station = (TextView) view.findViewById(R.id.fe_to_station);
            feed_discription = (TextView) view.findViewById(R.id.feed_discription);
            fe_number_service = (TextView) view.findViewById(R.id.fe_number_service);
            rout_sub = (LinearLayout)view.findViewById(R.id.rout_sub);
        }
    }

    public BusAdapter(List<FeederBus> feederBusList, Context context) {
        this.feederBusList = feederBusList;
        this.mcontext = context;
    }

    @Override
    public BusAdapter.BusAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeder_bus, parent, false);
        return new BusAdapter.BusAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BusAdapter.BusAdapterHolder holder, int position) {


        holder.fe_busrout_no.setText(feederBusList.get(position).getRouteNo());
        holder.fe_from_station.setText(feederBusList.get(position).getFrom());
        holder.fe_to_station.setText(feederBusList.get(position).getTo());
        holder.feed_discription.setText(feederBusList.get(position).getVia());
        holder.fe_number_service.setText(feederBusList.get(position).getNumberofServices());

        holder.fe_busrout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rout_sub.getVisibility() == View.GONE) {
                    holder.fe_busrout_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_black_24dp, 0, 0, 0);
                    holder.rout_sub.setVisibility(View.VISIBLE);

                } else {
                    holder.fe_busrout_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_black_24dp, 0, 0, 0);
                    holder.rout_sub.setVisibility(View.GONE);
                }
            }
        });



    }


    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return feederBusList.size();
    }
}