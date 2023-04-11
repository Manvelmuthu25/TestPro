package org.chennaimetrorail.appv1.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.NothingSelectedSpinnerAdapter;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.RoutAdapter;
import org.chennaimetrorail.appv1.modal.StationList;
import org.chennaimetrorail.appv1.database.DatabaseHandler;
import org.chennaimetrorail.appv1.modal.jsonmodal.FTDuration;
import org.chennaimetrorail.appv1.modal.jsonmodal.FTRoute;
import org.chennaimetrorail.appv1.modal.jsonmodal.FTTimeResponse;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/7/2017.
 */
public class TravelPlanner extends Fragment {

    String TAG= "TravelPlanner";
    Dialog dialog;
    String dialog_msgs;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    List<StationList> station_list = new ArrayList<StationList>();
    LinearLayout image_view_back, upcoming_trainlayout;
    Spinner from, to;
    FragmentManager fm;
    TextView routinfo_listbtn, station_in_btw_txt, first_class_txt, general_class_txt, total_distance_txt, from_txt, to_txt, timeduration_txt;
    ImageView icon, from_img, to_img;
    RecyclerView recyclerView;
    RelativeLayout from_relative, to_relative;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    EditText date_edt, time_edt;
    ProgressBar progressBar;
    TextView ftfirst_triantime, ftlast_triantime, fttrain_time1, fttrain_time2, fttrain_time3, fttrain_time4, ftplatform1, ftplatform2, ftplatform3, ftplatform4;
    LinearLayout fttime_layout1, fttime_layout2, fttime_layout3, fttime_layout4, recent_trintime_View;
    DatabaseHandler db;
    Calendar myCalendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    String mytimeformat = "HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    SimpleDateFormat sdft = new SimpleDateFormat(mytimeformat, Locale.US);
    ArrayAdapter<String> spinnerArrayAdapter;
    String fromStation_id, toStation_id;
    TextView error_note, message_info;
    ImageView fttime_layout1_img, fttime_layout2_img, fttime_layout3_img, fttime_layout4_img,fttime_img4,fttime_img3,fttime_img2,fttime_img1;
    List<FTDuration> ftDurations;

    List<FTRoute> ftDurationss;
    private List<FTRoute> homework_Response_listsses = new ArrayList<FTRoute>();

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat tf = new SimpleDateFormat("KK:mm aa");

    //SimpleDateFormat tf = new SimpleDateFormat("HH:MM");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.travel_planner, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        db = new DatabaseHandler(getActivity());
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        myCalendar = Calendar.getInstance();
        from = (Spinner) view.findViewById(R.id.from_spinner);
        to = (Spinner) view.findViewById(R.id.to_spinner);
        routinfo_listbtn = (TextView) view.findViewById(R.id.routinfo_listbtn);
        station_in_btw_txt = (TextView) view.findViewById(R.id.station_in_btw_txt);
        station_in_btw_txt.setTypeface(fontStyle.helveticabold_CE);
       // first_class_txt = (TextView) view.findViewById(R.id.first_class_txt);
       // first_class_txt.setTypeface(fontStyle.helveticabold_CE);
        general_class_txt = (TextView) view.findViewById(R.id.general_class_txt);
        general_class_txt.setTypeface(fontStyle.helveticabold_CE);
        total_distance_txt = (TextView) view.findViewById(R.id.total_distance_txt);
        total_distance_txt.setTypeface(fontStyle.helveticabold_CE);
        timeduration_txt = (TextView) view.findViewById(R.id.timeduration_txt);
        timeduration_txt.setTypeface(fontStyle.helveticabold_CE);

        ftfirst_triantime = (TextView) view.findViewById(R.id.ftfirst_triantime);
        ftlast_triantime = (TextView) view.findViewById(R.id.ftlast_triantime);
        fttrain_time1 = (TextView) view.findViewById(R.id.fttrain_time1);
        fttrain_time2 = (TextView) view.findViewById(R.id.fttrain_time2);
        fttrain_time3 = (TextView) view.findViewById(R.id.fttrain_time3);
        fttrain_time4 = (TextView) view.findViewById(R.id.fttrain_time4);
        ftplatform1 = (TextView) view.findViewById(R.id.ftplatform1);
        ftplatform2 = (TextView) view.findViewById(R.id.ftplatform2);
        ftplatform3 = (TextView) view.findViewById(R.id.ftplatform3);
        ftplatform4 = (TextView) view.findViewById(R.id.ftplatform4);

