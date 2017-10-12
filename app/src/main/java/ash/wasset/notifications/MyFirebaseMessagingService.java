package ash.wasset.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ash.wasset.R;
import ash.wasset.activities.MainActivity;
import ash.wasset.activities.SplashScreenActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    JSONObject jsonObject;
    String notificationType;
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageSent(String s) {
        Log.e("Message" , s);
        super.onMessageSent(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("ddd", "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d("Dd", "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d("fff", "FCM Data Message: " + remoteMessage.getData());
        try {

            notification(remoteMessage.getData().toString());
//            if (remoteMessage.getNotification() != null) {
//                sendNotification(remoteMessage.getNotification());
//            }
//
//            jsonObject = new JSONObject(remoteMessage.getData().toString());
//            if (GeneralClass.groupIDOpened.equals(jsonObject.getString("gid"))){
//                Intent intent = new Intent();
//                intent.putExtra("message", remoteMessage.getData().toString());
//                intent.setAction("bcNewMessage");
//                sendBroadcast(intent);
//            } else {
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("groupChat", jsonObject.getString("gid"));
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ne_logo)
//                        .setContentTitle(jsonObject.getString("fname") + " " + jsonObject.getString("lname"))
//                        .setContentText(jsonObject.getString("msg"))
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//            }
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Toast.makeText(getApplicationContext(), jsonObject.getString("message").toString(), Toast.LENGTH_LONG).show();
//                        //Toast.makeText(getApplicationContext(), extras.getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            });

            //sendNotification(jsonObject.getString("message"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendNotification(RemoteMessage.Notification notification) {
//        try {
//            jsonObject = new JSONObject(messageBody);
//            notificationType = jsonObject.getString("type");
//
//            if (notificationType.equals("Ticket")){
//                ticketNotification(messageBody);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }

//        if (GeneralClass.appOpened == 1){
//            Intent intent = new Intent();
//            intent.setAction("updateNotification");
//            sendBroadcast(intent);
//        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void notification(String messageBody){
        String message, messageKey;
        try {
            jsonObject = new JSONObject(messageBody);
            jsonObject = new JSONObject(jsonObject.getString("AllBody"));
            message = jsonObject.getString("message");
            messageKey = jsonObject.getString("messageKey");
            Intent intent = new Intent(this, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("frame", 1);
            intent.putExtra("ticket", messageBody);
            showNotifications(message, intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private void showNotifications(String details, Intent intent){
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(contentIntent)
                .setSound(defaultSoundUri)
                .setContentText(details)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                .bigText("s"))
//                .setPriority(NotificationCompat.PRIORITY_MAX)
                //.setContentInfo(message)
                .setAutoCancel(true);
        notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
