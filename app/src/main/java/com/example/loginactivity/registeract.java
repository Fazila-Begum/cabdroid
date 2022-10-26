package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registeract  extends AppCompatActivity {
    EditText fFullname,fphonenum,femail,fpassword;
    TextView floginbtn;
    Button fregisterbtn;
    FirebaseAuth fAuth;
    ProgressBar fprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeract);
        fFullname = findViewById(R.id.et_Fullname);
        femail = findViewById(R.id.et_Email);
        fphonenum = findViewById(R.id.et_phonenum);
        fpassword = findViewById(R.id.et_Password);
        fregisterbtn = findViewById(R.id.btn_login);
        floginbtn = findViewById(R.id.tv_regis);

        fAuth = FirebaseAuth.getInstance();
        fprogressbar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
            fregisterbtn.setOnClickListener(new View.OnClickListener() {
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
                    // register the user in firebase
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //send verification link
                                FirebaseUser user=fAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                       Toast.makeText(registeract.this,"Verification email has been sent",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure:Email not sent"+e.getMessage());
                                    }
                                });


                                Toast.makeText(registeract.this, "User created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(registeract.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        floginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),loginact.class));
                finish();
            }
        });

        }
}