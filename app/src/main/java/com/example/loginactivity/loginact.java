package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginact extends AppCompatActivity {
    EditText femail,fpassword;
    Button flogin;
    TextView fregister;
    ProgressBar fprogressbar;
    FirebaseAuth fAuth;
    TextView fforget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginact);

        femail = findViewById(R.id.et_Email);
        fpassword = findViewById(R.id.et_Password);
        fprogressbar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        flogin = findViewById(R.id.btn_login);
        fregister = findViewById(R.id.tv_regis);
        fforget = findViewById(R.id.tv_forgetpassword);

        flogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = femail.getText().toString().trim();
                String password = fpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    femail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    fpassword.setError("Password is required");
                    return;
                }
                if (password.length() < 8) {
                    fpassword.setError("Password must be 8 characters or more");
                    return;
                }
                fprogressbar.setVisibility(view.VISIBLE);
                //authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(loginact.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
            }
        });
        fregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),registeract.class));
                finish();
            }
        });
        fforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetmail = new EditText(view.getContext());
                AlertDialog.Builder passwordresetdialog = new AlertDialog.Builder(view.getContext());
                passwordresetdialog.setTitle("Reset password?");
                passwordresetdialog.setMessage("Enter your email to recieve the password reset link.");
                passwordresetdialog.setView(resetmail);
                passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send the email link
                        String mail= resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                              Toast.makeText(loginact.this,"Reset link sent to your mail.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginact.this,"Error! Reset link is not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the button
                    }
                });
                passwordresetdialog.create().show();
            }
        });
    }
}