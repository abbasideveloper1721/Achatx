package com.example.achatx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.achatx.Adapters.ChatAdapter;
import com.example.achatx.Models.MessagesModel;
import com.example.achatx.databinding.ActivityGroupchatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class groupchatActivity extends AppCompatActivity {
    ActivityGroupchatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(groupchatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.UserName.setText("Friends Group");

        final ChatAdapter adapter = new ChatAdapter(messagesModels,this);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messagesModels.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                                    messagesModels.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.sendMsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.edtextMessage.getText().toString();
                final MessagesModel model = new MessagesModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.edtextMessage.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });


    }
}