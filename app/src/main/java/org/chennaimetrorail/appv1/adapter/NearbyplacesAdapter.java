package org.chennaimetrorail.appv1.adapter;

/**
 * Created by 102525 on 9/6/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.Googleplacesphotos;
import org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal.Nearbyplaces;
import org.chennaimetrorail.appv1.modal.jsonmodal.nearbyjsonmodal.Results;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyplacesAdapter extends RecyclerView.Adapter<NearbyplacesAdapter.NearbyplacesAdapterHolder> {

    double lat, lon;
    SharedPreferences near_shared;
    SharedPreferences.Editor edit;
    private List<Results> resultses;
    private int rowLayout;
    private Context context;

    String TAG ="NearbyplacesAdapter";
    public NearbyplacesAdapter(List<Results> resultses, int rowLayout, double lat, double lon, Context context) {
        this.resultses = resultses;
        this.rowLayout = rowLayout;
        this.context = context;
        this.lat = lat;
        this.lon = lon;

    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        System.out.println(dist);
        return dist;
    }

    @Override
    public NearbyplacesAdapter.NearbyplacesAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(context, view);

        return new NearbyplacesAdapter.NearbyplacesAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(final NearbyplacesAdapter.NearbyplacesAdapterHolder holder, final int position) {

        near_shared = context.getSharedPreferences("station_lat_lon", 0);
        edit = near_shared.edit();
        holder.nerest_name_txt.setText(resultses.get(position).getName());
        holder.nerest_name_txt.setTypeface(Typeface.DEFAULT_BOLD);

        holder.nearest_address.setText(resultses.get(position).getVicinity());

        try {
            resultses.get(position).getOpeningHours().getOpenNow();
            holder.nearest_status.setText("Open now");
        } catch (Exception e) {
            holder.nearest_status.setText("Closed now");
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        holder.km_txt.setText(String.valueOf(decimalFormat.format(distFrom(lat, lon, resultses.get(position).getGeometry().getLocation().getLat(), resultses.get(position).getGeometry().getLocation().getLng()))) + " Km");

        if (resultses.get(position).getRating() != null) {
            DecimalFormat decimalFormatt = new DecimalFormat("#.#");
            holder.rating_txt.setText(String.valueOf(resultses.get(position).getRating()));
            holder.ratingBar.setRating(Float.parseFloat(decimalFormatt.format(resultses.get(position).getRating())));

        } else {
            holder.rating_txt.setText("No Reviews");
            holder.ratingBar.setVisibility(View.INVISIBLE);
        }


        if (resultses.get(position).getPhotos().size() != 0) {


            String maxWidth = String.valueOf(resultses.get(position).getPhotos().get(0).getWidth());
            String maxHeight =String.valueOf(resultses.get(position).getPhotos().get(0).getHeight());
            String photoReference = String.valueOf(resultses.get(position).getPhotos().get(0).getPhotoReference());

            PackageManager manager = context.getPackageManager();
            PackageInfo info = null;
            try {
                info = manager.getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            final ApiInterface apiInterface = ApiClient.getResponse().create(ApiInterface.class);

            Call<Googleplacesphotos> call = apiInterface.getgooglephotos("200", "200",photoReference, Constant.strApiKey, info.versionName);

            call.enqueue(new Callback<Googleplacesphotos>() {

                @Override
                public void onResponse(Call<Googleplacesphotos> call, final Response<Googleplacesphotos> response) {
                    Log.d(TAG,call.request().url().toString());
                    int statusCode = response.code();
                    if (statusCode == 200) {



                        byte[] decodedString = Base64.decode(response.body().getPhotoImage(), Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.nearestplaces_img.setImageBitmap(decodedByte);
                       // Picasso.with(context).load(response.body().getPhotoImage()).into(holder.nearestplaces_img);

                    }

                }

                @Override
                public void onFailure(Call<Googleplacesphotos> call, Throwable t) {
                    Log.d("onFailure", t.toString());

                }


            });



        }

    }

    @Override
    public int getItemCount() {
        return resultses.size();
    }



    public static class NearbyplacesAdapterHolder extends RecyclerView.ViewHolder {
        ImageView nearestplaces_img;
        TextView nerest_name_txt, rating_txt, nearest_address, nearest_status, km_txt;
        LinearLayout nearest_layout;
        RatingBar ratingBar;


        public NearbyplacesAdapterHolder(View v) {
            super(v);
            nearestplaces_img = (ImageView) v.findViewById(R.id.nearestplaces_img);
            nerest_name_txt = (TextView) v.findViewById(R.id.nerest_name_txt);
            rating_txt = (TextView) v.findViewById(R.id.rating_txt);
            nearest_address = (TextView) v.findViewById(R.id.nearest_address);
            nearest_status = (TextView) v.findViewById(R.id.nearest_status);
            km_txt = (TextView) v.findViewById(R.id.km_txt);
            nearest_layout = (LinearLayout) v.findViewById(R.id.nearest_layout);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);


        }
    }

}