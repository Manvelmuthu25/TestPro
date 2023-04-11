package org.chennaimetrorail.appv1.fragment;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.EditmenuAdapter;
import org.chennaimetrorail.appv1.adapter.OnSwipeTouchListener;
import org.chennaimetrorail.appv1.modal.LocalMenus;
import org.chennaimetrorail.appv1.database.DatabaseHandler;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.ArrayList;
import java.util.List;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

/**
 * Created by 102525 on 7/6/2017.
 */

public class EditMenu extends Fragment {

    List<LocalMenus> menus_list = new ArrayList<LocalMenus>();
    //private List<StationList> menus_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditmenuAdapter mAdapter;
    LinearLayout image_view_back;
    FragmentManager fm ;
    DatabaseHandler db;
    Fragment fragment;
    FragmentTransaction fragmentTransaction ;
    Home home;
    SharedPreferences prefrence;
    SharedPreferences.Editor editor;
    TextView inst_txt,title_txt;
    Typeface helveticabold_CE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.edit_menu_frg, container, false);

        TypefaceCollection typeface = new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaCE-Cond.ttf")).create();
        TypefaceHelper.init(typeface);
        typeface(view);
        Typeface helveticaCE = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaCE-CondBold.ttf");
        helveticabold_CE = Typeface.create(helveticaCE, Typeface.BOLD);

        inst_txt = (TextView)view.findViewById(R.id.inst_txt);
        title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Menu List");
        prefrence = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefrence.edit();
        editor.putString("dataSaved", "0");
        editor.apply();


        home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()){

            public void onSwipeRight() {
                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                updatetDB();
                updatedEditMenu();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.edit_menu_recycler_view);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);


        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        db = new DatabaseHandler(getActivity());
        db.getLocalAllMenus();

        if (Constant.local_menus_list.size() == 0) {

            db.addMenu(new LocalMenus("Travel Card Recharge", "TravelCardRecharge", "0", "ic_traval_card_white"));
            db.addMenu(new LocalMenus("MTC/Auto/Cab Services", "FeaderService", "0", "ic_feeder_service_white"));
            db.addMenu(new LocalMenus("Metro Network Information", "MetronetInformation", "0", "ic_metro_net_info_white"));
            db.addMenu(new LocalMenus("Notifications", "Notifications", "0", "ic_news_updates_white"));
            db.addMenu(new LocalMenus("Instruction For Commuters", "InstructiontoCommuters", "0", "ic_instruction_of_comm_white"));
            db.addMenu(new LocalMenus("Facilities For Disabled Persons", "FacilitesforDisabled", "0", "ic_facili_disb_white"));
            db.addMenu(new LocalMenus("Cultural Centres", "CulturalCenter", "0", "ic_cultural_centres_white"));
            db.addMenu(new LocalMenus("Local Weather Forecasts", "WeatherReport", "0", "ic_local_weather_white"));
            db.addMenu(new LocalMenus("Lost and Found", "LostFound", "0", "ic_lost_and_found_white"));
            db.addMenu(new LocalMenus("Contact", "Contact", "0", "ic_contactinfo"));
            db.addMenu(new LocalMenus("SugarBox Entertainment", "Sugarbox", "0", "sugarboxeditmenu"));
            db.getLocalAllMenus();
        } else {
            db.getLocalAllMenus();
        }

        mAdapter = new EditmenuAdapter(getActivity(), Constant.local_menus_list);

        recyclerView.setAdapter(mAdapter);

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                updatetDB();
                updatedEditMenu();
            }
        });



        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {


                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.toolbrtxt_layout.setVisibility(View.GONE);
                    home.toolbar_img.setVisibility(View.VISIBLE);
                    home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                   updatetDB();
                    updatedEditMenu();

                    return true;
                }



                return false;
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mAdapter.notifyDataSetChanged();

                    }
                }, 3000);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof EditmenuAdapter.MyViewHolder) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;

    }
    public void updatedEditMenu(){

        db.getLocalAllMenus();
        for (int i = 0; i < Constant.local_menus_list.size(); i++) {

            if (Constant.local_menus_list.get(i).getMenu_priority().equalsIgnoreCase("1")) {
                Constant.saved_menus_list.add(Constant.local_menus_list.get(i));
            }

        }

        home.addmenu1 = (LinearLayout)getActivity().findViewById(R.id.addmenu1);
        home.addmenu2 = (LinearLayout)getActivity().findViewById(R.id.addmenu2);
        home.addmenu3 = (LinearLayout) getActivity().findViewById(R.id.addmenu3);
        home.addmenu4 = (LinearLayout) getActivity().findViewById(R.id.addmenu4);
        home.addmenu1_txt = (TextView)getActivity(). findViewById(R.id.addmenu1_txt);
        home.addmenu2_txt = (TextView) getActivity().findViewById(R.id.addmenu2_txt);
        home.addmenu3_txt = (TextView) getActivity().findViewById(R.id.addmenu3_txt);
        home.addmenu4_txt = (TextView)getActivity(). findViewById(R.id.addmenu4_txt);
        home.addmenu1_img = (ImageView)getActivity(). findViewById(R.id.addmenu1_img);
        home.addmenu2_img = (ImageView)getActivity(). findViewById(R.id.addmenu2_img);
        home.addmenu3_img = (ImageView)getActivity(). findViewById(R.id.addmenu3_img);
        home.addmenu4_img = (ImageView) getActivity().findViewById(R.id.addmenu4_img);

        if (Constant.saved_menus_list.size() == 1) {
            home.addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu1_img.setImageResource(icon);
            home. addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            home.addmenu2_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu2_img.setImageResource(R.drawable.ic_plus);
            home.addmenu2_txt.setText("Add Menu");

            home.addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu3_img.setImageResource(R.drawable.ic_plus);
            home.addmenu3_txt.setText("Add Menu");

            home. addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu4_img.setImageResource(R.drawable.ic_plus);
            home.addmenu4_txt.setText("Add Menu");



        } else if (Constant.saved_menus_list.size() == 2) {

            home.addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu1_img.setImageResource(icon);
            home.addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            home.addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu2_img.setImageResource(icon2);
            home.addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            home.addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu3_img.setImageResource(R.drawable.ic_plus);
            home.addmenu3_txt.setText("Add Menu");

            home.addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu4_img.setImageResource(R.drawable.ic_plus);
            home.addmenu4_txt.setText("Add Menu");

        } else if (Constant.saved_menus_list.size() == 3) {

            home.addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu1_img.setImageResource(icon);
            home.addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            home.addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu2_img.setImageResource(icon2);
            home.addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            home.addmenu3_img.setBackgroundResource(R.drawable.circle_blue);
            int icon3 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(2).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu3_img.setImageResource(icon3);
            home.addmenu3_txt.setText(Constant.saved_menus_list.get(2).getMenu_name());

            home.addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu4_img.setImageResource(R.drawable.ic_plus);
            home.addmenu4_txt.setText("Add Menu");

        } else if (Constant.saved_menus_list.size() == 4) {
            home.addmenu1_img.setBackgroundResource(R.drawable.circle_blue);
            int icon = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(0).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu1_img.setImageResource(icon);
            home.addmenu1_txt.setText(Constant.saved_menus_list.get(0).getMenu_name());

            home.addmenu2_img.setBackgroundResource(R.drawable.circle_blue);
            int icon2 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(1).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu2_img.setImageResource(icon2);
            home.addmenu2_txt.setText(Constant.saved_menus_list.get(1).getMenu_name());

            home.addmenu3_img.setBackgroundResource(R.drawable.circle_blue);
            int icon3 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(2).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu3_img.setImageResource(icon3);
            home.addmenu3_txt.setText(Constant.saved_menus_list.get(2).getMenu_name());

            home.addmenu4_img.setBackgroundResource(R.drawable.circle_blue);
            int icon4 = getResources().getIdentifier("drawable/" + Constant.saved_menus_list.get(3).getMenu_icon(), "drawable", getActivity().getPackageName());
            home.addmenu4_img.setImageResource(icon4);
            home.addmenu4_txt.setText(Constant.saved_menus_list.get(3).getMenu_name());

        } else {
            home.addmenu1_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu1_img.setImageResource(R.drawable.ic_plus);
            home.addmenu1_txt.setText("Add Menu");

            home.addmenu2_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu2_img.setImageResource(R.drawable.ic_plus);
            home.addmenu2_txt.setText("Add Menu");

            home.addmenu3_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu3_img.setImageResource(R.drawable.ic_plus);
            home.addmenu3_txt.setText("Add Menu");

            home.addmenu4_img.setBackgroundResource(R.drawable.circle_thum);
            home.addmenu4_img.setImageResource(R.drawable.ic_plus);
            home.addmenu4_txt.setText("Add Menu");

        }

    }

    void moveItem(int oldPos, int newPos){
        LocalMenus localmenus = Constant.local_menus_list.get(oldPos);
        Constant.local_menus_list.remove(oldPos);
        Constant.local_menus_list.add(newPos, localmenus);
        mAdapter.notifyItemMoved(oldPos, newPos);

    }

    void deleteItem(final int position){
        Constant.local_menus_list.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void updatetDB() {

        db.deleteAllMenus();

        for (int i = 0; i < Constant.local_menus_list.size(); i++) {
            db.addMenu(new LocalMenus(Constant.local_menus_list.get(i).getMenu_name(), Constant.local_menus_list.get(i).getMenu_className(), Constant.local_menus_list.get(i).getMenu_priority(), Constant.local_menus_list.get(i).getMenu_icon()));

        }

        db.getLocalAllMenus();
        mAdapter = new EditmenuAdapter(getActivity(), Constant.local_menus_list);

        recyclerView.setAdapter(mAdapter);
        Constant.saved_menus_list.clear();
    }
}
