package org.chennaimetrorail.appv1.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.GPSTracker;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.FlashNewsAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.FetchPushNotifiResponse;
import org.chennaimetrorail.appv1.modal.jsonmodal.NewsNotificationList;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 8/23/2017.
 */

public class Notifications extends Fragment {
    String TAG= "Notifications";
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    List<NewsNotificationList> newsNotificationLists;
    RecyclerView recyclerView;
    Dialog dialog;
    String dialog_msgs;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    TextView erronr_note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notifications, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Notifications");
        erronr_note = view.findViewById(R.id.erronr_note);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new OtherInfo());
                fragmentTransaction.commit();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.flah_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();

        // create class object
        //GPSTracker gps = new GPSTracker(getActivity());
        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {

            fetchPushNotification();
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_place, new OtherInfo());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });


        return view;


    }

    /*CurrentLocation Flash News */
    public void fetchPushNotification() {
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        Call<FetchPushNotifiResponse> call = apiService.fetchPushNotificationrs(Constant.os_type, Constant.strApiKey, info.versionName);
        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<FetchPushNotifiResponse>() {
            @Override
            public void onResponse(Call<FetchPushNotifiResponse> call, Response<FetchPushNotifiResponse> response) {
                int statusCode = response.code();
                Log.e(TAG, "statusCode" + statusCode);

                Log.d(TAG,call.request().url().toString());

                if (statusCode == 200) {
                    erronr_note.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    newsNotificationLists = response.body().getNotifications_list();
                    if(newsNotificationLists!=null) {
                        progressDoalog.dismiss();
                        recyclerView.setAdapter(new FlashNewsAdapter(newsNotificationLists, R.layout.custom_flash_news, getActivity()));
                    }else {
                        progressDoalog.dismiss();
                        erronr_note.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else if (statusCode == 400) {
                    progressDoalog.dismiss();
                    erronr_note.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                } else {
                    progressDoalog.dismiss();
                    erronr_note.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<FetchPushNotifiResponse> call, Throwable t) {
                // Log error here since request failed
                progressDoalog.dismiss();
                Log.e(TAG, t.toString());
                Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


    }



}