package com.example.reminderappdesign;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
    int switchButtonStatus;
    public CustomAdapter(Context context, List<Notes> allNotes) {
        this.context = context;
        this.allNotes = allNotes;
        this.context=context;
        databaseHelper=new DataBaseHelper(context);
        copyAllNotes = new ArrayList<>(allNotes);//for searchView//dataList's copy
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater= LayoutInflater.from(context);
       View view= layoutInflater.inflate(R.layout.recyclerview_row_item,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.startTimeTextView.setText(allNotes.get(position).getStartTime());
        holder.endTimeTextView.setText(allNotes.get(position).getEndTime());
        holder.requestTextView.setText(String.valueOf(position));
        switchButtonStatus=allNotes.get(position).getSwitchStatus();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder  = new androidx.appcompat.app.AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_operation,null);

                builder.setView(view);

                final androidx.appcompat.app.AlertDialog alertDialog = builder.create();


                TextView updateTextView=view.findViewById(R.id.updateTextViewId);
                TextView deleteTextView=view.findViewById(R.id.deleteTextViewId);
                TextView cancelTextView=view.findViewById(R.id.cancelTextViewId);

                updateTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // customDialog(position);
                        alertDialog.dismiss();

                    }
                });

                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int status = databaseHelper.deleteData(allNotes.get(position).getId());
                        if (status == 1){
                            allNotes.remove(allNotes.get(position));
                            alertDialog.dismiss();
                            notifyDataSetChanged();
                        }else {
                        }
                    }
                });

                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
                return true;
            }
        });


        if (switchButtonStatus==1){
            holder.switchButton.setChecked(true);
            Intent intent=new Intent(context, NotificationReceiver.class);
            intent.putExtra("notificationRequestCode",  allNotes.get(position).getId());
            alarmIntent= PendingIntent.getBroadcast(context,
                    allNotes.get(position).getId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
            //get ALARM_SERVICE from SystemService
            alarm= (AlarmManager) context.getSystemService(ALARM_SERVICE);
            //alarm set
            alarm.set(AlarmManager.RTC_WAKEUP,Long.parseLong(allNotes.get(position).getEndTime()),alarmIntent);
            //alarm.cancel(alarmIntent);

        }
        else if (switchButtonStatus==0){
            Intent intent=new Intent(context, NotificationReceiver.class);
            alarmIntent= PendingIntent.getBroadcast(context,
                    allNotes.get(position).getId(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
            alarm= (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarm.cancel(alarmIntent);

            holder.switchButton.setChecked(false);

            // alarm.set(AlarmManager.RTC_WAKEUP,Long.parseLong(allNotes.get(position).getEndTime()),alarmIntent);
        }else {

        }

        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();

                    long status = databaseHelper.updateData(new Notes(allNotes.get(position).getId(),
                            allNotes.get(position).getStartTime(),allNotes.get(position).getEndTime(),allNotes.get(position).getNotificationRequestCode(),
                            allNotes.get(position).getNotificationId(),1));
                    if (status == 1){
                        allNotes.clear();
                        allNotes.addAll(databaseHelper.getAllNotes());
                       // notifyDataSetChanged();
                        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else {
                    }
                }else{
                    long status = databaseHelper.updateData(new Notes(allNotes.get(position).getId(),
                            allNotes.get(position).getStartTime(),allNotes.get(position).getEndTime(),allNotes.get(position).getNotificationRequestCode(),
                            allNotes.get(position).getNotificationId(),0));
                    if (status == 1){
                        allNotes.clear();
                        allNotes.addAll(databaseHelper.getAllNotes());
                        //notifyDataSetChanged();
                        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else {
                    }
                    Toast.makeText(context, "uncheck", Toast.LENGTH_SHORT).show();
                    // The toggle is disabled
                }
            }
        });

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView,endTimeTextView,requestTextView;
        Switch switchButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView= itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView= itemView.findViewById(R.id.endTimeTextView);
             requestTextView= itemView.findViewById(R.id.requestCodeTextViewId);
            switchButton= itemView.findViewById(R.id.switchButtonId);
        }
    }


}
