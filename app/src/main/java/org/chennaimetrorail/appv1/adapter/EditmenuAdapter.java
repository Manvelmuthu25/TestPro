package org.chennaimetrorail.appv1.adapter;

/**
 * Created by 102525 on 7/6/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.LocalMenus;
import org.chennaimetrorail.appv1.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

public class EditmenuAdapter extends RecyclerView.Adapter<EditmenuAdapter.MyViewHolder> {

    List<LocalMenus> menus_list1 = new ArrayList<LocalMenus>();
    List<LocalMenus> localMenus = new ArrayList<LocalMenus>();
    String list_size;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    DatabaseHandler db;
    int count = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView edit_menu_title;
        public ImageView edit_menu_img;
        public RelativeLayout relative_layoutedit;

        public MyViewHolder(View view) {
            super(view);
            typeface(view);

            edit_menu_title = (TextView) view.findViewById(R.id.edit_menu_title);
            edit_menu_img = (ImageView) view.findViewById(R.id.edit_menu_img);
            //edit_menu_title.setTypeface(fontStyle);
            relative_layoutedit = (RelativeLayout) view.findViewById(R.id.relative_layoutedit);
        }
    }

    public EditmenuAdapter(Context context, List<LocalMenus> localMenus) {
        this.context = context;
        this.localMenus = localMenus;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_edit_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();


        db = new DatabaseHandler(context);

        if (localMenus.get(position).getMenu_priority().equalsIgnoreCase("0")) {

            holder.edit_menu_img.setBackgroundResource(R.drawable.circle_thum);
            //holder.edit_menu_img.setBackgroundResource(Integer.parseInt(localMenus.get(position).get("images")));
            holder.edit_menu_img.setImageResource(R.drawable.ic_plus);
            holder.edit_menu_title.setText(localMenus.get(position).getMenu_name());


        } else {

            holder.edit_menu_img.setBackgroundResource(R.drawable.circle_blue);
            String uri = "drawable/" + localMenus.get(position).getMenu_icon();
            int icon = context.getResources().getIdentifier(uri, "drawable", context.getPackageName());
            holder.edit_menu_img.setImageResource(icon);
            holder.edit_menu_title.setText(localMenus.get(position).getMenu_name());


        }

        holder.relative_layoutedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("counddd", "" + position);

                if (pref.getInt("menu_count", 0) < 4) {
                    if (localMenus.get(position).getMenu_priority().equalsIgnoreCase("0")) {

                        Log.d("Tesssssssssstsss",""+position);
                        holder.edit_menu_img.setBackgroundResource(R.drawable.circle_thum);
                        holder.edit_menu_img.setImageResource(R.drawable.ic_plus);
                        holder.edit_menu_title.setText(localMenus.get(position).getMenu_name());
                        Constant.local_menus_list.get(position).setMenu_priority("1");

                        /*db.updateMenu(localMenus.get(position).getId(), localMenus.get(position).getMenu_name(), "1", localMenus.get(position).getMenu_className());
                        db.getLocalAllMenus();*/
                        notifyDataSetChanged();
                        editor.putInt("menu_count", pref.getInt("menu_count", 0) + 1);
                        editor.apply();

                    } else {
                        Log.d("Tesssssssssstsss",""+position);
                        holder.edit_menu_img.setBackgroundResource(R.drawable.circle_blue);
                        String uri = "drawable/" + localMenus.get(position).getMenu_icon();
                        int icon = context.getResources().getIdentifier(uri, "drawable", context.getPackageName());
                        holder.edit_menu_img.setImageResource(icon);
                        holder.edit_menu_title.setText(localMenus.get(position).getMenu_name());
                        Constant.local_menus_list.get(position).setMenu_priority("0");
                       /* db.updateMenu(localMenus.get(position).getId(), localMenus.get(position).getMenu_name(), "0", localMenus.get(position).getMenu_className());
                        db.getLocalAllMenus();*/
                        notifyDataSetChanged();
                        editor.putInt("menu_count", pref.getInt("menu_count", 0) - 1);
                        editor.apply();

                    }
                } else {

                    if (localMenus.get(position).getMenu_priority().equalsIgnoreCase("1")) {
                        holder.edit_menu_img.setBackgroundResource(R.drawable.circle_blue);
                        String uri = "drawable/" + localMenus.get(position).getMenu_icon();
                        int icon = context.getResources().getIdentifier(uri, "drawable", context.getPackageName());
                        holder.edit_menu_img.setImageResource(icon);
                        holder.edit_menu_title.setText(localMenus.get(position).getMenu_name());
                        Constant.local_menus_list.get(position).setMenu_priority("0");
                       /* db.updateMenu(localMenus.get(position).getId(), localMenus.get(position).getMenu_name(), "0", localMenus.get(position).getMenu_className());
                            db.getLo calAllMenus();*/
                        notifyDataSetChanged();
                        editor.putInt("menu_count", pref.getInt("menu_count", 0) - 1);
                        editor.apply();

                    } else {

                        editor.putInt("menu_count", pref.getInt("menu_count", 0));
                        editor.apply();
                        Toast.makeText(context,R.string.editment_limt,Toast.LENGTH_SHORT).show();
                    }

                }


                notifyDataSetChanged();
            }

        });
    }

    @Override
    public int getItemCount() {
        return localMenus.size();
    }


}