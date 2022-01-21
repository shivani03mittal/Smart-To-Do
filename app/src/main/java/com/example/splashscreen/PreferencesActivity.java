package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class PreferencesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList optionImg = new ArrayList<>(Arrays.asList(
            R.drawable.notif, R.drawable.alarm, R.drawable.hide_comp,
            R.drawable.font_size,
//            R.drawable.theme,
            R.drawable.rating, R.drawable.priv_pol, R.drawable.blank
    ));
    ArrayList optionName = new ArrayList<>(Arrays.asList(
            "Notifications", "Alarm", "Hide Completed", "Text Size",
//            "Theme",
            "Rate us", "Privacy policy", "Version"
    ));
    ArrayList optionDesc = new ArrayList<>(Arrays.asList("Set Notification preferences", "Set Alarm preferences", " ", "Set text size",
//            "Set App theme",
            " ", "Read our privacy policy","0.1"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewpref);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(PreferencesActivity.this, optionImg, optionName, optionDesc);
        recyclerView.setAdapter(adapter);





    }
}