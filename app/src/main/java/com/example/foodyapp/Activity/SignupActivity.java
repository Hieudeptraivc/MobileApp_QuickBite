package com.example.foodyapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodyapp.Domain.User;
import com.example.foodyapp.Helper.ActivityHelper;
import com.example.foodyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends BaseActivity {
    public static final String TAG = "TAG";
    private EditText signupEmail, signupPassword,confirmPassword, signupName;
    private Button signupButton;
    private TextView loginRedirect;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupName = findViewById(R.id.nameEdit);
        signupEmail = findViewById(R.id.emailEdit);
        signupPassword = findViewById(R.id.passEdit);
        confirmPassword = findViewById(R.id.cfpassEdit);
        signupButton = findViewById(R.id.btn_login);
        loginRedirect = findViewById(R.id.redirect_login);
        progressBar = findViewById(R.id.progressBar);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();
                if (fullName.isEmpty()){
                    signupName.setError("Thís field is required");
                    return;
                }
                if (email.isEmpty()){
                    signupEmail.setError("");
                    return;
                }
                if (password.isEmpty()){
                    signupPassword.setError("");
                    return;
                }
                if (password.length() < 6){
                    signupPassword.setError("");
                    return;

                }
                if (confirmPass.isEmpty() || !password.equals(confirmPass)){
                    confirmPassword.setError("");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            User newUser = new User(fullName,email,password,user.getUid()," ");

                            FirebaseDatabase.getInstance("https://quickbite-9284c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(user.getUid()).setValue(newUser);
                            Toast.makeText(SignupActivity.this, "User Create", Toast.LENGTH_SHORT).show();
                            ActivityHelper.startNextActivity(SignupActivity.this, LoginActivity.class);
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignupActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not sent "+ e.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(SignupActivity.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }

        });
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityHelper.startNextActivity(SignupActivity.this, LoginActivity.class);
            }
        });


    }
}