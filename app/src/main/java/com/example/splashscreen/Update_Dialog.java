package com.example.splashscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Update_Dialog extends AppCompatDialogFragment {
    //    private TextView Taskname,TaskDesc,TaskDate,TaskTime,TaskPriority,TaskLink,TaskLocation,TaskStatus;
//    TextView field_name;
    EditText updated_task;
    int task_number;
    DatabaseReference mDatabase;
    String userid;
    FirebaseUser curr_user;
    FirebaseAuth fb;
    String task,tid;
    TaskSettings ts;
    public Update_Dialog(int task_number,String tid){
        this.task_number = task_number;
        this.tid = tid;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        android.view.View view = inflater.inflate(R.layout.update_dialog,null);
        ts = new TaskSettings();
        updated_task = view.findViewById(R.id.dialog_update);
        if (task_number == 0) {
            builder.setView(view).setTitle("Edit Task Name");
        }
        else if (task_number == 1) {
            builder.setView(view).setTitle("Edit Task Description");
        }
        else if (task_number == 2) {
            builder.setView(view).setTitle("Edit Task Priority");
        }
        else if (task_number == 3) {
            builder.setView(view).setTitle("Edit Task Link");
        }
        else if (task_number == 4) {
            builder.setView(view).setTitle("Edit Task Location");
        }


        builder.setView(view).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fb = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                curr_user = fb.getCurrentUser();
                userid = curr_user.getUid();
                switch (task_number){
                    case 0:
                        task = updated_task.getText().toString().trim();
                        mDatabase.child("users").child(userid).child("Tasks").child(tid).child("TaskName").setValue(task);
                        break;
                    case 1:
                        task = updated_task.getText().toString().trim();
                        mDatabase.child("users").child(userid).child("Tasks").child(tid).child("Description").setValue(task);
                        break;
                    case 2:
                        task = updated_task.getText().toString().trim();
                        mDatabase.child("users").child(userid).child("Tasks").child(tid).child("Priority").setValue(task);
                        break;
                    case 3:
                        task = updated_task.getText().toString().trim();
                        mDatabase.child("users").child(userid).child("Tasks").child(tid).child("ltask").setValue(task);
                        break;
                    case 4:
                        task = updated_task.getText().toString().trim();
                        mDatabase.child("users").child(userid).child("Tasks").child(tid).child("TaskLocation").setValue(task);
                        break;
                }
                if(task_number!=0 && task_number!=1 && task_number!=2 && task_number!=3 && task_number!=4){
                    mDatabase.child("users").child(userid).child("Tasks").child(tid).child("TaskDate").setValue(task);
                    updated_task.setText(task);
                }
            }
        });

        return builder.create();
    }
    public void startTaskPage() {

        Intent logIntent = new Intent(getContext(), TaskSettings.class);
//        logIntent.putExtra("TaskName", String.valueOf(task));
//        logIntent.putExtra("password", String.valueOf(passEdit));

        startActivity(logIntent);

    }
}
