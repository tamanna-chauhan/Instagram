package com.acadview.instagram;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    images for stories
    ImageView storyImg1,storyImg2,storyImg3,storyImg4,storyImg5,storyImg6,storyImg7;


//    request code for signin
    private static final int RC_SIGN_IN = 123;
    private static final int RC_PIX = 101;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference userRef,postRef;

    FirebaseStorage storage;

    StorageReference storageRef,imageRef;

    RecyclerView recyclerView;

    PostAdapter adapter;
    ArrayList<PostModel> postList;


//    authenticator provider
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

//    authenticator

    private FirebaseAuth firebaseAuth;

//    user
    private FirebaseUser user;


//    auth state listener
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView email;
    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);

//        reference for the image View
        storyImg1 = findViewById(R.id.story1);
        storyImg2 =  findViewById(R.id.story2);
        storyImg3 =  findViewById(R.id.story3);
        storyImg4 = findViewById(R.id.story4);
        storyImg5 =  findViewById(R.id.story5);
        storyImg6 =  findViewById(R.id.story6);
        storyImg7 = findViewById(R.id.story7);


//recyclerView refernce and manager
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new PostAdapter(this);
        recyclerView.setAdapter(adapter);
        postList = new ArrayList<>();

//get instance
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            storage = FirebaseStorage.getInstance();

//            database nodes
            userRef = firebaseDatabase.getReference("users");
            postRef = firebaseDatabase.getReference("post");

//            ref of storage
        storageRef = storage.getReference();

        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                PostModel post = dataSnapshot.getValue(PostModel.class);
                postList.add(post);
                adapter.swap(postList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();


                if(user != null){

//                    email.setText(user.getEmail());

//                    get user detail
                    String id = user.getUid();
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    String imgURL = user.getPhotoUrl().toString();


//create model
                    UserModel userModel = new UserModel(id,name,email,imgURL);

//add to firebase database
                    userRef.child(userModel.getId()).setValue(userModel);



                } else{

                    // Create and launch sign-in intent

                    StartSignIn();
                     }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {

                Toast.makeText(MainActivity.this, response.getError().getErrorCode(),Toast.LENGTH_SHORT).show();

                StartSignIn();
                    }
        }else if(resultCode == Activity.RESULT_OK && requestCode == RC_PIX){
            ArrayList<String> images = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            uploadPost(images.get(0));
        }
    }

//    method for post
    private void uploadPost(String imageLocation) {
        showToast("Processing message");

        Uri file = Uri.fromFile(new File(imageLocation));
        imageRef = storageRef.child(file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        storageRef.child("images/mountains.jpg");
        uploadTask = imageRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    showToast("processing Failed");
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    showToast("processing field");

//                need to ppost

                    String userName = user.getDisplayName();
                    String userImgURL = user.getPhotoUrl().toString();
                    String userEmail = user.getEmail();
                    String postImgURL = downloadUri.toString();
                    String postTime = String.valueOf(System.currentTimeMillis() / 1000);

                    PostModel postModel = new PostModel(userName, userImgURL, userEmail, postImgURL, postTime);

                    postRef.push().setValue(postModel);

                    showToast("uploading Finished");

                } else {
                    showToast("upload Failed");
                }
            }
        });
    }


//method for the toast
    private void showToast(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }

//    method for the Start with sign in
    private void StartSignIn() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);
    }




    private void StartSignOut() {

        AuthUI.getInstance()
                .signOut(MainActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(MainActivity.this,"Succesfully Signed Out",Toast.LENGTH_SHORT).show();

                        // ...
                    }
                });

    }

    public void post(View view) {
        Pix.start(MainActivity.this,RC_PIX,1);
    }

    public void startstory(View view){
        switch (view.getId()){
            case R.id.story1:
                startIntent(0);
                break;
            case R.id.story2:
                startIntent(1);
                break;
            case R.id.story3:
                startIntent(2);
                break;
            case R.id.story4:
                startIntent(3);
                break;
            case R.id.story5:
                startIntent(4);
                break;
            case R.id.story6:
                startIntent(5);
                break;
            case R.id.story7:
                startIntent(6);
                break;

        }
    }

    private void startIntent(int i) {
        Intent intent = new Intent(MainActivity.this,Story.class);
        intent.putExtra("position",i);
        startActivity(intent);
    }
}
