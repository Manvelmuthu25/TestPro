package org.chennaimetrorail.appv1.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.GPSTracker;
import org.chennaimetrorail.appv1.GetWeatherDetails;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.RemoteFetch;
import org.chennaimetrorail.appv1.adapter.Weatheradapter;
import org.chennaimetrorail.appv1.modal.Weathers;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 102525 on 7/13/2017.
 */

public class WeatherReport extends Fragment {

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Handler handler;
    TextView date_txt,weather_discri,temp_txt, humidity_txt, wind_txt, date_1, date_2, date_3, date_4, date_1temp, date_2temp, date_3temp, date_4temp;
    ImageView date_1icon, date_2icon, date_3icon, date_4icon,weather_detailsic;
    SimpleDateFormat sdf;
    String dateString;
    RecyclerView today_weather_recy;
    FontStyle fontStyle = new FontStyle();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.weather_report, container, false);

        fontStyle.Changeview(getActivity(),view);

           /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new TourGuid());
                fragmentTransaction.commit();
            }
        });
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Local Weather Forecasts");
        handler = new Handler();
        //date_txt = (TextView)view.findViewById(R.id.date_txt);
        temp_txt = (TextView) view.findViewById(R.id.temp_txt);
        humidity_txt = (TextView) view.findViewById(R.id.humidity_txt);
        wind_txt = (TextView) view.findViewById(R.id.wind_txt);
        weather_discri = (TextView)view.findViewById(R.id.weather_discri);

        today_weather_recy = (RecyclerView) view.findViewById(R.id.today_weather_recy);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        today_weather_recy.setLayoutManager(llm);
        //today_weather_recy.setAdapter( adapter );
        date_1 = (TextView) view.findViewById(R.id.date_1);
        date_2 = (TextView) view.findViewById(R.id.date_2);
        date_3 = (TextView) view.findViewById(R.id.date_3);
        date_4 = (TextView) view.findViewById(R.id.date_4);

        date_1temp = (TextView) view.findViewById(R.id.date_1temp);
   /*     date_1temp.setTypeface(null,Typeface.BOLD);*/
        date_2temp = (TextView) view.findViewById(R.id.date_2temp);
      /*  date_2temp.setTypeface(null,Typeface.BOLD);*/
        date_3temp = (TextView) view.findViewById(R.id.date_3temp);
     /*   date_3temp.setTypeface(null,Typeface.BOLD);*/
        date_4temp = (TextView) view.findViewById(R.id.date_4temp);
   /*     date_4temp.setTypeface(null,Typeface.BOLD);*/

        date_1icon = (ImageView) view.findViewById(R.id.date_1icon);
        date_2icon = (ImageView) view.findViewById(R.id.date_2icon);
        date_3icon = (ImageView) view.findViewById(R.id.date_3icon);
        date_4icon = (ImageView) view.findViewById(R.id.date_4icon);

        weather_detailsic = (ImageView)view.findViewById(R.id.weather_detailsic);
        long date = System.currentTimeMillis();

        sdf = new SimpleDateFormat("MMM dd yyyy, EEEE");
        dateString = sdf.format(date);
        //date_txt.setText("Chennai\n"+dateString+"\n");
        int_chk = new Internet_connection_checking(getActivity());


        Connection = int_chk.checkInternetConnection();

        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            updateWeatherData();
            updatefivedaysWeatherData();
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new TourGuid());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });


        return view;

    }

    /*Update Weather api Call functions*/
    private void updateWeatherData() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON();
                // Set up progress before call


                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            progressDoalog.dismiss();
                            Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            progressDoalog.dismiss();
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    /*Update Weather api Call functions*/
    private void updatefivedaysWeatherData() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        new Thread() {
            public void run() {
                final JSONObject json = GetWeatherDetails.getJSON();
                // Set up progress before call


                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            progressDoalog.dismiss();
                            Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            progressDoalog.dismiss();
                            renderfivedaysWeather(json);
                            long t_date = System.currentTimeMillis();
                            sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String currentdate_str = sdf.format(t_date);

                            Constant.Currentweatherlist.clear();


                            String date1, date2, date3, date4, date5;

                            Calendar c1 = Calendar.getInstance();
                            try {
                                c1.setTime(sdf.parse(currentdate_str));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c1.add(Calendar.DATE, 1);  // number of days to add
                            date1 = sdf.format(c1.getTime());
                            Log.d("lkdjfdate1", "" + date1);

                            Calendar c2 = Calendar.getInstance();
                            try {
                                c2.setTime(sdf.parse(currentdate_str));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c2.add(Calendar.DATE, 2);  // number of days to add
                            date2 = sdf.format(c2.getTime());
                            Log.d("lkdjfdate1", "" + date2);

                            Calendar c3 = Calendar.getInstance();
                            try {
                                c3.setTime(sdf.parse(currentdate_str));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c3.add(Calendar.DATE, 3);  // number of days to add
                            date3 = sdf.format(c3.getTime());
                            Log.d("lkdjfdate1", "" + date3);


                            Calendar c4 = Calendar.getInstance();
                            try {
                                c4.setTime(sdf.parse(currentdate_str));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c4.add(Calendar.DATE, 4);  // number of days to add
                            date4 = sdf.format(c4.getTime());
                            Log.d("lkdjfdate1", "" + date4);


                            Calendar c5 = Calendar.getInstance();
                            try {
                                c5.setTime(sdf.parse(currentdate_str));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c5.add(Calendar.DATE, 4);  // number of days to add
                            date5 = sdf.format(c5.getTime());
                            DecimalFormat df = new DecimalFormat("#.#");


                            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            DateFormat outputFormat = new SimpleDateFormat("EEEE");

                            for (int k = 0; k < Constant.weathers_list.size(); k++) {


                                if (Constant.weathers_list.get(k).getDt_txt().equalsIgnoreCase(currentdate_str)) {

                                    Constant.Currentweatherlist.add(Constant.weathers_list.get(k));

                                }

                                Log.d("Datevalues",""+Constant.weathers_list.get(k).getTime_txt());
                                if (date1.equalsIgnoreCase(Constant.weathers_list.get(k).getDt_txt())&&Constant.weathers_list.get(k).getTime_txt().equals("00:00 PM")) {

                                    Date date = null;
                                    try {
                                        date = inputFormat.parse(String.valueOf(Constant.weathers_list.get(k).getDt_txt()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String outputText = outputFormat.format(date);
                                    date_1.setText(outputText);


                                    date_1temp.setText(String.valueOf(df.format(Double.parseDouble(Constant.weathers_list.get(k).getTemp()))) + " ℃");
                                    String uri = "@drawable/w_" + String.valueOf(Constant.weathers_list.get(k).getIcon()) + "";
                                    int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());

                                    Drawable res = getActivity().getResources().getDrawable(imageResource);
                                    date_1icon.setImageDrawable(res);
                                }
                                if (date2.equalsIgnoreCase(Constant.weathers_list.get(k).getDt_txt())&&Constant.weathers_list.get(k).getTime_txt().equals("00:00 PM")) {
                                    Date date = null;
                                    try {
                                        date = inputFormat.parse(String.valueOf(Constant.weathers_list.get(k).getDt_txt()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String outputText = outputFormat.format(date);
                                    date_2.setText(outputText);
                                    date_2temp.setText(String.valueOf(df.format(Double.parseDouble(Constant.weathers_list.get(k).getTemp()))) + " ℃");
                                    String uri = "@drawable/w_" + String.valueOf(Constant.weathers_list.get(k).getIcon()) + "";
                                    int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());

                                    Drawable res = getActivity().getResources().getDrawable(imageResource);
                                    date_2icon.setImageDrawable(res);
                                }
                                if (date3.equalsIgnoreCase(Constant.weathers_list.get(k).getDt_txt())&&Constant.weathers_list.get(k).getTime_txt().equals("00:00 PM")) {
                                    Date date = null;
                                    try {
                                        date = inputFormat.parse(String.valueOf(Constant.weathers_list.get(k).getDt_txt()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String outputText = outputFormat.format(date);
                                    date_3.setText(outputText);
                                    date_3temp.setText(String.valueOf(df.format(Double.parseDouble(Constant.weathers_list.get(k).getTemp()))) + " ℃");
                                    String uri = "@drawable/w_" + String.valueOf(Constant.weathers_list.get(k).getIcon()) + "";
                                    int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());
                                    Drawable res = getActivity().getResources().getDrawable(imageResource);
                                    date_3icon.setImageDrawable(res);
                                }
                                if (date4.equalsIgnoreCase(Constant.weathers_list.get(k).getDt_txt())&&Constant.weathers_list.get(k).getTime_txt().equals("00:00 PM")) {
                                    Date date = null;
                                    try {
                                        date = inputFormat.parse(String.valueOf(Constant.weathers_list.get(k).getDt_txt()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String outputText = outputFormat.format(date);
                                    date_4.setText(outputText);
                                    date_4temp.setText(String.valueOf(df.format(Double.parseDouble(Constant.weathers_list.get(k).getTemp()))) + " ℃");
                                    String uri = "@drawable/w_" + String.valueOf(Constant.weathers_list.get(k).getIcon()) + "";
                                    int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());

                                    Drawable res = getActivity().getResources().getDrawable(imageResource);
                                    date_4icon.setImageDrawable(res);


                                }


                            }
                            today_weather_recy.setAdapter(new Weatheradapter(Constant.Currentweatherlist, R.layout.custome_weatherforet, getActivity()));

                        }
                    });
                }
            }
        }.start();
    }

    /*Weather Details set*/
    private void renderWeather(JSONObject json) {
        try {

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            //date_txt .setText("Chennai\n"+dateString+"\n"+details.getString("description").substring(0,1).toUpperCase() + details.getString("description").substring(1));


            DecimalFormat df = new DecimalFormat("###.#");
            String temp = String.valueOf(df.format(main.getDouble("temp")));
            temp_txt.setText(temp + " ℃");
            temp_txt.setTypeface(fontStyle.helveticabold_CE);
            humidity_txt.setText(main.getDouble("humidity") + "%");
            humidity_txt.setTypeface(fontStyle.helveticabold_CE);
            wind_txt.setText(String.valueOf(wind.getDouble("speed") + "Km/h"));
            wind_txt.setTypeface(fontStyle.helveticabold_CE);


            weather_discri.setText(details.getString("description").substring(0, 1).toUpperCase() + details.getString("description").substring(1));

            String uri = "@drawable/w_" + details.getString("icon") + "";
            int imageResource = getActivity().getResources().getIdentifier(uri, null, getActivity().getPackageName());

            Drawable res = getActivity().getResources().getDrawable(imageResource);
            weather_detailsic.setImageDrawable(res);


        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    /*Weather Details set*/
    private void renderfivedaysWeather(JSONObject json) {
        try {


            JSONObject details;

            Constant.weathers_list.clear();


            for (int i = 0; i < json.getString("list").length(); i++) {
                Weathers weathers = new Weathers();
                weathers.setTemp(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp"));
                weathers.setIcon(json.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                weathers.setHumidity(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("humidity"));
                weathers.setDescription(json.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                weathers.setSpeed(json.getJSONArray("list").getJSONObject(i).getJSONObject("wind").getString("speed"));


                String input = json.getJSONArray("list").getJSONObject(i).getString("dt_txt");
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat outputtime = new SimpleDateFormat("KK:mm a");
                DateFormat outputdate = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    weathers.setDt_txt(outputdate.format(inputFormat.parse(input)));
                    weathers.setTime_txt(outputtime.format(inputFormat.parse(input)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // weathers.setDt_txt(json.getJSONArray("list").getJSONObject(i).getString("dt_txt"));
                Constant.weathers_list.add(weathers);

            }


        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }


}