package org.chennaimetrorail.appv1;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.chennaimetrorail.appv1.activity.Home;
import org.chennaimetrorail.appv1.travelcab.Booking;
import org.chennaimetrorail.appv1.travelcard.TravelCardList;

public class ProgressBar extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog;

    public ProgressBar(Home activity) {
        dialog = new ProgressDialog(activity);
    }
    public ProgressBar(TravelCardList activity) {
        Context getActivity = null;
        dialog = new ProgressDialog(getActivity);
    }
    public ProgressBar(Booking activity) {
        Context getActivity = null;
        dialog = new ProgressDialog(getActivity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Doing something, please wait.");
        dialog.show();
    }
    @Override
    protected Void doInBackground(Void... args) {
        // do background work here
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        // do UI work here
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}