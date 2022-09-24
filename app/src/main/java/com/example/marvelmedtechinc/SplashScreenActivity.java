package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.marvelmedtechinc.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getSupportActionBar().hide();

        CountDownTimer countDownTimer=new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (auth.getCurrentUser() != null) {
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.get("is_doctor").equals("false")) {
                                if(value.get("home_lat")!=null) {
                                    startActivity(new Intent(SplashScreenActivity.this, HomePageActivity.class));
                                }
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, DoctorActivity.class));
                            }
                            finish();
                        }
                    });
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }
        }.start();
    }
}