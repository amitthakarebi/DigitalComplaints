package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminViewComplaint extends AppCompatActivity {

    private TextView vFullName, vMobileNo, vLocation, vCity, vDescription,vStatus;
    private Button reviewBtn, processingBtn, completeBtn;
    private String fullName , mobileNo , location, city , description, image,uid,status;
    private ImageView vImage,toolBackViewComplaint;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        vFullName = findViewById(R.id.VFullName);
        vMobileNo = findViewById(R.id.VMobileNo);
        vLocation = findViewById(R.id.VLocation);
        vCity = findViewById(R.id.VCity);
        vDescription = findViewById(R.id.VDescription);
        vImage = findViewById(R.id.VImage);
        vStatus = findViewById(R.id.VStatus);

        toolBackViewComplaint = findViewById(R.id.toolbar_back_view_complaint);
        toolBackViewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reviewBtn = findViewById(R.id.vReview);
        processingBtn = findViewById(R.id.vProcessingBtn);
        completeBtn = findViewById(R.id.vCompleteBtn);
        getIntentData();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusFirebase("In Review");
            }
        });

        processingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusFirebase("In Processing");
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusFirebase("Completed");
            }
        });

    }

    private void setStatusFirebase(final String newStatus)
    {
        firebaseDatabase.getReference("Complaints").child(Variables.Department).child(uid).child("Status").setValue(newStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AdminViewComplaint.this, "Status Changed!", Toast.LENGTH_SHORT).show();
                            status = newStatus;
                            vStatus.setText(newStatus);

                            if (newStatus.equals("Completed"))
                            {
                                firebaseDatabase.getReference("Complaints").child(Variables.Department).child(uid)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    int random_int = (int)(Math.random() * (999999999 - 1000000 + 1) + 1000000);

                                                    Map<String,Object> complaintData = new HashMap<>();
                                                    complaintData.put("Image",image);
                                                    complaintData.put("Full_Name",fullName);
                                                    complaintData.put("Mobile_No",mobileNo);
                                                    complaintData.put("Department",Variables.Department);
                                                    complaintData.put("Address",location);
                                                    complaintData.put("City",city);
                                                    complaintData.put("Description",description);
                                                    complaintData.put("Status",newStatus);

                                                    firebaseDatabase.getReference("Complaints").child("Completed").child(random_int+"").setValue(complaintData)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(AdminViewComplaint.this, "Added To Completed!", Toast.LENGTH_SHORT).show();
                                                                    }else
                                                                    {
                                                                        Toast.makeText(AdminViewComplaint.this, "error", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }

                        }else
                        {
                            Toast.makeText(AdminViewComplaint.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getIntentData() {

        Intent intent = getIntent();
        fullName = intent.getStringExtra("name");
        mobileNo = intent.getStringExtra("mobile");
        location = intent.getStringExtra("location");
        city = intent.getStringExtra("city");
        description = intent.getStringExtra("description");
        image = intent.getStringExtra("image");
        uid = intent.getStringExtra("uid");
        status = intent.getStringExtra("status");
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        vFullName.setText(fullName);
        vMobileNo.setText(mobileNo);
        vLocation.setText(location);
        vCity.setText(city);
        vDescription.setText(description);
        vStatus.setText(status);
        Glide.with(this).load(image).into(vImage);

    }
}