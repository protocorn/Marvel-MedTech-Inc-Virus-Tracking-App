package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.marvelmedtechinc.Adapter.AppointmentsAdapter;
import com.example.marvelmedtechinc.Adapter.VirusUpdatesAdapter;
import com.example.marvelmedtechinc.databinding.ActivityVaccineAppointmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VaccineAppointmentsActivity extends AppCompatActivity {
    ActivityVaccineAppointmentsBinding binding;
    FirebaseAuth auth;
    ArrayList<ArrayList> list=new ArrayList<>();
    FirebaseFirestore firebaseFirestore;
    AppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityVaccineAppointmentsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Appointment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    ArrayList<String> arrayList=new ArrayList<>();
                    arrayList.add(""+snapshot.get("Name"));
                    arrayList.add(""+snapshot.get("Vacc_Centre"));
                    arrayList.add(""+snapshot.get("Date"));
                    arrayList.add(""+snapshot.get("Time"));
                    arrayList.add(""+snapshot.get("Vacc_Type"));

                    list.add(arrayList);
                }
                adapter = new AppointmentsAdapter(list, VaccineAppointmentsActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VaccineAppointmentsActivity.this);
                binding.appointmentList.setLayoutManager(linearLayoutManager);
                binding.appointmentList.setAdapter(adapter);
            }
        });
    }
}