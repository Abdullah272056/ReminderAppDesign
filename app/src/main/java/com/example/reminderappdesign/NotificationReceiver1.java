package com.example.reminderappdesign;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver1 extends BroadcastReceiver{
    AudioManager audioManager;
    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, "second", Toast.LENGTH_LONG).show();
        Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);

    }
}
