package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.marvelmedtechinc.Adapter.PatientsAdapter;
import com.example.marvelmedtechinc.Adapter.VaccinationAdapter;
import com.example.marvelmedtechinc.databinding.ActivityDoctorBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {
    ActivityDoctorBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    HashMap<String, Object> hashMap = new HashMap<>();
    List<String> list;
    PatientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDoctorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        hashMap.put("status","online");
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Doctor_Details").document("Details").update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Patients").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            list.add(""+snapshot.get("uid"));
                        }
                        adapter = new PatientsAdapter(list, DoctorActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DoctorActivity.this);
                        binding.patList.setLayoutManager(linearLayoutManager);
                        binding.patList.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hashMap.put("status","online");
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Doctor_Details").document("Details").update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hashMap.put("status","offline");
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Doctor_Details").document("Details").update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

}