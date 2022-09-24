package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.marvelmedtechinc.databinding.ActivityBookingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    ActivityBookingBinding binding;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    String vacc_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityBookingBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        vacc_c= getIntent().getStringExtra("vacc_c");

        binding.vacCName.setText(vacc_c);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.TimeSlot, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.timeSlot.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this, R.array.Vaccine, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.vaccType.setAdapter(adapter1);


        binding.dateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                showDialog(111);
            }
        });

        binding.imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                showDialog(111);
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingActivity.this,HomePageActivity.class));
                finish();
            }
        });

        binding.confBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.dateApp.getText().toString().equals("Select Date")){
                    binding.dateApp.setError("Date Field Is Empty");
                    return;
                }
                else if(binding.nameP.getText().toString().isEmpty()){
                    binding.nameP.setError("Name Field Is Empty");
                    return;
                }
                else {
                    ProgressDialog progress=new ProgressDialog(BookingActivity.this);
                    progress.setTitle("Booking You Appointment");
                    progress.setMessage("Please Wait...");
                    progress.show();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Name", binding.nameP.getText().toString());
                    hashMap.put("Date", binding.dateApp.getText().toString());
                    hashMap.put("Time", binding.timeSlot.getSelectedItem().toString());
                    hashMap.put("Vacc_Type",binding.vaccType.getSelectedItem().toString());
                    hashMap.put("Vacc_Centre",vacc_c);
                    firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Appointment").document(binding.nameP.getText().toString()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progress.dismiss();
                            startActivity(new Intent(BookingActivity.this,HomePageActivity.class));
                            Toast.makeText(BookingActivity.this, "Your Appointment Has Been Booked Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            binding.dateApp.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            binding.dateApp.setTextColor(Color.BLACK);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==111){
            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, datePickerListener, year, month, day);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
            Date newDate = calendar.getTime();
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));
            datePickerDialog.getDatePicker().setMaxDate(newDate.getTime()+(1000*60*60*24*10));
            return datePickerDialog;
        }
        return null;
    }
}