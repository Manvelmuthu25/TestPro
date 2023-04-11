package org.chennaimetrorail.appv1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.StationAdapter;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 102525 on 7/10/2017.
 */

public class StationInfo extends Fragment {


    List<StationList> station_list = new ArrayList<StationList>();

    RecyclerView recyclerView;
    StationAdapter sAdapter;
    EditText search;
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    final Home home = new Home();
    DatabaseHandler db;
    View stationInfoview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        stationInfoview = inflater.inflate(R.layout.station_info_list, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),stationInfoview);
        final Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

        recyclerView = (RecyclerView) stationInfoview.findViewById(R.id.stationinfo_recycler_view);
        image_view_back = (LinearLayout) stationInfoview.findViewById(R.id.image_view_back);
        search = (EditText) stationInfoview.findViewById(R.id.search);

        TextView title_txt = (TextView) stationInfoview.findViewById(R.id.title_txt);
        title_txt.setText(getString(R.string.station_info));

        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        db = new DatabaseHandler(getActivity());
        station_list = db.getAllStationdetails();

        Log.d("lsdk",""+station_list.size());
        if (station_list.size() > 0) {
            Collections.sort(station_list, new Comparator<StationList>() {
                @Override
                public int compare(final StationList stationList, final StationList t1) {
                    return stationList.getStation_LongName().compareTo(t1.getStation_LongName());
                }
            });
        }

        sAdapter = new StationAdapter(station_list, getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidekeyboard();
                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative =(RelativeLayout)getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

            }
        });


        addTextListener();


        return stationInfoview;

    }

    @Override
    public void onResume() {

        super.onResume();

    }
    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<StationList> filteredList = new ArrayList<StationList>();

                for (int i = 0; i < station_list.size(); i++) {

                    final String text = station_list.get(i).getStation_LongName().toLowerCase();
                    if (text.contains(query)) {

                        StationList stationList = new StationList();

                        stationList.setStation_LongName(station_list.get(i).getStation_LongName());

                      //  Log.d("LongName1", ""+station_list.get(i).getStation_LongName());

                        stationList.setStation_LineColour(station_list.get(i).getStation_LineColour());
                        stationList.setStation_Id(station_list.get(i).getStation_Id());
                        stationList.setStation_Id(station_list.get(i).getStation_Id());
                        Log.d("LongNameid", ""+ (station_list.get(i).getStation_Id()));

                        stationList.setStation_Code(station_list.get(i).getStation_Code());
                        stationList.setStation_ShortName(station_list.get(i).getStation_ShortName());
                        Log.d("LongName1", ""+station_list);

                        stationList.setStation_LongName(stationList.getStation_LongName());
                        stationList.setStation_Latitude(station_list.get(i).getStation_Latitude());
                        Log.d("Latitude", "1"+ (station_list.get(i).getStation_Latitude()));

                        stationList.setStation_Longitude(station_list.get(i).getStation_Longitude());
                        Log.d("Latitude", "2"+ (station_list.get(i).getStation_Longitude()));

                        stationList.setStation_Priority(station_list.get(i).getStation_Priority());
                        stationList.setStation_LineColour(station_list.get(i).getStation_LineColour());
                        stationList.setGates_Directions(station_list.get(i).getGates_Directions());
                        stationList.setStation_Contacts(station_list.get(i).getStation_Contacts());
                        stationList.setPlatform_Info(station_list.get(i).getPlatform_Info());
                        stationList.setStation_Type(station_list.get(i).getStation_Type());
                        filteredList.add(stationList);
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                sAdapter = new StationAdapter(filteredList, getActivity());
                recyclerView.setAdapter(sAdapter);
                sAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    public void hidekeyboard() {
        InputMethodManager inputManager = (InputMethodManager) stationInfoview.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = stationInfoview.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}