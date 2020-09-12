package com.example.reminderappdesign;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.reminderappdesign.database.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DataBaseHelper dataBaseHelper;
    List<Notes> dataList;
    DatePickerDialog.OnDateSetListener dateListener;
    TimePickerDialog.OnTimeSetListener timeListener;
    Calendar calendar;
    CustomAdapter customAdapter;
    int notificationId=0;
    AlarmManager alarm;
    PendingIntent alarmIntent,alarmIntentStop;
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
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
    private void CustomDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater =LayoutInflater.from(MainActivity.this);
        View view=layoutInflater.inflate(R.layout.input_box,null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        Button saveButton=view.findViewById(R.id.saveButtonId);
        final TimePicker startTimePicker =view.findViewById(R.id.startTimePickerId);
        final TimePicker endTimePicker =view.findViewById(R.id.endTimePickerId);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String startTime=startTimePicker.getHour()+":"+startTimePicker.getMinute();
               String endTime=endTimePicker.getHour()+":"+endTimePicker.getMinute();
                int hour=startTimePicker.getCurrentHour();
                int minute=startTimePicker.getCurrentMinute();
                Calendar time=Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY,hour);
                time.set(Calendar.MINUTE,minute);
                time.set(Calendar.SECOND,0);
                long alarmStartTime=time.getTimeInMillis();
                int notificationRequestCode= (int) alarmStartTime;
                long id=dataBaseHelper.insertData(new Notes("S-"+startTime,"E-"+endTime,notificationRequestCode,notificationId));
                if (id != -1){
                    Intent intent=new Intent(MainActivity.this, NotificationReceiver.class);
                    intent.putExtra("notificationRequestCode",notificationRequestCode);
                    alarmIntent= PendingIntent.getBroadcast(MainActivity.this,
                            notificationRequestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    alarm= (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
                    alertDialog.dismiss();
                    loadData();
                    Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to Insert", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }
}