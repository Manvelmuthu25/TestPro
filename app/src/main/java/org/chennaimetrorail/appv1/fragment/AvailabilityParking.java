package org.chennaimetrorail.appv1.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.RecyclerItemClickListener;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.adapter.ParkingSpinnerAdapter;
import org.chennaimetrorail.appv1.adapter.ParkingpriceAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.ParkingResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.Parkingtariff;
import org.chennaimetrorail.appv1.modal.jsonmodal.Vechilelist;
import org.chennaimetrorail.appv1.modal.jsonmodal.VehicleType;
import org.chennaimetrorail.appv1.modal.jsonmodal.parkingZone;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/14/2017.
 */

public class AvailabilityParking extends Fragment {

    String TAG = "AvailabilityParking";
    TextView station_title, spintxt, available_name, avilabile_count,station_in_btw_current_status;
    ImageView spinimg;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    List<Vechilelist> vechilelists;
    RecyclerView parkingtarrif_recyle,parkingtarrif_recyle1;
    List<VehicleType> vehicleTypes;
    Dialog dialog;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    LinearLayout custome_spinnerbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.availability_of__parking, container, false);
        /*Get Shared preferences Station Details*/
        pref = getActivity().getSharedPreferences("stationdetails", 0);
        //set Fragment Manager
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        //set Font style
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);

        //Check internet connection
        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();
        //initialize home activity
        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);

        //initialize all layout id's
        station_title = (TextView) view.findViewById(R.id.platform_title);
        parkingtarrif_recyle = (RecyclerView) view.findViewById(R.id.parkingtarrif_recyle);
        parkingtarrif_recyle1 = (RecyclerView) view.findViewById(R.id.parkingtarrif_recyle1);
        spintxt = (TextView) view.findViewById(R.id.spintxt);
        spinimg = (ImageView) view.findViewById(R.id.spinimg);
        custome_spinnerbtn = (LinearLayout) view.findViewById(R.id.custome_spinnerbtn);
       // available_name = view.findViewById(R.id.available_name);
       // avilabile_count = view.findViewById(R.id.avilabile_count);
        TextView parkingtime = view.findViewById(R.id.parkingtime);
        //station_in_btw_current_status=view.findViewById(R.id.station_in_btw_current_status);
        TextView price_txt = view.findViewById(R.id.price_txt);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        parkingtime.setTypeface(fontStyle.helveticabold_CE);
        price_txt.setTypeface(fontStyle.helveticabold_CE);
        station_title.setTypeface(fontStyle.helveticabold_CE);
        /*Title Text*/
        title_txt.setText("Availability of Parking");

        station_title.setText(pref.getString("station_longname", null));

        //initialize layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        parkingtarrif_recyle.setLayoutManager(layoutManager);
        parkingtarrif_recyle.setNestedScrollingEnabled(false);

        //initialize layoutManager
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        parkingtarrif_recyle1.setLayoutManager(layoutManager1);
        parkingtarrif_recyle1.setNestedScrollingEnabled(false);

        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            getParkingDetails();
        }

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

    /*
     * Get Parking Type List
     * */
    public void getParkingDetails() {
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final ApiInterface apiInterface = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);

        Call<ParkingResponse> call = apiInterface.getParkingTarrif(pref.getString("station_id", null), Constant.strApiKey, "ANDROID|"+info.versionName);
        call.enqueue(new Callback<ParkingResponse>() {
            @Override
            public void onResponse(Call<ParkingResponse> call, Response<ParkingResponse> response) {
                int statusCode = response.code();
                Log.d(TAG,call.request().url().toString());

                Log.d("AvailabilityParking",""+call.request().url().toString());

                if (statusCode == 200) {

                    vehicleTypes = response.body().getVehicletype();
                    spintxt.setText(vehicleTypes.get(0).getVehiclename());
                    Picasso.with(getContext()).load(vehicleTypes.get(0).getImageurl()).into(spinimg);
                    vechilelists = response.body().getVechilelist();


                    /*
                    if (vechilelists.get(0).getAvailableSlot().equalsIgnoreCase("0")) {
                        avilabile_count.setText("Nil");
                    } else {
                        avilabile_count.setText(vechilelists.get(0).getAvailableSlot());
                    }

                    if (vechilelists.get(0).getAvailableSlotStatus().equalsIgnoreCase("0")) {
                        station_in_btw_current_status.setText("Not Available");
                    }else  if (vechilelists.get(0).getAvailableSlot().equalsIgnoreCase("0")) {
                        station_in_btw_current_status.setText("Not Available");
                    }
                    else {
                        station_in_btw_current_status.setText(vechilelists.get(0).getAvailableSlotStatus());
                        station_in_btw_current_status.setTextColor(Color.parseColor(vechilelists.get(0).getColourCode()));
                    }

*/


                  //  parkingtarrif_recyle1.setAdapter(new parkingZoneAdapter(vechilelists.get(0).getParkingZone(), getContext()));

                    progressDoalog.dismiss();
                    parkingtarrif_recyle.setAdapter(new ParkingpriceAdapter(vechilelists.get(0).getParkingtariff(), getContext()));

                } else if (statusCode == 400) {
                    progressDoalog.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                } else {
                    progressDoalog.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ParkingResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


        custome_spinnerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(vehicleTypes!=null){
                    vehicledialogs(pref.getString("station_longname", null), vehicleTypes);
                }

            }
        });
    }

    /*
     * LoadParking Price Type List
     * */
    public void loadpricedetails(String vechletype, int position) {

        for (int i = 0; i < vechilelists.size(); i++) {
            if (vechletype.equalsIgnoreCase(vechilelists.get(i).getVehiclename())) {
                spintxt.setText(vehicleTypes.get(position).getVehiclename());
                Picasso.with(getContext()).load(vehicleTypes.get(position).getImageurl()).into(spinimg);
                List<Parkingtariff> parkingtariff = vechilelists.get(i).getParkingtariff();
                parkingtarrif_recyle.setAdapter(new ParkingpriceAdapter(parkingtariff, getActivity()));




                /*
                if (vechilelists.get(i).getAvailableSlot().equalsIgnoreCase("0")) {
                    avilabile_count.setText("Nil");
                } else {
                    avilabile_count.setText(vechilelists.get(i).getAvailableSlot());
                }

                if (vechilelists.get(i).getAvailableSlotStatus().equalsIgnoreCase("0")) {
                    station_in_btw_current_status.setText("Not Available");
                }else  if (vechilelists.get(i).getAvailableSlot().equalsIgnoreCase("0")) {
                    station_in_btw_current_status.setText("Not Available");
                }
                    else {
                    station_in_btw_current_status.setText(vechilelists.get(i).getAvailableSlotStatus());
                    station_in_btw_current_status.setTextColor(Color.parseColor(vechilelists.get(i).getColourCode()));
                }
*/
            }
        }
    }

    /*
     * VehicleDialogBox Type List
     * */
    public void vehicledialogs(String stationname, List<VehicleType> list) {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.parkingspinner_layout);
        dialog.setCancelable(false);


        TextView selected_stationtxt = (TextView) dialog.findViewById(R.id.selected_stationtxt);
        RecyclerView vehiclelist_view = (RecyclerView) dialog.findViewById(R.id.vehiclelist_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        vehiclelist_view.setLayoutManager(layoutManager);
        vehiclelist_view.setNestedScrollingEnabled(false);
        selected_stationtxt.setText("Select Vehicle -" + stationname);
        vehiclelist_view.setAdapter(new ParkingSpinnerAdapter(list, getActivity(), spintxt.getText().toString()));
        vehiclelist_view.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), vehiclelist_view, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        loadpricedetails(vehicleTypes.get(position).getVehiclename(), position);

                        dialog.dismiss();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        dialog.show();
    }


}