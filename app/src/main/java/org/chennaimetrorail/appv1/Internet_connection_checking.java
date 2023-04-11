package org.chennaimetrorail.appv1;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class Internet_connection_checking {

    Activity activity;
    Context context;

    public Internet_connection_checking(Activity mactivity) {
        activity = mactivity;
    }


    public boolean checkInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        // ARE WE CONNECTED TO THE NET

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;

        }
    }
}
