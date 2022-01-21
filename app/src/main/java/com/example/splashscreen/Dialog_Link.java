package com.example.splashscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.core.view.View;

public class Dialog_Link extends AppCompatDialogFragment {
    private EditText tasklink;
    private EditText meetlink;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        android.view.View view = inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(view).setTitle("Add Task Links").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tasklink = view.findViewById(R.id.linkTask);
//                meetlink = view.findViewById(R.id.MeetLink);
                BottomSheet b = new BottomSheet();
//                b.link_meet = meetlink.toString().trim();
                b.link_task = tasklink.getText().toString().trim();
//                Log.d("TaskLink", tasklink.getText().toString().trim());

            }
        });

        return builder.create();
    }
}
