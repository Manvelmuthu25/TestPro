package org.chennaimetrorail.appv1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;

/**
 * Created by 102525 on 7/10/2017.
 */

public class TourGuid extends Fragment implements View.OnClickListener {

    LinearLayout image_view_back, tour_spot_details, cultural_centres, local_weather_forecasts;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tour_guid, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        final Home home = new Home();
        home.toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        tour_spot_details = (LinearLayout) view.findViewById(R.id.tour_spot_details);
        cultural_centres = (LinearLayout) view.findViewById(R.id.cultural_centres);
        local_weather_forecasts = (LinearLayout) view.findViewById(R.id.local_weather_forecasts);


        tour_spot_details.setOnClickListener(this);
        cultural_centres.setOnClickListener(this);
        local_weather_forecasts.setOnClickListener(this);

        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Tour Guide");

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

                    return true;
                }
                return false;
            }
        });

        return view;

    }

    public void onClick(final View v) { //check for what button is pressed
        switch (v.getId()) {
            case R.id.tour_spot_details:
                fragmentTransaction.replace(R.id.fragment_place, new TourSpotsDetails());
                fragmentTransaction.commit();
                break;
            case R.id.cultural_centres:
                fragmentTransaction.replace(R.id.fragment_place, new CulturalCenter());
                fragmentTransaction.commit();
                break;
            case R.id.local_weather_forecasts:
                fragmentTransaction.replace(R.id.fragment_place, new WeatherReport());
                fragmentTransaction.commit();
                break;


        }
    }

}
