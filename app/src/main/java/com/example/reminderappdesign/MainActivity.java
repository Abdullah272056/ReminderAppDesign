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
    AlarmManager alarm;
    PendingIntent alarmIntent;

    long alarmStartTimeMiliSecond;
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
                int hour=startTimePicker.getCurrentHour();
                int minute=startTimePicker.getCurrentMinute();
                Calendar startTimeCalender=Calendar.getInstance();
                startTimeCalender.set(Calendar.HOUR_OF_DAY,hour);
                startTimeCalender.set(Calendar.MINUTE,minute);
                startTimeCalender.set(Calendar.SECOND,0);
                String startTimeString = dateFormat.format(startTimeCalender.getTime()).toString();
               alarmStartTimeMiliSecond=startTimeCalender.getTimeInMillis();

               // convert long to int for notificationRequestCode
                int notificationRequestCode= (int) alarmStartTimeMiliSecond;
                //data inserting by insertData method
                int  id=dataBaseHelper.insertData(new Notes(startTimeString,String.valueOf(alarmStartTimeMiliSecond),notificationRequestCode,notificationId,1));
                if (id != -1){

                    Intent intent=new Intent(MainActivity.this, NotificationReceiver.class);
            //intent.putExtra("notificationRequestCode",  allNotes.get(position).getId());
            alarmIntent= PendingIntent.getBroadcast(MainActivity.this,
                    (int) id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            //get ALARM_SERVICE from SystemService
            alarm= (AlarmManager) getSystemService(ALARM_SERVICE);
            //alarm set
            alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTimeMiliSecond,alarmIntent);

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