        fttime_layout1 = (LinearLayout) view.findViewById(R.id.fttime_layout1);
        fttime_layout2 = (LinearLayout) view.findViewById(R.id.fttime_layout2);
        fttime_layout3 = (LinearLayout) view.findViewById(R.id.fttime_layout3);
        fttime_layout4 = (LinearLayout) view.findViewById(R.id.fttime_layout4);

        fttime_layout1_img = view.findViewById(R.id.fttime_layout1_img);
        fttime_layout2_img = view.findViewById(R.id.fttime_layout2_img);
        fttime_layout3_img = view.findViewById(R.id.fttime_layout3_img);
        fttime_layout4_img = view.findViewById(R.id.fttime_layout4_img);

        fttime_img1 = view.findViewById(R.id.fttime_img1);
        fttime_img2 = view.findViewById(R.id.fttime_img2);
        fttime_img3 = view.findViewById(R.id.fttime_img3);
        fttime_img4 = view.findViewById(R.id.fttime_img4);
        message_info = view.findViewById(R.id.message_info);
        recent_trintime_View = (LinearLayout) view.findViewById(R.id.recent_trintime_View);
        fttime_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fttime_layout1_img.setVisibility(View.VISIBLE);
                fttime_layout2_img.setVisibility(View.GONE);
                fttime_layout3_img.setVisibility(View.GONE);
                fttime_layout4_img.setVisibility(View.GONE);
                message_info.setText(ftDurations.get(0).getRouteinfo());
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                homework_Response_listsses = ftDurations.get(0).getRoute();
                Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));



                station_in_btw_txt.setText(ftDurations.get(0).getInbetweenstation_child());
                total_distance_txt.setText(ftDurations.get(0).getDistance_child());
                timeduration_txt.setText(ftDurations.get(0).getDuration_child());
            }
        });

        fttime_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fttime_layout1_img.setVisibility(View.GONE);
                fttime_layout2_img.setVisibility(View.VISIBLE);
                fttime_layout3_img.setVisibility(View.GONE);
                fttime_layout4_img.setVisibility(View.GONE);
                message_info.setText(ftDurations.get(1).getRouteinfo());
                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                homework_Response_listsses = ftDurations.get(1).getRoute();
                Log.d(TAG, "StatusCOe" + ftDurations.get(1).getRoute());
                recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));



                station_in_btw_txt.setText(ftDurations.get(1).getInbetweenstation_child());
                total_distance_txt.setText(ftDurations.get(1).getDistance_child());
                timeduration_txt.setText(ftDurations.get(1).getDuration_child());
            }
        });

        fttime_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fttime_layout1_img.setVisibility(View.GONE);
                fttime_layout2_img.setVisibility(View.GONE);
                fttime_layout3_img.setVisibility(View.VISIBLE);
                fttime_layout4_img.setVisibility(View.GONE);
                message_info.setText(ftDurations.get(2).getRouteinfo());
                recyclerView.setVisibility(View.VISIBLE);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                homework_Response_listsses = ftDurations.get(2).getRoute();
                Log.d(TAG, "StatusCOe" + ftDurations.get(2).getRoute());
                recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));


                station_in_btw_txt.setText(ftDurations.get(2).getInbetweenstation_child());
                total_distance_txt.setText(ftDurations.get(2).getDistance_child());
                timeduration_txt.setText(ftDurations.get(2).getDuration_child());
            }
        });

        fttime_layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fttime_layout1_img.setVisibility(View.GONE);
                fttime_layout2_img.setVisibility(View.GONE);
                fttime_layout3_img.setVisibility(View.GONE);
                fttime_layout4_img.setVisibility(View.VISIBLE);
                message_info.setText(ftDurations.get(3).getRouteinfo());


                recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                homework_Response_listsses = ftDurations.get(3).getRoute();
                Log.d(TAG, "StatusCOe" + ftDurations.get(3).getRoute());
                recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));


                station_in_btw_txt.setText(ftDurations.get(3).getInbetweenstation_child());
                total_distance_txt.setText(ftDurations.get(3).getDistance_child());
                timeduration_txt.setText(ftDurations.get(3).getDuration_child());



            }
        });
        icon = (ImageView) view.findViewById(R.id.icon);
        from_relative = (RelativeLayout) view.findViewById(R.id.from_relative);
        to_relative = (RelativeLayout) view.findViewById(R.id.to_relative);
        from_img = (ImageView) view.findViewById(R.id.from_status);
        to_img = (ImageView) view.findViewById(R.id.to_status);
        from_txt = (TextView) view.findViewById(R.id.from_rout_name);
        to_txt = (TextView) view.findViewById(R.id.to_rout_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.rout_recy);

        progressBar = view.findViewById(R.id.progressBar);
        date_edt = (EditText) view.findViewById(R.id.date_edt);
        time_edt = (EditText) view.findViewById(R.id.time_edt);
        upcoming_trainlayout = (LinearLayout) view.findViewById(R.id.upcoming_trainlayout);

        error_note = (TextView) view.findViewById(R.id.error_note);
        final int mYear = myCalendar.get(Calendar.YEAR);
        final int mMonth = myCalendar.get(Calendar.MONTH);
        final int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        final int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        final int minute = myCalendar.get(Calendar.MINUTE);
        final Date currentTime = Calendar.getInstance().getTime();
        time_edt.setText(sdft.format(currentTime));


        date_edt.setText(sdf.format(new Date()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
        date_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), date, mYear, mMonth, mDay);
                dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpDialog.show();
            }
        });

        time_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String am_pm;

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        am_pm = getTime(selectedHour, selectedMinute);

                        stringBuilder.append(" ");
                        stringBuilder.append(am_pm);
                        time_edt.setText(stringBuilder);


                        upcoming_trainlayout.setVisibility(View.VISIBLE);
                        if (fromStation_id != null && toStation_id != null) {


                            int_chk = new Internet_connection_checking(getActivity());


                            Connection = int_chk.checkInternetConnection();

                            if (!Connection) {
                                recent_trintime_View.setVisibility(View.GONE);
                                error_note.setVisibility(View.VISIBLE);
                                error_note.setText(R.string.this_internet);
                            } else {
                                recent_trintime_View.setVisibility(View.GONE);
                                getUpcomingTrinDetails(fromStation_id, toStation_id);
                            }


                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        if (station_list != null) {
            station_list = db.getAllStationdetails();
        }
        if (station_list != null) {
            if (station_list.size() > 0) {
                Collections.sort(station_list, new Comparator<StationList>() {
                    @Override
                    public int compare(final StationList stationList, final StationList t1) {
                        return stationList.getStation_LongName().compareTo(t1.getStation_LongName());
                    }
                });

        }
        }

        routinfo_listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.intermediate_station_list.size() != 0) {
                    if (recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.VISIBLE);
                        from_relative.setVisibility(View.VISIBLE);
                        to_relative.setVisibility(View.VISIBLE);
                        icon.setImageResource(R.drawable.ic_remove_black_24dp);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        from_relative.setVisibility(View.GONE);
                        to_relative.setVisibility(View.GONE);
                        icon.setImageResource(R.drawable.ic_add_black_24dp);
                    }
                }

            }
        });

        String[] spinnerArray = new String[station_list.size()];
        final HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
        if (spinnerArray != null) {

            for (int i = 0; i < station_list.size(); i++) {
                spinnerMap.put(i, station_list.get(i).getStation_Id());
                spinnerArray[i] = station_list.get(i).getStation_LongName();
            }
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        from.setAdapter(spinnerArrayAdapter);
        to.setAdapter(spinnerArrayAdapter);

        from.setAdapter(new NothingSelectedSpinnerAdapter(spinnerArrayAdapter, R.layout.contact_spinner_row_nothing_selected, getActivity()));
        to.setAdapter(new NothingSelectedSpinnerAdapter(spinnerArrayAdapter, R.layout.contact_spinner_row_nothing_selected_to, getActivity()));


        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here


                fromStation_id = spinnerMap.get(from.getSelectedItemPosition() - 1);
                toStation_id = spinnerMap.get(to.getSelectedItemPosition() - 1);
                if (fromStation_id != null && toStation_id != null && fromStation_id != toStation_id) {
                    getIntermediatestation(fromStation_id, toStation_id);

                    int_chk = new Internet_connection_checking(getActivity());


                    Connection = int_chk.checkInternetConnection();

                    if (!Connection) {
                        recent_trintime_View.setVisibility(View.GONE);
                        error_note.setVisibility(View.VISIBLE);
                        error_note.setText(R.string.this_internet);
                    } else {
                        recent_trintime_View.setVisibility(View.GONE);
                        getUpcomingTrinDetails(fromStation_id, toStation_id);
                    }
                } else {

                    if (fromStation_id != null && toStation_id != null && fromStation_id == toStation_id) {
                        Toast.makeText(getActivity(),R.string.fromto_same,Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });
        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                fromStation_id = spinnerMap.get(from.getSelectedItemPosition() - 1);
                toStation_id = spinnerMap.get(to.getSelectedItemPosition() - 1);

                if (fromStation_id != null && toStation_id != null && fromStation_id != toStation_id) {
                    getIntermediatestation(fromStation_id, toStation_id);

                    int_chk = new Internet_connection_checking(getActivity());


                    Connection = int_chk.checkInternetConnection();

                    if (!Connection) {
                        recent_trintime_View.setVisibility(View.GONE);
                        error_note.setVisibility(View.VISIBLE);
                        error_note.setText(R.string.this_internet);
                    } else {
                        recent_trintime_View.setVisibility(View.GONE);
                        getUpcomingTrinDetails(fromStation_id, toStation_id);
                    }
                } else {
                    if (fromStation_id != null && toStation_id != null && fromStation_id == toStation_id) {

                        Toast.makeText(getActivity(),R.string.fromto_same,Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });


        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Travel Planner");
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.toolbar.setBackgroundResource(R.color.color_transprant);
                home.toolbrtxt_layout.setVisibility(View.GONE);
                home.toolbar_img.setVisibility(View.VISIBLE);
                home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                home.home_relative.setVisibility(View.VISIBLE);
                Constant.intermediate_station_list.clear();
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

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
                    Constant.intermediate_station_list.clear();
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

                    return true;
                }
                return false;
            }
        });
        return view;

    }








    public String getTime(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(cal.getTime());
    }

    private void updateLabel() {

        date_edt.setText(sdf.format(myCalendar.getTime()));
        if (fromStation_id != null && toStation_id != null) {

            int_chk = new Internet_connection_checking(getActivity());


            Connection = int_chk.checkInternetConnection();

            if (!Connection) {
                recent_trintime_View.setVisibility(View.GONE);
                error_note.setVisibility(View.VISIBLE);
                error_note.setText(R.string.this_internet);
            } else {
                recent_trintime_View.setVisibility(View.GONE);
                getUpcomingTrinDetails(fromStation_id, toStation_id);
            }
        }
    }

    public void getIntermediatestation(String f_station_id, String t_station_id) {

     //  db.getIntermediateStations(f_station_id, t_station_id);


        if (Constant.intermediate_station_list != null) {



        } else {
            general_class_txt.setText("-");
           // first_class_txt.setText("-");
            station_in_btw_txt.setText("-");
            total_distance_txt.setText("-");
        }

    }

    //Error Messages
    public void dialogs() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialogs);
        dialog.setCancelable(false);
        isShown = true;


        TextView tv_msg = (TextView) dialog.findViewById(R.id.dialog_texts);

        tv_msg.setText("" + dialog_msgs);

        Button btn_ok = (Button) dialog.findViewById(R.id.btn_dialogs_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void getUpcomingTrinDetails(final String fstationid, final String toStation_id) {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        String startDateString = date_edt.getText().toString();


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
        String finalString = newFormat.format(date);
        Log.d(TAG, "getTimet---able" + finalString);


        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Call<FTTimeResponse> call = apiService.TravelPlannerUpcomingTrainsFromTo(fstationid, toStation_id, Constant.strApiKey, finalString, time_edt.getText().toString(), info.versionName);
        // Set up progress before call
        progressBar.setVisibility(View.VISIBLE);

        general_class_txt.setText("-");
       // first_class_txt.setText("-");
        station_in_btw_txt.setText("-");
        total_distance_txt.setText("-");
        timeduration_txt.setText("-");

        recyclerView.setVisibility(View.GONE);

        call.enqueue(new Callback<FTTimeResponse>() {
            @Override
            public void onResponse(Call<FTTimeResponse> call, Response<FTTimeResponse> response) {
                int statusCode = response.code();

                  Log.d("travelplanner",""+call.request().url().toString());


                if (statusCode == 200) {

                    if (response.body() != null) {

                        if (response.body().getTimetable() != null) {


                            progressBar.setVisibility(View.GONE);
                            //          Log.d(TAG, "getTimetable" + response.body().getTimetable().toString());
                            ftDurations = response.body().getTimetable();
                            ftDurationss = ftDurations.get(0).getRoute();

                            if (response.body().getTimetable() == null) {

                                progressBar.setVisibility(View.GONE);
                                recent_trintime_View.setVisibility(View.GONE);
                                error_note.setVisibility(View.VISIBLE);
                            } else if (response.body().getReason().equals("Trains Available")) {
                                //    Log.d(TAG, "getColorcode" + ftDurations.size());
                                //    Log.d(TAG, "getColorcode1" + ftDurationss.size());
                                recent_trintime_View.setVisibility(View.VISIBLE);
                                error_note.setVisibility(View.GONE);
                                ftfirst_triantime.setText(response.body().getFirsttrain());
                                ftlast_triantime.setText(response.body().getLasttrain());
                                message_info.setText(ftDurations.get(0).getRouteinfo());
                                fttime_layout1_img.setVisibility(View.VISIBLE);
                                fttime_layout2_img.setVisibility(View.GONE);
                                fttime_layout3_img.setVisibility(View.GONE);
                                fttime_layout4_img.setVisibility(View.GONE);
                                message_info.setText(ftDurations.get(0).getRouteinfo());
                                general_class_txt.setText(response.body().getGeneralclassfare());
                                //    first_class_txt.setText(response.body().getSpecialclassfare());

                                station_in_btw_txt.setText(ftDurations.get(0).getInbetweenstation_child());
                                total_distance_txt.setText(ftDurations.get(0).getDistance_child());
                                timeduration_txt.setText(ftDurations.get(0).getDuration_child());


                                if (ftDurations.size() == 1) {

                                    fttime_layout1.setVisibility(View.VISIBLE);
                                    Picasso.with(getActivity()).load(ftDurations.get(0).getColorcode()).into(fttime_img1);
                                    fttrain_time1.setText(ftDurations.get(0).getTrain_time());
                                    //   Log.d(TAG, "StatusCOetime" + ftDurations.get(0).getColorcode());

                                    ftplatform1.setText("P" + ftDurations.get(0).getPlatform());
                                    fttime_layout2.setVisibility(View.GONE);
                                    fttime_layout3.setVisibility(View.GONE);
                                    fttime_layout4.setVisibility(View.GONE);
                                    message_info.setText(ftDurations.get(0).getRouteinfo());
                                    fttime_img2.setImageResource(0);
                                    fttime_img3.setImageResource(0);
                                    fttime_img4.setImageResource(0);


                                    recyclerView.setVisibility(View.VISIBLE);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);
                                    homework_Response_listsses = ftDurations.get(0).getRoute();
                                    //  Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                                    recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));

                                } else if (ftDurations.size() == 2) {

                                    fttime_layout1.setVisibility(View.VISIBLE);
                                    fttime_layout2.setVisibility(View.VISIBLE);
                                    Picasso.with(getActivity()).load(ftDurations.get(0).getColorcode()).into(fttime_img1);
                                    Picasso.with(getActivity()).load(ftDurations.get(1).getColorcode()).into(fttime_img2);
                                    fttrain_time1.setText(ftDurations.get(0).getTrain_time());
                                    ftplatform1.setText("P" + ftDurations.get(0).getPlatform());
                                    fttrain_time2.setText(ftDurations.get(1).getTrain_time());
                                    ftplatform2.setText("P" + ftDurations.get(1).getPlatform());
                                    fttime_layout3.setVisibility(View.GONE);
                                    fttime_layout4.setVisibility(View.GONE);
                                    fttime_img3.setImageResource(0);
                                    fttime_img4.setImageResource(0);


                                    recyclerView.setVisibility(View.VISIBLE);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);
                                    homework_Response_listsses = ftDurations.get(0).getRoute();
                                    Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                                    recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));
                                } else if (ftDurations.size() == 3) {

                                    fttime_layout1.setVisibility(View.VISIBLE);
                                    fttime_layout2.setVisibility(View.VISIBLE);
                                    fttime_layout3.setVisibility(View.VISIBLE);
                                    Picasso.with(getActivity()).load(ftDurations.get(0).getColorcode()).into(fttime_img1);
                                    Picasso.with(getActivity()).load(ftDurations.get(1).getColorcode()).into(fttime_img2);
                                    Picasso.with(getActivity()).load(ftDurations.get(2).getColorcode()).into(fttime_img3);
                                    fttrain_time1.setText(ftDurations.get(0).getTrain_time());
                                    ftplatform1.setText("P" + ftDurations.get(0).getPlatform());
                                    fttrain_time2.setText(ftDurations.get(1).getTrain_time());
                                    ftplatform2.setText("P" + ftDurations.get(1).getPlatform());
                                    fttrain_time3.setText(ftDurations.get(2).getTrain_time());
                                    ftplatform3.setText("P" + ftDurations.get(2).getPlatform());
                                    fttime_layout4.setVisibility(View.GONE);
                                    fttime_img4.setImageResource(0);


                                    recyclerView.setVisibility(View.VISIBLE);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);
                                    homework_Response_listsses = ftDurations.get(0).getRoute();
                                    //  Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                                    recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));
                                } else {
                                    Picasso.with(getActivity()).load(ftDurations.get(0).getColorcode()).into(fttime_img1);
                                    Picasso.with(getActivity()).load(ftDurations.get(1).getColorcode()).into(fttime_img2);
                                    Picasso.with(getActivity()).load(ftDurations.get(2).getColorcode()).into(fttime_img3);
                                    Picasso.with(getActivity()).load(ftDurations.get(3).getColorcode()).into(fttime_img4);
                                    fttime_layout1.setVisibility(View.VISIBLE);
                                    fttime_layout2.setVisibility(View.VISIBLE);
                                    fttime_layout3.setVisibility(View.VISIBLE);
                                    fttime_layout4.setVisibility(View.VISIBLE);
                                    fttrain_time1.setText(ftDurations.get(0).getTrain_time());
                                    ftplatform1.setText("P" + ftDurations.get(0).getPlatform());
                                    fttrain_time2.setText(ftDurations.get(1).getTrain_time());
                                    ftplatform2.setText("P" + ftDurations.get(1).getPlatform());
                                    fttrain_time3.setText(ftDurations.get(2).getTrain_time());
                                    ftplatform3.setText("P" + ftDurations.get(2).getPlatform());
                                    fttrain_time4.setText(ftDurations.get(3).getTrain_time());
                                    ftplatform4.setText("P" + ftDurations.get(3).getPlatform());


                                    recyclerView.setVisibility(View.VISIBLE);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);
                                    homework_Response_listsses = ftDurations.get(0).getRoute();
                                    ///   Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                                    recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));


                                }
                            } else if (response.body().getReason().equals("No Trains Available")) {

                                progressBar.setVisibility(View.GONE);
                                recent_trintime_View.setVisibility(View.GONE);
                                error_note.setVisibility(View.VISIBLE);
                                error_note.setText(R.string.we_cannot_train);

                                general_class_txt.setText(response.body().getGeneralclassfare());
                                //  first_class_txt.setText(response.body().getSpecialclassfare());
                                recyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                homework_Response_listsses = ftDurations.get(0).getRoute();
                                //  Log.d(TAG, "StatusCOe" + ftDurations.get(0).getRoute());
                                recyclerView.setAdapter(new RoutAdapter(homework_Response_listsses, getContext()));


                                station_in_btw_txt.setText(ftDurations.get(0).getInbetweenstation_child());
                                total_distance_txt.setText(ftDurations.get(0).getDistance_child());
                                timeduration_txt.setText(ftDurations.get(0).getDuration_child());

                            }

                        }
                    }
                } else if (statusCode == 400) {

                    progressBar.setVisibility(View.GONE);
                    recent_trintime_View.setVisibility(View.GONE);
                    error_note.setVisibility(View.VISIBLE);
                    error_note.setText(R.string.we_cannot_find);



                } else {
                    progressBar.setVisibility(View.GONE);
                    recent_trintime_View.setVisibility(View.GONE);
                    error_note.setVisibility(View.VISIBLE);
                    error_note.setText(R.string.we_cannot_find);


                }


            }

            @Override
            public void onFailure(Call<FTTimeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
                recent_trintime_View.setVisibility(View.GONE);
                error_note.setVisibility(View.VISIBLE);
                error_note.setText(R.string.we_cannot_find);




            }
        });
    }




}