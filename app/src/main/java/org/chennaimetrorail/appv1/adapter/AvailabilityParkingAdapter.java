package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by 102525 on 3/27/2018. jaialal;
 */


public class AvailabilityParkingAdapter extends RecyclerView.Adapter<AvailabilityParkingAdapter.AvailabilityParkingHolder> {

    JSONArray jsonArray = new JSONArray();
    Context mcontext;

    public class AvailabilityParkingHolder extends RecyclerView.ViewHolder {
        public TextView av_title;

        public AvailabilityParkingHolder(View view) {
            super(view);

            av_title = (TextView) view.findViewById(R.id.av_title);
        }
    }

    public AvailabilityParkingAdapter(JSONArray jsonArray , Context context) {
        this.jsonArray = jsonArray;
        this.mcontext = context;
    }

    @Override
    public AvailabilityParkingAdapter.AvailabilityParkingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.availability_parking_custome, parent, false);
        return new AvailabilityParkingAdapter.AvailabilityParkingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AvailabilityParkingAdapter.AvailabilityParkingHolder holder, int position) {


        try {
            holder.av_title.setText(jsonArray.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


}