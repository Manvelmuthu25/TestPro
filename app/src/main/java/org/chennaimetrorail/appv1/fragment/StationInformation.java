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
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;

/**
 * Created by 102525 on 7/10/2017.
 */

public class StationInformation extends Fragment implements View.OnClickListener {


    LinearLayout station_details, lost_and_found, contact_information,train_taimings,availability_parking,platforms,gates_directions,where_to_say_and_dine;
    FragmentManager fm;
    TextView station_title;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    Fragment fragment;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.station_information, container, false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        pref = getActivity().getSharedPreferences("stationdetails", 0);
        Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);

        station_title = (TextView)view.findViewById(R.id.station_title);
        station_title.setText(pref.getString("station_longname",null));
        station_title.setTypeface(fontStyle.helveticabold_CE);


        station_details = (LinearLayout) view.findViewById(R.id.station_details);
        station_details.setOnClickListener(this);
        train_taimings = (LinearLayout) view.findViewById(R.id.train_taimings);
        train_taimings.setOnClickListener(this);
        platforms = (LinearLayout) view.findViewById(R.id.platforms);
        platforms.setOnClickListener(this);
        gates_directions = (LinearLayout) view.findViewById(R.id.gates_directions);
        gates_directions.setOnClickListener(this);
        lost_and_found = (LinearLayout) view.findViewById(R.id.lost_and_found);
        lost_and_found.setOnClickListener(this);
        contact_information = (LinearLayout) view.findViewById(R.id.contact_information);
        contact_information.setOnClickListener(this);
        availability_parking = (LinearLayout)view.findViewById(R.id.availability_parking);
        availability_parking.setOnClickListener(this);
        where_to_say_and_dine = (LinearLayout)view.findViewById(R.id.where_to_say_and_dine);
        where_to_say_and_dine.setOnClickListener(this);





   /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInfo());
                fragmentTransaction.commit();
            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Station Information");



        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new StationInfo());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });


        return view;


    }

    public void onClick(final View v) { //check for what button is pressed
        switch (v.getId()) {
            case R.id.station_details:
                fragmentTransaction.replace(R.id.fragment_place, new StationDetails());
                fragmentTransaction.commit();
                break;
            case R.id.lost_and_found:
                fragmentTransaction.replace(R.id.fragment_place, new LostFound());
                fragmentTransaction.commit();
                break;
            case R.id.platforms:
                fragmentTransaction.replace(R.id.fragment_place, new Platforms());
                fragmentTransaction.commit();
                break;
            case R.id.gates_directions:
                fragmentTransaction.replace(R.id.fragment_place, new GateDirection());
                fragmentTransaction.commit();
                break;
            case R.id.contact_information:
                fragmentTransaction.replace(R.id.fragment_place, new ContactInformation());
                fragmentTransaction.commit();
                break;
            case R.id.train_taimings:
                fragmentTransaction.replace(R.id.fragment_place, new TrainTiming());
                fragmentTransaction.commit();
                break;
            case R.id.availability_parking:
                fragmentTransaction.replace(R.id.fragment_place, new AvailabilityParking());
                fragmentTransaction.commit();
                break;
            case R.id.where_to_say_and_dine:
                fragmentTransaction.replace(R.id.fragment_place, new WheretoStayDin());
                fragmentTransaction.commit();
                break;
        }
    }


}
