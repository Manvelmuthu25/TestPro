package org.chennaimetrorail.appv1.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.TrainTimingAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.TimeResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.Times;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/13/2017.
 */

public class TrainTiming extends Fragment {

    String TAG = "TrainTiming";
    Dialog dialog;
    String dialog_msgs;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    SharedPreferences pref;
    TextView station_title, error_notetime;
    RecyclerView recyclerView;

    EditText date_edttrintime, time_edttrintime;
    Calendar myCalendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    String mytimeformat = "HH:mm";
    String serverDate_frmt = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    SimpleDateFormat sdft = new SimpleDateFormat(mytimeformat, Locale.US);
    SimpleDateFormat serverdft = new SimpleDateFormat(serverDate_frmt, Locale.US);

    Handler mHandler;
    boolean mStopHandler = false;
    Runnable runnable;
    Date currentTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment opensans
        final View view = inflater.inflate(R.layout.train_timing, container, false);
        myCalendar = Calendar.getInstance();
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);

        pref = getActivity().getSharedPreferences("stationdetails", 0);

        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        station_title = (TextView) view.findViewById(R.id.station_title);
        station_title.setText(pref.getString("station_longname", null));
        station_title.setTypeface(fontStyle.helveticabold_CE);
        error_notetime = (TextView) view.findViewById(R.id.error_notetime);
        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Train Timings");

        date_edttrintime = (EditText) view.findViewById(R.id.date_edttrintime);
        time_edttrintime = (EditText) view.findViewById(R.id.time_edttrintime);

        final int mYear = myCalendar.get(Calendar.YEAR);
        final int mMonth = myCalendar.get(Calendar.MONTH);
        final int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        final int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        final int minute = myCalendar.get(Calendar.MINUTE);
        currentTime = Calendar.getInstance().getTime();
        time_edttrintime.setText(sdft.format(currentTime));
        date_edttrintime.setText(sdf.format(new Date()));

        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();





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
        date_edttrintime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), date, mYear, mMonth, mDay);
                dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpDialog.show();
            }
        });

        time_edttrintime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String am_pm ;

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        am_pm = getTime(selectedHour,selectedMinute);

                        stringBuilder.append(" ");
                        stringBuilder.append(am_pm);
                        time_edttrintime.setText(stringBuilder);
                        if (!Connection) {

                            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        } else {
                            getUpcomingTrinDetails();
                        }
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.timing_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);


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

    @Override
    public void onResume() {
        super.onResume();
        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            backgroundRun();
        } else {

            backgroundRun();


        }
    }

    public void backgroundRun(){
        getUpcomingTrinDetails();
        mHandler = new Handler();
        runnable = new Runnable() {
            public void run() {
                // do your stuff - don't create a new runnable here!
                currentTime = Calendar.getInstance().getTime();
                time_edttrintime.setText(sdft.format(currentTime));
                getUpcomingTrinDetails();
                mHandler.postDelayed(this, 10000);
            }
        };
// start it with:
        mHandler.postDelayed(runnable, 10000);
    }
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);
        mHandler.removeMessages(0);
        mHandler.removeCallbacks(runnable);
    }

    public String getTime(int hr, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hr);
        cal.set(Calendar.MINUTE,min);
        Format formatter;
        formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(cal.getTime());
    }
    public void getUpcomingTrinDetails() {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        String startDateString = date_edttrintime.getText().toString();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
        String finalString = newFormat.format(date);
        Log.d(TAG, "convertdate" + finalString);


        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Call<TimeResponse> call = apiService.getTrainDetails(
                pref.getString("station_id", null),
                Constant.strApiKey,
                info.versionName,
                finalString,
                time_edttrintime.getText().toString());

        call.enqueue(new Callback<TimeResponse>() {
            @Override
            public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                Log.d(TAG,call.request().url().toString());
                int statusCode = response.code();
                Log.d(TAG, "status" + statusCode);
                Log.e("Urltie -> ", call.request().url().toString());
                if (statusCode == 200) {
                    Log.d(TAG, "statuslist" + response.body().getTimetable());
                    List<Times> times = response.body().getTimetable();
                    if (response.body().getReason() != null) {
                        recyclerView.setVisibility(View.GONE);
                        error_notetime.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        error_notetime.setVisibility(View.GONE);
                        recyclerView.setAdapter(new TrainTimingAdapter(times, R.layout.trin_timing_custome, getActivity()));
                    }


                } else if(statusCode==400) {
                    recyclerView.setVisibility(View.GONE);
                    error_notetime.setVisibility(View.VISIBLE);
                   // Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }else {

                    recyclerView.setVisibility(View.GONE);
                    error_notetime.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TimeResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                recyclerView.setVisibility(View.GONE);
                error_notetime.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_edttrintime.setText(sdf.format(myCalendar.getTime()));
        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            getUpcomingTrinDetails();
        }
    }




}