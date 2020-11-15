package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {

    private EditText editTextMobileOtp, editTextOtpOtp;
    private Button sendOtpBtn, verifyOtpBtn;
    FirebaseAuth firebaseAuth;
    private TextView otpSentText;
    private String phoneNumber, emailId, password;
    private String otpId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        initialize();

    }

    private void initialize() {

        editTextMobileOtp = findViewById(R.id.editTextMobileOtp);
        editTextOtpOtp = findViewById(R.id.editTextOtpOtp);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        otpSentText = findViewById(R.id.sentOtpText);
        verifyOtpBtn.setClickable(false);
        Intent intent = getIntent();
        String mobileno = intent.getStringExtra("mobile");
        Toast.makeText(this, mobileno, Toast.LENGTH_SHORT).show();
        phoneNumber = "+91" + mobileno.trim();
        emailId = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        editTextMobileOtp.setText(phoneNumber);

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtpBtn.setClickable(false);
                otpSentText.setVisibility(View.VISIBLE);
                sendOtpBtn.setBackgroundColor(getResources().getColor(R.color.faint_blue));
                verifyOtpBtn.setClickable(true);
                initiateOtp();
            }
        });

        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OtpVerification.this, "Verifying Otp...", Toast.LENGTH_SHORT).show();
                verifyOtpBtn.setClickable(false);
                verifyOtpBtn.setBackgroundColor(getResources().getColor(R.color.faint_blue));
                if (!TextUtils.isEmpty(editTextOtpOtp.getText())) {
                    if (editTextOtpOtp.getText().length() == 6) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, editTextOtpOtp.getText().toString());
                        signInWithPhoneAuthCredential(credential);

                    } else {
                        Toast.makeText(OtpVerification.this, "Invalid Otp!", Toast.LENGTH_SHORT).show();
                        verifyOtpBtn.setClickable(true);
                        verifyOtpBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                } else {
                    Toast.makeText(OtpVerification.this, "Please Enter Otp!", Toast.LENGTH_SHORT).show();
                    verifyOtpBtn.setClickable(true);
                    verifyOtpBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }
        });
    }


    private void initiateOtp() {
        Toast.makeText(this, "Sending Otp...", Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                //super.onCodeSent(s, forceResendingToken);
                                otpId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(OtpVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(OtpVerification.this, "Signin Success!", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();

                            Intent intent = new Intent(OtpVerification.this, Home.class);
                            startActivity(intent);
                            finish();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(OtpVerification.this, "Signin Failure!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}