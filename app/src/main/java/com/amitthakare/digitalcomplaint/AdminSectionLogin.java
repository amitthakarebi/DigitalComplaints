package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminSectionLogin extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private Button adminSignInBtn;
    private TextView adminForgetPassword;
    private ProgressDialog progressDialogSignInAdmin;
    private ArrayList<String> emails;
    private ArrayList<String> departments;
    private int childrenCount;
    private boolean logged=false;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_section_login);

        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        adminForgetPassword = findViewById(R.id.adminForgetPassword);
        adminSignInBtn = findViewById(R.id.adminSignInBtn);
        adminForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSectionLogin.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

        adminSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogSignInAdmin = new ProgressDialog(AdminSectionLogin.this);
                progressDialogSignInAdmin.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialogSignInAdmin.setTitle("Checking User Info...");
                progressDialogSignInAdmin.show();
                if (!TextUtils.isEmpty(adminEmail.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(adminEmail.getText().toString()).matches())
                {
                    if (!TextUtils.isEmpty(adminPassword.getText().toString()))
                    {
                        //getEmailAndDepartment();
                        //adminSignIn();
                        if (emails.contains(adminEmail.getText().toString()))
                        {
                            int i = emails.indexOf(adminEmail.getText().toString());
                            adminSignIn2(emails.get(i),departments.get(i));
                        }else
                        {
                            Toast.makeText(AdminSectionLogin.this, "Incorrect Details!", Toast.LENGTH_SHORT).show();
                            progressDialogSignInAdmin.dismiss();
                        }

                        /*for (int i=0;i<childrenCount;i++)
                        {
                            if (!logged)
                            {
                                Toast.makeText(AdminSectionLogin.this, "loop", Toast.LENGTH_SHORT).show();
                                adminSignIn2(emails.get(i),departments.get(i));
                            }else
                                break;
                        }*/

                    }else
                    {
                        adminPassword.setError("Enter Valid Password!");
                        progressDialogSignInAdmin.dismiss();
                    }
                }else
                {
                    adminEmail.setError("Enter Valid Email!");
                    progressDialogSignInAdmin.dismiss();
                }
            }
        });

    }

    private void getEmailAndDepartment() {
        emails = new ArrayList<>();
        departments = new ArrayList<>();
        Toast.makeText(AdminSectionLogin.this, "database", Toast.LENGTH_SHORT).show();

        firebaseDatabase.getReference("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childrenCount = (int) snapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    //Toast.makeText(AdminSectionLogin.this, dataSnapshot.getKey()+" | "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                    emails.add((String) dataSnapshot.getValue());
                    departments.add(dataSnapshot.getKey());
                }
                //Toast.makeText(AdminSectionLogin.this, emails+" | "+departments, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void adminSignIn() {

        if (adminEmail.getText().toString().equals("kajallodha165@gmail.com") && adminPassword.getText().toString().equals("asdfasdf"))
        {

            Intent intent = new Intent(AdminSectionLogin.this,AdminHome.class);
            intent.putExtra("department","Muncipal Council");
            progressDialogSignInAdmin.dismiss();
            startActivity(intent);
            finish();

            /*firebaseAuth.signInWithEmailAndPassword(adminEmail.getText().toString(),adminPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                            }else
                            {
                                Toast.makeText(AdminSectionLogin.this, task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/

        }else if (adminEmail.getText().toString().equals("kalyanijat41@gmail.com"))
        {
            Intent intent = new Intent(AdminSectionLogin.this,AdminHome.class);
            intent.putExtra("department","Ministry Of Power");
            progressDialogSignInAdmin.dismiss();
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(this, "Incorrect Details!", Toast.LENGTH_SHORT).show();
            progressDialogSignInAdmin.dismiss();
        }

    }

    private void adminSignIn2(String email, final String department){
        if (adminEmail.getText().toString().equals(email))
        {
            firebaseAuth.signInWithEmailAndPassword(adminEmail.getText().toString(),adminPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(AdminSectionLogin.this,AdminHome.class);
                                intent.putExtra("department",department);
                                progressDialogSignInAdmin.dismiss();
                                startActivity(intent);
                                logged=true;
                                finish();
                            }else
                                Toast.makeText(AdminSectionLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialogSignInAdmin.dismiss();
                        }
                    });
        }else
        {
            Toast.makeText(this, "Incorrect Details!", Toast.LENGTH_SHORT).show();
            progressDialogSignInAdmin.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getEmailAndDepartment();


    }
}