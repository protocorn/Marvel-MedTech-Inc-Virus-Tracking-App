package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.marvelmedtechinc.Adapter.VirusUpdatesAdapter;
import com.example.marvelmedtechinc.databinding.ActivityVirusUpdatesBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VirusUpdatesActivity extends AppCompatActivity {

    ActivityVirusUpdatesBinding binding;
    ArrayList<ArrayList> list=new ArrayList<>();
    FirebaseFirestore firebaseFirestore;
    VirusUpdatesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityVirusUpdatesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();

        String[] states = new String[]{"Andaman & Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra & Nagar Haveli and Daman & Diu",
                "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshwadeep", "Madhya Pradesh", "Maharashtra","Manipur","Meghalaya", "Mizoram",
                "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};


        firebaseFirestore.collection("States").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int i=0;
                for (DocumentSnapshot snapshot: value.getDocuments()) {
                    ArrayList<String> arrayList=new ArrayList<>();
                    String act= ""+snapshot.get("active_cases");
                    String rec= ""+snapshot.get("recovered");
                    String dec= ""+snapshot.get("deceased");
                    arrayList.add(states[i]);
                    arrayList.add(act);
                    arrayList.add(rec);
                    arrayList.add(dec);
                    i=i+1;
                    list.add(arrayList);
                }
                adapter = new VirusUpdatesAdapter(list, VirusUpdatesActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VirusUpdatesActivity.this);
                binding.dataList.setLayoutManager(linearLayoutManager);
                binding.dataList.setAdapter(adapter);
            }
        });

        binding.searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }

            private void search(String newText) {
                Query query = firebaseFirestore.collection("States").orderBy("name").startAt(newText).endAt(newText + "\uf8ff");
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            ArrayList<String> arrayList=new ArrayList<>();
                            String act= ""+snapshot.get("active_cases");
                            String rec= ""+snapshot.get("recovered");
                            String dec= ""+snapshot.get("deceased");
                            arrayList.add(""+snapshot.get("name"));
                            arrayList.add(act);
                            arrayList.add(rec);
                            arrayList.add(dec);
                            list.add(arrayList);
                            adapter = new VirusUpdatesAdapter(list, VirusUpdatesActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VirusUpdatesActivity.this);
                            binding.dataList.setLayoutManager(linearLayoutManager);
                            binding.dataList.setAdapter(adapter);
                        }
                    }
                });
            }
        });

    }
}