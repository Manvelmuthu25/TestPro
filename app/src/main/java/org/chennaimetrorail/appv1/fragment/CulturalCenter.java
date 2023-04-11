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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chennaimetrorail.appv1.FontStyle;
import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.R;

/**
 * Created by 102525 on 7/12/2017.
 */

public class CulturalCenter extends Fragment {

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    TextView text_1, text_2, text_3, text_4, text_5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cultural_centre, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        final Home home = new Home();
        home.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);

        text_1 = (TextView) view.findViewById(R.id.text_1);
        text_2 = (TextView) view.findViewById(R.id.text_2);
        text_3 = (TextView) view.findViewById(R.id.text_3);
        text_4 = (TextView) view.findViewById(R.id.text_4);
        text_5 = (TextView) view.findViewById(R.id.text_5);

        text_1.setTypeface(fontStyle.helveticabold_CE);
        text_2.setTypeface(fontStyle.helveticabold_CE);
        text_3.setTypeface(fontStyle.helveticabold_CE);
        text_4.setTypeface(fontStyle.helveticabold_CE);
        text_5.setTypeface(fontStyle.helveticabold_CE);


        /*Back button action*/
        LinearLayout image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new TourGuid());
                fragmentTransaction.commit();
            }
        });
        /*Title Text*/
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText("Cultural Centre");
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
}