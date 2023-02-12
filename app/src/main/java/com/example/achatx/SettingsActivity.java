package com.example.achatx;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.achatx.Models.Users;
import com.example.achatx.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseStorage storage;

    ActivityResultLauncher<String> mtakePhoto;






    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mtakePhoto = registerForActivityResult(new
                        ActivityResultContracts.GetContent(),





                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                     Uri   imageuri = result;

                        binding.profileimage.setImageURI(result);
                        final StorageReference reference = storage.getReference().child("profile pictures")
                                .child(FirebaseAuth.getInstance().getUid());
                        reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                               .child("profilepic").setValue(uri.toString());
                                       Toast.makeText(SettingsActivity.this,"String updated",Toast.LENGTH_SHORT).show();
                                   }
                               });
                            }
                        });


                    }});


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilepic())
                        .placeholder(R.drawable.avatar)
                        .into(binding.profileimage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtakePhoto.launch("image/*");

            }
        });

    }
}