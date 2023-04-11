package org.chennaimetrorail.appv1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;

/**
 * Created by 102525 on 7/12/2017.
 */

public class TourSpotsDetails extends Fragment {

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    TextView  text_1,text_2,text_3,text_4,text_5,text_6,text_7,text_8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tour_spots_details, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);

        text_1 = (TextView)view.findViewById(R.id.text_1);
        text_2 = (TextView)view.findViewById(R.id.text_2);
        text_3 = (TextView)view.findViewById(R.id.text_3);
        text_4 = (TextView)view.findViewById(R.id.text_4);
        text_5 = (TextView)view.findViewById(R.id.text_5);
        text_6 = (TextView)view.findViewById(R.id.text_6);
        text_7 = (TextView)view.findViewById(R.id.text_7);
        text_8 = (TextView)view.findViewById(R.id.text_8);

        text_1.setTypeface(fontStyle.helveticabold_CE);
        text_2.setTypeface(fontStyle.helveticabold_CE);
        text_3.setTypeface(fontStyle.helveticabold_CE);
        text_4.setTypeface(fontStyle.helveticabold_CE);
        text_5.setTypeface(fontStyle.helveticabold_CE);
        text_6.setTypeface(fontStyle.helveticabold_CE);
        text_7.setTypeface(fontStyle.helveticabold_CE);
        text_8.setTypeface(fontStyle.helveticabold_CE);

           /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new TourGuid());
                fragmentTransaction.commit();
            }
        });
        Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        /*Title Text*/
        TextView title_txt = (TextView)view.findViewById(R.id.title_txt);
        title_txt.setText("Tour Spots & Details");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

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
}