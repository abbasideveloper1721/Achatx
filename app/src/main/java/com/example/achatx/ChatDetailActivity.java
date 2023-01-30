package com.example.achatx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.achatx.Adapters.ChatAdapter;
import com.example.achatx.Models.MessagesModel;
import com.example.achatx.databinding.ActivityChatDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId =auth.getUid();
        String reciveId = getIntent().getStringExtra("userId");
        String profilepic = getIntent().getStringExtra("profilepic");
        String userName = getIntent().getStringExtra("userName");


        binding.UserName.setText(userName);
        Picasso.get().load(profilepic).placeholder(R.drawable.avatar).into(binding.profileImage);

        binding.BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messagesModels,this);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


    }
}