package com.example.marvelmedtechinc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelmedtechinc.R;
import com.example.marvelmedtechinc.VaccineAppointmentsActivity;
import com.example.marvelmedtechinc.VirusUpdatesActivity;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {
    Context context;
    ArrayList<ArrayList> list;

    public AppointmentsAdapter(ArrayList<ArrayList> list, VaccineAppointmentsActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointments_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList = list.get(position);
        holder.name.setText(arrayList.get(0));
        holder.vacc_c.setText(arrayList.get(1));
        holder.date.setText(arrayList.get(2));
        holder.time.setText(arrayList.get(3));
        holder.vacc_typ.setText(arrayList.get(4));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, vacc_c, date, time, vacc_typ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            vacc_c = itemView.findViewById(R.id.vacc_centre);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            vacc_typ= itemView.findViewById(R.id.vacc_type);
        }
    }
}

