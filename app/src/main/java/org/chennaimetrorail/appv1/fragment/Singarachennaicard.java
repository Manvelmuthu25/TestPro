package org.chennaimetrorail.appv1.fragment;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.support.annotation.Nullable;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationRequest;

        import org.chennaimetrorail.appv1.FontStyle;
        import org.chennaimetrorail.appv1.Internet_connection_checking;
        import org.chennaimetrorail.appv1.R;
        import org.chennaimetrorail.appv1.activity.Home;


public class Singarachennaicard extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    Button Sugarbox_setuid;
    String version, menuitemstatuscode;
    public static final int DEFAULT_LOCATION_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        pref = getActivity().getSharedPreferences("LoginDetails", 0);
        editor = pref.edit();
        int_chk = new Internet_connection_checking(getActivity());

        View view = inflater.inflate(R.layout.activity_sugarbox, container, false);
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
        Sugarbox_setuid = (Button) view.findViewById(R.id.Sugarbox_setuid);

        menuitemstatuscode = pref.getString("menuitemstatuscode", null);
        Connection = int_chk.checkInternetConnection();
        //https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu
        // Use package name which we want to check

        final String sugarboxPackageName = "com.sboxnw.freeplay";
        //final String sugarboxPackageName = "com.baseproject";
        // final String sugarboxPackageName ;
        final boolean isAppInstalled = appInstalledOrNot(getContext(), sugarboxPackageName);
        Log.e("isAppInstalled", isAppInstalled + "");
        if (!Connection) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "This function requires internet connection.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
        }

//
//        if (getContext() != null) {
//
//            Sugarbox_setuid.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isAppInstalled) {
//                        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(sugarboxPackageName);
//                        startActivity(intent);
//                    } else {
//                       // launchPlayStore(getContext(), sugarboxPackageName);
//                    }
//                }
//            });
//            //      }
//
//        }


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
                    home.home_relative = (RelativeLayout) getActivity().findViewById(R.id.home_relative);
                    home.home_relative.setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_place)).commit();
                    return true;

                }

                return false;
            }
        });


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initView();
    }

//    public void launchPlayStore(Context context, String sugarboxPackageName) {
//        Intent intent = null;
//        try {
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse("https://a.sbnw.in/cmrl-app" + sugarboxPackageName));
//            context.startActivity(intent);
//        } catch (android.content.ActivityNotFoundException anfe) {
////            Toast.makeText(getApplicationContext(), "You've to install sugarboxPackageName in your mobile" , Toast.LENGTH_LONG).show();
//            //  https://a.sbnw.in/cmrl-app
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://a.sbnw.in/cmrl-app"+sugarboxPackageName)));
//        }
//    }

//    public void launchPlayStore(Context context, String packageName) {
//        Intent intent = null;
//        try {
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse("market://details?id=" + packageName));
//            context.startActivity(intent);
//        } catch (android.content.ActivityNotFoundException anfe) {
////            Toast.makeText(getApplicationContext(), "You've to install sugarboxPackageName in your mobile" , Toast.LENGTH_LONG).show();
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
//        }
//    }


    private boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
//            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return false;
    }


}