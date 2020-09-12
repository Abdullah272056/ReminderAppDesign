package com.example.reminderappdesign;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderappdesign.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    private List<Notes> allNotes;
    List<Notes> copyAllNotes;
    private DataBaseHelper databaseHelper;

    public CustomAdapter(Context context, List<Notes> allNotes) {
        this.context = context;
        this.allNotes = allNotes;
        this.context=context;
        databaseHelper=new DataBaseHelper(context);

        copyAllNotes = new ArrayList<>(allNotes);//for searchView//dataList's copy
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
       View view= layoutInflater.inflate(R.layout.recyclerview_row_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.startTimeTextView.setText(allNotes.get(position).getStartTime());
        holder.endTimeTextView.setText(allNotes.get(position).getEndTime());

    }
    @Override
    public int getItemCount() {
        return allNotes.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView,endTimeTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView= itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView= itemView.findViewById(R.id.endTimeTextView);

        }
    }


}
