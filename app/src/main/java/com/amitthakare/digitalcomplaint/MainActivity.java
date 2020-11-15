package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private Button signUpHomeBtn;
    private Button signInHomeBtn;
    private Button adminSectionBtn;
    private EditText editTextEmailMain, editTextPasswordMain;
    FirebaseAuth firebaseAuth;

    private TextView resetPasswordTextView;

    private ProgressDialog progressDialogSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetPasswordTextView = findViewById(R.id.resetPasswordMain);
        resetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

        signUpHomeBtn = findViewById(R.id.signUpHomeBtn);
        signInHomeBtn = findViewById(R.id.signInHomeBtn);
        adminSectionBtn = findViewById(R.id.adminSectionBtn);
        editTextEmailMain = findViewById(R.id.editTextEmailMain);
        editTextPasswordMain = findViewById(R.id.editTextPasswordMain);
        firebaseAuth = FirebaseAuth.getInstance();

        signInHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,Home.class);
                //startActivity(intent);

                signInHomeBtn.setClickable(false);
                signInHomeBtn.setBackgroundColor(getResources().getColor(R.color.faint_blue));

                progressDialogSignIn = new ProgressDialog(MainActivity.this);
                progressDialogSignIn.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialogSignIn.setTitle("Checking User Info...");
                progressDialogSignIn.show();

                if (!TextUtils.isEmpty(editTextEmailMain.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(editTextEmailMain.getText().toString()).matches())
                {
                    if (!TextUtils.isEmpty(editTextPasswordMain.getText().toString()))
                    {
                        firebaseAuth.signInWithEmailAndPassword(editTextEmailMain.getText().toString(),editTextPasswordMain.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this,Home.class);
                                            startActivity(intent);
                                            finish();
                                        }else
                                        {
                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialogSignIn.dismiss();
                                            signInHomeBtn.setClickable(true);
                                            signInHomeBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                                        }
                                    }
                                });
                    }else
                    {
                        editTextPasswordMain.setError("Enter Valid Password!");
                        progressDialogSignIn.dismiss();
                        signInHomeBtn.setClickable(true);
                        signInHomeBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                }else
                {
                    editTextEmailMain.setError("Enter Valid Email!");
                    progressDialogSignIn.dismiss();
                    signInHomeBtn.setClickable(true);
                    signInHomeBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                }


            }
        });

        signUpHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        adminSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AdminSectionLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Set the message show for the Alert time
        builder.setMessage("Click Yes to exit!");
        builder.setIcon(R.drawable.digital_complaint_logo);

        // Set Alert Title
        builder.setTitle("Do you want to Exit?");

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
                                finish();
                                System.exit(0);
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