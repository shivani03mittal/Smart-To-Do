package com.example.splashscreen;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Model> list;
    static ArrayList<String> Task_Id;
    UUID userId;
    String currentDate;

    SimpleDateFormat sdf;
    String currentDateandTime;
    BottomSheet b = new BottomSheet();
    private DrawerLayout drawer ;
    private NavigationView navigationView;

    TextView textNotifItemCount;
    int mNotifItemCount = 10;
    FloatingActionButton floatadd;
    FloatingActionButton bottomsheet, mapbtn;
    FloatingActionButton dateButton;
    
    Calendar c;

    private static Context mContext;

//    @Override
//    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
//        c = Calendar.getInstance();
//        c.set(Calendar.YEAR,year);
//        c.set(Calendar.MONTH,month);
//        c.set(Calendar.DAY_OF_MONTH,date);
//        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//        Log.d("DATE::",currentDate);
//        b.date = currentDate;
//
////        Intent settingsIntent = new Intent(this, BottomSheet.class);
////        settingsIntent.putExtra("reminderYear",""+year);
////        settingsIntent.putExtra("reminderMonth",""+month);
////        settingsIntent.putExtra("reminderDate",""+date);
////        startActivity(settingsIntent);
//
//        b.nday = ""+date;
//        b.nmonth = ""+month;
//        b.nyear = ""+year;
//    }
    DatabaseReference mDatabase,databaseReference;
    FirebaseDatabase db;
    FirebaseAuth fa;
    FirebaseUser curr_user;
    ImageView listSettings;
    ImageView calendar_date;
    String mail;

    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        boolean haspermission=(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED);
        if(!haspermission)
        {
            String[] permissionarr= {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permissionarr, PackageManager.PERMISSION_GRANTED);
        }

        boolean haspermission2= (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED);
        if(!haspermission2)
        {
            String[] permissionarr2= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permissionarr2,PackageManager.PERMISSION_GRANTED);
        }

        mContext = getApplicationContext();



        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mapbtn = findViewById(R.id.mapbutton);
        sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        currentDateandTime = sdf.format(new Date());
//        dateButton = findViewById(R.id.notif);
//        floatadd = findViewById(R.id.floatingadd);

        Toolbar toolbar = findViewById(R.id.toolbar);


        fa = FirebaseAuth.getInstance();
        curr_user = fa.getCurrentUser();
        mail = curr_user.getUid();

        Task_Id = new ArrayList<>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView =  navigationView.getHeaderView(0);
        ImageView profileimage= (ImageView) hView.findViewById(R.id.prof_img);
        TextView profiletext=(TextView) hView.findViewById(R.id.nav_text);

        if(curr_user != null) {
            if (curr_user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(curr_user.getPhotoUrl())
                        .into(profileimage);
            }
            else {
                profileimage.  setImageResource(R.drawable.personprofile);
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mail).child("UserName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("Username profile", snapshot.getValue(String.class));
                String username= snapshot.getValue(String.class).toString();
//                Log.d("useriner check",username);
                profiletext.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_profile:
                        Toast.makeText(HomeActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                        Intent profileintent = new Intent(HomeActivity.this, ProfilePage.class);
                        startActivity(profileintent);
                        break;
                    case R.id.settings:
                        Intent i1 = new Intent(HomeActivity.this, PreferencesActivity.class);
                        startActivity(i1);
                        break;

                    case R.id.theme:
                        if ((AppCompatDelegate.getDefaultNightMode())!=(AppCompatDelegate.MODE_NIGHT_NO))
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        else
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;

                    case R.id.logout:

                        FirebaseAuth.getInstance().signOut();
                        Intent logIntent = new Intent(HomeActivity.this, LogInActivity.class);
                        startActivity(logIntent);
                        Toast.makeText(mContext, "Logged out", Toast.LENGTH_SHORT).show();
                        break;



                }

                return false;
            }



        });



        userId = (UUID) UUID.randomUUID();
        bottomsheet = findViewById(R.id.list_settings);
        recyclerView = findViewById(R.id.recycler_tasks);
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(mail).child("Tasks");
        calendar_date = findViewById(R.id.task_date);

        bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showDialog();
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"Tag");
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Model>();
        myAdapter = new MyAdapter(HomeActivity.this,list);
        recyclerView.setAdapter(myAdapter);
        EventChangeListner();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openmaps();
            }
        });

        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        createNotificationChannel();



    }

    public void openmaps() {
        boolean hasmappermission= (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED);
        if(!hasmappermission)
        {
            String[] permissionarr= {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this,permissionarr,PackageManager.PERMISSION_GRANTED);
//            ActivityCompat.requestPermissions(this,String[] {Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
        }
        Intent mapintent= new Intent(HomeActivity.this,MapsActivity.class);
        startActivity(mapintent);
    }

    Model deleted_task = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            int sz = Task_Id.size();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    deleted_task = list.get(pos);
                    list.remove(pos);
                    myAdapter.notifyItemRemoved(pos);
                    deleteTask(mail,Task_Id.get(sz-pos-1));
                    break;
