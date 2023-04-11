package org.chennaimetrorail.appv1.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.DirectionFloatingWidgetService;
import org.chennaimetrorail.appv1.DirectionsJSONParser;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.StepsAdapter;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 102525 on 8/29/2017.
 */

public class NearestMetroRouteDirection extends Fragment implements OnMapReadyCallback {

    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1232;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    RecyclerView root_recyview;
    LinearLayout image_view_back, dirc_discr;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    ArrayList<LatLng> markerPoints;
    SharedPreferences nearst_pref;
    TextView distence, rout_start_txt, rout_end_txt, show_hide_btn1, show_hide_btn;
    ImageView redirect_address;
    LinearLayout car_l, train_l, walk_l;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nearestroortdirection, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        nearst_pref = getActivity().getSharedPreferences("station_lat_lon", 0);
        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        car_l = (LinearLayout) view.findViewById(R.id.car_btn);
        train_l = (LinearLayout) view.findViewById(R.id.train_btn);
        walk_l = (LinearLayout) view.findViewById(R.id.walk_btn);
        redirect_address = (ImageView) view.findViewById(R.id.redirect_addres);
        root_recyview = (RecyclerView) view.findViewById(R.id.root_recyview);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        show_hide_btn1 = (TextView) view.findViewById(R.id.show_hide_btn1);
        show_hide_btn = (TextView) view.findViewById(R.id.show_hide_btn);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Direction to " + nearst_pref.getString("direction_name", null));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        root_recyview.setLayoutManager(layoutManager);

        dirc_discr = (LinearLayout) view.findViewById(R.id.dirc_discr);

        distence = (TextView) view.findViewById(R.id.distance_txt);
        rout_start_txt = (TextView) view.findViewById(R.id.rout_start_txt);
        rout_end_txt = (TextView) view.findViewById(R.id.rout_end_txt);

