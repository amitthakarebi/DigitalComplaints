package com.amitthakare.digitalcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EmergencyServices extends AppCompatActivity {

    private ImageView callPolice, callAmbulance, callFire, callDomestic, callSuicide, callAntiPoison, callDaminiPathak;
    private ImageView toolBackEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_services);

        callPolice = findViewById(R.id.callPolice);
        callAmbulance = findViewById(R.id.callAmbulance);
        callFire = findViewById(R.id.callFire);
        callDomestic = findViewById(R.id.callDomestic);
        callSuicide = findViewById(R.id.callSuicide);
        callAntiPoison = findViewById(R.id.callAntiPoison);
        callDaminiPathak = findViewById(R.id.callDaminiPathak);

        toolBackEmergency = findViewById(R.id.toolbar_back_emergency);
        toolBackEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07232242500"));
                startActivity(callIntent);
            }
        });

        callAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9373770145"));
                startActivity(callIntent);
            }
        });

        callFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07232256715"));
                startActivity(callIntent);
            }
        });

        callDomestic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07232256715"));
                startActivity(callIntent);
            }
        });

        callSuicide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07232256715"));
                startActivity(callIntent);
            }
        });

        callAntiPoison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:07232256715"));
                startActivity(callIntent);
            }
        });

        callDaminiPathak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:02532200495"));
                startActivity(callIntent);
            }
        });


    }


}