package com.example.giveaway;

import android.content.Intent;
import android.content.QuickViewConstants;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    Button addpost,learn,allposts;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        addpost = (Button)findViewById(R.id.addpost);
        learn = (Button)findViewById(R.id.learn);
        allposts = (Button)findViewById(R.id.allposts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Give Away");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Toast.makeText(MainActivity.this,"Login or Register",Toast.LENGTH_SHORT).show();
            change();
        }
        else  {
            firebaseFirestore.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(!task.getResult().exists()) {
                            Intent intent = new Intent(MainActivity.this,setup.class);
                            Toast.makeText(MainActivity.this,"Regitration not yet completed",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            change();
        }
        else if(item.getItemId() == R.id.profile) {
            Intent intent = new Intent(this,setup.class);
            startActivity(intent);
        }


        return true;
    }

    private void change() {
        Intent intent = new Intent(this,start.class);
        startActivity(intent);
        finish();
    }

    public void allposts(View view) {
        Intent intent = new Intent(this,AllPosts.class);
        startActivity(intent);
    }

    public void addpost(View view) {
        Intent intent = new Intent(MainActivity.this,addpost.class);
        startActivity(intent);
    }

    public void learn(View view) {
        Intent intent = new Intent(MainActivity.this, com.example.giveaway.learn.class);
        startActivity(intent);
    }
}