        rout_start_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dirc_discr.getVisibility() == View.GONE) {
                    show_hide_btn.setText("Hide Steps");
                    dirc_discr.setVisibility(View.VISIBLE);
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up, 0, 0, 0);
                } else {

                    dirc_discr.setVisibility(View.GONE);
                    show_hide_btn.setText("Show Steps");
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down, 0, 0, 0);
                }
            }
        });
        distence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dirc_discr.getVisibility() == View.GONE) {
                    show_hide_btn.setText("Hide Steps");
                    dirc_discr.setVisibility(View.VISIBLE);
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up, 0, 0, 0);
                } else {

                    dirc_discr.setVisibility(View.GONE);
                    show_hide_btn.setText("Show Steps");
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down, 0, 0, 0);
                }
            }
        });
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.fragment_place, new NearestMetro());
                fragmentTransaction.commit();
            }
        });
        show_hide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (dirc_discr.getVisibility() == View.GONE) {
                    show_hide_btn.setText("Hide Steps");
                    dirc_discr.setVisibility(View.VISIBLE);
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up, 0, 0, 0);
                } else {

                    dirc_discr.setVisibility(View.GONE);
                    show_hide_btn.setText("Show Steps");
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down, 0, 0, 0);
                }

            }
        });
        show_hide_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (dirc_discr.getVisibility() == View.GONE) {
                    show_hide_btn.setText("Hide Steps");
                    dirc_discr.setVisibility(View.VISIBLE);
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up, 0, 0, 0);
                } else {

                    dirc_discr.setVisibility(View.GONE);
                    show_hide_btn.setText("Show Steps");
                    //show_hide_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down, 0, 0, 0);
                }

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new NearestMetro());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
// Initializing
        markerPoints = new ArrayList<LatLng>();
        int_chk = new Internet_connection_checking(getActivity());

        Connection = int_chk.checkInternetConnection();

        if (!Connection) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {


                car_l.setBackgroundResource(R.drawable.bottomblue_line);
                train_l.setBackgroundResource(0);
                walk_l.setBackgroundResource(0);
                // nearestMetroDetails(latitude,longitude);
                mMap.clear();
                Constant.steps_list.clear();
                drawStartStopMarkers();

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl("driving", nearst_pref.getString("cureent_lat",null), nearst_pref.getString("cureent_lon",null), nearst_pref.getString("destination_lat", null), nearst_pref.getString("destination_lon", null));

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

                car_l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int_chk = new Internet_connection_checking(getActivity());

                        Connection = int_chk.checkInternetConnection();

                        if (!Connection) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        } else {
                            car_l.setBackgroundResource(R.drawable.bottomblue_line);
                            train_l.setBackgroundResource(0);
                            walk_l.setBackgroundResource(0);
                            // nearestMetroDetails(latitude,longitude);
                            mMap.clear();
                            Constant.steps_list.clear();
                            drawStartStopMarkers();

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl("driving", nearst_pref.getString("cureent_lat",null), nearst_pref.getString("cureent_lon",null), nearst_pref.getString("destination_lat", null), nearst_pref.getString("destination_lon", null));

                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);
                        }
                    }
                });
                train_l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int_chk = new Internet_connection_checking(getActivity());

                        Connection = int_chk.checkInternetConnection();

                        if (!Connection) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        } else {
                            car_l.setBackgroundResource(0);
                            train_l.setBackgroundResource(R.drawable.bottomblue_line);
                            walk_l.setBackgroundResource(0);
                            // nearestMetroDetails(latitude,longitude);
                            mMap.clear();
                            Constant.steps_list.clear();
                            drawStartStopMarkers();

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl("transit", nearst_pref.getString("cureent_lat",null), nearst_pref.getString("cureent_lon",null), nearst_pref.getString("destination_lat", null), nearst_pref.getString("destination_lon", null));

                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);
                        }
                    }
                });
                walk_l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int_chk = new Internet_connection_checking(getActivity());

                        Connection = int_chk.checkInternetConnection();

                        if (!Connection) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        } else {
                            car_l.setBackgroundResource(0);
                            train_l.setBackgroundResource(0);
                            walk_l.setBackgroundResource(R.drawable.bottomblue_line);
                            // nearestMetroDetails(latitude,longitude);

                            mMap.clear();
                            Constant.steps_list.clear();
                            drawStartStopMarkers();

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl("walking", nearst_pref.getString("cureent_lat",null), nearst_pref.getString("cureent_lon",null), nearst_pref.getString("destination_lat", null), nearst_pref.getString("destination_lon", null));

                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);
                        }
                    }
                });



        }

        redirect_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + Double.parseDouble(nearst_pref.getString("cureent_lat",null)) + "," + Double.parseDouble(nearst_pref.getString("cureent_lon",null)) + "&daddr=" + nearst_pref.getString("destination_lat", null) + "," + nearst_pref.getString("destination_lon", null) + ""));
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intentt = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intentt, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                } else
                    //If permission is granted start floating widget service
                    startFloatingWidgetService();
            }
        });

    }


    // Drawing Start and Stop locations
    private void drawStartStopMarkers() {


        LatLng coordinate = new LatLng(Double.parseDouble(nearst_pref.getString("cureent_lat",null)), Double.parseDouble(nearst_pref.getString("cureent_lon",null)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(nearst_pref.getString("cureent_lat",null)), Double.parseDouble(nearst_pref.getString("cureent_lon",null)))).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(nearst_pref.getString("cureent_lat",null)), Double.parseDouble(nearst_pref.getString("cureent_lon",null))), 13));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(nearst_pref.getString("destination_lat", null)), Double.valueOf(nearst_pref.getString("destination_lon", null)))).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red)));


    }


    private String getDirectionsUrl(String mode_type, String origin_lat, String origin_lon, String desti_lat, String desti_long) {


        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String url = ApiClient.BASE_URL+"api/NearbyPlaces/GetDirections?origin="+nearst_pref.getString("cureent_lat",null) + "," + nearst_pref.getString("cureent_lon",null)+"&destination=" + desti_lat + "," + desti_long+"&mode=" + mode_type + "&secretcode=$3cr3t&appv="+info.versionName+"";


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        Log.d("check_googlemap_url", "" + strUrl);

        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while", "" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        Log.d("Google_mapdatasss", "" + data);
        return data;
    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        getActivity().startService(new Intent(getActivity(), DirectionFloatingWidgetService.class));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            progressDoalog.setCanceledOnTouchOutside(false);
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            progressDoalog.dismiss();

        }

    }

    /**
     * A class to parse the Google Places in JSON format
     */
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            ArrayList<String> steps = new ArrayList<String>();
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            String start_addres = "", end_address = "";
            JSONArray stepsArray = null;


            if (result.size() < 1) {
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                steps = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                Log.d("paths", "" + path);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");

                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    } else if (j == 2) {

                        rout_start_txt.setText((String) point.get("start_address"));

                    } else if (j == 3) {
                        rout_end_txt.setText((String) point.get("end_address"));
                    } else {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));


                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }


                }
                distence.setText(distance + " About " + duration);

                root_recyview.setAdapter(new StepsAdapter(Constant.steps_list, getActivity()));


                //rout_start_txt.setText(points.get(0));

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(R.color.colorPrimaryDark);


            }

            // tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


}