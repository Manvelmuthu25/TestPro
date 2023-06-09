package org.chennaimetrorail.appv1;

/**
 * Created by 102525 on 9/19/2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.chennaimetrorail.appv1.activity.Home;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class CmrlFirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "CmrlFirebaseMsgService";
    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;

    /***
     * On Message Received action
     * */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        //Generate notificationId random values
        int notificationId = new Random().nextInt(60000);
        Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));
        Intent likeIntent = new Intent(this, Home.class);
        likeIntent.putExtra(NOTIFICATION_ID_EXTRA, notificationId);
        likeIntent.putExtra(IMAGE_URL_EXTRA, remoteMessage.getData().get("image-url"));
        PendingIntent likePendingIntent = PendingIntent.getService(this, notificationId + 1, likeIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /***
         * Check the OS version
         * */

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***
         * Check image-url is there or not
         * */
        if (remoteMessage.getData().get("image-url") == null) {
            Log.d("notification-imgurl", "noimage");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.noti_icon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSound(defaultSoundUri)
                    // .addAction(R.drawable.ic_launcher, getString(R.string.notification_add_to_cart_button), likePendingIntent)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());

        } else {
            Log.d("notification-imgurl", "image");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.noti_icon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                    .setStyle(new NotificationCompat.BigPictureStyle().setSummaryText(remoteMessage.getNotification().getBody()).bigPicture(bitmap))//Notification with Image
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    // .addAction(R.drawable.ic_launcher, getString(R.string.notification_add_to_cart_button), likePendingIntent)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }

    }

    /***
     * Check image-url convert bitmap
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * set Channel for android O and above versions
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


}