package org.chennaimetrorail.appv1.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.chennaimetrorail.appv1.Constant;
import org.chennaimetrorail.appv1.Internet_connection_checking;
import org.chennaimetrorail.appv1.R;
import org.chennaimetrorail.appv1.rest.ApiClient;
import org.chennaimetrorail.appv1.rest.ApiInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by trainee4 on 7/5/2017.
 */

public class SplashScreen extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private final int SPLASH_DISPLAY_LENGTH = 4000;
    GifTextView load_gif;
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    String TAG = "SplashScreen";
    Dialog dialog = null;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final int EXTERNAL_READ_INTENT_ID = 4;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);
        FirebaseMessaging.getInstance().subscribeToTopic("cmrlapp_android");
        FirebaseInstanceId.getInstance().getToken();
        String notificationToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Check_token" + notificationToken);
        // show progress cmrl logo
        load_gif = (GifTextView) findViewById(R.id.load_gif);

        int_chk = new Internet_connection_checking(SplashScreen.this);

        runTimePermission();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * Ask RequestPermissions (SDCARD and LOCATION)
     */
    @Override
    protected void onResume() {
        super.onResume();

        //runTimePermission();
    }


    public void runTimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    storagedialogs();

                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        locationdialogs();

                    } else {
                        Connection();
                    }
                }
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                storagedialogs();

            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationdialogs();

                } else {
                    Connection();
                }

            }
        }else {
            Connection();
        }

    }

    private void  requesdExternalReadPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((this), Manifest.permission.ACCESS_MEDIA_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
                        EXTERNAL_READ_INTENT_ID);
            } else {
                requestPermissions( new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},
                        EXTERNAL_READ_INTENT_ID);
            }
        }else {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            EXTERNAL_READ_INTENT_ID);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            EXTERNAL_READ_INTENT_ID);
                }
            }else {
                Connection();
            }
        }
    }

    /**
     * RequestPermissions (SDCARD and LOCATION)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                Connection();
                break;

            case EXTERNAL_READ_INTENT_ID:
                runTimePermission();
                break;
            default:
                break;
        }
    }



    private void locationdialogs() {

        final Dialog dialog = new Dialog(SplashScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.location_permission);
        dialog.setCancelable(false);
        Button otp_verify_btn = (Button) dialog.findViewById(R.id.location_permissionid);
        otp_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);

            }
        });
        dialog.show();
    }
    private void storagedialogs() {
        if(dialog !=null && dialog.isShowing()){
            dialog.dismiss();
        }
        dialog = new Dialog(SplashScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.storage_permission);
        dialog.setCancelable(false);
        Button otp_verify_btn = (Button) dialog.findViewById(R.id.storage_permissionid);
        otp_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requesdExternalReadPermission();
            }
        });
        dialog.show();
    }


    public void runTimePemissionAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
        alert.setMessage(getString(R.string.location_write_external_storage).toUpperCase(Locale.getDefault()));
        alert.setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  runTimePermission();

            }
        });
        alert.setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * Check Internet Connection
     */
    public void Connection() {
        int_chk = new Internet_connection_checking(SplashScreen.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            File database = new File(getExternalFilesDir(null) + File.separator + Constant.db_name);
            if (!database.exists()) {
                if (copyDataBase(this)) {
                    Log.d("", "Copy database success");
                    moveActivity();

                } else {
                    Log.d("", "Copy database faild");
                    moveActivity();
                    return;
                }
            } else {
                moveActivity();
            }
        } else {
            downloadfilefromdb();

        }
    }


    /**
     * Background Async Task to download file
     */
    public void downloadfilefromdb() {
        ApiInterface apiService = ApiClient.getResponse().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.downloadFileWithFixedUrl();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("SplashScreen",""+call.request().url().toString());
                if (response.code() == 200) {
                    if (response.isSuccessful()) {

                        File file = new File(getExternalFilesDir(null) + File.separator + Constant.db_name);
                        File file1 = new File(getExternalFilesDir(null) + File.separator + Constant.db_name_journal);
                        if (file.exists() && file1.exists()) {
                            file.delete();
                            file1.delete();
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                            if (writtenToDisk) {
                                Log.d("Splashscreen", "file download was a success? " + writtenToDisk);
                                moveActivity();
                            } else {
                                writeResponseBodyToDisk(response.body());
                            }

                        } else {

                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            if (writtenToDisk) {
                                Log.d("Splashscreen", "file download was a success? " + writtenToDisk);
                                moveActivity();
                            } else {
                                writeResponseBodyToDisk(response.body());
                            }
                        }

                    } else {
                        Log.d("Splashscreen", "server contact failed");
                        downloadfilefromdb();
                    }
                } else {

                    downloadfilefromdb();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Splashscreen test", "error");
                downloadfilefromdb();
            }
        });
    }

    /**
     * writeResponseDBToDisk convert Byte code
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + Constant.db_name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Splashscreen1111", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Move Home Activity after all finished
     */
    public void moveActivity() {

        Handler handler = new Handler();
        Runnable myRunnable = new Runnable() {
            public void run() {
                // do something
                /*Intent mainIntent = new Intent(SplashScreen.this, Home.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                finish();*/

                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                finish();
            }
        };
        handler.postDelayed(myRunnable, SPLASH_DISPLAY_LENGTH);
    }

    /**
     * In case once download app then user goes to offline write AssetDBToDisk convert Byte code
     */
    private boolean copyDataBase(Context context) {

        try {
            InputStream inputStream = context.getAssets().open(Constant.db_name);
            String outfileName = getExternalFilesDir(null) + File.separator + Constant.db_name;

            OutputStream outputStream = new FileOutputStream(outfileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();

            Log.d("Splashcreen22 ", "DB Copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }


}