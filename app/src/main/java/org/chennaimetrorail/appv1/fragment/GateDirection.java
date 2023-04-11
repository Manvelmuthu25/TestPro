package org.chennaimetrorail.appv1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by 102525 on 7/13/2017.
 */

public class GateDirection extends Fragment {
    LinearLayout image_view_back,gates;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    Animation animFadein;
    SharedPreferences pref;
    TextView station_title,gate_alrt;
    ImageView train1, train2;
    LinearLayout gate_a_linear, gate_b_linear, gate_c_linear;
    TextView gate_a_txt, gate_b_txt, gate_c_txt, gate_a,gate_b,gate_c;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.gates_and__directions, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);

        Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
           /*Back button action*/
        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);

        pref = getActivity().getSharedPreferences("stationdetails", 0);
        station_title = (TextView) view.findViewById(R.id.platform_title);
        station_title.setText(pref.getString("station_longname", null));
        station_title.setTypeface(fontStyle.helveticabold_CE);
        gate_a_linear = (LinearLayout)view.findViewById(R.id.gate_a_linear);
        gate_b_linear = (LinearLayout)view.findViewById(R.id.gate_b_linear);
        gate_c_linear = (LinearLayout)view.findViewById(R.id.gate_c_linear);
        gates = (LinearLayout)view.findViewById(R.id.gates);
        gate_alrt =(TextView)view.findViewById(R.id.gates_alrt);
        gate_a_txt =(TextView)view.findViewById(R.id.gate_a_txt);
        gate_b_txt =(TextView)view.findViewById(R.id.gate_b_txt);
        gate_c_txt =(TextView)view.findViewById(R.id.gate_c_txt);
        gate_a =(TextView)view.findViewById(R.id.gate_a);
        gate_b =(TextView)view.findViewById(R.id.gate_b);
        gate_c =(TextView)view.findViewById(R.id.gate_c);

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new StationInformation());
                fragmentTransaction.commit();
            }
        });


        /*Title Text*/
        TextView title_txt = (TextView)view.findViewById(R.id.title_txt);
        title_txt.setText("Gates and Directions");

        gates = (LinearLayout)view.findViewById(R.id.gates);
        gate_alrt =(TextView)view.findViewById(R.id.gates_alrt);

    /*Train Animation*/
        train1 = (ImageView) view.findViewById(R.id.train);
        train2 = (ImageView) view.findViewById(R.id.train_1);
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.slid_up);
        train1.startAnimation(animFadein);
        animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        train2.startAnimation(animFadein);

        Log.d("jai",""+"j"+pref.getString("station_Gates_directions", null)+"k");


        if(pref.getString("station_Gates_directions", null).equalsIgnoreCase("null")){

            gates.setVisibility(View.GONE);
            gate_alrt.setVisibility(View.VISIBLE);

        }else {

            try {

                JSONArray jsonArray = new JSONArray(pref.getString("station_Gates_directions", null));
                if (jsonArray.length()== 1) {

                    if(jsonArray.getJSONObject(0).getString("gate").equalsIgnoreCase("GATE A")){
                        gate_a_linear.setVisibility(View.VISIBLE);
                        gate_b_linear.setVisibility(View.GONE);
                        gate_c_linear.setVisibility(View.GONE);
                        gate_a.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_a_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                    }else if(jsonArray.getJSONObject(0).getString("gate").equalsIgnoreCase("GATE B")) {
                        gate_a_linear.setVisibility(View.GONE);
                        gate_b_linear.setVisibility(View.VISIBLE);
                        gate_c_linear.setVisibility(View.GONE);
                        gate_b.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_b_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                    }else {
                        gate_a_linear.setVisibility(View.GONE);
                        gate_b_linear.setVisibility(View.GONE);
                        gate_c_linear.setVisibility(View.VISIBLE);
                        gate_c.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_c_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                    }

                }else if (jsonArray.length()== 2) {


                    if(jsonArray.getJSONObject(0).getString("gate").equals("GATE A")&&jsonArray.getJSONObject(1).getString("gate").equals("GATE B")){
                        gate_a.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_a_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                        gate_b.setText(jsonArray.getJSONObject(1).getString("gate"));
                        gate_b_txt.setText(jsonArray.getJSONObject(1).getString("towards"));
                        gate_a_linear.setVisibility(View.VISIBLE);
                        gate_b_linear.setVisibility(View.VISIBLE);
                        gate_c_linear.setVisibility(View.GONE);
                    }else if(jsonArray.getJSONObject(0).getString("gate").equals("GATE B")&&jsonArray.getJSONObject(1).getString("gate").equals("GATE C")) {

                        gate_b.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_b_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                        gate_c.setText(jsonArray.getJSONObject(1).getString("gate"));
                        gate_c_txt.setText(jsonArray.getJSONObject(1).getString("towards"));
                        gate_a_linear.setVisibility(View.GONE);
                        gate_b_linear.setVisibility(View.VISIBLE);
                        gate_c_linear.setVisibility(View.VISIBLE);
                    }


                }else {

                    if(jsonArray.getJSONObject(0).getString("gate").equals("GATE A")&&jsonArray.getJSONObject(1).getString("gate").equals("GATE B")&&jsonArray.getJSONObject(2).getString("gate").equals("GATE C")){
                        gate_a_linear.setVisibility(View.VISIBLE);
                        gate_b_linear.setVisibility(View.VISIBLE);
                        gate_c_linear.setVisibility(View.VISIBLE);
                        gate_a.setText(jsonArray.getJSONObject(0).getString("gate"));
                        gate_a_txt.setText(jsonArray.getJSONObject(0).getString("towards"));
                        gate_b.setText(jsonArray.getJSONObject(1).getString("gate"));
                        gate_b_txt.setText(jsonArray.getJSONObject(1).getString("towards"));
                        gate_c.setText(jsonArray.getJSONObject(2).getString("gate"));
                        gate_c_txt.setText(jsonArray.getJSONObject(2).getString("towards"));
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

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
}