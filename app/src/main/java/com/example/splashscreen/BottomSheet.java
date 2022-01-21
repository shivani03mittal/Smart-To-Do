package com.example.splashscreen;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class BottomSheet extends BottomSheetDialogFragment  {
    public BottomSheet(){

    }


    HomeActivity homeActivity;
    String currentDateTime;
    SimpleDateFormat sdf;
    TaskSettings taskSettings;


    static String date;
    String currentDate;
    private DatabaseReference mDatabase;

    static int thour,tmin;
    String Task_Time;
    static String link_task;
    static String link_meet;
    FirebaseAuth fb;
    String mail;
    FirebaseUser curr_user;

    long millis;


    String year1, month1, day1;
    String compDate, time1;

    TextView dateTV, timeTV;

    String checkBox="Not Completed";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view1 = inflater.inflate(R.layout.bottomsheetlayout,container,false);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        view1.startAnimation(hyperspaceJumpAnimation);
        Button Savebtn = view1.findViewById(R.id.task_save);
        ImageView calendar_date = view1.findViewById(R.id.task_date);
        ImageView Time = view1.findViewById(R.id.task_time);

        homeActivity = new HomeActivity();
        dateTV = view1.findViewById(R.id.dateTV);
        timeTV = view1.findViewById(R.id.timeTV);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        currentDateTime = sdf.format(new Date());

        Log.d("SHUBH ",currentDateTime);

        fb = FirebaseAuth.getInstance();
        curr_user = fb.getCurrentUser();
        mail = curr_user.getUid();
        Log.d("uid", ""+mail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView time = view1.findViewById(R.id.task_time);
        ImageView meet_link = view1.findViewById(R.id.task_link);
        meet_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });


        calendar_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int day =c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,day);
                        year1 = ""+year;
                        month1 = ""+month;
                        day1 = ""+day;
                        compDate = day1+"-"+month1+"-"+year1;
                        Log.d("Acha sa tag:",compDate);
                        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                        dateTV.setText(currentDate);


                    }
                },year,month,day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
//                    Log.d("normal:",""+year+year1+month+month1+day+day1);

            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
                        thour = selectedHour;
                        tmin = selectedMin;
                        String time = thour + ":" + tmin;
                        time1 = thour + ":" + tmin;
                        SimpleDateFormat f24hour = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24hour.parse(time);
                            Log.d("DATE_",time);
                            SimpleDateFormat f12hour = new SimpleDateFormat("HH:mm aa");
                            Task_Time = f12hour.format(date);
                            timeTV.setText(time);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),onTimeSetListener,thour,tmin,false);
                timePickerDialog.show();
            }
        });

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskname = view1.findViewById(R.id.Task_Name);
                EditText taskdesc = view1.findViewById(R.id.Task_Desc);
//                HomeActivity hm = new HomeActivity();
//                EditText taskname = view.findViewById(R.id.task_name);
                String tname,tdesc;


                if (currentDate!=null && time!=null)
                {
//                    Log.d("DATE::>",nday+nmonth+nyear);

                    setAlarm( day1, month1, year1, String.valueOf(thour), String.valueOf(tmin));
                }



                if(currentDate==null){
                    currentDate = " ";
                }


                if(Task_Time==null){
                    Task_Time = " ";
                }


