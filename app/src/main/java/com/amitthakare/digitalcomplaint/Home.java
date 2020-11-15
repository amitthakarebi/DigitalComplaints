package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amitthakare.digitalcomplaint.Model.RecyclerInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    FirebaseAuth firebaseAuth;

    private ImageView newComplaintImg, myComplaintImg, allComplaintImg, emergencyServicesImg;

    //to get user location
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigationAndToolbarSetup();
        initialize();

        newComplaintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getLocation();

                //Intent intent = new Intent(Home.this,NewComplaint.class);
                //startActivity(intent);

            }
        });

        myComplaintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyComplaint.class);
                startActivity(intent);
            }
        });

        allComplaintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AllComplaints.class);
                startActivity(intent);
            }
        });

        emergencyServicesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, EmergencyServices.class);
                startActivity(intent);
            }
        });

    }

    private void initialize() {
        newComplaintImg = findViewById(R.id.newComplaintImg);
        myComplaintImg = findViewById(R.id.myComplaintImg);
        allComplaintImg = findViewById(R.id.allComplaintImg);
        emergencyServicesImg = findViewById(R.id.emergencyServicesImg);
        firebaseAuth = FirebaseAuth.getInstance();


        //initialize location fusedlocationprovider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted get location

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize location
                    Location location = task.getResult();
                    if (location != null)
                    {
                        try {

                            //initialize geoCoder
                            Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                            //initialize address list
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                            //set Address to variable
                            //getFeatureName = Unnamed Road
                            //subLocality = Tulja Nagri
                            //getSubAdminArea = Yavatmal
                            //getAdminArea = Maharashtra
                            //getSubThoroughfare() = null
                            //getThoroughfare() = unnamed road


                            Variables.LATITUDE = addresses.get(0).getLatitude();
                            Variables.LONGITUDE = addresses.get(0).getLongitude();
                            Variables.COUNTRY = addresses.get(0).getCountryName();
                            Variables.ADDRESSLINE = addresses.get(0).getAddressLine(0);
                            Variables.LOCALITY = addresses.get(0).getLocality();
                            Variables.STATE = addresses.get(0).getAdminArea();
                            Variables.PINCODE = addresses.get(0).getPostalCode();


                            Toast.makeText(Home.this, Variables.ADDRESSLINE+" ", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Home.this,NewComplaint.class);
                            startActivity(intent);



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            //when permission denied
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void navigationAndToolbarSetup() {
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        //Tool Bar Work
        setSupportActionBar(toolbar);

        // Navigation Menu too and fro when button is click
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /* To set Icon on Navigation */
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_privacy_policy:
                Intent intent = new Intent(Home.this,PrivacyPolicy.class);
                startActivity(intent);
                break;
            case R.id.nav_aboutus:
                Intent intent1 = new Intent(Home.this,AboutUs.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        // Set the message show for the Alert time
        builder.setMessage("Click Yes to logout!");
        builder.setIcon(R.drawable.digital_complaint_logo);

        // Set Alert Title
        builder.setTitle("Do you want to Logout?");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                //finishAffinity();
                                //finish();
                                //System.exit(0);
                                firebaseAuth.signOut();
                                Intent intent = new Intent(Home.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

}