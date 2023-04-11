package org.chennaimetrorail.appv1.travelcard;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.adapter.ViewPagerAdapter;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.Tips;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 102525 on 1/30/2018.
 */

public class CreateMpin extends Fragment implements View.OnClickListener {

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
      Button mpin_submit_btn;
    TextView forget_password_btn, create_account_btn;

    /*Storage for Login details*/
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Dialog dialog;
    String dialog_msgs;
    String version;
    PinEntryEditText new_pinEntry,confirm_pinEntry;
    ViewPagerAdapter viewPagerAdapter;
    String TAG = "CreateMpin";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.travelcard_create_mpin, container, false);

        Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setTitle("");
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

         new_pinEntry = (PinEntryEditText)view.findViewById(R.id.new_pin_entry);
         confirm_pinEntry = (PinEntryEditText)view.findViewById(R.id.confirm_pin_entry);
        mpin_submit_btn = (Button) view.findViewById(R.id.mpin_submit_btn);
        forget_password_btn = (TextView) view.findViewById(R.id.forget_password);
        create_account_btn = (TextView) view.findViewById(R.id.create_account);

        mpin_submit_btn.setOnClickListener(this);
        forget_password_btn.setOnClickListener(this);
        create_account_btn.setOnClickListener(this);
        int_chk = new Internet_connection_checking(getActivity());
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    Home home = new Home();
                    home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();

                    return true;
                }
                return false;
            }
        });


        /*Slider image view..*/
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) view.findViewById(R.id.sliderDots);


        Connection = int_chk.checkInternetConnection();
                /*Check Internet Connection Connect Or Not*/
        if (Connection == false) {

            if (isShown) {
                dialog.dismiss();
                dialog_msgs = "Oops!\nThere was a problem connecting to the internet. Please check your connection and Try again";
                dialogs();

            } else {
                dialog_msgs = "Oops!\nThere was a problem connecting to the internet. Please check your connection and Try again";
                dialogs();
            }
        } else {
                    /*Call Validate login parameters method..*/

            Tips();

        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.mpin_submit_btn:

                validateMpin();

                break;
            case R.id.create_account:

                fragmentTransaction.replace(R.id.fragment_place, new TRegister());
                fragmentTransaction.commit();

                break;
            case R.id.forget_password:
                // handle click from button 3
                break;
        }


    }

    //Error Messages
    public void dialogs() {

        dialog = new Dialog(getContext());
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


    /*Check mpin Details Hear...!*/
    private void validateMpin() {



            if (!new_pinEntry.getText().toString().equalsIgnoreCase("")&&!confirm_pinEntry.getText().toString().equalsIgnoreCase("")) {

                if (new_pinEntry.getText().toString().equals(confirm_pinEntry.getText().toString())) {
                    Toast.makeText(getActivity(), " m-Pin was Successfully Created", Toast.LENGTH_SHORT).show();

                    editor.putString("m_pin",confirm_pinEntry.getText().toString());
                    editor.apply();

                    fragmentTransaction.replace(R.id.fragment_place, new TravelCardList());
                    fragmentTransaction.commit();
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter the Correct m-Pin", Toast.LENGTH_SHORT).show();

                }
                    /*pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                        @Override
                        public void onPinEntered(CharSequence str) {
                            if (str.toString().equals("1234")) {
                                Toast.makeText(Register.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "FAIL", Toast.LENGTH_SHORT).show();
                                pinEntry.setText(null);
                            }
                        }
                    });*/
            }else {
                Toast.makeText(getActivity(), "Please Enter the m-Pin", Toast.LENGTH_SHORT).show();
            }


    }

    /*Password Validate Method*/
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }




    /*Attempt To LOGIN Process to Server........*/
    private void Tips() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*Declaring ApiInterFace Class heaf....*/
        final ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        /*Send Parameters to the Api*/
        Call<Tips> call = apiService.getT_Tips(Constant.strApiKey,version);
        /*Enqueue the Login Api......*/
        call.enqueue(new Callback<Tips>() {
            @Override
            public void onResponse(Call<Tips> call, Response<Tips> response) {
                int statusCode = response.code();
                /*Get Login Good Response...*/

                Log.d(TAG,call.request().url().toString());
                if (statusCode == 200) {
                    Log.d(TAG, "Get_tips");

                    Constant.tips_array = response.body().getTips();
                    viewPagerAdapter = new ViewPagerAdapter(getContext());
                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = Constant.tips_array.size();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getContext());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);

                        sliderDotspanel.addView(dots[i], params);

                    }


                    dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

                } else {

                }


            }

            @Override
            public void onFailure(Call<Tips> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
            }
        });
    }


}