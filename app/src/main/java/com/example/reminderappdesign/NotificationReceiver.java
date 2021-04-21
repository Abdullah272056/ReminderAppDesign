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

public class NotificationReceiver extends BroadcastReceiver{
    AudioManager audioManager;
    @Override
    public void onReceive(Context context, Intent intent) {


        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mod=audioManager.getRingerMode();
        if (mod== AudioManager.RINGER_MODE_NORMAL){
            audioManager.setRingerMode(1);
        }else if (mod==AudioManager.RINGER_MODE_VIBRATE){
            audioManager.setRingerMode(1);
            //Toast.makeText(context, "not allow", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "no change", Toast.LENGTH_SHORT).show();
        }



        int notificationRequest=intent.getIntExtra("notificationRequestCode",0);
        long TargetTimeMilliSecond=intent.getIntExtra("TargetTimeMilliSecond",0);


        //String message=intent.getStringExtra("todo");
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationI = 100;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Intent destinationIntent=new Intent(context, MainActivity.class);
        destinationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,notificationRequest,destinationIntent
                ,PendingIntent.FLAG_UPDATE_CURRENT);
        //for Notification sound........

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Teachers's Diary"+String.valueOf(notificationRequest))
                .setContentText("Be Ready for your Next Classes")
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);
        notificationManager.notify(notificationRequest,builder.build());
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();



        //set Vibrator
        Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

    }
}
