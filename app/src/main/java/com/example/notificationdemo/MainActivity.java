package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

public class MainActivity extends AppCompatActivity {
    public static int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_ID_2 = 2;
    public static final int NOTIFICATION_ID_3 = 3;
    public static final String CHANNEL_ID = "bearAppNChannel";

    public static final String KEY_TEXT_REPLY = "reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Button sendNotificationButton = findViewById(R.id.btn_notification);
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        Button sendMessageNotificationButton = findViewById(R.id.btn_mess_noti);
        sendMessageNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotificationWithReply();
            }
        });
        Button sendNotificationWithActionButton = findViewById(R.id.btn_action_noti);
        sendNotificationWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotificationWithActionButton();
            }
        });
    }

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = new Intent(this, InputActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(MainActivity.this, "bearAppNChannel")
                .setContentTitle("From GauNgo")
                .setContentText("Cute bear <3")
                .setSmallIcon(R.drawable.bear_icon)
                .setLargeIcon(bitmap) //optional
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setGroup("group_key")
                .setAutoCancel(true)
                .setTimeoutAfter(50000); //1000milliseconds = 1s

        Notification notification = nBuilder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            // notificationId is a unique int for each notification that you must define.
            manager.notify(NOTIFICATION_ID++, notification);
        }
    }

    private void displayNotificationWithActionButton() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent welcomeIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(MainActivity.this, "bearAppNChannel")
                .setContentTitle("From GauNgo")
                .setContentText("Cute bear <3")
                .setSmallIcon(R.drawable.bear_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.frog, "Welcome", welcomeIntent)
                .setAutoCancel(true)
                .setTimeoutAfter(5000); //5000milliseconds

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            // notificationId is a unique int for each notification that you must define.
            manager.notify(NOTIFICATION_ID_2, nBuilder.build());
        }
    }

    private void displayNotificationWithReply(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent welcomeIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(MainActivity.this, "bearAppNChannel")
                .setContentTitle("From GauNgo")
                .setContentText("Cute bear <3")
                .setSmallIcon(R.drawable.bear_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(welcomeIntent)
                .setAutoCancel(true)
                .setTimeoutAfter(50000); //5000milliseconds

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Reply").build();
            Intent remoteIntent = new Intent(this, InputActivity.class);
            remoteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent remotePendingIntent = PendingIntent.getActivity(this, 0, remoteIntent, PendingIntent.FLAG_MUTABLE);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                  R.drawable.frog,
                    "Reply",
                    remotePendingIntent
            ).addRemoteInput(remoteInput).build();
            nBuilder.addAction(action);
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            // notificationId is a unique int for each notification that you must define.
            manager.notify(NOTIFICATION_ID_3, nBuilder.build());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "gauNgo";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.rgb(123, 211, 143));
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.setDescription("This is default channel used for all other notifications");

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}

