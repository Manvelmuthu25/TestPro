package org.chennaimetrorail.appv1;

/**
 * Created by 102525 on 9/19/2017.
 */

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.chennaimetrorail.appv1.modal.jsonmodal.DeviceDetails;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CmrlFirebaseInstanceIdService extends FirebaseInstanceIdService {

  //  private static final String TAG = "CmrlFirebaseInstanceIdService";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String version;
    String TAG = "CmrlFirebaseInstanceIdService";
    /***
     * Get Token id
     * */
    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        pref = getApplicationContext().getSharedPreferences("deviceID_Token", 0);
        editor = pref.edit();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d("Cechjald", "FireRefreshedtoken: " + refreshedToken);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Cechjald2", "FireRefreshedtoken: " + android_id);
        editor.putString("token_id", refreshedToken);
        editor.putString("device_id", android_id);
        editor.commit();
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        sendRegistrationToServer(refreshedToken, android_id);
    }


    /***
     * Send Token id to our server token_id,android_id
     * */

    private void sendRegistrationToServer(String token, String imei) {
        //Implement this method if you want to store the token on your server

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);

        Call<DeviceDetails> call = apiService.getDeviceDetails(imei, token, "Android", Constant.strApiKey, version);

        call.enqueue(new Callback<DeviceDetails>() {
            @Override
            public void onResponse(Call<DeviceDetails> call, Response<DeviceDetails> response) {
                int statusCode = response.code();
                Log.d(TAG,call.request().url().toString());
                Log.d(TAG, "statusCode" + statusCode);
                if (statusCode == 200) {
                    Log.d(TAG, "--200" + response.body().getStatus());
                } else {
                    onTokenRefresh();
                }
            }

            @Override
            public void onFailure(Call<DeviceDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
                onTokenRefresh();
            }
        });


    }
}