package org.chennaimetrorail.appv1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.fragment.NearestMetroRouteDirection;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.StationList;

import java.util.List;

/**
 * Created by 102525 on 8/28/2017.
 */

public class NerestStationAdapter extends RecyclerView.Adapter<NerestStationAdapter.NerestStationAdapterHolder> {

    private List<StationList> stationList;
    private int rowLayout;
    private Context context;
    private boolean isShown = false, Connection;
    private Internet_connection_checking int_chk;
    SharedPreferences near_shared;
    SharedPreferences.Editor edit;
    double lat,lon;

    public static class NerestStationAdapterHolder extends RecyclerView.ViewHolder {
        ImageView station_img;
        TextView station_code_txt,stationname_txt,stationtype_txt,km_txt;
        LinearLayout nearest_layout;



        public NerestStationAdapterHolder(View v) {
            super(v);

            station_img = (ImageView) v.findViewById(R.id.nearest_img);
            station_code_txt = (TextView) v.findViewById(R.id.station_code_txt);
            stationname_txt = (TextView) v.findViewById(R.id.stationname_txt);
            stationtype_txt = (TextView)v.findViewById(R.id.stationtype_txt);
            nearest_layout = (LinearLayout) v.findViewById(R.id.nearest_layout);
            km_txt = (TextView) v.findViewById(R.id.km_txt);


        }
    }

    public NerestStationAdapter(double lat,double lon,List<StationList> stationList, int rowLayout, Context context) {
       this.stationList = stationList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.lat =lat;
        this.lon = lon;
    }

    @Override
    public NerestStationAdapter.NerestStationAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(context,view);
        return new NerestStationAdapter.NerestStationAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(NerestStationAdapter.NerestStationAdapterHolder holder, final int position) {

        near_shared = context.getSharedPreferences("station_lat_lon", 0);
        edit = near_shared.edit();
       holder.stationname_txt.setText(stationList.get(position).getStation_longName());
        holder.stationtype_txt.setText(stationList.get(position).getStation_code());
        holder.stationtype_txt.setText(stationList.get(position).getStation_type());
        holder.km_txt.setText(stationList.get(position).getDistance()+" KMs");

        Log.d("Testing",""+stationList.get(position).getStation_longName());

        if(stationList.get(position).getStation_lineColour().equalsIgnoreCase("Blue")){
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_trainblue));


        }else if(stationList.get(position).getStation_lineColour().equalsIgnoreCase("Green")){
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_train_green));
        }else {
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_train_bluegreen));
        }

        holder.nearest_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int_chk = new Internet_connection_checking((Activity) context);
                Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
                if (!Connection) {
                    Toast.makeText(context, "This function requires internet connection.", Toast.LENGTH_LONG).show();

                } else {

                    edit.putString("cureent_lat",String.valueOf(lat));
                    edit.putString("cureent_lon",String.valueOf(lon));
                    edit.putString("direction_name", stationList.get(position).getStation_longName());
                    edit.putString("destination_lat", stationList.get(position).getStation_latitude());
                    edit.putString("destination_lon", stationList.get(position).getStation_longitude());
                    edit.apply();
                    //You can change the fragment, something like this, not tested, please correct for your desired output:
                    FragmentActivity activity = (FragmentActivity) view.getContext();
                    NearestMetroRouteDirection nearestMetroRouteDirection = new NearestMetroRouteDirection();
                    //Create a bundle to pass data, add data, set the bundle to your fragment and:
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, nearestMetroRouteDirection).addToBackStack(null).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }
}