//                case ItemTouchHelper.RIGHT:
//                    list.remove(pos);
//                    myAdapter.notifyItemRemoved(pos);
//                    deleteTask(mail,Task_Id.get(sz-pos-1));
//                    break;
            }
        }
    };

    private void deleteTask (String Uid,String taskid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(Uid).child("Tasks").child(taskid);
        reference.removeValue();
        Toast.makeText(this,"Task Deleted",Toast.LENGTH_SHORT).show();
    }


    private void EventChangeListner() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Task_Id.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Log.d("Nnn", ""+dataSnapshot.getKey());
                    Task_Id.add(dataSnapshot.getKey());
                    Model model=  dataSnapshot.getValue(Model.class);
                    list.add(0,model);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        for (int i=0;i<list.size();i++){
//            Log.d("FFF",list.get(i).TaskName);
//        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    public void writeNewUser(String currentDateandTime, String name, String taskdesc) {
//        user newuser = new user();
        mDatabase.child("users").child(mail).child("Tasks").child(currentDateandTime).child("taskname").setValue(name);

    }


    public void startSettingsActivity() {
        Intent settingsIntent = new Intent(HomeActivity.this, PreferencesActivity.class);
        startActivity(settingsIntent);
    }

    public void startListSettings() {
        Intent listIntent = new Intent(HomeActivity.this, ListSettings.class);
        startActivity(listIntent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
              this.finishAffinity();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.top_menu, menu);
        menu.add("Delete all");
//        final MenuItem menuItem = menu.findItem(R.id.share);
//
//        View actionView = menuItem.getActionView();
////        textNotifItemCount = (TextView) actionView.findViewById(R.id.notif_icon);
//
////        setupBadge();
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onOptionsItemSelected(menuItem);
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.share:
                if(list.size() !=0){
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                    // type of the content to be shared
                    sharingIntent.setType("text/plain");

                    // Body of the content
//
                    StringBuilder bld = new StringBuilder();
                    bld.append("Tasks:- ");

                    for (int i = 0; i < list.size(); i++) {
                        bld.append("\r\n"+list.get(i).TaskName);
                    }

                    String str = bld.toString();
                    Log.d("allname",str);
                    String tasknamelist= list.get(0).TaskName;
                    Log.d("taskfetch",tasknamelist);
                    String shareBody = "Your Body Here";

                    // subject of the content. you can share anything
                    String shareSubject = "Your Subject Here";

                    // passing body of the content
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, str);

                    // passing subject of the content
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    startActivity(Intent.createChooser(sharingIntent, "Share Tasks using"));}
                else {
                    Toast.makeText(this, "No task to share!", Toast.LENGTH_SHORT).show();

                }
                break;

//
        }
        if(item.getTitle()=="Delete all") {
            if (list.size() != 0) {
                Toast.makeText(this, "Delete all ", Toast.LENGTH_SHORT).show();
//            DatabaseReference deleteall= FirebaseStorage.getInstance().getReference()
//                    .child("profileImages")
//                    .child(uid+".jpeg");


                AlertDialog.Builder builder= new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Delete all tasks?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(HomeActivity.this, "All tasks removed", Toast.LENGTH_SHORT).show();
                            }
                        });
                            }
                        }).setNegativeButton("CANCEL",null);
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
//                mDatabase.removeValue()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(HomeActivity.this, "All tasks removed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
            else
            {
                Toast.makeText(this, "No task to delete", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }




//    private void setupBadge() {
//        if(textNotifItemCount != null){
//            if (mNotifItemCount == 0){
//                if (textNotifItemCount.getVisibility() != View.GONE){
//                    textNotifItemCount.setVisibility(View.GONE);
//                }
//            } else{
//                textNotifItemCount.setText(String.valueOf(Math.min(mNotifItemCount, 99)));
//                if (textNotifItemCount.getVisibility() != View.VISIBLE){
//                    textNotifItemCount.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//    }



    public void setAlarm( String day, String month, String year, String hour, String minute) {
//        Context c = getApplicationContext();

        Intent intent = new Intent(mContext, DeviceBootReceiver.class);
        intent.setAction(DeviceBootReceiver.ACTION_ALARM);

        Calendar now = Calendar.getInstance();

        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext,
                ((int) now.getTimeInMillis()), intent, 0);

        Toast.makeText(mContext, String.valueOf(now.getTimeInMillis()), Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) mContext.
                getSystemService(mContext.ALARM_SERVICE);


        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, Integer.parseInt(String.valueOf(year)));
        startTime.set(Calendar.MONTH, Integer.parseInt(String.valueOf(month))-1);
        startTime.set(Calendar.DATE, Integer.parseInt(String.valueOf(day)));
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(hour)));
        startTime.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(minute)));
        startTime.set(Calendar.SECOND, 1);

        long time;
        if (now.before(startTime)) {
            time = startTime.getTimeInMillis();
        } else {
            startTime.add(Calendar.DATE, 1);
            time = startTime.getTimeInMillis();
        }

        alarmManager.setExact(AlarmManager.RTC, time, alarmIntent);




    }

    private void createNotificationChannel(){
        if (SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "NotifChannel";
            String description = "Channel for notif reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifID",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}