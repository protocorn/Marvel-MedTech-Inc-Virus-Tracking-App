package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.marvelmedtechinc.Adapter.ChipAdapter;
import com.example.marvelmedtechinc.databinding.ActivitySelfAssessmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelfAssessmentActivity extends AppCompatActivity {

    ActivitySelfAssessmentBinding binding;
    int virus_probability_score = 0;
    FirebaseFirestore firebaseFirestore;

    ArrayAdapter<String> adapter;
    List<String> list;
    ChipAdapter chipAdapter;
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySelfAssessmentBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chipAdapter = new ChipAdapter(list, SelfAssessmentActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelfAssessmentActivity.this);
        binding.stateRecycler.setLayoutManager(linearLayoutManager);
        binding.stateRecycler.setAdapter(chipAdapter);

        String[] states = new String[]{"Andaman & Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra & Nagar Haveli and Daman & Diu",
                "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshwadeep", "Madhya Pradesh", "Maharashtra","Manipur","Meghalaya", "Mizoram",
                "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};

        adapter = new ArrayAdapter<String>(SelfAssessmentActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, states);
        binding.stateList.setAdapter(adapter);

        binding.stateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.contains(binding.stateList.getText().toString())) {
                    binding.stateList.setText("");
                    Toast.makeText(SelfAssessmentActivity.this, "Already Added", Toast.LENGTH_SHORT).show();
                } else {
                    list.add(binding.stateList.getText().toString());
                    binding.stateList.setText("");
                    chipAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nextBtn.getText().equals("NEXT")) {
                    if (binding.cough.isChecked()) {
                        virus_probability_score += 10;
                    }
                    if (binding.fever.isChecked()) {
                        virus_probability_score += 20;
                    }
                    if (binding.bless.isChecked()) {
                        virus_probability_score += 30;
                    }
                    if (binding.headache.isChecked()) {
                        virus_probability_score += 10;
                    }
                    if (binding.cold.isChecked()) {
                        virus_probability_score += 10;
                    }
                    if (binding.soreThroat.isChecked()) {
                        virus_probability_score += 20;
                    }
                    binding.nextBtn.setText("next");
                    binding.asses1.setVisibility(View.GONE);
                    binding.asses2.setVisibility(View.VISIBLE);
                } else if (binding.nextBtn.getText().equals("next")) {
                    if (binding.diabetes.isChecked()) {
                        virus_probability_score += 20;
                    }
                    if (binding.hypertension.isChecked()) {
                        virus_probability_score += 10;
                    }
                    if (binding.lungDisorder.isChecked()) {
                        virus_probability_score += 30;
                    }
                    if (binding.kidneyDisorder.isChecked()) {
                        virus_probability_score += 10;
                    }
                    if (binding.asthma.isChecked()) {
                        virus_probability_score += 30;
                    }
                    binding.nextBtn.setText("nexT");
                    binding.asses2.setVisibility(View.GONE);
                    binding.asses3.setVisibility(View.VISIBLE);
                } else if (binding.nextBtn.getText().equals("nexT")) {
                    for (int i = 0; i < list.size(); i++) {
                        String state = list.get(i).replace(" ", "");
                        firebaseFirestore.collection("States").document(state).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String danger_value = "" + value.get("danger_level");
                                virus_probability_score += Integer.parseInt(danger_value);
                            }
                        });
                    }
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SelfAssessmentActivity.this);
                    builder.setMessage("Are Sure That The Data You Entered Is Correct").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.nextBtn.setText("Submit");
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                if (binding.nextBtn.getText().equals("Submit")) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("assessment_score", String.valueOf(virus_probability_score));
                    if(virus_probability_score>=0 && virus_probability_score<=30)
                    {
                        hashMap.put("self_test_result", "negative");
                    }
                    else{
                        hashMap.put("self_test_result", "positive");
                    }
                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SelfAssessmentActivity.this, "" + virus_probability_score, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SelfAssessmentActivity.this, ResultsActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
    }
}