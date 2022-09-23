package com.example.marvelmedtechinc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelmedtechinc.ChatActivity;
import com.example.marvelmedtechinc.ConsultDoctorActivity;
import com.example.marvelmedtechinc.DoctorActivity;
import com.example.marvelmedtechinc.R;
import com.example.marvelmedtechinc.VaccinationActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    Context context;
    List<String> list;
    List<String> uid;

    public DoctorAdapter(List<String> list,List<String> uid ,ConsultDoctorActivity context) {
        this.list = list;
        this.uid = uid;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

        holder.name.setText(list.get(position));
        holder.con_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("uid",auth.getCurrentUser().getUid());
                firebaseFirestore.collection("Users").document(uid.get(position)).collection("Patients").document(auth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent=new Intent(context, ChatActivity.class);
                        intent.putExtra("uid",uid.get(position));
                        context.startActivity(intent);
                        Toast.makeText(context, "Consultation Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, spec;
        Button con_btn;
        ImageView avatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.doc_name);
            con_btn= itemView.findViewById(R.id.consult_btn);
            spec = itemView.findViewById(R.id.spec);
            avatar = itemView.findViewById(R.id.doc_avatar);
        }
    }
}
