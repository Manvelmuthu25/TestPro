package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.Parkingtariff;

import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

public class ParkingpriceAdapter extends RecyclerView.Adapter<ParkingpriceAdapter.ParkingpriceAdapterHolder> {


    List<Parkingtariff> parkingtariff;
    Context mcontext;

    public class ParkingpriceAdapterHolder extends RecyclerView.ViewHolder {
        public TextView text_name,hrs_txt2;

        public ParkingpriceAdapterHolder(View view) {
            super(view);
            typeface(view);
            text_name = (TextView) view.findViewById(R.id.text_name);
            hrs_txt2 = (TextView) view.findViewById(R.id.hrs_txt2);
        }
    }


    public ParkingpriceAdapter(List<Parkingtariff> parkingtariff, Context context) {
        this.parkingtariff = parkingtariff;
        this.mcontext = context;
    }

    @Override
    public ParkingpriceAdapter.ParkingpriceAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_parkingprice, parent, false);
        return new ParkingpriceAdapter.ParkingpriceAdapterHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ParkingpriceAdapter.ParkingpriceAdapterHolder holder, final int position) {



        Log.d("TAG","dkslakdlf"+parkingtariff.get(position).getParkingtime()+"-----"+parkingtariff.get(position).getParkingtime());
        holder.text_name.setText(parkingtariff.get(position).getParkingtime());
        holder.hrs_txt2.setText(parkingtariff.get(position).getParkingamount());

    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return parkingtariff.size();
    }



}