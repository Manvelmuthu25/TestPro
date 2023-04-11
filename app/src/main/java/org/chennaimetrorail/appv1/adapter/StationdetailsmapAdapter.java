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
import org.chennaimetrorail.appv1.modal.jsonmodal.Stationmapdetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

public class StationdetailsmapAdapter extends RecyclerView.Adapter<StationdetailsmapAdapter.StationdetailsmapAdapterHolder> {

    List<Stationmapdetails> stationmapdetails_array = new ArrayList<Stationmapdetails>();
    Context mcontext;

    public class StationdetailsmapAdapterHolder extends RecyclerView.ViewHolder {
        public TextView map_name;
        public ImageView map_img;

        public StationdetailsmapAdapterHolder(View view) {
            super(view);
            typeface(view);
            map_img = (ImageView) view.findViewById(R.id.map_img);
            map_name = (TextView) view.findViewById(R.id.map_name);
        }
    }

    public StationdetailsmapAdapter(List<Stationmapdetails> stationmapdetails_array, Context context) {
        this.stationmapdetails_array = stationmapdetails_array;
        this.mcontext = context;
    }

    @Override
    public StationdetailsmapAdapter.StationdetailsmapAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stationdetailsmap_custome, parent, false);
        return new StationdetailsmapAdapter.StationdetailsmapAdapterHolder(itemView);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(StationdetailsmapAdapter.StationdetailsmapAdapterHolder holder, int position) {
        holder.map_name.setText(stationmapdetails_array.get(position).getMapname());
        //holder.map_img.setImageDrawable( );
        Picasso.with(mcontext).load(stationmapdetails_array.get(position).getMapimage()).into(holder.map_img);



    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return stationmapdetails_array.size();
    }
}