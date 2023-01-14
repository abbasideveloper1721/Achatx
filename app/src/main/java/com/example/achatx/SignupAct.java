package com.example.achatx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.achatx.databinding.ActivitySignupBinding;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
    }
}