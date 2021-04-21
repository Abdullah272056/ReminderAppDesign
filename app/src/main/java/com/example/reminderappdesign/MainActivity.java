package com.example.reminderappdesign;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.reminderappdesign.database.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DataBaseHelper dataBaseHelper;
    List<Notes> dataList;
    Calendar calendar;
    CustomAdapter customAdapter;
    int notificationId=0;
    AlarmManager alarm,alarm1;
    PendingIntent alarmIntent,alarmIntent1;

    long alarmStartTimeMilliSecond;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper=new DataBaseHelper(MainActivity.this);
        dataBaseHelper.getWritableDatabase();
        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        loadData();
        floatingActionButton=findViewById(R.id.floatingButtonId);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CustomDialog();
            }
        });
    }

    private void loadData(){
        dataList  = new ArrayList<>();
        dataList = dataBaseHelper.getAllNotes();
        if (dataList.size() > 0){
            customAdapter = new CustomAdapter(MainActivity.this,dataList);
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        }else {
            //Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void CustomDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater =LayoutInflater.from(MainActivity.this);
        View view=layoutInflater.inflate(R.layout.input_box,null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        Button saveButton=view.findViewById(R.id.saveButtonId);
        Button cancelButton=view.findViewById(R.id.cancelButtonId);
        final TimePicker startTimePicker =view.findViewById(R.id.startTimePickerId);
       // final TimePicker endTimePicker =view.findViewById(R.id.endTimePickerId);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //dateFormat
                DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");

              // for startTime
               // int hour=startTimePicker.getCurrentHour();
                int hour=startTimePicker.getHour();
               // int minute=startTimePicker.getCurrentMinute();
                int minute=startTimePicker.getMinute();
                Calendar startTimeCalender=Calendar.getInstance();
                startTimeCalender.set(Calendar.HOUR_OF_DAY,hour);
                startTimeCalender.set(Calendar.MINUTE,minute);
                startTimeCalender.set(Calendar.SECOND,0);
                String startTimeString = dateFormat.format(startTimeCalender.getTime()).toString();
                alarmStartTimeMilliSecond=startTimeCalender.getTimeInMillis();

               // convert long to int for notificationRequestCode
                int notificationRequestCode= (int) alarmStartTimeMilliSecond;
                //data inserting by insertData method
                int  id=dataBaseHelper.insertData(new Notes(startTimeString,String.valueOf(alarmStartTimeMilliSecond),notificationRequestCode,notificationId,1));
                if (id != -1){



            Intent intent=new Intent(getApplication(), NotificationReceiver.class);
            Intent intent1=new Intent(getApplication(), NotificationReceiver1.class);
            intent.putExtra("notificationRequestCode",id);
            intent.putExtra("TargetTimeMilliSecond",alarmStartTimeMilliSecond);
            alarmIntent= PendingIntent.getBroadcast(MainActivity.this,
                    (int) id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmIntent1= PendingIntent.getBroadcast(MainActivity.this,
                    (int) id,intent1,PendingIntent.FLAG_UPDATE_CURRENT);





            //get ALARM_SERVICE from SystemService
            alarm= (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm1= (AlarmManager) getSystemService(ALARM_SERVICE);
            //alarm set
                    long alrm=alarmStartTimeMilliSecond+60000;
            alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTimeMilliSecond,alarmIntent);
            alarm1.set(AlarmManager.RTC_WAKEUP,alrm,alarmIntent1);

                    alertDialog.dismiss();
                    loadData();
                    Toast.makeText(MainActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to Insert", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}