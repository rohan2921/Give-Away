package com.example.giveaway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db ;

    EditText phone,e,p;
    Button cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        phone=(EditText)findViewById(R.id.name);
        e=(EditText)findViewById(R.id.e);
        p=(EditText)findViewById(R.id.p);
        cr = (Button)findViewById(R.id.cr);
        db = FirebaseFirestore.getInstance();
    }

    public void cr(View view) {
        String n = phone.getText().toString();
        String email = e.getText().toString();
        String password = p.getText().toString();
        if(!TextUtils.isEmpty(n) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            reg(n,email,password);
        }
    }

    private void reg(final String n, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("phone", n);
                                db.collection("Users").document(currentUser.getUid().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(register.this,"Registration sucessful",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(register.this,setup.class);
                                            Toast.makeText(register.this,"success registration",Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            String e = task.getException().getMessage();
                                            Toast.makeText(register.this,"Fire store error " + e ,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        }
                else {
                    Toast.makeText(register.this, " ERROR registering", Toast.LENGTH_SHORT).show();
                }
                    }});

    }
}
