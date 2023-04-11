package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.IntermediateStations;
import org.chennaimetrorail.appv1.modal.jsonmodal.FTRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 102525 on 7/10/2017.
 */

public class RoutAdapter extends RecyclerView.Adapter<RoutAdapter.RoutHolder> {


    List<FTRoute> filter_station_list = new ArrayList<>();
    Context mcontext;

    public class RoutHolder extends RecyclerView.ViewHolder {
        public TextView rout_name;
        public ImageView status ;
        RelativeLayout routs;

        public RoutHolder(View view) {
            super(view);

            status = (ImageView) view.findViewById(R.id.status);
            rout_name = (TextView) view.findViewById(R.id.rout_name);
            routs = (RelativeLayout)view.findViewById(R.id.recycl_rl);


        }
    }


    public RoutAdapter(List<FTRoute> filter_station_list, Context context) {
        this.filter_station_list = filter_station_list;
        this.mcontext = context;

    }

    @Override
    public RoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_routinfo, parent, false);
        return new RoutHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RoutHolder holder, final int position) {


        if(filter_station_list.size()==1){

            if(filter_station_list.get(position).getStationname()!=null) {

                holder.rout_name.setText(filter_station_list.get(position).getStationname());
                if (filter_station_list.get(position).getLinecolor().equalsIgnoreCase("Green")) {

                    holder.status.setImageResource(R.drawable.circle_green);

                } else if (filter_station_list.get(position).getLinecolor().equalsIgnoreCase("Blue")) {

                    holder.status.setImageResource(R.drawable.circle_blue);

                } else {
                    holder.status.setImageResource(R.drawable.circle_orange);
                }
            }else {
               holder.routs.setVisibility(View.GONE);

            }
        }

        if(filter_station_list.get(position).getStationname()!=null) {

            holder.rout_name.setText(filter_station_list.get(position).getStationname());
            if (filter_station_list.get(position).getLinecolor().equalsIgnoreCase("Green")) {

                holder.status.setImageResource(R.drawable.circle_green);

            } else if (filter_station_list.get(position).getLinecolor().equalsIgnoreCase("Blue")) {

                holder.status.setImageResource(R.drawable.circle_blue);

            }
            else {
                holder.status.setImageResource(R.drawable.circle_orange);
            }
        }
        if (position==0) {
            holder.status.setBackgroundResource(R.drawable.dotted_circle_g);

        }
         else if(position==(getItemCount()-1)){
            holder.status.setBackgroundResource(R.drawable.dotted_circle_r);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return filter_station_list.size();
    }

    public void clear() {
        int size = this.filter_station_list.size();
        this.filter_station_list.clear();
        notifyDataSetChanged();
    }
}