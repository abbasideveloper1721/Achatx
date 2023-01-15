package com.example.achatx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.achatx.Models.Users;
import com.example.achatx.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignupAct.this);
        progressDialog.setTitle("creating Account");
        progressDialog.setMessage("we are creating your account");
        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.editTextEmailAddress.getText().toString(),binding.editTextPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                             if(task.isSuccessful()){
                                 Users user = new Users(binding.editTextTextPersonName.getText().toString(),binding.editTextEmailAddress.getText().toString(),binding.editTextPassword.getText().toString());
                                 String id = task.getResult().getUser().getUid();
                                 database.getReference().child("Users").child(id).setValue(user);
                                 Toast.makeText(SignupAct.this, "User created Succesfully", Toast.LENGTH_SHORT).show();

                             }else{
                                 Toast.makeText(SignupAct.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                             }
                            }
                        });

            }
        });

    }
}