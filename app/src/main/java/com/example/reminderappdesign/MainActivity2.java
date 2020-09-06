package com.example.reminderappdesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TimePicker;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TimePicker startPicker=findViewById(R.id.startTimePickerId);
        startPicker.setIs24HourView(true);
        TimePicker endPicker=findViewById(R.id.endTimePickerId);
        endPicker.setIs24HourView(true);
    }
}