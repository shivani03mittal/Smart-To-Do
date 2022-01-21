package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TaskLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView searchimg;
    EditText searchtext;
    String location;
    DatabaseReference mDatabase;
    FirebaseDatabase db;
    FirebaseAuth fa;
    FirebaseUser curr_user;
    TaskSettings ts;
    String mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_location);
        searchimg = findViewById(R.id.search_iv);
        searchtext=findViewById(R.id.search);
        ts = new TaskSettings();
        boolean haspermission= (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED);
        if(!haspermission)
        {
            String[] permissionarr= {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permissionarr,PackageManager.PERMISSION_GRANTED);
//            ActivityCompat.requestPermissions(this,String[] {Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
        }
//        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.comtainer,mapFragment).commit();

        mapFragment.getMapAsync(this);

        searchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location= searchtext.getText().toString();
                if(location ==null)
                {
                    Toast.makeText(TaskLocation.this, "Mark Task Location", Toast.LENGTH_SHORT).show();
                }
                else {
                    Geocoder geocoder1= new Geocoder(TaskLocation.this, Locale.getDefault());
                    try{
                        List<Address> addressList1= geocoder1.getFromLocationName(location,1);
                        if(addressList1.size()>0)
                        {
                            LatLng latLng1= new LatLng(addressList1.get(0).getLatitude(),addressList1.get(0).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            mMap.addMarker(markerOptions.position(latLng1).title(location));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                            mMap.setMaxZoomPreference(30.0f);
                            mMap.setMinZoomPreference(6.0f);
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        }
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat= location.getLatitude();
                double lng= location.getLongitude();

                Geocoder geocoder= new Geocoder(getBaseContext(), Locale.getDefault());
                String countryname="", add="";
                try {
                    List<Address> addressList= geocoder.getFromLocation(lat,lng,1);
                    Address address= addressList.get(0);

                    add= address.getAddressLine(0);
                    countryname= address.getCountryName();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                LatLng latLng = new LatLng(lat,lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions.position(latLng).title(add));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.setMaxZoomPreference(30.0f);
                mMap.setMinZoomPreference(6.0f);
                // Zoom in, animating the camera.
//                mMap.animateCamera(CameraUpdateFactory.zoomIn());
//
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        };

        LocationManager locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
        try
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,4,locationListener);
        }
        catch(SecurityException e)
        { e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tasklocationmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(location ==null)
                {
                    Toast.makeText(TaskLocation.this, "Mark Task Location", Toast.LENGTH_SHORT).show();
                }
                else {
                    fa = FirebaseAuth.getInstance();
                    curr_user = fa.getCurrentUser();
                    mail = curr_user.getUid();
                    String tid = ts.gettaskid();
                    Log.d("FFF", ""+tid);
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").child(mail).child("Tasks").child(tid).child("TaskLocation").setValue(location);
                    Intent locationintent = new Intent(this,HomeActivity.class);
                    startActivity(locationintent);
                    Toast.makeText(this,"Task Location Added",Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}