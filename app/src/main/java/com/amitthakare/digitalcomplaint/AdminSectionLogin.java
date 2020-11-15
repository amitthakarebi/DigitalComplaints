package com.amitthakare.digitalcomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminSectionLogin extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private Button adminSignInBtn;
    private TextView adminForgetPassword;
    private ProgressDialog progressDialogSignInAdmin;

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
                        adminSignIn();
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

    private void adminSignIn() {

        if (adminEmail.getText().toString().equals("kajallodha165@gmail.com") && adminPassword.getText().toString().equals("kajal@1234"))
        {
            Intent intent = new Intent(AdminSectionLogin.this,AdminHome.class);
            intent.putExtra("department","Muncipal Council");
            progressDialogSignInAdmin.dismiss();
            startActivity(intent);
            finish();

        }else if (adminEmail.getText().toString().equals("kalyanijat41@gmail.com") && adminPassword.getText().toString().equals("kalyani@1234"))
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
}