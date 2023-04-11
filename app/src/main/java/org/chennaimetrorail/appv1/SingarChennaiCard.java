package org.chennaimetrorail.appv1;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.activity.Travelcardbalance;
import org.chennaimetrorail.appv1.adapter.ViewPagerAdapter;

public class SingarChennaiCard extends AppCompatActivity {
    Button webbutton,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_singar_chennai_card);
        back= (Button)findViewById(R.id.back) ;
        webbutton = (Button) findViewById(R.id.chennai);

        webbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://transit.sbi/swift/customerportal?pagename=cmrl";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);

            }
        });

    }
}