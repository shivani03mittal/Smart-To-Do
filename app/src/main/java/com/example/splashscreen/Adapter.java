package com.example.splashscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList optionImg, optionName, optionDesc;
    Context context;


    public Adapter(Context context, ArrayList optionImg, ArrayList optionName, ArrayList optionDesc){
        this.context = context;
        this.optionImg = optionImg;
        this.optionName = optionName;
        this.optionDesc = optionDesc;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferences_list_item, parent, false);


        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view, context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        int res = (int) optionImg.get(position);
        holder.images.setImageResource(res);
        holder.text.setText((String) optionName.get(position));
        holder.desc.setText((String) optionDesc.get(position));


    }

    @Override
    public int getItemCount() {
        return optionImg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView images;
        TextView text, desc;

        SharedPreferences preferences = context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final boolean isDarkModeOn = preferences.getBoolean("isDarkModeOn",false);


        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            images = itemView.findViewById(R.id.optionImg);
            text = itemView.findViewById(R.id.optionName);
            desc = itemView.findViewById(R.id.optionDesc);
            text.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            switch (position) {
                case 0:
                    Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "Hide completed", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(context, "text size", Toast.LENGTH_SHORT).show();
                    break;
//                case 4:
////                    showThemeSelection();
//
//                    if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO) {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                        desc.setText("NIGHT MODE");
//                    }
//                    else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                        desc.setText("LIGHT MODE");
//                    }
//                    Toast.makeText(context, "theme", Toast.LENGTH_SHORT).show();
//                    break;
                case 4:
                    Toast.makeText(context, "rating", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(context, "privacy policy", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(context, "version", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

//        public void showThemeSelection() {
//            int choice;
////            if (preferences.get)
//            if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO) {
//                choice = 0;
//            }
//            else
//                choice = 1;
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            alertDialog.setTitle("Theme");
//            String[] items = {"Light", "Dark"};
//            alertDialog.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    switch (i) {
//                        case 0:
//
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//                            editor.putBoolean("isDarkModeOn",false);
//                            Toast.makeText(context, "Light theme", Toast.LENGTH_SHORT).show();
//                            break;
//                        case 1:
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            editor.putBoolean("isDarkModeOn",true);
//                            Toast.makeText(context, "Dark theme", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//            }).setPositiveButton("Set theme",null);
//            AlertDialog alert = alertDialog.create();
//            alert.setCanceledOnTouchOutside(true);
//            try {
//                alert.show();
//            } catch (){
//
//            }
//        }
    }
}
