package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.TimeDuration;
import org.chennaimetrorail.appv1.modal.jsonmodal.Times;

import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;


public class TrainTimingAdapter extends RecyclerView.Adapter<TrainTimingAdapter.TrainTimingHolder> {

    private List<Times> times;
    private int rowLayout;
    private Context context;


    public static class TrainTimingHolder extends RecyclerView.ViewHolder {
        LinearLayout timeLayout, bg_set, img_st, time_layout1, time_layout2, time_layout3, time_layout4, time_layout5, time_layout6, time_layout7, time_layout8;
        TextView first_train,last_train,timeTitle, train_time1, train_time2, train_time3, train_time4, train_time5, train_time6, train_time7, train_time8, platform1, platform2, platform3, platform4, platform5, platform6, platform7, platform8;


        public TrainTimingHolder(View v) {
            super(v);
            typeface(v);

            timeLayout = (LinearLayout) v.findViewById(R.id.time_layout);
            time_layout1 = (LinearLayout) v.findViewById(R.id.time_layout1);
            time_layout2 = (LinearLayout) v.findViewById(R.id.time_layout2);
            time_layout3 = (LinearLayout) v.findViewById(R.id.time_layout3);
            time_layout4 = (LinearLayout) v.findViewById(R.id.time_layout4);
            time_layout5 = (LinearLayout) v.findViewById(R.id.time_layout5);
            time_layout6 = (LinearLayout) v.findViewById(R.id.time_layout6);
            time_layout7 = (LinearLayout) v.findViewById(R.id.time_layout7);
            time_layout8 = (LinearLayout) v.findViewById(R.id.time_layout8);

            bg_set = (LinearLayout) v.findViewById(R.id.bg_set);
            timeTitle = (TextView) v.findViewById(R.id.time_title);
            first_train = (TextView) v.findViewById(R.id.first_triantime);
            last_train = (TextView)v.findViewById(R.id.last_triantime);

            train_time1 = (TextView) v.findViewById(R.id.train_time1);
            platform1 = (TextView) v.findViewById(R.id.platform1);
            train_time2 = (TextView) v.findViewById(R.id.train_time2);
            platform2 = (TextView) v.findViewById(R.id.platform2);
            train_time3 = (TextView) v.findViewById(R.id.train_time3);
            platform3 = (TextView) v.findViewById(R.id.platform3);
            train_time4 = (TextView) v.findViewById(R.id.train_time4);
            platform4 = (TextView) v.findViewById(R.id.platform4);
            train_time5 = (TextView) v.findViewById(R.id.train_time5);
            platform5 = (TextView) v.findViewById(R.id.platform5);
            train_time6 = (TextView) v.findViewById(R.id.train_time6);
            platform6 = (TextView) v.findViewById(R.id.platform6);
            train_time7 = (TextView) v.findViewById(R.id.train_time7);
            platform7 = (TextView) v.findViewById(R.id.platform7);
            train_time8 = (TextView) v.findViewById(R.id.train_time8);
            platform8 = (TextView) v.findViewById(R.id.platform8);

        }
    }

    public TrainTimingAdapter(List<Times> times, int rowLayout, Context context) {
        this.times = times;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public TrainTimingAdapter.TrainTimingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TrainTimingHolder(view);
    }


    @Override
    public void onBindViewHolder(TrainTimingHolder holder, final int position) {

        List<TimeDuration> timeDurations = times.get(position).getTiming();

        holder.timeTitle.setText("Towards " + times.get(position).getTowards());
        holder.first_train.setText(times.get(position).getFirsttrain());
        holder.last_train.setText(times.get(position).getLasttrain());
        if (position == 0) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_1);

        } else if (position == 1) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_2);

        } else if (position == 2) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_3);

        } else if (position == 3) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_4);

        } else if (position == 4) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_5);

        } else if (position == 5) {
            holder.bg_set.setBackgroundResource(R.color.color_bag_6);
        }


