package com.example.metagram;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityResultLauncher<String> imagePickerLauncher;

    TextView name;
    TextView date;
    TextView days;
    ImageButton logout;
    ImageButton saved;
    Button history;
    long count =0;
    String username = "";
    String dat = "";
    TextView l1;
    TextView l2;
    TextView l3;
    TextView logs;
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);
        setpfp();
        profile = findViewById(R.id.profile_picture);
        saved = findViewById(R.id.saved);
        name = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        l1 = findViewById(R.id.logs);
        l2 = findViewById(R.id.followers);
        l3 = findViewById(R.id.following);
        filldetails();
        saved.setOnClickListener(this);
        logout.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l1.setOnClickListener(this);
        //logs.setOnClickListener(this);
        profile.setOnClickListener(this);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Upload the selected image to Firebase Storage
                        uploadImageToFirebaseStorage(uri);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        filldetails();
        setpfp();
    }

    public void filldetails(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.child("username").getValue(String.class);
                    dat = dataSnapshot.child("Date").getValue(String.class);
                    name.setText(username);
                    //date.setText("Joined : " + dat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error appropriately
            }
        });

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("posts");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                l1.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                count = 0;
                // Handle the error appropriately
            }
        });
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("Followers");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                l2.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                count = 0;
                // Handle the error appropriately
            }
        });
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("Following");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                l3.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                count = 0;
                // Handle the error appropriately
            }
        });
        setpfp();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logout){
            File file = new File(getApplicationContext().getFilesDir(), "User Details");
            file.delete();
            Intent intent = new Intent(ProfileActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(v.getId() == R.id.saved){
            Intent intent = new Intent(ProfileActivity.this, Saved.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.logs){
            Self.id = MainActivity.userId;
            Intent intent = new Intent(ProfileActivity.this, Self.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.followers || v.getId() == R.id.f1){
            FollowActivity.userId = MainActivity.userId;
            FollowActivity.node = "Followers";
            Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.following || v.getId() == R.id.f2){
            FollowActivity.userId = MainActivity.userId;
            FollowActivity.node = "Following";
            Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.profile_picture){
            imagePickerLauncher.launch("image/*");
            setpfp();
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        String userId = MainActivity.userId;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_pictures")
                .child(userId + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    storageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Update the user's profile with the image URL
                                updateProfileWithImageUrl(uri.toString());
                            });
                })
                .addOnFailureListener(e -> {
                });
        setpfp();
    }


    private void updateProfileWithImageUrl(String imageUrl) {
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId);
        userRef.child("profilePictureUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    //filldetails();
                    setpfp();
                    // Image URL updated successfully in the database
                })
                .addOnFailureListener(e -> {
                    setpfp();
                    // Handle failure to update image URL
                });
    }

    public void setpfp(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(MainActivity.userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                    ImageView pp = findViewById(R.id.profile_picture);
                    if (imageUrl != null) {
                        // Use an image loading library to display the image
                        Glide.with(ProfileActivity.this)
                                .load(imageUrl)
                                .into(pp);
                    } else {
                        ImageView ppp = findViewById(R.id.profile_picture);
                        ppp.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if necessary
                ImageView ppp = findViewById(R.id.profile_picture);
                ppp.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
    }
}
