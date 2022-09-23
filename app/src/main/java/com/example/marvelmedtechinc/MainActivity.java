package com.example.marvelmedtechinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.marvelmedtechinc.databinding.ActivityMainBinding;
import com.example.marvelmedtechinc.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    String mVerificationId, otp;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.countCode.registerCarrierNumberEditText(binding.phoneNo);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.sendBtn.getText().equals("SEND OTP")) {
                    binding.phoneView.setVisibility(View.GONE);
                    binding.otpView.setVisibility(View.VISIBLE);
                    binding.sendBtn.setText("VALIDATE");
                    String mobileno = binding.countCode.getFullNumberWithPlus().replace(" ", "");
                    Toast.makeText(MainActivity.this, "" + mobileno, Toast.LENGTH_SHORT).show();

                    initiateOtp(mobileno);
                } else {
                    if (binding.no1.getText().toString().isEmpty() || binding.no2.getText().toString().isEmpty()
                            || binding.no3.getText().toString().isEmpty() || binding.no4.getText().toString().isEmpty()
                            || binding.no5.getText().toString().isEmpty() || binding.no6.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    otp = binding.no1.getText().toString() +
                            binding.no2.getText().toString() +
                            binding.no3.getText().toString() +
                            binding.no4.getText().toString() +
                            binding.no5.getText().toString() +
                            binding.no6.getText().toString();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });

    }

    private void initiateOtp(String mobileno) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(mobileno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificationId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(MainActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    Toast.makeText(MainActivity.this, "SMS Quota Exceeded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    Users users = new Users();
                    users.setUserid(user.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("uid", user.getUid());

                    firebaseFirestore.collection("Users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()) {
                                if (value.get("is_doctor").equals("false")) {
                                    firebaseFirestore.collection("Users").document(user.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            if(value.get("home_lat")!=null) {
                                                startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                                            }
                                            else{
                                                startActivity(new Intent(MainActivity.this, HomeAddressActivity.class));
                                            }
                                        }
                                    });
                                } else {
                                    startActivity(new Intent(MainActivity.this, DoctorActivity.class));
                                }
                            } else {
                                hashMap.put("is_doctor", "false");
                                firebaseFirestore.collection("Users").document(user.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(MainActivity.this, HomeAddressActivity.class));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}