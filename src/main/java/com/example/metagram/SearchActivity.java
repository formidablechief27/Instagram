package com.example.metagram;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, listener {
    List<User> l2 = new ArrayList<>();
    RecyclerView list1;
    RecyclerView list2;
    EditText search1;
    ImageButton button1;
    static String value = "";
    static long count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        //init();
        list1 = findViewById(R.id.listuser);
        search1 = findViewById(R.id.textuser);
        button1 = findViewById(R.id.searchuser);
        button1.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        l2.clear();
        super.onResume();
    }

    public void show(){
        UserAdapter adapter = new UserAdapter(this, l2, this);
        list1.setAdapter(adapter);
        list1.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchuser){
            //l1 = new ArrayList<>();
            l2 = new ArrayList<>();
            EditText t = findViewById(R.id.textuser);
            value = t.getText().toString().trim().toLowerCase();
            if(value.equals("") || value.equals(null)){
                return;
            }
            t.setText("");
            //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //l1.clear();
                    l2.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class).trim();
                        String dateOfJoining = userSnapshot.child("Date").getValue(String.class).trim();
                        String UID = userSnapshot.getKey();
                        DatabaseReference logsReference = databaseReference.child(UID).child("Posts");
                        logsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                long count = dataSnapshot1.getChildrenCount();
                                if (find(username, value) && !UID.equals(MainActivity.userId)) {
                                    l2.add(new User(username, count, dateOfJoining, R.drawable.ic_launcher_foreground, UID));

                                    show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError1) {
                                if (find(username, value)&& !UID.equals(MainActivity.userId)) {
                                    l2.add(new User(username, 0, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
                                    show();
                                }
                            }
                        });
                    }
                    //show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if necessary
                }
            });
        }
//        else if (v.getId() == R.id.searchexercise){
//            EditText text = findViewById(R.id.textexercise);
//            value = text.getText().toString().trim().toLowerCase();
//            if(value.equals("") || value.equals(null)){
//                return;
//            }
//            l1 = new ArrayList<>();
//            l2 = new ArrayList<>();
//            for(int i=0;i<name.size();i++){
//                if(name.get(i).toLowerCase().contains(value)){
//                    l1.add(new Exercise(name.get(i), image.get(i), ppl.get(i), part.get(i)));
//                }
//            }
//            show();
//        }
    }
    @Override
    public void OnItemClick(int position, int source) {
        ProfileStalking.userId = l2.get(position).getID();
        if(ProfileStalking.userId.equals(MainActivity.userId)){

        }
        else {
            Intent intent = new Intent(SearchActivity.this, ProfileStalking.class);
            startActivity(intent);
        }
    }

    public boolean find(String username, String value){
        username = username.toLowerCase().trim();
        value = value.toLowerCase().trim();
        if(value.length() > username.length()){
            return false;
        }
        String match = username.substring(0, value.length());
        if(match.equalsIgnoreCase(value)){
            return true;
        }
        else{
            return false;
        }
    }
//
//    public void setpfp(String ID, ImageView image){
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(ID);
//
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String imageUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
//                    set(imageUrl, image);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error if necessary
//                set(null, image);
//            }
//        });
//    }
//    public void set(String imageUrl, ImageView image){
//        if (imageUrl != null) {
//            // Use an image loading library to display the image
//            Glide.with(SearchActivity.this)
//                    .load(imageUrl)
//                    .into(image);
//        } else {
//            image.setImageResource(R.drawable.ic_launcher_foreground);
//        }
//    }

}