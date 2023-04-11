package org.chennaimetrorail.appv1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by 102525 on 7/12/2017.
 */

public class Platforms extends Fragment {
    LinearLayout image_view_back,platforms;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    Animation animFadein;
    SharedPreferences pref;
    TextView station_title,platform_alrt;
    ImageView train1, train2;
    LinearLayout lev_a, lev_b, lev_c, lev_d;
    TextView text_t_a, text_t_b, text_t_c, text_t_d,level_1,level_2,level_3,level_4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.platforms, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);


        Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        platforms = (LinearLayout)view.findViewById(R.id.platforms);
        platform_alrt =(TextView)view.findViewById(R.id.platform_alrt);

        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Platforms");

        lev_a = (LinearLayout) view.findViewById(R.id.lev_1);
        lev_b = (LinearLayout) view.findViewById(R.id.lev_2);
        lev_c = (LinearLayout) view.findViewById(R.id.lev_3);
        lev_d = (LinearLayout) view.findViewById(R.id.lev_4);

        level_1 = (TextView) view.findViewById(R.id.level_1);
        level_2 = (TextView) view.findViewById(R.id.level_2);
        level_3 = (TextView) view.findViewById(R.id.level_3);
        level_4 = (TextView) view.findViewById(R.id.level_4);

        text_t_a = (TextView) view.findViewById(R.id.level_a);
        text_t_b = (TextView) view.findViewById(R.id.level_b);
        text_t_c = (TextView) view.findViewById(R.id.level_c);
        text_t_d = (TextView) view.findViewById(R.id.level_d);



        pref = getActivity().getSharedPreferences("stationdetails", 0);
        station_title = (TextView) view.findViewById(R.id.platform_title);
        station_title.setText(pref.getString("station_longname", null));
        station_title.setTypeface(fontStyle.helveticabold_CE);



        if(pref.getString("station_platform_info", null).equalsIgnoreCase("null")){

            platforms.setVisibility(View.GONE);
            platform_alrt.setVisibility(View.VISIBLE);
        }else {

            try {

                JSONArray jsonArray = new JSONArray(pref.getString("station_platform_info", null));
                if (jsonArray.length()== 2) {
                    lev_a.setVisibility(View.VISIBLE);
                    lev_b.setVisibility(View.VISIBLE);
                    lev_c.setVisibility(View.GONE);
                    lev_d.setVisibility(View.GONE);

                    if(jsonArray.getJSONObject(0).getString("level").equalsIgnoreCase("Level 1")){
                        level_1.setText(jsonArray.getJSONObject(0).getString("level"));
                        level_2.setText(jsonArray.getJSONObject(1).getString("level"));
                        text_t_a.setText("Towards "+jsonArray.getJSONObject(0).getString("towards"));
                        text_t_b.setText("Towards "+jsonArray.getJSONObject(1).getString("towards"));
                    }else {
                        level_3.setText(jsonArray.getJSONObject(0).getString("level"));
                        level_4.setText(jsonArray.getJSONObject(1).getString("level"));
                        text_t_c.setText("Towards "+jsonArray.getJSONObject(0).getString("towards"));
                        text_t_d.setText("Towards "+jsonArray.getJSONObject(1).getString("towards"));
                    }

                }else if (jsonArray.length()== 3) {
                    lev_a.setVisibility(View.VISIBLE);
                    lev_b.setVisibility(View.VISIBLE);
                    lev_c.setVisibility(View.VISIBLE);
                    lev_d.setVisibility(View.GONE);
                    Log.d("json values", ""+jsonArray.length() + jsonArray.getJSONObject(0).getString("towards"));
                    if(jsonArray.getJSONObject(0).getString("level").equalsIgnoreCase("Level 1")&&jsonArray.getJSONObject(1).getString("level").equalsIgnoreCase("Level 1")) {
                        level_1.setText(jsonArray.getJSONObject(0).getString("level"));
                        level_2.setText(jsonArray.getJSONObject(1).getString("level"));
                        text_t_a.setText("Towards "+jsonArray.getJSONObject(0).getString("towards"));
                        text_t_b.setText("Towards "+jsonArray.getJSONObject(1).getString("towards"));
                    }
                    if(jsonArray.getJSONObject(2).getString("level").equalsIgnoreCase("Level 2")) {
                        level_3.setText(jsonArray.getJSONObject(2).getString("level"));
                        text_t_c.setText("Towards "+jsonArray.getJSONObject(2).getString("towards"));
                    }



                }else{
                    lev_a.setVisibility(View.VISIBLE);
                    lev_b.setVisibility(View.VISIBLE);
                    lev_c.setVisibility(View.VISIBLE);
                    lev_d.setVisibility(View.VISIBLE);
                    Log.d("json values", ""+jsonArray.length() + jsonArray.getJSONObject(0).getString("towards"));
                    if(jsonArray.getJSONObject(0).getString("level").equalsIgnoreCase("Level 1")&&jsonArray.getJSONObject(1).getString("level").equalsIgnoreCase("Level 1")) {
                        level_1.setText(jsonArray.getJSONObject(0).getString("level"));
                        level_2.setText(jsonArray.getJSONObject(1).getString("level"));
                        text_t_a.setText("Towards "+jsonArray.getJSONObject(0).getString("towards"));
                        text_t_b.setText("Towards "+jsonArray.getJSONObject(1).getString("towards"));
                    }
                    if(jsonArray.getJSONObject(2).getString("level").equalsIgnoreCase("Level 2")&&jsonArray.getJSONObject(3).getString("level").equalsIgnoreCase("Level 2")) {
                        level_3.setText(jsonArray.getJSONObject(2).getString("level"));
                        level_4.setText(jsonArray.getJSONObject(3).getString("level"));
                        text_t_c.setText("Towards "+jsonArray.getJSONObject(2).getString("towards"));
                        text_t_d.setText("Towards "+jsonArray.getJSONObject(3).getString("towards"));
                    }



                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


          /*Title Animation*/
        train1 = (ImageView) view.findViewById(R.id.train);
        train2 = (ImageView) view.findViewById(R.id.train_1);
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        //  textView.setText(content);
        animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.slid_up);
        train1.startAnimation(animFadein);
        animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        train2.startAnimation(animFadein);


        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        return view;


    }
}