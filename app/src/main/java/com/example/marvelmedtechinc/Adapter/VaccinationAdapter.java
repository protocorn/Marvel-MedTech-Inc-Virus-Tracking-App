package com.example.marvelmedtechinc.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelmedtechinc.BookingActivity;
import com.example.marvelmedtechinc.R;
import com.example.marvelmedtechinc.VaccinationActivity;
import com.example.marvelmedtechinc.VirusUpdatesActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.ViewHolder> {
    Context context;
    List<String> list;

    public VaccinationAdapter(List<String> list, VaccinationActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VaccinationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vacc_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccinationAdapter.ViewHolder holder, int position) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        holder.name.setText(list.get(position));
        firebaseFirestore.collection("Vaccination Center").document(list.get(position)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                double lt = Double.parseDouble("" + value.get("Latitude"));
                double ln = Double.parseDouble("" + value.get("Longitude"));

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lt, ln, 1);
                    holder.address.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BookingActivity.class);
                intent.putExtra("vacc_c",list.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        Button sub_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vacc_name);
            address = itemView.findViewById(R.id.vacc_add);
            sub_btn = itemView.findViewById(R.id.book_btn);

        }
    }
}
