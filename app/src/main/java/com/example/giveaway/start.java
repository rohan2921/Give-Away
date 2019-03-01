package com.example.giveaway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class start extends AppCompatActivity {

    Button login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
    }

    public void sigup(View view) {

        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }

}
