package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationfacilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

/**
 * Created by 102525 on 3/19/2018.
 */

public class StationDetailAdapter extends RecyclerView.Adapter<StationDetailAdapter.StationDetailHolder> {

    List<Stationfacilities> stationdetailsModals_array = new ArrayList<Stationfacilities>();
    Context mcontext;

    public class StationDetailHolder extends RecyclerView.ViewHolder {
        public TextView title_txtname;
        public ImageView drawable_station_img;

        public StationDetailHolder(View view) {
            super(view);
typeface(view);
            drawable_station_img = (ImageView) view.findViewById(R.id.drawable_station_img);
            title_txtname = (TextView) view.findViewById(R.id.title_txtname);
        }
    }

    public StationDetailAdapter(List<Stationfacilities> stationdetailsModals_array, int station_details_custome, Context context) {
        this.stationdetailsModals_array = stationdetailsModals_array;
        this.mcontext = context;
    }

    @Override
    public StationDetailAdapter.StationDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_details_custome, parent, false);
        return new StationDetailAdapter.StationDetailHolder(itemView);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(StationDetailHolder holder, int position) {
        holder.title_txtname.setText(stationdetailsModals_array.get(position).getFacilityname());
        Picasso.with(mcontext).load(stationdetailsModals_array.get(position).getFacilityimage()).into(holder.drawable_station_img);

}
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return stationdetailsModals_array.size();
    }
}