//                Log.d("TASKTIME",Task_Time);
                if(!TextUtils.isEmpty(taskname.getText().toString().trim())){
                    tname = taskname.getText().toString().trim();
                    if(!TextUtils.isEmpty(taskdesc.getText().toString().trim())){
                        tdesc = taskdesc.getText().toString().trim();
                        HomeActivity h = new HomeActivity();

                        if(!TextUtils.isEmpty(link_meet)){
                            if(!TextUtils.isEmpty(link_task)){
                                taskSettings.tlink = link_task;
                                writeNewUser(currentDateTime,tname,tdesc,""+currentDate,""+Task_Time,""+link_meet,""+link_task,checkBox);
                            }
                            else{
                                writeNewUser(currentDateTime,tname,tdesc,""+currentDate,""+Task_Time,""+link_meet,checkBox);
                            }
                        }
                        else{
                            if(!TextUtils.isEmpty(link_task)){
                                taskSettings.tlink = link_task;
                                mwriteNewUser(currentDateTime,tname,tdesc,""+currentDate,""+Task_Time,""+link_task,checkBox);
                            }
                            else{
                                mtwriteNewUser(currentDateTime,tname,tdesc,""+currentDate,""+Task_Time,checkBox);
                            }
                        }
                        Toast.makeText(getContext(),"Task Added Successfully",Toast.LENGTH_LONG).show();
                    }
                    else{
                        tdesc = taskdesc.getText().toString().trim();
                        if(!TextUtils.isEmpty(link_meet)){
                            if(!TextUtils.isEmpty(link_task)){
                                dwriteNewUser(currentDateTime,tname,""+currentDate,""+Task_Time,""+link_meet,""+link_task,checkBox);
                            }
                            else{
                                dwriteNewUser(currentDateTime,tname,""+currentDate,""+Task_Time,""+link_meet,checkBox);
                            }
                        }
                        else{
                            if(!TextUtils.isEmpty(link_task)){
                                mdwriteNewUser(currentDateTime,tname,""+currentDate,""+Task_Time,""+link_task,checkBox);
                            }
                            else{
                                mtdwriteNewUser(currentDateTime,tname,""+currentDate,""+Task_Time,checkBox);
                            }
                        }
                        Toast.makeText(getContext(),"Task Added Successfully",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getContext(),"Enter Task Details",Toast.LENGTH_LONG).show();
                }
                Animation hyperspaceJumpAnimation1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
                view1.startAnimation(hyperspaceJumpAnimation1);
//                startHomePage();
            }
        });

        return view1;




    }

    public void setAlarm( String day, String month, String year, String hour, String minute) {
//        Context c = getApplicationContext();

        String toParse = compDate + " " + time1; // Results in "2-5-2012 20:43"
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
        try {
            Date date = formatter.parse(toParse);
            millis = date.getTime();
            Log.d("MILLIS",""+millis);// You will need try/catch around this
        }catch (ParseException p){

        }


        Intent intent = new Intent(getContext(), DeviceBootReceiver.class);
        intent.setAction(DeviceBootReceiver.ACTION_ALARM);

        Calendar now = Calendar.getInstance();

        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),
                ((int) millis), intent, 0);

        Toast.makeText(getContext(), String.valueOf(now.getTimeInMillis()), Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);


//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.YEAR, Integer.parseInt(String.valueOf(year)));
//        startTime.set(Calendar.MONTH, Integer.parseInt(String.valueOf(month))-1);
//        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(String.valueOf(day)));
//        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(hour)));
//        startTime.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(minute)));
//        startTime.set(Calendar.SECOND, 1);



//        long time;
//        if (now.before(startTime)) {
//            time = startTime.getTimeInMillis();
//        } else {
//            startTime.add(Calendar.DATE, 1);
//            time = startTime.getTimeInMillis();
//        }
//        Log.d("TIME_",""+time);

        alarmManager.setExact(AlarmManager.RTC, millis, alarmIntent);


    }

    private void startDialog() {
        Dialog_Link dialog_link = new Dialog_Link();
        dialog_link.show(getActivity().getSupportFragmentManager(), "Edit Task Details");
    }

    public void writeNewUser(String currentDateTime, String name, String taskdesc,String taskdate,String Time,String checkBox) {
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
    }
    public void writeNewUser(String currentDateTime, String name, String taskdesc,String taskdate,String Time,String meet,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("meet").setValue(meet);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }

    public void writeNewUser(String currentDateTime, String name, String taskdesc,String taskdate,String Time,String meet,String ltask,String checkBox){

        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("meet").setValue(meet);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("ltask").setValue(ltask);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);

    }
    public void mtwriteNewUser(String currentDateTime, String name, String taskdesc,String taskdate,String Time,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }
    public void mwriteNewUser(String currentDateTime, String name, String taskdesc,String taskdate,String Time,String ltask,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("ltask").setValue(ltask);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }
    public void mtdwriteNewUser(String currentDateTime, String name,String taskdate,String Time,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
//        mDatabase.child("users").child(mail).child(userId.toString()).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }
    public void mdwriteNewUser(String currentDateTime, String name,String taskdate,String Time,String ltask,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
//        mDatabase.child("users").child(mail).child(userId.toString()).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("ltask").setValue(ltask);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }
    public void dwriteNewUser(String currentDateTime, String name,String taskdate,String Time,String meet,String ltask,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
//        mDatabase.child("users").child(mail).child(userId.toString()).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("meet").setValue(meet);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("ltask").setValue(ltask);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("checkboxStatus").setValue(checkBox);
    }
    public void dwriteNewUser(String currentDateTime, String name,String taskdate,String Time,String meet,String checkBox){
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskName").setValue(name);
//        mDatabase.child("users").child(mail).child(userId.toString()).child("Description").setValue(taskdesc);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("TaskDate").setValue(taskdate);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("Time").setValue(Time);
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateTime).child("meet").setValue(meet);
    }

    public String sendCurrentDate(){
        return currentDateTime;
    }




}