package com.example.marvelmedtechinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.marvelmedtechinc.Adapter.VaccinationAdapter;
import com.example.marvelmedtechinc.Adapter.VirusUpdatesAdapter;
import com.example.marvelmedtechinc.databinding.ActivityVaccinationBinding;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VaccinationActivity extends AppCompatActivity {
    ActivityVaccinationBinding binding;
    FirebaseFirestore firebaseFirestore;
    List<String> list;

    VaccinationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityVaccinationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        firebaseFirestore=FirebaseFirestore.getInstance();
        list=new ArrayList<>();

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.slider.getVisibility()==View.VISIBLE){
                    binding.slider.setVisibility(View.GONE);
                }
                else if(binding.slider.getVisibility()==View.GONE){
                    binding.slider.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                //binding.slider.setVisibility(View.GONE);
                Location startPoint = new Location(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Location").document("Location").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        double lt1,ln1;
                        lt1 = Double.parseDouble("" + value.get("Latitude"));
                        ln1 = Double.parseDouble("" + value.get("Longitude"));
                        startPoint.setLatitude(lt1);
                        startPoint.setLongitude(ln1);
                    }
                });
                firebaseFirestore.collection("Vaccination Center").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            Location endPoint = new Location("" +snapshot.get("name"));
                            double lt1,ln1;
                            lt1 = Double.parseDouble("" + snapshot.get("Latitude"));
                            ln1 = Double.parseDouble("" + snapshot.get("Longitude"));
                            endPoint.setLatitude(lt1);
                            endPoint.setLongitude(ln1);

                            float distanceInMeters = startPoint.distanceTo(endPoint);
                            if(distanceInMeters<=binding.slider.getValue()){
                                list.add(""+snapshot.get("name"));
                            }
                            while(list.isEmpty()){
                                binding.slider.setValue(binding.slider.getValue()+500);
                                if(distanceInMeters<=binding.slider.getValue()){
                                    list.add(""+snapshot.get("name"));
                                }
                            }
                            if(!list.isEmpty()){
                                binding.imageView9.setVisibility(View.GONE);
                                binding.vaccList.setVisibility(View.VISIBLE);
                            }
                        }
                        adapter = new VaccinationAdapter(list, VaccinationActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VaccinationActivity.this);
                        binding.vaccList.setLayoutManager(linearLayoutManager);
                        binding.vaccList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}