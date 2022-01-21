package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ListSettings extends AppCompatActivity {

    Button colorButton;
    RelativeLayout colorWindow;
    Button nextListButton;
    int defaultColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_settings);

        colorButton = findViewById(R.id.color_button);
        colorWindow = findViewById(R.id.color_layout);

        nextListButton = findViewById(R.id.next_list);

        defaultColor = ContextCompat.getColor(ListSettings.this, R.color.gradient_end);


        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        nextListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTaskSettings();
            }
        });

    }

    public void startTaskSettings() {
        Intent nextListInt = new Intent(ListSettings.this, TaskSettings.class);
        startActivity(nextListInt);
    }

    public void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                    defaultColor = color;
                    colorWindow.setBackgroundColor(defaultColor);
            }
        });
        ambilWarnaDialog.show();
    }
}