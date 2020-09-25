package com.example.reminderappdesign.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.reminderappdesign.Notes;
import com.example.reminderappdesign.database.Constants;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;
    public DataBaseHelper(@Nullable Context context) {
        super(context, Constants.TABLE_NAME, null, Constants.DATABASE_Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE);
        Toast.makeText(context, "On created is called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<Notes> getAllNotes(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<Notes> dataList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+Constants.TABLE_NAME,null);
        if (cursor.moveToFirst()){
            do {
                Notes note = new Notes(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(Constants.COLUMN_END_TIME)),
                        cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_NOTIFICATION_REQUEST_CODE)),
                        cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_NOTIFICATION_ID)),
                        cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_SWITCH_STATUS))
                      );

                dataList.add(note);
            }while (cursor.moveToNext());
        }
        return dataList;
    }

    public int insertData(Notes notes){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.COLUMN_START_TIME,notes.getStartTime());
        contentValues.put(Constants.COLUMN_END_TIME,notes.getEndTime());
        contentValues.put(Constants.COLUMN_NOTIFICATION_REQUEST_CODE,notes.getEndTime());
        contentValues.put(Constants.COLUMN_NOTIFICATION_ID,notes.getEndTime());
        contentValues.put(Constants.COLUMN_SWITCH_STATUS,notes.getSwitchStatus());
       // contentValues.put(Constants.COLUMN,notes.getSwitchStatus());
        int id= (int) sqLiteDatabase.insert(Constants.TABLE_NAME,null,contentValues);
        return id;
    }


    public int updateData(Notes notes){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_START_TIME,notes.getStartTime());
        contentValues.put(Constants.COLUMN_END_TIME,notes.getEndTime());
        contentValues.put(Constants.COLUMN_NOTIFICATION_REQUEST_CODE,notes.getEndTime());
        contentValues.put(Constants.COLUMN_NOTIFICATION_ID,notes.getEndTime());
        contentValues.put(Constants.COLUMN_SWITCH_STATUS,notes.getSwitchStatus());
        int status = sqLiteDatabase.update(Constants.TABLE_NAME,contentValues," id=? ",new String[]{String.valueOf(notes.getId())});
        return status;
    }

    public int deleteData(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int status = sqLiteDatabase.delete(Constants.TABLE_NAME,"id=?",new String[]{String.valueOf(id)});
        return status;

    }

}
