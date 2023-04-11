package org.chennaimetrorail.appv1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.fragment.StationInformation;
import org.chennaimetrorail.appv1.modal.StationList;

import java.util.ArrayList;
import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

/**
 * Created by 102525 on 7/10/2017.
 */

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationHolder> {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    List<StationList> station_array = new ArrayList<StationList>();
    Context mcontext;

    public class StationHolder extends RecyclerView.ViewHolder {
        public TextView station_name;
        public ImageView station_img;
        public RelativeLayout station_relative;

        public StationHolder(View view) {
            super(view);
            typeface(view);
            station_name = (TextView) view.findViewById(R.id.station_name);
            station_img = (ImageView) view.findViewById(R.id.station_img);
            station_relative = (RelativeLayout)view.findViewById(R.id.station_relative);
        }
    }


    public StationAdapter(List<StationList> station_array, Context context) {
        this.station_array = station_array;
        this.mcontext = context;
    }

    @Override
    public StationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_stationinfo, parent, false);
        return new StationHolder(itemView);
    }


    @Override
    public void onBindViewHolder(StationHolder holder, final int position) {

        pref = mcontext.getSharedPreferences("stationdetails", 0);
        editor = pref.edit();

        if(station_array.get(position).getStation_LineColour().equalsIgnoreCase("Blue")){
            holder.station_name.setText(station_array.get(position).getStation_LongName());
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_trainblue));

        }else if(station_array.get(position).getStation_LineColour().equalsIgnoreCase("Green")){
            holder.station_name.setText(station_array.get(position).getStation_LongName());
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_train_green));
        }else {
            holder.station_name.setText(station_array.get(position).getStation_LongName());
            holder.station_img.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_train_bluegreen));
        }


        holder.station_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("station_id", station_array.get(position).getStation_Id());
                editor.putString("station_code", String.valueOf(station_array.get(position).getStation_Code()));
                editor.putString("station_shorname", String.valueOf(station_array.get(position).getStation_ShortName()));
                editor.putString("station_longname", String.valueOf(station_array.get(position).getStation_LongName()));
                editor.putString("station_latitude", String.valueOf(station_array.get(position).getStation_Latitude()));
                editor.putString("station_longitude", String.valueOf(station_array.get(position).getStation_Longitude()));
                editor.putString("station_priority", String.valueOf(station_array.get(position).getStation_Priority()));
                editor.putString("station_linecolour", String.valueOf(station_array.get(position).getStation_LineColour()));
                editor.putString("station_Gates_directions", String.valueOf(station_array.get(position).getGates_Directions()));
                editor.putString("station_contacts", String.valueOf(station_array.get(position).getStation_Contacts()));
                editor.putString("station_platform_info", String.valueOf(station_array.get(position).getPlatform_Info()));
                editor.putString("station_type", String.valueOf(station_array.get(position).getStation_Type()));

                editor.apply();
                hideKeyboard(mcontext);
                //You can change the fragment, something like this, not tested, please correct for your desired output:
                FragmentActivity activity = (FragmentActivity) view.getContext();
                StationInformation stationInformation = new StationInformation();
                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, stationInformation).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return station_array.size();
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}