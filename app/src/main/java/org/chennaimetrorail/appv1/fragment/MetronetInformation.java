package org.chennaimetrorail.appv1.fragment;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.MetroNetWorkstatus;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 7/11/2017.
 */

public class MetronetInformation extends Fragment {

    String TAG = "MetronetInformation";
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    String version;
    ZoomageView myZoomageViewmetro;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.metro__network__information, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(), view);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);


        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Metro Network Information");

        myZoomageViewmetro = view.findViewById(R.id.myZoomageViewmetro);
        int_chk = new Internet_connection_checking(getActivity());
        Connection = int_chk.checkInternetConnection();

        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.this_internet, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            getMetroNetWorkListImage();
        }

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new OtherInfo());
                fragmentTransaction.commit();
            }
        });
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


    /*Attempt To GetStation Facility Process to Server........*/
    private void getMetroNetWorkListImage() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Constant.stationLatitudeandLongitude.clear();
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        final ProgressDialog loader = ProgressDialog.show(getActivity(), "", "Loading...", true);
        /*Send Parameters to the Api*/
        Call<MetroNetWorkstatus> call = apiService.GetMetroNetworkInfo(Constant.strApiKey, version);

        call.enqueue(new Callback<MetroNetWorkstatus>() {
            @Override
            public void onResponse(Call<MetroNetWorkstatus> call, Response<MetroNetWorkstatus> response) {
                int statusCode = response.code();

                Log.d(TAG,call.request().url().toString());

                if (statusCode == 200) {

                    if (response.body().getStatus().equals("success")) {

                        Glide.with(getContext())
                                .load(response.body().getMetroNetWorkList().get(0).getImageUrl())
                                .dontAnimate()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        loader.dismiss();
                                        return false;
                                    }
                                })

                                .into(myZoomageViewmetro);

                    }

                } else if (statusCode == 400) {
                    loader.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                } else {
                    loader.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<MetroNetWorkstatus> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "Error" + t.toString());
                loader.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });


    }


}
