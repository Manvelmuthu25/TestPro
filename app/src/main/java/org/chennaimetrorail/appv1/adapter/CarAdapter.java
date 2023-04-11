package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.feederfragment.GalleryViewCar;
import org.chennaimetrorail.appv1.modal.jsonmodal.FeederCar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarAdapterHolder> {

    List<FeederCar> feederCarList = new ArrayList<FeederCar>();
    Context mcontext;

    public CarAdapter(List<FeederCar> feederCarList, Context context) {
        this.feederCarList = feederCarList;
        this.mcontext = context;
    }

    @Override
    public CarAdapter.CarAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeder_car, parent, false);
        return new CarAdapter.CarAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarAdapter.CarAdapterHolder holder, int position) {


        Picasso.with(mcontext).load(feederCarList.get(position).getImageUrl()).into(holder.car_images);

        holder.car_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentActivity activity = (FragmentActivity) view.getContext();
                GalleryViewCar galleryViewCar = new GalleryViewCar();
                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, galleryViewCar).addToBackStack(null).commit();

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return 0;       //you should not return 0 for every item, change it to position or your a possible id, if you have one.
    }

    @Override
    public int getItemCount() {
        return feederCarList.size();
    }

    public class CarAdapterHolder extends RecyclerView.ViewHolder {

        public ImageView car_images;

        public CarAdapterHolder(View view) {
            super(view);

            car_images = (ImageView) view.findViewById(R.id.car_images);

        }
    }
}