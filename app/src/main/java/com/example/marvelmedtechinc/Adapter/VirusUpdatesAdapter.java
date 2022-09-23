package com.example.marvelmedtechinc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelmedtechinc.R;
import com.example.marvelmedtechinc.VirusUpdatesActivity;

import java.util.ArrayList;
import java.util.List;


public class VirusUpdatesAdapter extends RecyclerView.Adapter<VirusUpdatesAdapter.ViewHolder> {
    Context context;
    ArrayList<ArrayList> list;

    public VirusUpdatesAdapter(ArrayList<ArrayList> list, VirusUpdatesActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VirusUpdatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.state_data_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VirusUpdatesAdapter.ViewHolder holder, int position) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList=list.get(position);
        holder.state_name.setText(arrayList.get(0));
        holder.active_case.setText(arrayList.get(1));
        holder.recovered.setText(arrayList.get(2));
        holder.deceased.setText(arrayList.get(3));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView state_name, active_case, recovered, deceased;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            state_name = itemView.findViewById(R.id.state_name);
            active_case = itemView.findViewById(R.id.active_cases);
            recovered = itemView.findViewById(R.id.recovered);
            deceased = itemView.findViewById(R.id.deceased);
        }
    }
}