Log.d("Check",""+timeDurations.size());

        if (timeDurations.size() == 1) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.time_layout2.setVisibility(View.GONE);
            holder.time_layout3.setVisibility(View.GONE);
            holder.time_layout4.setVisibility(View.GONE);
            holder.time_layout5.setVisibility(View.GONE);
            holder.time_layout6.setVisibility(View.GONE);
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 2) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.time_layout3.setVisibility(View.GONE);
            holder.time_layout4.setVisibility(View.GONE);
            holder.time_layout5.setVisibility(View.GONE);
            holder.time_layout6.setVisibility(View.GONE);
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 3) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.time_layout4.setVisibility(View.GONE);
            holder.time_layout5.setVisibility(View.GONE);
            holder.time_layout6.setVisibility(View.GONE);
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 4) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.time_layout4.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.train_time4.setText(timeDurations.get(3).getTrain_time());
            holder.platform4.setText("P" + timeDurations.get(3).getPlatform());
            holder.time_layout5.setVisibility(View.GONE);
            holder.time_layout6.setVisibility(View.GONE);
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 5) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.time_layout4.setVisibility(View.VISIBLE);
            holder.time_layout5.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.train_time4.setText(timeDurations.get(3).getTrain_time());
            holder.platform4.setText("P" + timeDurations.get(3).getPlatform());
            holder.train_time5.setText(timeDurations.get(4).getTrain_time());
            holder.platform5.setText("P" + timeDurations.get(4).getPlatform());
            holder.time_layout6.setVisibility(View.GONE);
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 6) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.time_layout4.setVisibility(View.VISIBLE);
            holder.time_layout5.setVisibility(View.VISIBLE);
            holder.time_layout6.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.train_time4.setText(timeDurations.get(3).getTrain_time());
            holder.platform4.setText("P" + timeDurations.get(3).getPlatform());
            holder.train_time5.setText(timeDurations.get(4).getTrain_time());
            holder.platform5.setText("P" + timeDurations.get(4).getPlatform());
            holder.train_time6.setText(timeDurations.get(5).getTrain_time());
            holder.platform6.setText("P" + timeDurations.get(5).getPlatform());
            holder.time_layout7.setVisibility(View.GONE);
            holder.time_layout8.setVisibility(View.GONE);
        } else if (timeDurations.size() == 7) {
            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.time_layout4.setVisibility(View.VISIBLE);
            holder.time_layout5.setVisibility(View.VISIBLE);
            holder.time_layout6.setVisibility(View.VISIBLE);
            holder.time_layout7.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.train_time4.setText(timeDurations.get(3).getTrain_time());
            holder.platform4.setText("P" + timeDurations.get(3).getPlatform());
            holder.train_time5.setText(timeDurations.get(4).getTrain_time());
            holder.platform5.setText("P" + timeDurations.get(4).getPlatform());
            holder.train_time6.setText(timeDurations.get(5).getTrain_time());
            holder.platform6.setText("P" + timeDurations.get(5).getPlatform());
            holder.train_time7.setText(timeDurations.get(6).getTrain_time());
            holder.platform7.setText("P" + timeDurations.get(6).getPlatform());
            holder.time_layout8.setVisibility(View.GONE);
        } else {

            holder.time_layout1.setVisibility(View.VISIBLE);
            holder.time_layout2.setVisibility(View.VISIBLE);
            holder.time_layout3.setVisibility(View.VISIBLE);
            holder.time_layout4.setVisibility(View.VISIBLE);
            holder.time_layout5.setVisibility(View.VISIBLE);
            holder.time_layout6.setVisibility(View.VISIBLE);
            holder.time_layout7.setVisibility(View.VISIBLE);
            holder.time_layout8.setVisibility(View.VISIBLE);
            holder.train_time1.setText(timeDurations.get(0).getTrain_time());
            holder.platform1.setText("P" + timeDurations.get(0).getPlatform());
            holder.train_time2.setText(timeDurations.get(1).getTrain_time());
            holder.platform2.setText("P" + timeDurations.get(1).getPlatform());
            holder.train_time3.setText(timeDurations.get(2).getTrain_time());
            holder.platform3.setText("P" + timeDurations.get(2).getPlatform());
            holder.train_time4.setText(timeDurations.get(3).getTrain_time());
            holder.platform4.setText("P" + timeDurations.get(3).getPlatform());
            holder.train_time5.setText(timeDurations.get(4).getTrain_time());
            holder.platform5.setText("P" + timeDurations.get(4).getPlatform());
            holder.train_time6.setText(timeDurations.get(5).getTrain_time());
            holder.platform6.setText("P" + timeDurations.get(5).getPlatform());
            holder.train_time7.setText(timeDurations.get(6).getTrain_time());
            holder.platform7.setText("P" + timeDurations.get(6).getPlatform());
            holder.train_time8.setText(timeDurations.get(7).getTrain_time());
            holder.platform8.setText("P" + timeDurations.get(7).getPlatform());

        }

    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}