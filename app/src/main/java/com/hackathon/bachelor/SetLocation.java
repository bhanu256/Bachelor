package com.hackathon.bachelor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SetLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;
    public LatLng mylatlong;
    public LocationRequest locationRequest;
    public Toolbar toolbar;

    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference,locationrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("my", Context.MODE_PRIVATE);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60000);


        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        this.mMap = googleMap;

        Task<Location> locationResult = locationProviderClient.getLastLocation();
        Log.v("TAG", locationResult.toString());
        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.v("loca",location.getLatitude()+" "+location.getLongitude());
                    mylatlong = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatlong,18));
                }
            }
        });

        // Add a marker in Sydney and move the camera
        if(mMap!=null){

        }
    }

    public void tick(View view){


        LatLng lt = mMap.getCameraPosition().target;
        Log.d("Loc",lt.toString());

        Intent intent = getIntent();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("houseInt",1);
        editor.putInt("job",1);
        editor.putInt("hostel",1);
        editor.commit();
        //DatabaseReference locationrefer;
        String type = intent.getStringExtra("Where");
        if(type=="RentHouses" || type.equals("RentHouses")) {
            Log.d("TAG",intent.getStringExtra("Where"));
            String mail = sharedPreferences.getString("id",null);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("RentHouses").child(mail);
            locationrefer = FirebaseDatabase.getInstance().getReference().child("RentHouses").child(mail).child("location");
            Map<String,Object> data = new HashMap<String,Object>();
            Map<String,Object> loc = new HashMap<String,Object>();
            data.put("rest",intent.getStringExtra("rest"));
            data.put("advance",intent.getStringExtra("advance"));
            data.put("Sno",(sharedPreferences.getInt("houseInt",1)+1));
            Log.d("TAG",data.toString());
            databaseReference.updateChildren(data);
            //data.clear();
            loc.put("latitude",lt.latitude);
            loc.put("longitude",lt.longitude);
            locationrefer.updateChildren(loc);
        }

        if(type=="FullJobs" || type.equals("FullJobs")) {
            Log.d("TAG",intent.getStringExtra("Where"));
            String mail = sharedPreferences.getString("id",null);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FullJobs").child(mail);
            locationrefer = FirebaseDatabase.getInstance().getReference().child("FullJobs").child(mail).child("location");
            Map<String,Object> data = new HashMap<String,Object>();
            Map<String,Object> loc = new HashMap<String,Object>();
            data.put("salary",intent.getStringExtra("salary"));
            data.put("desc",intent.getStringExtra("desc"));
            data.put("req",intent.getStringExtra("req"));
            data.put("Sno",(sharedPreferences.getInt("job",1)+1));
            Log.d("TAG",data.toString());
            databaseReference.updateChildren(data);
            //data.clear();
            loc.put("latitude",lt.latitude);
            loc.put("longitude",lt.longitude);
            locationrefer.updateChildren(loc);
        }

        if(type=="Hostel" || type.equals("Hostel")) {
            Log.d("TAG",intent.getStringExtra("Where"));
            String mail = sharedPreferences.getString("id",null);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Hostel").child(mail);
            locationrefer = FirebaseDatabase.getInstance().getReference().child("Hostel").child(mail).child("location");
            Map<String,Object> data = new HashMap<String,Object>();
            Map<String,Object> loc = new HashMap<String,Object>();
            data.put("rentPD",intent.getStringExtra("rentPD"));
            data.put("max",intent.getStringExtra("max"));
            data.put("Sno",(sharedPreferences.getInt("hostel",1)+1));
            Log.d("TAG",data.toString());
            databaseReference.updateChildren(data);
            //data.clear();
            loc.put("latitude",lt.latitude);
            loc.put("longitude",lt.longitude);
            locationrefer.updateChildren(loc);
        }

        Intent intent1 = new Intent(this,Tick.class);
        startActivity(intent1);
    }
}
