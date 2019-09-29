package com.hackathon.bachelor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainMaps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Menu menu;

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavigationView navview;

    boolean bat = true;
    boolean tou = false;

    public Spinner spinner;

    MenuItem Houseitem;
    MenuItem JobItem;
    MenuItem Hostelitem;

    LatLng mylatlong;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    DatabaseReference databaseReference;

    ArrayList<ArrayList<String>> markerlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);

        toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navview = findViewById(R.id.nav);

        spinner = findViewById(R.id.mainspin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolspin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        markerlist = new ArrayList<ArrayList<String>>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                Log.d("TAG",item);
                switch (item) {
                    case "Batchelor":;

                    case "Tourist": ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;

                switch (menuItem.getItemId()) {
                    case R.id.Rhouse:
                        intent = new Intent(MainMaps.this, RentHouse.class);
                        startActivity(intent);
                        return true;

                    case R.id.Job:
                        intent = new Intent(MainMaps.this, Job.class);
                        startActivity(intent);
                        return true;

                    case R.id.Hostel:
                        intent = new Intent(MainMaps.this, Hostel.class);
                        startActivity(intent);
                        return true;

                }
                return false;
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60000);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
        Log.v("TAG", locationResult.toString());
        locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.v("loca",location.getLatitude()+" "+location.getLongitude());
                    mylatlong = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatlong,15));
                }
            }
        });

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                displayall();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mapsidemenu,menu);

        this.menu = menu;



        // MenuItem item = menu.findItem(R.id.tspin);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {


        return super.onPrepareOptionsMenu(menu);
    }

    public void displayall(){

        Log.d("Tag","sssssss");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG","hghh");
                for(DataSnapshot list : dataSnapshot.getChildren()){
                    ArrayList<String> ltm = new ArrayList<String>();
                    for(DataSnapshot minilist : list.getChildren()){
                        ltm.add(minilist.child("Sno").getValue().toString());
                        ltm.add(minilist.child("location").child("latitude").getValue().toString());
                        ltm.add(minilist.child("location").child("longitude").getValue().toString());
                    }
                    markerlist.add(ltm);
                }

                Log.d("SIZE",markerlist.toString());
                for(int i=0;i<markerlist.size();i++){
                    LatLng lt = new LatLng(Double.parseDouble(markerlist.get(i).get(1)),Double.parseDouble(markerlist.get(i).get(2)));
                    mMap.addMarker(new MarkerOptions().position(lt).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
