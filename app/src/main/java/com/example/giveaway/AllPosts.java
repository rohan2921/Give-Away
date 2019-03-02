package com.example.giveaway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllPosts extends AppCompatActivity {

    Query databaseReference;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        databaseReference = FirebaseFirestore.getInstance().collection("Posts");
        recyclerView = (RecyclerView) findViewById(R.id.main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Contributions");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            assert  getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recycle();
    }

    private void recycle() {

        Toast.makeText(AllPosts.this,"Starting RecycleView",Toast.LENGTH_SHORT).show();
        FirestoreRecyclerOptions<RPosts> options = new FirestoreRecyclerOptions.Builder<RPosts>()
                .setQuery(databaseReference, RPosts.class)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<RPosts, RPostsViewHolder>(options) {
            @Override
            public void onBindViewHolder(RPostsViewHolder holder, int position, RPosts model) {
                holder.setname(model.getName());
                holder.setimage(model.getImage());
                holder.setpost(model.getPost());
                holder.setpimage(model.getPimage());


            }

            @Override
            public RPostsViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.singlepost, group, false);

                return new RPostsViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}
