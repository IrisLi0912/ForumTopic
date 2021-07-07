package com.example.forumtopictest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

//    //permissions constants
//    private static final int CAMERA_REQUEST_CODE = 100;
//    private static final int STORAGE_REQUEST_CODE = 200;
//
//    //image pick constents
//    private static final int IMAGE_PICK_CAMERA_CODE = 300;
//    private static final int IMAGE_PICK_GALLERY_CODE = 400;
//    //permissions array
//    String [] cameraPermissions;
//    String [] storagePermissions;



    //user infor
    String name, email, uid, dp;

    ProgressDialog pd;

    Uri image_uri = null;


    // ini views activity_add_post.xml

    ImageView popupUserImage, popupPostImage, popupAddBtn;
    EditText popupTitle, popupDescription, popupTag;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //init permissions arrays
//        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
//        checkUserStatus();

        // get some infor if current user including in the post
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    dp = "" + ds.child("image").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //ini views
        popupUserImage = findViewById(R.id.popup_user_image);
        popupPostImage = findViewById(R.id.popup_img);
        popupTitle = findViewById(R.id.popup_title);
        popupDescription = findViewById(R.id.popup_description);
        popupAddBtn = findViewById(R.id.popup_add);
        popupTag = findViewById(R.id.popup_discussionTag);

        //load the current user image
        Glide.with(AddPostActivity.this).load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(popupUserImage);



        //get image from camre/gallery on click
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();

            }
        });
    }

        private void checkAndRequestForPermission () {


            if (ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    Toast.makeText(AddPostActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

                } else {
                    ActivityCompat.requestPermissions(AddPostActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PReqCode);
                }

            } else{
                // everything goes well : we have permission to access user gallery
                openGallery();

        }
    }


        private void openGallery() {
            //TODO: open gallery intent and wait for user to pick an image !

            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, REQUESCODE);
        }


        // when user picked an image ...
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

                // the user has successfully picked an image
                // we need to save its reference to a Uri variable
                image_uri = data.getData();
                popupPostImage.setImageURI(image_uri);

            }




        //upload button click
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility(View.INVISIBLE);

                //get data (tag, title, description) from edittext
                String tag = popupTag.getText().toString().trim();
                String title = popupTitle.getText().toString().trim();
                String description = popupDescription.getText().toString().trim();

                System.out.println(description);

                if (TextUtils.isEmpty(tag)){
                    Toast.makeText(AddPostActivity.this, "Please enter post tag...",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddPostActivity.this, "Please enter post title...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    Toast.makeText(AddPostActivity.this, "Please enter post description...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (image_uri == null){
                    uploadData(tag,title,description, "noImage");

                }else{
                    uploadData(tag,title,description, String.valueOf(image_uri));
                }
            }
        });

    }

    private void uploadData(String tag, String title, String description, String uri){
        pd.setMessage("Publishing post....");
        pd.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" +timeStamp;

        if (! uri.equals("noImage")){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // image uploaded
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){

                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("uid", uid );
                                hashMap.put("uName", name);
                                hashMap.put("uEmail", email );
                                hashMap.put("uDp", dp );
                                hashMap.put("pID",timeStamp );
                                hashMap.put("pTag", tag);
                                hashMap.put("pTitle", title);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);
                                hashMap.put("pDes", description);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //added in data base
                                        pd.dismiss();
                                        Toast.makeText(AddPostActivity.this, "Post has successfully added!",Toast.LENGTH_SHORT).show();
                                        popupTag.setText("");
                                        popupTitle.setText("");
                                        popupDescription.setText("");
                                        popupPostImage.setImageURI(null);
                                        image_uri = null;
                                        popupAddBtn.setVisibility(View.VISIBLE);


                                        startActivity(new Intent(getApplicationContext(),ForumRecyclerView.class));
                                        finish();

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //fail adding post in database
                                                pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //image failed to upload
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });




        }else{
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("uid", uid );
            hashMap.put("uName", name);
            hashMap.put("uEmail", email );
            hashMap.put("uDp", dp );
            hashMap.put("pID",timeStamp );
            hashMap.put("pTag", tag);
            hashMap.put("pTitle", title);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);
            hashMap.put("pDes", description);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //added in data base
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this, "Post has successfully added!",Toast.LENGTH_SHORT).show();
                    //reset views
                    popupTag.setText("");
                    popupTitle.setText("");
                    popupDescription.setText("");
                    popupPostImage.setImageURI(null);
                    image_uri = null;
                    startActivity(new Intent(getApplicationContext(),ForumRecyclerView.class));
                    finish();


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //fail adding post in database
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

//    private void showImagePickDialog(){
//        //option(camera, gallery)
//        String [] options = {"Cammera","Gallery"};
//
//        //Dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose Image from");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(which == 0){
//                    //camera clicked
//                    if(! checkCameraPermission()){
//                        requestCameraPermission();
//                    }else{
//                        pickFromCamera();
//                    }
//
//                }if (which == 1){
//                    // gallery clicked
//                    if (! checkStoragePermission()){
//                        requestStoragePermission();
//                    }else{
//                        pickFromGallery();
//                    }
//                }
//
//            }
//        });
//
//        builder.create().show();
//    }
//
//
//    private void pickFromCamera() {
//        ContentValues cv = new ContentValues();
//        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
//        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
//
//    }
//
//    private void pickFromGallery(){
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
//
//    }
//
//    private boolean checkStoragePermission(){
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//
//    private void requestStoragePermission(){
//        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
//    }
//
//    private boolean checkCameraPermission(){
//        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result && result1;
//    }
//
//    private void requestCameraPermission(){
//        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            email = user.getEmail();
            uid = user.getUid();


        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    //handle permission result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode){
//            case CAMERA_REQUEST_CODE:{
//                if (grantResults.length>0){
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && storageAccepted){
//                        pickFromCamera();
//                    }
//                    else{
//                        Toast.makeText(this, "Camera and Storage both permissions are neccessary to gain images",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                }
//
//            }
//            break;
//            case STORAGE_REQUEST_CODE:{
//                if (grantResults.length>0){
//                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (storageAccepted){
//                        pickFromGallery();
//                    }else{
//                        Toast.makeText(this, "Storage permissions are neccessary to gain images",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//
//                }
//
//            }
//            break;
//        }
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK){
//            if (requestCode == IMAGE_PICK_GALLERY_CODE){
//                image_uri = data. getData();
//
//                popupPostImage.setImageURI(image_uri);
//            }
//            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
//                popupPostImage.setImageURI(image_uri);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//
//
//    }
}