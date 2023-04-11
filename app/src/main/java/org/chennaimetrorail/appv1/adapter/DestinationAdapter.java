package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.StationMacList;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationAdapterHolder> {


    List<StationMacList> stationMacLists;
    Context mcontext;
    String stationname;

    public DestinationAdapter(List<StationMacList> stationMacLists, Context context, String stationname) {
        this.stationMacLists = stationMacLists;
        this.mcontext = context;
        this.stationname = stationname;
    }

    @Override
    public DestinationAdapter.DestinationAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_custome, parent, false);


        return new DestinationAdapter.DestinationAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DestinationAdapter.DestinationAdapterHolder holder, final int position) {


        holder.station_names.setText(stationMacLists.get(position).getStation_Name().toString());
        if (stationname.equalsIgnoreCase(stationMacLists.get(position).getStation_Name())) {

            holder.station_radio.setChecked(true);
        } else {

            holder.station_radio.setChecked(false);
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return stationMacLists.size();
    }

    public class DestinationAdapterHolder extends RecyclerView.ViewHolder {
        public TextView station_names;
        public RadioButton station_radio;

        public DestinationAdapterHolder(View view) {
            super(view);
            station_radio = (RadioButton) view.findViewById(R.id.station_radio);
            station_names = (TextView) view.findViewById(R.id.station_names);
        }
    }


}