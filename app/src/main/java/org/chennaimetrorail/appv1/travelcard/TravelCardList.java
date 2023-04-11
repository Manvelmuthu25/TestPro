package org.chennaimetrorail.appv1.travelcard;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.CardlistModal;
import org.chennaimetrorail.appv1.modal.jsonmodal.travalcardmodals.TravalCardlistModal;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;
import org.chennaimetrorail.appv1.travelcard.travelcardadapter.TravalcardListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TravelCardList extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    LinearLayout toolbar_menu;
    TextView menu_textview;
    List<CardlistModal> cardlistModals_array;
    RecyclerView card_list_recy;
    Button quick_rc_btn, add_t_card_btn;
    TextView showhide_btn, no_cardtxt;
    LinearLayout instructionLayout, layoutdisable;
    private Boolean exit = false;

    String version,menuitemstatuscode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();
        int_chk = new Internet_connection_checking(getActivity());

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.travelcard_list, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FontStyle fontStyle = new FontStyle();

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        fontStyle.Changeview(getActivity(), view);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView) getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        no_cardtxt = (TextView) view.findViewById(R.id.no_cardtxt);
        layoutdisable = (LinearLayout) view.findViewById(R.id.layoutdisable);
        card_list_recy = (RecyclerView) view.findViewById(R.id.card_list_recy);
        card_list_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
      /*  ViewGroup.LayoutParams params=card_list_recy.getLayoutParams();
        params.height=100;
        card_list_recy.setLayoutParams(params);*/
        add_t_card_btn = (Button) view.findViewById(R.id.add_t_card_btn);
        add_t_card_btn.setTypeface(fontStyle.helveticabold_CE);
        add_t_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardlistModals_array.size() < 5) {
                    fragmentTransaction.replace(R.id.fragment_place, new AddTravelCard());
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Maximum save 5 card only", Toast.LENGTH_LONG).show();
                }

            }
        });

        quick_rc_btn = (Button) view.findViewById(R.id.quick_rc_btn);
        quick_rc_btn.setTypeface(fontStyle.helveticabold_CE);
        quick_rc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QuickRechargeActivity.class);
                startActivity(i);
            }
        });

        showhide_btn = view.findViewById(R.id.showhide_btn);

        instructionLayout = view.findViewById(R.id.instructionLayout);

        showhide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showhide_btn.getText().toString().equals("Show Less")) {
                 /*   showhide_btn.setText("Show More");
                    ViewGroup.LayoutParams params=card_list_recy.getLayoutParams();
                    params.height=500;
                    card_list_recy.setLayoutParams(params);*/

                } else {
                    showhide_btn.setVisibility(View.GONE);
                    showhide_btn.setText("");
                    ViewGroup.LayoutParams params = card_list_recy.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    card_list_recy.setLayoutParams(params);
                }
            }
        });
        Connection = int_chk.checkInternetConnection();
        /*Check Internet Connection Connect Or Not*/
        if (!Connection) {

            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {
            /*Call Validate login parameters method..*/
            GetCardListData();
        }


        menuitemstatuscode=pref.getString("menuitemstatuscode", null);
        //Toast.makeText(getContext(),menuitemstatuscode,Toast.LENGTH_LONG).show();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    home.toolbar.setBackgroundResource(R.color.color_transprant);
                    home.toolbrtxt_layout.setVisibility(View.GONE);
                    home.toolbar_img.setVisibility(View.VISIBLE);
                    home.home_relative =(RelativeLayout)getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                    return true;
                }
                return false;
            }
        });


        menu_textview = (TextView) view.findViewById(R.id.menu_textview);
        menu_textview.setText("Select Card to Recharge");
        menu_textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        menu_textview.setTypeface(fontStyle.helveticabold_CE);
        toolbar_menu = (LinearLayout) view.findViewById(R.id.toolbar_menu);
        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        return view;

    }



   /* {
        "username": "9600676567",
            "password": "Test@123",
            "secretcode": "croW13Ak",
            "appv": "2.0.0"
    }
*/



    public void GetCardListData() {

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

    //  Call<TravalCardlistModal> call = apiService.getT_CardList(pref.getString("username", null),
        //  pref.getString("password", null),
        //  pref.getString("securitycode", null),version);

        JsonObject values = new JsonObject();
        values.addProperty("username",pref.getString("username", null));
        values.addProperty("password", pref.getString("password", null));
        values.addProperty("secretcode", pref.getString("securitycode", null));
        values.addProperty("appv","ANDROID|"+version);
        Log.d(TAG, "cartlist" + values);
        Call<TravalCardlistModal> call = apiService.getT_CardList(values);

        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<TravalCardlistModal>() {
            @Override
            public void onResponse(Call<TravalCardlistModal> call, Response<TravalCardlistModal> response) {
                int statusCode = response.code();

                Log.d(TAG,call.request().url().toString());

                if (statusCode == 200) {

                    Log.e("statusCode", "" + pref.getString("username", null) + pref.getString("password", null));
                    cardlistModals_array = response.body().getCardlistModal();

                    progressDoalog.dismiss();
                    Log.e("statusCode", "" + response.body().getStatus());
                    card_list_recy.setAdapter(new TravalcardListAdapter(cardlistModals_array, R.layout.travelcard_list_custome, getActivity(), TravelCardList.this));

                    if (cardlistModals_array.size() > 2) {
                        showhide_btn.setVisibility(View.VISIBLE);
                        showhide_btn.setText(R.string.showmore);
                    } else {
                        showhide_btn.setVisibility(View.GONE);
                        showhide_btn.setText("");
                    }

                    if (cardlistModals_array.size() == 0) {
                        card_list_recy.setVisibility(View.INVISIBLE);
                        layoutdisable.setVisibility(View.INVISIBLE);
                        no_cardtxt.setVisibility(View.VISIBLE);
                    } else {
                        card_list_recy.setVisibility(View.VISIBLE);
                        layoutdisable.setVisibility(View.VISIBLE);
                        no_cardtxt.setVisibility(View.GONE);
                    }


                } else if (statusCode == 400) {
                    progressDoalog.dismiss();
                    Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }else {
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TravalCardlistModal> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        FontStyle fontStyle = new FontStyle();
        ;
        fontStyle.Changeview(getActivity(), popupView);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        TextView t_sign_out = (TextView) popupView.findViewById(R.id.t_sign_out);
        TextView t_all_history = (TextView) popupView.findViewById(R.id.t_all_history);
        TextView t_change_password = (TextView) popupView.findViewById(R.id.change_passwordtxt);
        t_all_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewAllTransactionActivity.class);
                startActivity(i);
            }
        });
        t_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_place, new ChangePassword());
                fragmentTransaction.commit();
                popupWindow.dismiss();
            }
        });
        t_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragment_place, new TravelcardLogin());
                fragmentTransaction.commit();
                popupWindow.dismiss();
                editor.putString("securitycode", null);
                editor.putString("username", null);
                editor.putString("password", null);
                editor.putString("login_name", null);
                editor.putString("dob", null);
                editor.putString("gender", null);
                editor.putString("registeredmobile", null);
                editor.putString("email", null);
                editor.putString("m_pinLoginState", "no");
                editor.putString("menuitemstatuscode", "1");
                editor.apply();
            }
        });
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }


}
