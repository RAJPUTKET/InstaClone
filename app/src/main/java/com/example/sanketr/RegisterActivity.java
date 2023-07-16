package com.example.sanketr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText username;
    private EditText password;
    private Button register;


    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);


        auth = FirebaseAuth.getInstance();
        DatabaseReference mRootRef;
        mRootRef = FirebaseDatabase.getInstance().getReference();


        TextView login_user = findViewById(R.id.login_user);
        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action when the text is clicked
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_username = username.getText().toString();
                String txt_name = name.getText().toString();


                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)) {

                    Toast.makeText(RegisterActivity.this, "Empty Credentials !", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_name, txt_password, txt_username, txt_email);
                }
            }

            private void registerUser(String txt_name, String txt_password, String txt_username, String txt_email) {


                auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", name);
                        map.put("email", email);
                        map.put("username", username);
                        map.put("id", auth.getCurrentUser().getUid());
                        map.put("bio", "");
                        map.put("imageurl", "default");

                        mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Update the profile " +
                                            "for better expereince", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


//
//                auth.createUserWithEmailAndPassword(txt_email , txt_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//               @Override
//               public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(RegisterActivity.this, "Registration Successful !", Toast.LENGTH_SHORT).show();
//
//                        startActivities(new Intent[]{new Intent(RegisterActivity.this, MainActivity.class)});
//                        finish();
//
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                    }
//               }
//           });
//            }
//        });


            }
        });
    }
}