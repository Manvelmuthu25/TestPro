package org.chennaimetrorail.appv1.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.NothingSelectedSpinnerAdapter;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.AutoAdapter;
import org.chennaimetrorail.appv1.adapter.BusAdapter;
import org.chennaimetrorail.appv1.adapter.CarAdapter;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.database.DatabaseHandler;
import org.chennaimetrorail.appv1.modal.jsonmodal.Feeders;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/12/2017.
 */

public class FeaderService extends Fragment {
    String TAG = "FeederService";
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    DatabaseHandler db;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    TableRow rout_btn1;
    TextView select_sta_txt;
    SharedPreferences s_prf;
    SharedPreferences.Editor s_editor;
    Dialog dialog;
    ViewPager viewPager;
    Spinner fs_spinner;
    String version;
    RecyclerView feeders_layout;
    LinearLayout btnbus, btncar, btnauto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feeder_service, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        feeders_layout = view.findViewById(R.id.feeders_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        feeders_layout.setLayoutManager(layoutManager);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        select_sta_txt = view.findViewById(R.id.select_sta_txt);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fs_spinner = view.findViewById(R.id.fs_station_name);
        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();
        db = new DatabaseHandler(getActivity());
        db.getAllStationdetails();
        s_prf = getActivity().getSharedPreferences("feeders", 0);
        s_editor = s_prf.edit();
        if (Constant.station_list.size() > 0) {
            Collections.sort(Constant.station_list, new Comparator<StationList>() {
                @Override
                public int compare(final StationList stationList, final StationList t1) {
                    return stationList.getStation_LongName().compareTo(t1.getStation_LongName());
                }
            });
        }
        String[] spinnerArray = new String[Constant.station_list.size()];
        final HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
        if (spinnerArray != null) {

            for (int i = 0; i < Constant.station_list.size(); i++) {
                spinnerMap.put(i, Constant.station_list.get(i).getStation_Id());
                spinnerArray[i] = Constant.station_list.get(i).getStation_LongName();
            }
        }

        btnbus = (LinearLayout) view.findViewById(R.id.btnbus);
        btncar = (LinearLayout) view.findViewById(R.id.btncar);
        btnauto = (LinearLayout) view.findViewById(R.id.btnauto);


        btnbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btnbus.setBackgroundResource(R.drawable.bottomblue_line);
                btncar.setBackgroundResource(0);
                btnauto.setBackgroundResource(0);
                s_editor.putString("message", null);
                s_editor.apply();
                if (fs_spinner.getSelectedItemPosition() == 0) {
                    select_sta_txt.setText(R.string.selectstation);
                } else if (Constant.feederBusList.size() != 0) {
                    feeders_layout.setAdapter(new BusAdapter(Constant.feederBusList, getActivity()));
                    select_sta_txt.setVisibility(View.GONE);
                    feeders_layout.setVisibility(View.VISIBLE);
                } else {
                    feeders_layout.setVisibility(View.GONE);
                    select_sta_txt.setVisibility(View.VISIBLE);
                    select_sta_txt.setText(R.string.no_feeder);
                }
            }
        });

        btncar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnbus.setBackgroundResource(0);
                btncar.setBackgroundResource(R.drawable.bottomblue_line);
                btnauto.setBackgroundResource(0);
                s_editor.putString("message", "car");
                s_editor.commit();
                if (fs_spinner.getSelectedItemPosition() == 0) {
                    select_sta_txt.setText(R.string.selectstation);
                } else if (Constant.feederCarList.size() != 0) {
                    feeders_layout.setAdapter(new CarAdapter(Constant.feederCarList, getActivity()));
                    select_sta_txt.setVisibility(View.GONE);
                    feeders_layout.setVisibility(View.VISIBLE);
                } else {
                    feeders_layout.setVisibility(View.GONE);
                    select_sta_txt.setVisibility(View.VISIBLE);
                    select_sta_txt.setText(R.string.no_feeder);
                }
            }
        });


        btnauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnbus.setBackgroundResource(0);
                btncar.setBackgroundResource(0);
                btnauto.setBackgroundResource(R.drawable.bottomblue_line);
                s_editor.putString("message", "auto");
                s_editor.commit();
                if (fs_spinner.getSelectedItemPosition() == 0) {
                    select_sta_txt.setText(R.string.selectstation);
                } else if (Constant.feederAutoList.size() != 0) {
                    feeders_layout.setAdapter(new AutoAdapter(Constant.feederAutoList, getActivity(), new FeaderService()));
                    select_sta_txt.setVisibility(View.GONE);
                    feeders_layout.setVisibility(View.VISIBLE);
                } else {
                    feeders_layout.setVisibility(View.GONE);
                    select_sta_txt.setVisibility(View.VISIBLE);
                    select_sta_txt.setText(R.string.no_feeder);
                }
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);


        fs_spinner.setAdapter(spinnerArrayAdapter);
        fs_spinner.setAdapter(new NothingSelectedSpinnerAdapter(spinnerArrayAdapter, R.layout.nothing_selected_station, getActivity()));
        if (s_prf.getString("slectStation", null) != null) {
            fs_spinner.setSelection(Integer.parseInt(s_prf.getString("slectStation", null)));
        }
        fs_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String station_id = spinnerMap.get(fs_spinner.getSelectedItemPosition() - 1);
                if (station_id != null) {

                    int_chk = new Internet_connection_checking(getActivity());
                    Connection = int_chk.checkInternetConnection();
                    if (!Connection) {

                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else {

                        s_editor.putString("slectStation", String.valueOf(fs_spinner.getSelectedItemPosition()));
                        s_editor.commit();
                        GetFeederService(station_id);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_editor.putString("slectStation", null);
                s_editor.putString("message", null);
                s_editor.commit();
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
        title_txt.setText("Feeder Service");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    s_editor.putString("slectStation", null);
                    s_editor.putString("message", null);
                    s_editor.commit();
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

    /*Send Feedback Details to server*/
    private void GetFeederService(String station_id) {


        final ApiInterface apiService = Feedback.getResponsee().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*Send Parameters to the Api*/
        Call<Feeders> call = apiService.GetFeederService(station_id, Constant.strApiKey, version);

        call.enqueue(new Callback<Feeders>() {
            @Override
            public void onResponse(Call<Feeders> call, Response<Feeders> response) {

                int statusCode = response.code();

                Log.d(TAG,call.request().url().toString());
                if (statusCode == 200) {
                    Constant.feederCarList.clear();
                    Constant.feederBusList.clear();
                    Constant.feederAutoList.clear();
                    loader.dismiss();
                    Constant.feederBusList = response.body().getBus();
                    Constant.feederCarList = response.body().getCar();
                    Constant.feederAutoList = response.body().getAuto();

                    if (s_prf.getString("message", null) != null) {

                        if (s_prf.getString("message", null).equals("auto")) {
                            btnbus.setBackgroundResource(0);
                            btncar.setBackgroundResource(0);
                            btnauto.setBackgroundResource(R.drawable.bottomblue_line);
                            if (Constant.feederAutoList.size() != 0) {
                                feeders_layout.setAdapter(new AutoAdapter(Constant.feederAutoList, getActivity(), new FeaderService()));
                                select_sta_txt.setVisibility(View.GONE);
                                feeders_layout.setVisibility(View.VISIBLE);
                            } else {
                                feeders_layout.setVisibility(View.GONE);
                                select_sta_txt.setVisibility(View.VISIBLE);
                                select_sta_txt.setText(R.string.no_feeder);
                            }
                        } else if (s_prf.getString("message", null).equals("car")) {
                            btnbus.setBackgroundResource(0);
                            btncar.setBackgroundResource(R.drawable.bottomblue_line);
                            btnauto.setBackgroundResource(0);
                            if (Constant.feederCarList.size() != 0) {
                                feeders_layout.setAdapter(new CarAdapter(Constant.feederCarList, getActivity()));
                                select_sta_txt.setVisibility(View.GONE);
                                feeders_layout.setVisibility(View.VISIBLE);
                            } else {
                                feeders_layout.setVisibility(View.GONE);
                                select_sta_txt.setVisibility(View.VISIBLE);
                                select_sta_txt.setText(R.string.no_feeder);
                            }
                        } else {
                            btnbus.setBackgroundResource(R.drawable.bottomblue_line);
                            btncar.setBackgroundResource(0);
                            btnauto.setBackgroundResource(0);

                            if (Constant.feederBusList.size() != 0) {
                                feeders_layout.setAdapter(new BusAdapter(Constant.feederBusList, getActivity()));
                                select_sta_txt.setVisibility(View.GONE);
                                feeders_layout.setVisibility(View.VISIBLE);
                            } else {
                                feeders_layout.setVisibility(View.GONE);
                                select_sta_txt.setVisibility(View.VISIBLE);
                                select_sta_txt.setText(R.string.no_feeder);
                            }
                        }
                    } else {
                        btnbus.setBackgroundResource(R.drawable.bottomblue_line);
                        btncar.setBackgroundResource(0);
                        btnauto.setBackgroundResource(0);

                        if (Constant.feederBusList.size() != 0) {
                            feeders_layout.setAdapter(new BusAdapter(Constant.feederBusList, getActivity()));
                            select_sta_txt.setVisibility(View.GONE);
                            feeders_layout.setVisibility(View.VISIBLE);
                        } else {
                            feeders_layout.setVisibility(View.GONE);
                            select_sta_txt.setVisibility(View.VISIBLE);
                            select_sta_txt.setText(R.string.no_feeder);
                        }
                    }

                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                } else {
                    loader.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Feeders> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "FeederExceiption" + t.toString());
                loader.dismiss();
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


}