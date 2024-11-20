package com.example.foodyapp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodyapp.Helper.ActivityHelper;
import com.example.foodyapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private EditText loginEmail, loginPassword;
    private TextView signupRedirect,forgetPassword;
    private ProgressBar progressBar;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.emailEdit);
        loginPassword = findViewById(R.id.passEdit);
        forgetPassword = findViewById(R.id.text_forgetpass);
        loginButton = findViewById(R.id.btn_login);
        signupRedirect = findViewById(R.id.redirect_signup);
        progressBar = findViewById(R.id.progressBar);

        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {

            ActivityHelper.startNextActivity(LoginActivity.this, MainActivity.class);
            finish();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                if (email.isEmpty()){
                    loginEmail.setError("");
                    return;
                }
                if (password.isEmpty()){
                    loginPassword.setError("");
                    return;
                }
                if (password.length() < 6){
                    loginPassword.setError("");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)


                    {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            if (user.isEmailVerified()){
                                progressBar.setVisibility(View.GONE);
                                ActivityHelper.startNextActivity(LoginActivity.this, MainActivity.class);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Error !"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



            }

        });
        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityHelper.startNextActivity(LoginActivity.this, SignupActivity.class);
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });

    }
}