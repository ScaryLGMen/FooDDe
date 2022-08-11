package com.gmail.foodde.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushService extends FirebaseMessagingService {

    private static final int NOTIFY_ID = 101;
    public static final String TAG = "PushService";

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String val1 = remoteMessage.getData().get("val1");
            String val2 = remoteMessage.getData().get("val2");
            String val3 = remoteMessage.getData().get("val3");
            ShowNotification(val1, val2, val3);
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody()
                    +" "+ remoteMessage.getData().toString());
        }
    }

    void ShowNotification(String title, String text, String data){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"default")
                .setSmallIcon(android.R.drawable.ic_lock_lock)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.gmail.foodde");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "com.gmail.foodde",
                    "FooDDe",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(NOTIFY_ID,builder.build());
    }
}
