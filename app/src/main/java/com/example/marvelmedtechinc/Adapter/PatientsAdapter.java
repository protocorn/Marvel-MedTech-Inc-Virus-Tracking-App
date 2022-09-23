package com.example.marvelmedtechinc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelmedtechinc.ChatActivity;
import com.example.marvelmedtechinc.DoctorActivity;
import com.example.marvelmedtechinc.R;
import com.example.marvelmedtechinc.VaccinationActivity;

import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder>{
    Context context;
    List<String> list;

    public PatientsAdapter(List<String> list, DoctorActivity context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public PatientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsAdapter.ViewHolder holder, int position) {
        holder.name.setText(list.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("uid",list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
        }
    }
}
