package com.example.forumtopictest;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TopicDetails extends AppCompatActivity {

    ImageView disDetailCover, profilePic,btnLike;
    TextView disTag, disTitle, disDate_Author, disDetaildes,userName; //noComments
    EditText addCommentDetail;
    Button btnPostComment;
    RecyclerView rvComment;
    String postID;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    boolean mProcessLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        FirebaseApp.initializeApp(this);

        //initial views
        disDetailCover = findViewById(R.id.ivDetailDiscussionCover);
        profilePic = findViewById(R.id.ivCurrentUserPic);
        rvComment = findViewById(R.id.rvComments);

        disTag = findViewById(R.id.ttDiscussionTag);
        disTitle = findViewById(R.id.ttDiscussionDetailTitle);
        disDate_Author = findViewById(R.id.ttDiscussionDate);
        disDetaildes = findViewById(R.id.ttDiscussionDetailDes);
        userName = findViewById(R.id.ttDiscussionUsername);

        addCommentDetail = findViewById(R.id.ttComment);
        btnPostComment = findViewById(R.id.postCommentbtn);
        btnLike = findViewById(R.id.likeBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();


        // like function
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;

                    DatabaseReference likeRef = firebaseDatabase.getReference().child("Likes");
                    likeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (mProcessLike) {
                                if (snapshot.child(postID).hasChild(firebaseUser.getUid())) {
                                    likeRef.child(postID).child(firebaseUser.getUid()).removeValue();
                                    mProcessLike = false;

                                } else {
                                    likeRef.child(postID).child(firebaseUser.getUid()).setValue("RandomValue");
                                    mProcessLike = false;
                                }

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


        });

        DatabaseReference likeRef = firebaseDatabase.getReference().child("Posts").child("Likes");
        likeRef.keepSynced(true);
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postID).hasChild(firebaseUser.getUid())) {

                    btnLike.setImageResource(R.drawable.ic_liked);

                } else {

                    btnLike.setImageResource(R.drawable.ic_like);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // enable add comment button clickable
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPostComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").push();
                String comment_content = addCommentDetail.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl().toString();

                String postLinker = postID;
                System.out.println(postID + "is GAYYY");

                Comment comment = new Comment(comment_content, uid, uimg, uname, postLinker);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment successfully added");
                        addCommentDetail.setText("");
                        btnPostComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });


            }
        });



//        String postImage = getIntent().getExtras().getString("postImg") ;
        Glide.with(this).load(getIntent().getStringExtra("postImg")).into(disDetailCover);

//        String postTag = getIntent().getExtras().getString("disTag");
        disTag.setText(getIntent().getStringExtra("postTag"));

//        String postTitle = getIntent().getExtras().getString("disTitle");
        disTitle.setText(getIntent().getStringExtra("postTitle"));

        //TODO： username cannot be show on topicdetail page
        userName.setText(getIntent().getStringExtra("userName"));

//        String postDescription = getIntent().getExtras().getString("disDes");
        disDetaildes.setText(getIntent().getStringExtra("postDes"));


        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profilePic);
        // get post id
        postID = getIntent().getStringExtra("postID");

//        String date = timestampToString(getIntent().getExtras().getLong("postDate"));

        //TODO: the date can not be show on topic detail page
        String date = timestampToString(getIntent().getExtras().getLong("postTime"));
        disDate_Author.setText(date);

        // ini Recyclerview Comment
        inirvComment();

    }


    private void inirvComment() {

        rvComment.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference commentRef = firebaseDatabase.getReference().child("Comment");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);

                    listComment.add(comment);
                    System.out.println();
                    //if (postId = the child "postLinker" in comment) , then run "listComment.add(comment);"
                }
                //printing the array
                //ALLOW THE APP TO ONLY SHOW COMMENT NEEDED
                //eyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy

                Iterator<Comment> iter = listComment.iterator();
                while (iter.hasNext()) {
                    Comment p = iter.next();
                    if (!p.getPostLinker().equals(postID)) iter.remove();
                    //if matched, delete the row.
                }


//                for(Comment model : listComment) {
//                    //for testing
//                    System.out.println(model.getPostLinker() + " test my man " +Math.random());
//
//                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                rvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


        private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm aa",calendar).toString();
        return date;


    }
    class Model {

        private String name;

        public String getName() {
            System.out.println( "get name is called with value" + getName());
            return name;
        }

        public void setName(String name) {
            this.name = name;
            System.out.println( "set name is called");
        }
}}
