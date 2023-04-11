package org.chennaimetrorail.appv1.fragment;

import android.content.Intent;
import android.net.Uri;
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
 * Created by 102525 on 7/10/2017.
 */

public class ContactUs extends Fragment {
    LinearLayout image_view_back;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    TextView telephone_cont,mail_cont;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.contacts, container, false);
        FontStyle fontStyle = new FontStyle();
        fontStyle.Changeview(getActivity(),view);
        final Home home = new Home();
        home.toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        home.toolbar.setBackgroundResource(R.color.colorPrimary);
        home.toolbar_img = (ImageView)getActivity().findViewById(R.id.toolbar_img);
        home.toolbrtxt_layout = (LinearLayout) getActivity().findViewById(R.id.toolbrtxt_layout);
        home.toolbrtxt_layout.setVisibility(View.VISIBLE);
        home.toolbar_img.setVisibility(View.GONE);
        telephone_cont=view.findViewById(R.id.telephone_cont);
        mail_cont =view.findViewById(R.id.mail_cont);

        image_view_back = (LinearLayout) view.findViewById(R.id.image_view_back);
        TextView title_txt = (TextView)view.findViewById(R.id.title_txt);
        title_txt.setText("Contact Information");

        image_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.fragment_place, new OtherInfo());
                fragmentTransaction.commit();

            }
        });

  telephone_cont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:18604251515"));
                startActivity(callIntent);

            }
        });
/*
        mail_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType(mail_cont.getText().toString());
                startActivity(emailIntent);
            }
        });*/

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

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


}