package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.VehicleType;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

public class ParkingSpinnerAdapter extends RecyclerView.Adapter<ParkingSpinnerAdapter.ParkingSpinnerAdapterHolder> {


    List<VehicleType> vehicleTypes;
    Context mcontext;
    String stationname;

    public class ParkingSpinnerAdapterHolder extends RecyclerView.ViewHolder {
        public TextView cu_spintxt;
        public ImageView cu_spinimg;
        public RadioButton cu_radio;

        public ParkingSpinnerAdapterHolder(View view) {
            super(view);
            typeface(view);
            cu_radio =(RadioButton)view.findViewById(R.id.cu_radio);
            cu_spintxt = (TextView) view.findViewById(R.id.cu_spintxt);
            cu_spinimg = (ImageView) view.findViewById(R.id.cu_spinimg);
        }
    }


    public ParkingSpinnerAdapter(List<VehicleType> vehicleTypes, Context context,String stationname) {
        this.vehicleTypes = vehicleTypes;
        this.mcontext = context;
        this.stationname=stationname;
    }

    @Override
    public ParkingSpinnerAdapter.ParkingSpinnerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parkingspinner_custome, parent, false);


        return new ParkingSpinnerAdapter.ParkingSpinnerAdapterHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ParkingSpinnerAdapter.ParkingSpinnerAdapterHolder holder, final int position) {




        holder.cu_spintxt.setText(vehicleTypes.get(position).getVehiclename().toString());
       Picasso.with(mcontext).load(vehicleTypes.get(position).getImageurl()).into(holder.cu_spinimg);
       if(stationname.equalsIgnoreCase(vehicleTypes.get(position).getVehiclename())){
           Log.d("TAG","lskd"+stationname);
           holder.cu_radio.setChecked(true);
       }else {
           Log.d("TAG","lskd"+stationname);
           holder.cu_radio.setChecked(false);
       }

    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return vehicleTypes.size();
    }



}