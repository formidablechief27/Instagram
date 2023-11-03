package com.example.metagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AgreePost extends AppCompatActivity implements View.OnClickListener{

    private ActivityResultLauncher<String> imagePickerLauncher;
    ImageView image;
    Button submit;
    Button story;

    Button select;

    String imageUrl = "";

    Uri imageUri = null;

    String DATE = "";

    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreepost);
        image = findViewById(R.id.image);
        submit = findViewById(R.id.submit);
        select = findViewById(R.id.selectImage);
        story = findViewById(R.id.story);
        story.setOnClickListener(this);
        image.setOnClickListener(this);
        submit.setOnClickListener(this);
        select.setOnClickListener(this);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = sdf.format(calendar.getTime());
                        String date = formattedDateTime;
                        DATE = date;
                        uploadImageToFirebaseStorage(uri);
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.selectImage) {

            imagePickerLauncher.launch("image/*");
        }
        if(v.getId() == R.id.submit){
            EditText text = findViewById(R.id.caption);
            updateProfileWithImageUrl();
            usernamefill();
            captionfill(text.getText().toString());
            post();
            Intent intent = new Intent(AgreePost.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId() == R.id.story){
            EditText text = findViewById(R.id.caption);
            updateStoryWithImageUrl();
            captionfillStory(text.getText().toString());
            Intent intent = new Intent(AgreePost.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void inDatabase(String imageUrl) {
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId);
        userRef.child("posts").child(userId + " " + DATE).setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    setimage();
                    //filldetails();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    // Handle failure to update image URL
                });
        setimage();
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        String userId = MainActivity.userId;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("posts")
                .child(userId + " " + DATE + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                inDatabase(uri.toString());
                            });
                })
                .addOnFailureListener(e -> {

                });
        setimage();
    }


    private void updateProfileWithImageUrl() {
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(userId + " " + DATE);
        userRef.child("post_image").setValue(link)
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    //setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    //setpfp();
                    // Handle failure to update image URL
                });
    }

    private void updateStoryWithImageUrl() {
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("story")
                .child(userId + " " + DATE);
        userRef.child("link").setValue(link)
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    //setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    //setpfp();
                    // Handle failure to update image URL
                });
    }


    void post(){
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId);
        userRef.child("post_count").child(userId + " " + DATE).setValue("0")
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    //setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    //setpfp();
                    // Handle failure to update image URL
                });
    }
    void captionfill(String caption){
        if(caption == null) caption = "";
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(userId + " " + DATE);
        userRef.child("post_caption").setValue(caption)
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    //setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    //setpfp();
                    // Handle failure to update image URL
                });
    }

    void captionfillStory(String caption){
        if(caption == null) caption = "";
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("story")
                .child(userId + " " + DATE);
        userRef.child("caption").setValue(caption)
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    //setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    //setpfp();
                    // Handle failure to update image URL
                });
    }

    void usernamefill(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("username").getValue(String.class);
                    String userId = MainActivity.userId;
                    DatabaseReference userRef1 = FirebaseDatabase.getInstance().getReference()
                            .child("posts")
                            .child(userId + " " + DATE);
                    userRef1.child("post_uploader").setValue(name)
                            .addOnSuccessListener(aVoid -> {
                                // Handle successful update
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to update
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error appropriately
            }
        });
    }

    public void setimage(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(MainActivity.userId).child("posts");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child(MainActivity.userId + " " + DATE).getValue(String.class);
                    ImageView pp = findViewById(R.id.image);
                    if (imageUrl != null) {
                        link = imageUrl;
                        // Use an image loading library to display the image
                        Glide.with(AgreePost.this)
                                .load(imageUrl)
                                .into(pp);
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if neces
            }
        });
    }
}
