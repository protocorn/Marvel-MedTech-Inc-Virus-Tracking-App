package com.example.marvelmedtechinc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.marvelmedtechinc.Adapter.ChatAdapter;
import com.example.marvelmedtechinc.databinding.ActivityChatBinding;
import com.example.marvelmedtechinc.models.Message_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;
    FirebaseFirestore firestore;

    String senderId;
    String receiverId;
    String SenderRoom;
    String ReceiverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        senderId = auth.getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("uid");

        SenderRoom = senderId + receiverId;
        ReceiverRoom = receiverId + senderId;

        binding.fUsername.setText(receiverId);

        final ArrayList<Message_Model> message_models = new ArrayList<>();
        chatAdapter = new ChatAdapter(message_models, ChatActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        binding.chatlist.setLayoutManager(layoutManager);
        binding.chatlist.setAdapter(chatAdapter);

        firestore.collection("chats").document(SenderRoom).collection(SenderRoom).orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                message_models.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Message_Model model = snapshot.toObject(Message_Model.class);
                    message_models.add(model);
                    binding.chatlist.smoothScrollToPosition(binding.chatlist.getAdapter().getItemCount());
                }
                chatAdapter.notifyDataSetChanged();
            }
        });
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.userMessage.getText().toString().isEmpty()) {
                    String msg = String.valueOf(binding.userMessage.getText());
                    Message_Model message_model = new Message_Model(msg, senderId);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    String time = simpleDateFormat.format(calendar.getTime());
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    message_model.setTimestamp(timestamp);
                    message_model.setTime(time);

                    binding.userMessage.setText("");

                    firestore.collection("chats").document(SenderRoom).collection(SenderRoom).add(message_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            firestore.collection("chats").document(ReceiverRoom).collection(ReceiverRoom).add(message_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    chatAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });

                }
            }
        });
    }
}