package com.example.forumtopictest;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ForumRecyclerView extends AppCompatActivity {

    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<Post> postList;
    PostAdapter postAdapter;
    FloatingActionButton btnCreatePost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forum);

        actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        // ini recycler view
        recyclerView = findViewById(R.id.rvForum);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity.class));

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first
        layoutManager.setStackFromEnd(true);
        layoutManager.setRecycleChildrenOnDetach(true);

        recyclerView.setLayoutManager(layoutManager);
        //init post list
        postList = new ArrayList<>();
        loadPosts();

        return;

    }

    private void loadPosts(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    System.out.println("title:"+post.getpTitle());
                    System.out.println("pdes:"+post.getpDes());
                    System.out.println("posttag:"+post.getpTag());
                    System.out.println("img:"+post.getpImage());
                    postList.add(post);
                }

                    postAdapter = new PostAdapter(getApplicationContext(), postList);
                    recyclerView.setAdapter(postAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForumRecyclerView.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

    }

}