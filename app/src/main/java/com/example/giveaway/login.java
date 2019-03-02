package com.example.giveaway;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    FirebaseAuth mAuth;
    Toolbar toolbar;
    EditText e,p;
    Button cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        e=(EditText)findViewById(R.id.e);
        p=(EditText)findViewById(R.id.p);
        cr = (Button)findViewById(R.id.cr);
        setTitle("Settings");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            assert  getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void cr(View view) {
        String email = e.getText().toString();
        String password = p.getText().toString();
        if( !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            lo(email,password);
        }

    }

    private void lo(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(login.this,MainActivity.class);
                    Toast.makeText(login.this,"success registration",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
                else {
                    String error = task.getException().getMessage().toString();
                    Toast.makeText(login.this, " ERROR registering : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
