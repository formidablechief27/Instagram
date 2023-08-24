package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class ProfileStalking extends AppCompatActivity implements View.OnClickListener {
    TextView name;
    TextView date;
    TextView days;
    Button history;
    long count =0;
    String username = "";
    String dat = "";
    TextView l1;
    TextView l2;
    TextView l3;
    TextView logs;
    static String userId="";
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    Button follow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilestalk);
        name = findViewById(R.id.username);
        date = findViewById(R.id.date);
        l1 = findViewById(R.id.logs);
        l2 = findViewById(R.id.followers);
        l3 = findViewById(R.id.following);
        logs = findViewById(R.id.logs2);
        follow = findViewById(R.id.followbutton);
        layout1 = findViewById(R.id.l1);
        layout2 = findViewById(R.id.f1);
        layout3 = findViewById(R.id.f2);
        filldetails();
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        logs.setOnClickListener(this);
        l1.setOnClickListener(this);
        follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.logs || v.getId() == R.id.logs2 || v.getId() == R.id.l1){
//            PreviousLogs.userId = ProfileStalking.userId;
//            Intent intent = new Intent(ProfileStalking.this, PreviousLogs.class);
//            startActivity(intent);
//        }
        if(v.getId() == R.id.followers || v.getId() == R.id.followers2 || v.getId() == R.id.f1){
            FollowActivity.userId = ProfileStalking.userId;
            FollowActivity.node = "Followers";
            Intent intent = new Intent(ProfileStalking.this, FollowActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.following || v.getId() == R.id.following2 || v.getId() == R.id.f2){
            FollowActivity.userId = ProfileStalking.userId;
            FollowActivity.node = "Following";
            Intent intent = new Intent(ProfileStalking.this, FollowActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.followbutton){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("Following");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(ProfileStalking.userId)) {
                        databaseReference.child(ProfileStalking.userId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileStalking.this, "Following Removed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // An error occurred while removing the key
                                    }
                                });
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId).child("Followers");
                        databaseReference1.child(MainActivity.userId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // mission accomplished
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // An error occurred while removing the key
                                    }
                                });
                        follow.setText("Follow");
                    } else {
                        databaseReference.child(ProfileStalking.userId).setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileStalking.this, "Following Added", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // An error occurred while adding the key
                                    }
                                });
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId).child("Followers");
                        databaseReference1.child(MainActivity.userId).setValue("0")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // mission accomplished
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // An error occurred while adding the key
                                    }
                                });
                        follow.setText("Following");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //nothing
                }
            });
            Intent intent = new Intent(ProfileStalking.this, ProfileStalking.class);
            startActivity(intent);
            finish();
        }
    }

    public void filldetails(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.child("username").getValue(String.class);
                    dat = dataSnapshot.child("Date").getValue(String.class);
                    name.setText(username);
                    date.setText("Joined : " + dat);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error appropriately
            }
        });

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId).child("Logs");
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
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId).child("Followers");
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
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(ProfileStalking.userId).child("Following");
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("Following");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(ProfileStalking.userId)) {
                    follow.setText("Following");
                } else {
                    follow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                follow.setText("Follow");
            }
        });
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(ProfileStalking.userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                    ImageView pp = findViewById(R.id.profile_picture);
                    if (imageUrl != null) {
                        // Use an image loading library to display the image
                        Glide.with(ProfileStalking.this)
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
