package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.example.splashscreen.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference df;
    HomeActivity hm;
    String mail;
    ArrayList<NameLocation> nameLocations;
    static ArrayList<String> TaskID;
    ArrayList<String> Name;

//    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        mail= firebaseUser.getUid();
        hm = new HomeActivity();
        Name = new ArrayList<String>();
        TaskID = hm.Task_Id;
        nameLocations = new ArrayList<NameLocation>();

        df = FirebaseDatabase.getInstance().getReference("users").child(mail).child("Tasks");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameLocations.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){


//                    Task_Id.add(dataSnapshot.getKey());
                    NameLocation model=  dataSnapshot.getValue(NameLocation.class);
                    Name.add(model.TaskLocation);
                    nameLocations.add(model);
                    Log.d("SSS", ""+nameLocations.size());
                    Log.d("SSS", ""+model.TaskLocation);

//                    Log.d("VVV", ""+nameLocations.get().TaskLocation);

                }
//                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mail).child("Tasks");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("Username profile", snapshot.getValue(String.class));
//                String username= snapshot.getValue(String.class).toString();
//                Log.d("useriner check",username);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        boolean haspermission= (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED);
        if(!haspermission)
        {
            String[] permissionarr= {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permissionarr,PackageManager.PERMISSION_GRANTED);
//            ActivityCompat.requestPermissions(this,String[] {Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
        }
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        for(int i=0;i<nameLocations.size();++i){
//            Log.d("DDD","a");
//            Log.d("CCC", ""+nameLocations.get(i).TaskLocation);
            if(nameLocations.get(i).TaskLocation!=null){
                String location = nameLocations.get(i).TaskLocation;
                String Taskname = "";
//                Log.d("CCC", ""+location);
//                Log.d("CCC", ""+Taskname);
                Geocoder geocoder1= new Geocoder(this, Locale.getDefault());
                try{
                    List<Address> addressList1= geocoder1.getFromLocationName(location,1);
                    if(addressList1.size()>0)
                    {
                        LatLng latLng1= new LatLng(addressList1.get(0).getLatitude(),addressList1.get(0).getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mMap.addMarker(markerOptions.position(latLng1).title(Taskname).snippet(location));
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





    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
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
}