package org.chennaimetrorail.appv1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.activity.Home;

/**
 * Created by 102525 on 7/12/2017.
 */

public class ContactInformation extends Fragment {
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    TextView contact_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.contact_information, container, false);
        //set Fragment Manager
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        //set Font style
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        //initialize home activity
        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        /*Get Shared preferences Station Details*/
        pref = getActivity().getSharedPreferences("stationdetails", 0);

        TextView station_title = (TextView) view.findViewById(R.id.station_title);
        contact_info = (TextView) view.findViewById(R.id.contact_txt);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);

        station_title.setTypeface(fontStyle.helveticabold_CE);

        station_title.setText(pref.getString("station_longname", null));
        contact_info.setText(pref.getString("station_contacts", null));
        title_txt.setText("Contact Information");

        /*Back button action*/
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });
        /*Keyboard Back button action*/
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

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