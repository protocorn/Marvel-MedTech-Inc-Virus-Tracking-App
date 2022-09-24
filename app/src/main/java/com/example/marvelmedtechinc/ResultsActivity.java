package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.marvelmedtechinc.databinding.ActivityResultsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ResultsActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    ActivityResultsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityResultsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();


        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String scr=value.get("assessment_score").toString();
                binding.score.setText(scr);

                int int_scr= Integer.parseInt(scr);

                if(int_scr>=0 && int_scr<=10){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.is1);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.ds2);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.vt1);
                }
                else if(int_scr>=11 && int_scr<=30){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.is1);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.ds2);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.vt2);
                }
                else if(int_scr>=31 && int_scr<=50){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.is2);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.ds3);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).into(binding.vt2);
                }
                else if(int_scr>=51 && int_scr<=70){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.is4);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.ds4);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.vt3);
                }
                else if(int_scr>=71 && int_scr<=100){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.is5);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.ds4);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.vt4);
                }
                else if(int_scr>100){
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.is5);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.ds5);
                    Glide.with(ResultsActivity.this).load(R.drawable.check).override(60,60).into(binding.vt5);
                }
            }
        });
    }
}