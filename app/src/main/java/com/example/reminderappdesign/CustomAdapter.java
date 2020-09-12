package com.example.reminderappdesign;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reminderappdesign.database.DataBaseHelper;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.ALARM_SERVICE;
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    private List<Notes> allNotes;
    List<Notes> copyAllNotes;
    private DataBaseHelper databaseHelper;
    AlarmManager alarm;
    PendingIntent alarmIntent;
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

        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent=new Intent(context, NotificationReceiver.class);
                    alarmIntent= PendingIntent.getBroadcast(context,
                            allNotes.get(position).getNotificationRequestCode(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    alarm= (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    alarm.cancel(alarmIntent);
                    // The toggle is enabled
                    Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "uncheck", Toast.LENGTH_SHORT).show();
                    // The toggle is disabled
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return allNotes.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView,endTimeTextView;
        Switch switchButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView= itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView= itemView.findViewById(R.id.endTimeTextView);
            switchButton= itemView.findViewById(R.id.switchButtonId);
        }
    }


}
