package org.chennaimetrorail.appv1.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.chennaimetrorail.appv1.feederfragment.GalleryViewAuto;
import org.chennaimetrorail.appv1.fragment.FeaderService;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederAuto;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoAdapter extends RecyclerView.Adapter<AutoAdapter.AutoAdapterHolder> {

    List<FeederAuto> feederAutoList = new ArrayList<FeederAuto>();
    Context mcontext;
    FeaderService feaderService;
    Dialog dialog;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    public AutoAdapter(List<FeederAuto> feederAutoList, Context context, FeaderService feaderService) {
        this.feederAutoList = feederAutoList;
        this.mcontext = context;
        this.feaderService = feaderService;
    }

    @Override
    public AutoAdapter.AutoAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeder_auto, parent, false);
        return new AutoAdapter.AutoAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AutoAdapter.AutoAdapterHolder holder, final int position) {

        Picasso.with(mcontext).load(feederAutoList.get(position).getImageUrl()).into(holder.auto_images);

        holder.auto_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentActivity activity = (FragmentActivity) view.getContext();
                GalleryViewAuto galleryView = new GalleryViewAuto();
                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, galleryView).addToBackStack(null).commit();


            }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return feederAutoList.size();
    }

    public class AutoAdapterHolder extends RecyclerView.ViewHolder {

        public ImageView auto_images;

        public AutoAdapterHolder(View view) {
            super(view);

            auto_images = (ImageView) view.findViewById(R.id.auto_images);

        }
    }





}