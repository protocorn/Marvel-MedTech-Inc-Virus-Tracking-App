package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.marvelmedtechinc.Adapter.DoctorAdapter;
import com.example.marvelmedtechinc.Adapter.VaccinationAdapter;
import com.example.marvelmedtechinc.databinding.ActivityConsultDoctorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultDoctorActivity extends AppCompatActivity {
    ActivityConsultDoctorBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    List<String> list;
    List<String> uid;
    DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityConsultDoctorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        list=new ArrayList<>();
        uid=new ArrayList<>();

        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    if(snapshot.get("is_doctor").equals("true")){
                        uid.add(""+snapshot.get("uid"));

                        firebaseFirestore.collection("Users").document(""+snapshot.get("uid")).collection("Doctor_Details").document("Details").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value.get("status").equals("online")){
                                    list.add(""+value.get("name"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
                adapter = new DoctorAdapter(list,uid, ConsultDoctorActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConsultDoctorActivity.this);
                binding.docList.setLayoutManager(linearLayoutManager);
                binding.docList.setAdapter(adapter);
            }
        });

    }
}