package org.chennaimetrorail.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.NewsNotificationList;

import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

/**
 * Created by 102525 on 8/23/2017.
 */

public class FlashNewsAdapter extends RecyclerView.Adapter<FlashNewsAdapter.MyViewHolder> {


    List<NewsNotificationList> newsNotificationLists;
    Context context;
    private int rowLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView flash_date,flash_news_txt,flash_time;
        public RelativeLayout flash_layout;

        public MyViewHolder(View view) {
            super(view);
            typeface(view);

            flash_date = (TextView) view.findViewById(R.id.flash_date);
            flash_news_txt = (TextView) view.findViewById(R.id.flash_news_txt);
            flash_time = (TextView) view.findViewById(R.id.flash_time);
            flash_layout = (RelativeLayout) view.findViewById(R.id.flash_layout);
        }
    }


    public FlashNewsAdapter(List<NewsNotificationList> newsNotificationLists, int rowLayout, Context context) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.newsNotificationLists = newsNotificationLists;
    }

    @Override
    public FlashNewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FlashNewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FlashNewsAdapter.MyViewHolder holder, final int position) {


        holder.flash_news_txt.setText(newsNotificationLists.get(position).getMessagetext());
        holder.flash_time.setText(newsNotificationLists.get(position).getCreatedtime());
        holder.flash_date.setText(newsNotificationLists.get(position).getNoti_dd()+"/"+newsNotificationLists.get(position).getNoti_mmm()+"/2017");



    }

    @Override
    public int getItemCount() {
        return newsNotificationLists.size();
    }


}