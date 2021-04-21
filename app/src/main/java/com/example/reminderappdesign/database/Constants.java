package com.example.reminderappdesign.database;

public class Constants {
    public  static final String DATABASE_NAME="Reminder.db";
    public  static final int DATABASE_Version=15;
    public  static final String TABLE_NAME="ReminderInformation";
    public  static final String COLUMN_ID="id";
    public  static final String COLUMN_START_TIME="StartTime";
    public  static final String COLUMN_END_TIME="EndTime";
    public  static final String COLUMN_NOTIFICATION_REQUEST_CODE="NotificationRequestCode";
    public  static final String COLUMN_NOTIFICATION_ID="NotificationId";
    public  static final String COLUMN_SWITCH_STATUS="SwitchStatus";
    public  static final String COLUMN_START_TIME_MILLI="StartTimeMilli";
    public static final String CREATE_TABLE  = " CREATE TABLE "+TABLE_NAME+"("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_START_TIME+" INTEGER, "
            +COLUMN_END_TIME+" INTEGER, "
            +COLUMN_NOTIFICATION_REQUEST_CODE+" INTEGER, "
            +COLUMN_NOTIFICATION_ID+" INTEGER, "
            +COLUMN_SWITCH_STATUS+" INTEGER "

            +")";

}

