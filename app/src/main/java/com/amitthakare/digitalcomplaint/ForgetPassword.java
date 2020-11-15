package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText email_reset;
    private Button reset_btn;
    private String email_validation = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
    private TextView sentTextView;

    private ImageView backTool;

    // Firebase Variables
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        firebaseAuth=FirebaseAuth.getInstance();
        sentTextView = findViewById(R.id.sentTextView);

        backTool = findViewById(R.id.toolbar_back_forget_password);
        backTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email_reset=findViewById(R.id.emailReset);
        reset_btn=findViewById(R.id.resetPasswordBtn);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_reset.getText().toString().matches(email_validation))
                {
                    firebaseAuth.sendPasswordResetEmail(email_reset.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sentTextView.setVisibility(View.VISIBLE);
                                    Toast.makeText(ForgetPassword.this, "Email Sent Successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgetPassword.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}