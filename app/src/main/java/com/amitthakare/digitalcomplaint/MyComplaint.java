package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import  com.amitthakare.digitalcomplaint.Adapter.RecyclerAdapter;
import  com.amitthakare.digitalcomplaint.Model.RecyclerInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyComplaint extends AppCompatActivity {

    String fullName , mobileNo , location, city , description, image, status;

    List<RecyclerInfo> listComplaints;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    StorageReference storageReference;

    ImageView toolbarBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaint);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbarBack = findViewById(R.id.toolbar_back_my_complaint);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.myComplaintRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        setupListDatabase();

    }

    private void setupListDatabase() {
        listComplaints = new ArrayList<>();

        // here do the firebase loop and get data into adapter and show as the complaint

        DatabaseReference databaseReference =firebaseDatabase.getReference("Complaints");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        if (snapshot1.hasChild(firebaseAuth.getCurrentUser().getUid()))
                        {
                            for (DataSnapshot snapshot2 : snapshot1.child(firebaseAuth.getCurrentUser().getUid()).getChildren())
                            {
                                String key = snapshot2.getKey();
                                if (key.equals("Full_Name"))
                                {
                                    fullName = String.valueOf(snapshot2.getValue());
                                }else if (key.equals("Address"))
                                {
                                    location = String.valueOf(snapshot2.getValue());
                                }else if (key.equals("Mobile_No"))
                                {
                                    mobileNo = String.valueOf(snapshot2.getValue());
                                }else if (key.equals("City"))
                                {
                                    city = String.valueOf(snapshot2.getValue());
                                }else if (key.equals("Description"))
                                {
                                    description = String.valueOf(snapshot2.getValue());
                                }else if (key.equals("Image"))
                                {
                                    image = String.valueOf(snapshot2.getValue());
                                }else if(key.equals("Status"))
                                {
                                    status = String.valueOf(snapshot2.getValue());
                                }
                            }
                            RecyclerInfo recyclerInfo = new RecyclerInfo(fullName,mobileNo,location,description,image,city,status);
                            listComplaints.add(recyclerInfo);
                        }
                    }
                    recyclerAdapter= new RecyclerAdapter(getApplicationContext(),listComplaints);
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}