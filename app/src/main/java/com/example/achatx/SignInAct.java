package com.example.achatx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.achatx.Models.Users;
import com.example.achatx.databinding.ActivitySignInBinding;
import com.example.achatx.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class    SignInAct extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    ProgressDialog progressDialog;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignInAct.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        binding.buttongoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.eTEmailAddress.getText().toString().isEmpty()){
                    binding.eTEmailAddress.setText("Enter your Email");
                    return;
                }
                if(binding.eTPassword.getText().toString().isEmpty()){
                    binding.eTPassword.setText("Enter your Email");
                    return;
                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.eTEmailAddress.getText().toString(),binding.eTPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    Intent intent = new Intent(SignInAct.this,MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(SignInAct.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });



            }
        });
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInAct.this,SignupAct.class);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(SignInAct.this ,MainActivity.class);
            startActivity(intent);
        }
    }




    //@Override
    //public void startActivityForResult(Intent intent, int requestCode) {
      //  super.startActivityForResult(signInIntent, RC_SIGN_IN);
    //}
    int RC_SIGN_IN = 65;
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("","FirbaseAuthWithGoogle:"+account.getId());
                FirbaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("","Google Sign-in failed",e);
            }
        }
    }
    private void FirbaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("TAG","signInWithCredntials:Success");
                            FirebaseUser user = auth.getCurrentUser();
                            Users users = new Users();
                            users.setUserID(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);
                            Log.d("TAG","Google credentials saved in database");
                            Intent intent = new Intent(SignInAct.this ,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInAct.this, "Signed In with Google:Success", Toast.LENGTH_SHORT).show();
                           // updateUI(user);
                            //
                        }else{
                            Log.w("TAG","signInWithCredntials:Failure",task.getException());
                            Toast.makeText(SignInAct.this, "Signed In with Google:Failed", Toast.LENGTH_SHORT).show();


                           // Snackbar.make(mBinding.mainLayout,"Authentication  Failed",Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }
}