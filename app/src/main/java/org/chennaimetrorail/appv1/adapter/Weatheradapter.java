package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.Weathers;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 102525 on 10/3/2017.
 */

public class Weatheradapter extends RecyclerView.Adapter<Weatheradapter.MyViewHolder> {


    List<Weathers> weatherses;
    Context context;
    private int rowLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView forecat_timetxt, forecat_temp;
        public ImageView forecat_icon;

        public MyViewHolder(View view) {
            super(view);

            forecat_timetxt = (TextView) view.findViewById(R.id.forecat_timetxt);
            forecat_icon = (ImageView) view.findViewById(R.id.forecat_icon);
            forecat_temp = (TextView) view.findViewById(R.id.forecat_temp);
        }
    }


    public Weatheradapter(List<Weathers> weatherses, int rowLayout, Context context) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.weatherses = weatherses;
    }

    @Override
    public Weatheradapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Weatheradapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Weatheradapter.MyViewHolder holder, final int position) {


        Log.d("check", "" + weatherses.get(position).getDt_txt());
        DecimalFormat df = new DecimalFormat("#.#");
        String temp = String.valueOf(df.format(Double.parseDouble(weatherses.get(position).getTemp())));
        holder.forecat_temp.setText(temp + " ℃");

        String uri = "@drawable/w_" + String.valueOf(weatherses.get(position).getIcon()) + "";  // where myresource (without the extension) is the file

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

        Drawable res = context.getResources().getDrawable(imageResource);
        holder.forecat_icon.setImageDrawable(res);

        if(weatherses.get(position).getTime_txt().equalsIgnoreCase("00:00 PM")){
            holder.forecat_timetxt.setText("12:00 PM");
        }else if(weatherses.get(position).getTime_txt().equalsIgnoreCase("00:00 AM")){
            holder.forecat_timetxt.setText("12:00 AM");
        }else {
            holder.forecat_timetxt.setText(weatherses.get(position).getTime_txt());
        }



    }

    @Override
    public int getItemCount() {
        return weatherses.size();
    }


}