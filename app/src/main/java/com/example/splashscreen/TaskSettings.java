package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TaskSettings extends AppCompatActivity {

    //    RecyclerView recyclerView;
//    ArrayList taskSettingImg = new ArrayList<>(Arrays.asList(
//       R.drawable.task,R.drawable.task, R.drawable.calendar,
//       R.drawable.clock,R.drawable.priority,
//       R.drawable.link, R.drawable.location,R.drawable.task
//    ));
//    Context context;
//    public void UsersAdapter(Context context) {
//        this.context = context;
//    }
    String currentDate;
    int hour,min,year,month,date;
    String Task_Time;
    String Location;
    String priority;
    String tname,ttime,tdesc,tdate,taskid,updated_name;
    String tid;
    static String idtask;
    String is_completed;
    static String tlink;
    TextView Taskname,TaskDesc,TaskDate,TaskTime,TaskPriority,TaskLink,TaskLocation,TaskStatus;
    DatabaseReference mDatabase;
    String userid;
    FirebaseUser curr_user;

    String Copy_link;
    ImageView copyLink,map_button;
    //    ArrayList taskSettingName = new ArrayList<>();
//    ArrayList taskSettingDesc = new ArrayList<>(Arrays.asList("Task Name", "Task Description", "Task date", "Task time", "Task priority", "Task link", "Task location","Task Status"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_settings);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        curr_user = FirebaseAuth.getInstance().getCurrentUser();
        userid = curr_user.getUid();


        Taskname = findViewById(R.id.Taskname);
        TaskDesc = findViewById(R.id.taskDescription);
        TaskDate = findViewById(R.id.taskDate);
        TaskTime = findViewById(R.id.taskTime);
        TaskPriority = findViewById(R.id.taskPriority);
        TaskLink = findViewById(R.id.taskLink);
        TaskLocation = findViewById(R.id.taskLocation);
        TaskStatus = findViewById(R.id.taskStatus);
        copyLink = findViewById(R.id.copyLink);
        map_button = findViewById(R.id.map_button);

        tname = getIntent().getStringExtra("name");
        tdesc = getIntent().getStringExtra("Desc");
        ttime = getIntent().getStringExtra("Time");
        tdate = getIntent().getStringExtra("Date");
        tid = getIntent().getStringExtra("tid");
        tlink = getIntent().getStringExtra("Link");
        priority = getIntent().getStringExtra("Priority");
        is_completed = getIntent().getStringExtra("Status");
        Location = getIntent().getStringExtra("Location");
        idtask = tid;

        Copy_link = tlink;

        Taskname.setText(tname);
        TaskDesc.setText(tdesc);
        TaskTime.setText(ttime);
        TaskDate.setText(tdate);
        TaskPriority.setText(priority);
        TaskLink.setText(tlink);
        TaskStatus.setText(is_completed);
        TaskLocation.setText(Location);


//        taskSettingName.add(tname);
//        taskSettingName.add(tdesc);
//        taskSettingName.add(tdate);
//        taskSettingName.add(ttime);
//        taskSettingName.add(tid);
//        taskSettingName.add(tlink);
//        taskSettingName.add("Add location");
//        taskSettingName.add(is_completed);
//        try{
//            Log.d("lol", updated_name);
//        }
//        catch (NullPointerException n){
//
//        }
//        if(!updated_name.isEmpty()){
//            taskSettingName.set(0,updated_name);
//        }
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewtask);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        TaskSettingsAdapter taskSettingsAdapter = new TaskSettingsAdapter(TaskSettings.this, taskSettingImg, taskSettingName, taskSettingDesc);
//        recyclerView.setAdapter(taskSettingsAdapter);

        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyLink(view);
                Toast.makeText(getApplicationContext(),"Link Copied !!",Toast.LENGTH_SHORT).show();
            }
        });

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(TaskSettings.this, TaskLocation.class);
                startActivity(mapIntent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        menu.add("Delete");
//        return super.onCreateOptionsMenu(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.share:

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                    // type of the content to be shared
                    sharingIntent.setType("text/plain");

                    // Body of the content
//
                    StringBuilder bld = new StringBuilder();
                    bld.append("Task:- ");

//                    for (int i = 0; i < list.size(); i++) {
                        bld.append("\r\n"+"Taskname:"+tname).append("\r\n"+"Task Description:"+tdesc).append("\r\n"+"Due date:"+tdate).append(ttime).append("\r\n"+tlink);
//                    }

                    String str = bld.toString();
                    Log.d("allname",str);
//                    String tasknamelist= list.get(0).TaskName;
//                    Log.d("taskfetch",tasknamelist);
//                    String shareBody = "Your Body Here";

                    // subject of the content. you can share anything
                    String shareSubject = "Task Details";

                    // passing body of the content
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, str);

                    // passing subject of the content
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    startActivity(Intent.createChooser(sharingIntent, "Share Tasks using"));

