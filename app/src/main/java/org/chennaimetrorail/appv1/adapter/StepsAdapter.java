package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 102525 on 9/1/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    List<Steps> steps_list = new ArrayList<Steps>();
    Context mcontext;

    public class StepsHolder extends RecyclerView.ViewHolder {
        public TextView steps, near_destance;
        public ImageView direct_img;

        public StepsHolder(View view) {
            super(view);

            direct_img = (ImageView)view.findViewById(R.id.direct_img);
            steps = (TextView) view.findViewById(R.id.steps);
            near_destance = (TextView) view.findViewById(R.id.near_destance);
        }
    }


    public StepsAdapter(List<Steps> steps_list, Context context) {
        this.steps_list = steps_list;
        this.mcontext = context;
    }

    @Override
    public StepsAdapter.StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_steps, parent, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(mcontext,itemView);
        return new StepsAdapter.StepsHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(StepsHolder holder, int position) {

        holder.steps.setText(Html.fromHtml(position + 1 + ". " + steps_list.get(position).getSteps()));
        holder.near_destance.setText(steps_list.get(position).getDistance());

        if (steps_list.get(position).getManeuver() != null) {

            if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-sharp-left")) {
                holder.direct_img.setImageResource(R.drawable.turn_sharp_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("uturn-right")) {
                holder.direct_img.setImageResource(R.drawable.uturn_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-slight-right")) {
                holder.direct_img.setImageResource(R.drawable.turn_slight_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("merge")) {
                holder.direct_img.setImageResource(R.drawable.merge);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("roundabout-left")) {
                holder.direct_img.setImageResource(R.drawable.roundabout_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("roundabout-right")) {
                holder.direct_img.setImageResource(R.drawable.roundabout_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("uturn-left")) {
                holder.direct_img.setImageResource(R.drawable.uturn_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-slight-left")) {
                holder.direct_img.setImageResource(R.drawable.turn_slight_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-left")) {
                holder.direct_img.setImageResource(R.drawable.turn_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("ramp-right")) {
                holder.direct_img.setImageResource(R.drawable.ramp_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-right")) {
                holder.direct_img.setImageResource(R.drawable.turn_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("fork-right")) {
                holder.direct_img.setImageResource(R.drawable.fork_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("straight")) {
                holder.direct_img.setImageResource(R.drawable.straight);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("fork-left")) {
                holder.direct_img.setImageResource(R.drawable.fork_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("ferry-train")) {
                holder.direct_img.setImageResource(R.drawable.ferry_train);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("turn-sharp-right")) {
                holder.direct_img.setImageResource(R.drawable.turn_sharp_right);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("ramp-left")) {
                holder.direct_img.setImageResource(R.drawable.ramp_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("ferry")) {
                holder.direct_img.setImageResource(R.drawable.ferry);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("keep-left")) {
                holder.direct_img.setImageResource(R.drawable.keep_left);
            } else if (steps_list.get(position).getManeuver().equalsIgnoreCase("keep-right")) {
                holder.direct_img.setImageResource(R.drawable.keep_right);
            } else {
                holder.direct_img.setImageResource(0);
            }

        } else {
            holder.direct_img.setImageResource(0);
        }


    }


    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return steps_list.size();
    }


}