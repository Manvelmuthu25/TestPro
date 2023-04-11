package org.chennaimetrorail.appv1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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

public class OtherInfo extends Fragment implements View.OnClickListener {

    LinearLayout image_view_back,fare_products,flash_news,metro_network_information,instruction_commuters,facilities_disabled,helpline;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.other_info, container, false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        final Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);

        fare_products = (LinearLayout) view.findViewById(R.id.fare_products);
        flash_news = (LinearLayout) view.findViewById(R.id.flash_news);
        metro_network_information = (LinearLayout) view.findViewById(R.id.metro_network_info);
        instruction_commuters = (LinearLayout) view.findViewById(R.id.instruction_commuters);
        facilities_disabled = (LinearLayout) view.findViewById(R.id.facilities_disabled);
        helpline = (LinearLayout) view.findViewById(R.id.helpline);

        TextView title_txt = (TextView)view.findViewById(R.id.title_txt);
        title_txt.setText("Other Information");
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative =(RelativeLayout)getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

            }
        });
        fare_products.setOnClickListener(this);
        flash_news.setOnClickListener(this);
        metro_network_information.setOnClickListener(this);
        instruction_commuters.setOnClickListener(this);
        facilities_disabled.setOnClickListener(this);
        helpline.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {


                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.toolbrtxt_layout.setVisibility(View.GONE);
                    home.toolbar_img.setVisibility(View.VISIBLE);
                    home.home_relative =(RelativeLayout)getActivity().findViewById(R.id.home_relative);
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
            case R.id.fare_products:
                fragmentTransaction.replace(R.id.fragment_place, new FareProducts());
                fragmentTransaction.commit();
                break;
            case R.id.flash_news:
                fragmentTransaction.replace(R.id.fragment_place, new Notifications());
                fragmentTransaction.commit();
                break;
            case R.id.metro_network_info:
                fragmentTransaction.replace(R.id.fragment_place, new MetronetInformation());
                fragmentTransaction.commit();
                break;
            case R.id.instruction_commuters:
                fragmentTransaction.replace(R.id.fragment_place, new InstructiontoCommuters());
                fragmentTransaction.commit();
                break;
            case R.id.facilities_disabled:
                fragmentTransaction.replace(R.id.fragment_place, new FacilitesforDisabled());
                fragmentTransaction.commit();
                break;
            case R.id.helpline:
                fragmentTransaction.replace(R.id.fragment_place, new ContactUs());
                fragmentTransaction.commit();
                break;

        }
    }

}