//                    Toast.makeText(this, "No task to share!", Toast.LENGTH_SHORT).show();
                break;

//
        }
        if(item.getTitle()=="Delete") {
//            if (list.size() != 0) {
//                Toast.makeText(this, "Delete all ", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder= new AlertDialog.Builder(TaskSettings.this);
            builder.setMessage("Delete task?")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference deleteall= FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("Tasks").child(tid);
//                    .child("profileImages")
//                    .child(uid+".jpeg");
                            deleteall.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(TaskSettings.this, "Task removed", Toast.LENGTH_SHORT).show();
                                            Intent listIntent = new Intent(TaskSettings.this, HomeActivity.class);
                                            startActivity(listIntent);

                                        }
                                    });
                        }
                    }).setNegativeButton("CANCEL",null);
            AlertDialog alertDialog= builder.create();
            alertDialog.show();

//            }
//            else
//            {
//                Toast.makeText(this, "No task to delete", Toast.LENGTH_SHORT).show();
//            }
        }

        return super.onOptionsItemSelected(item);
    }



    private void startDialog(int task_number, String tid) {
        Update_Dialog update_dialog = new Update_Dialog(task_number,tid);
        if(task_number==0){
            update_dialog.show(this.getSupportFragmentManager(), "Edit Task Name");
        }
        else if(task_number==1){
            update_dialog.show(this.getSupportFragmentManager(), "Edit Task Description");
        }
        else if (task_number==2){
            update_dialog.show(this.getSupportFragmentManager(), "Edit Task Priority");
        }
        else if(task_number==3){
            update_dialog.show(this.getSupportFragmentManager(), "Edit Task Link");
        }
        else if(task_number==4){
            update_dialog.show(this.getSupportFragmentManager(), "Edit Task Location");
        }

//        dialog_link.task = tname;
//        Taskname.setText(tname);
    }
    private void startDialog(int task_number,String tid,boolean dialog_need){

    }
    public void startTaskPage() {

        Intent logIntent = new Intent(TaskSettings.this, TaskSettings.class);
//        logIntent.putExtra("username", String.valueOf(userEdit));
//        logIntent.putExtra("password", String.valueOf(passEdit));
        startActivity(logIntent);

    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
                hour = selectedHour;
                min = selectedMin;
                String time = hour+":"+min;
                SimpleDateFormat f24hour =  new SimpleDateFormat("HH:mm");
                try{
                    Date date = f24hour.parse(time);
                    SimpleDateFormat f12hour = new SimpleDateFormat("HH:mm aa");
                    Task_Time = f12hour.format(date);


                }
                catch (ParseException e){
                    e.printStackTrace();
                }
                mDatabase.child("users").child(userid).child("Tasks").child(tid).child("Time").setValue(Task_Time);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,onTimeSetListener,hour,min,false);
        timePickerDialog.show();
    }

    public void popDatePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,date);
                currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                mDatabase.child("users").child(userid).child("Tasks").child(tid).child("TaskDate").setValue(currentDate);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,onDateSetListener,2021,month,date);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    public void popTaskName(View view){
        startDialog(0,tid);
    }
    public void popTaskDesc(View view){
        startDialog(1,tid);
    }
    public void popTaskPriority(View view){
        startDialog(2,tid);
    }
    public void popTaskLink(View view){
        startDialog(3,tid);
    }
    public void popTaskLocation(View view){
        startDialog(4,tid);
    }

    public void copyLink(View view) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(Copy_link);
//            Toast.makeText(this,"Link Copied !!",Toast.LENGTH_SHORT);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", Copy_link);
            clipboard.setPrimaryClip(clip);

        }

    }
    public String gettaskid(){
        return idtask;
    }


}