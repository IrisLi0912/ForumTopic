////package com.example.forumtopictest;
////
////
////import android.Manifest;
////import android.app.Dialog;
////import android.content.Intent;
////import android.content.pm.PackageManager;
////import android.graphics.Color;
////import android.graphics.drawable.ColorDrawable;
////import android.net.Uri;
////import android.os.Bundle;
////import android.view.Gravity;
////import android.view.View;
////import android.widget.ImageView;
////import android.widget.ProgressBar;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.app.ActivityCompat;
////import androidx.core.content.ContextCompat;
////import androidx.recyclerview.widget.GridLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.bumptech.glide.Glide;
////import com.google.android.gms.tasks.OnFailureListener;
////import com.google.android.gms.tasks.OnSuccessListener;
////import com.google.android.material.floatingactionbutton.FloatingActionButton;
////import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.auth.FirebaseUser;
////import com.google.firebase.database.DataSnapshot;
////import com.google.firebase.database.DatabaseError;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.database.ValueEventListener;
////import com.google.firebase.storage.FirebaseStorage;
////import com.google.firebase.storage.StorageReference;
////import com.google.firebase.storage.UploadTask;
////
////import java.util.ArrayList;
////import java.util.List;
////
//////import com.google.android.material.bottomnavigation.BottomNavigationView;
////
////public class HomeExplorePage extends AppCompatActivity {
////
////    private RecyclerView recyclerView;
////    FirebaseAuth mAuth;
////    FirebaseUser currentUser;
//////    Dialog popAddPost;
//////    ImageView popupUserImage, popupPostImage, popupAddBtn;
//////    TextView popupTitle, popupDescription, popupTag;
//////    ProgressBar popupClickProgress;
//////    private Uri pickedImgUri = null;
//////    private FloatingActionButton createPost;
//////    private static final int PReqCode = 2;
//////    private static final int REQUESCODE = 2;
//////    PostAdapter mainForumAdapter;
////    FirebaseDatabase firebaseDatabase;
////    DatabaseReference databaseReference;
////    List<Post> postList;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.fragment_forum);
////        recyclerView = findViewById(R.id.rvForum);
////        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
////        recyclerView.setHasFixedSize(true);
////        firebaseDatabase = FirebaseDatabase.getInstance();
////        databaseReference = firebaseDatabase.getReference("Posts");
////
////
////        mAuth = FirebaseAuth.getInstance();
////        currentUser = mAuth.getCurrentUser();
////
////
////        createPost = findViewById(R.id.btnCreatePost);
////        createPost.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                popAddPost.show();
////            }
////        });
////
////        iniPopup();
////        setupPopupImageClick();
////
////
////        // Get List Posts from the database
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////                postList = new ArrayList<>();
////                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
////
////                    Post post = postsnap.getValue(Post.class);
////                    postList.add(post);
////
////                }
////
////                mainForumAdapter = new PostAdapter(getApplicationContext(), postList);
////                recyclerView.setAdapter(mainForumAdapter);
////                startActivity(new Intent(getApplicationContext(), HomeExplorePage.class));
////                finish();
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////
////    }
////
////    private void addPost(Post post) {
////
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("Posts").push();
////
////
////        // get post unique ID and upadte post key
////        String ID = myRef.getKey();
////        post.setPostID(ID);
////
////        // add post data to firebase database
////
////        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
////            @Override
////            public void onSuccess(Void aVoid) {
////                showMessage("Post Added successfully");
////                popupClickProgress.setVisibility(View.INVISIBLE);
////                popupAddBtn.setVisibility(View.VISIBLE);
////                popAddPost.dismiss();
////
////            }
////        });
////
////
////    }
////
////    private void setupPopupImageClick() {
////
////        popupPostImage.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                // here when image clicked we need to open the gallery
////                // before we open the gallery we need to check if our app have the access to user files
////                // we did this before in register activity I'm just going to copy the code to save time ...
////
////                checkAndRequestForPermission();
////
////            }
////        });
////    }
////
////    private void checkAndRequestForPermission() {
////
////
////        if (ContextCompat.checkSelfPermission(HomeExplorePage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
////                != PackageManager.PERMISSION_GRANTED) {
////            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeExplorePage.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
////
////                Toast.makeText(HomeExplorePage.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
////
////            } else {
////                ActivityCompat.requestPermissions(HomeExplorePage.this,
////                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
////                        PReqCode);
////            }
////
////        } else
////            // everything goes well : we have permission to access user gallery
////            openGallery();
////
////    }
////
////
////    private void openGallery() {
////        //TODO: open gallery intent and wait for user to pick an image !
////
////        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
////        galleryIntent.setType("image/*");
////        startActivityForResult(galleryIntent, REQUESCODE);
////    }
////
////
////    // when user picked an image ...
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
////
////            // the user has successfully picked an image
////            // we need to save its reference to a Uri variable
////            pickedImgUri = data.getData();
////            popupPostImage.setImageURI(pickedImgUri);
////
////        }
////
////
////    }
//
//
//    private void iniPopup() {
//
//        popAddPost = new Dialog(this);
//        popAddPost.setContentView(R.layout.popup_add_post);
//        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
//
//        // ini popup widgets
//        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
//        popupPostImage = popAddPost.findViewById(R.id.popup_img);
//        popupTitle = popAddPost.findViewById(R.id.popup_title);
//        popupDescription = popAddPost.findViewById(R.id.popup_description);
//        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
//        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
//        popupTag = popAddPost.findViewById(R.id.popup_discussionTag);
//
//
//
//        // load Current user profile photo
//
//        Glide.with(HomeExplorePage.this).load(currentUser.getPhotoUrl()).into(popupUserImage);
//
//
//        // Add post click Listener
//
//        popupAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                popupAddBtn.setVisibility(View.INVISIBLE);
//                popupClickProgress.setVisibility(View.VISIBLE);
//
//                if (!popupTitle.getText().toString().isEmpty()
//                        && !popupDescription.getText().toString().isEmpty()
//                        && !popupTag.getText().toString().isEmpty()
//                        && pickedImgUri != null) {
//
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
//                    final StorageReference imageFilePath =storageReference.child(pickedImgUri.getLastPathSegment());
//                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    // create post Object
//                                    String imageDownlaodLink = uri.toString();
//                                    // create post Object
//                                    Post post = new Post(popupTag.getText().toString(),
//                                            popupTitle.getText().toString(),
//                                            popupDescription.getText().toString(),
//                                            imageDownlaodLink,
//                                            currentUser.getUid(),
//                                            currentUser.getPhotoUrl().toString());
//
//                                    // Add post to firebase database
//
//                                    addPost(post);
//
//                                }
//
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // something goes wrong uploading picture
//
//                                    showMessage(e.getMessage());
//                                    popupClickProgress.setVisibility(View.INVISIBLE);
//                                    popupAddBtn.setVisibility(View.VISIBLE);
//
//                                }
//
//                            });
//
//                        }
//                    });
//
//                } else {
//                    showMessage("Please verify all input fields and choose Post Image");
//                    popupAddBtn.setVisibility(View.VISIBLE);
//                    popupClickProgress.setVisibility(View.INVISIBLE);
//
//
//                }
//
//
//            }
//
//
//        });
//
//    }
//
//    private void showMessage(String message) {
//
//        Toast.makeText(HomeExplorePage.this, message, Toast.LENGTH_LONG).show();
//
//    }
//
//
//}
//
//
//
//
//
//
