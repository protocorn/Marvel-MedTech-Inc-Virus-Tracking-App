package com.example.marvelmedtechinc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.marvelmedtechinc.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
    }
}