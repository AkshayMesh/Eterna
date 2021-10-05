package eterna.data.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import eterna.R;
import eterna.ui.activity.Login;

public class FirebaseNotifyService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="order_channel";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        String message= remoteMessage.getData().get("message");
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent =  stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            if(message != null) {
                JSONObject jsonObject = new JSONObject(message);
                notify(jsonObject,pendingIntent);
            }
            else {
                notify(null,pendingIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notify(JSONObject json, PendingIntent pendingIntent) {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        if (json!=null){
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            try {
                new Handler(Looper.getMainLooper()).post(() -> {
                    NotificationCompat.Builder notificationBuilder;
                    try {
                        notificationBuilder = new NotificationCompat.Builder(FirebaseNotifyService.this, ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setStyle(new NotificationCompat.BigPictureStyle()
                                        .bigLargeIcon(null))
                                .setContentTitle(json.getString("title"))
                                .setContentText(json.getString("desc"))
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setSound(notificationSoundUri)
                                .setContentIntent(pendingIntent);
                        notificationManager.notify(notificationID, notificationBuilder.build());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "Eterna";
        String adminChannelDescription = "New Updates";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.YELLOW);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}