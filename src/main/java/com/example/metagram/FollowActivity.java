package com.example.metagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity implements View.OnClickListener, listener {
    static String node = "";
    static String userId = "";
    List<User> list = new ArrayList<>();
    RecyclerView recyclerView;
    static String value = "";

    EditText v1;
    ImageButton search;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        recyclerView = findViewById(R.id.listuser);
        v1 = findViewById(R.id.textuser);
        search = findViewById(R.id.searchuser);
        def();
        //show();
        search.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        def();
        //show();
    }

    public void show() {
        UserAdapter adapter = new UserAdapter(this, list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void def() {
        List<User> tempList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class).trim();
                    String dateOfJoining = userSnapshot.child("Date").getValue(String.class).trim();
                    String UID = userSnapshot.getKey();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(FollowActivity.userId).child(FollowActivity.node);
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.hasChild(UID)) {
                                DatabaseReference logsReference = databaseReference.child(UID).child("Logs");
                                logsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        long count = dataSnapshot1.getChildrenCount();
                                        User user = new User(username, count, dateOfJoining, R.drawable.ic_launcher_foreground, UID);
                                        if (!tempList.contains(user)) {
                                            tempList.add(user);
                                        }
                                        show();
                                        // Call show() method here if you want to update the RecyclerView immediately
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError1) {
                                        User user = new User(username, 0, dateOfJoining, R.drawable.ic_launcher_foreground, UID);
                                        if (!tempList.contains(user)) {
                                            tempList.add(user);
                                        }
                                        show();
                                        // Call show() method here if you want to update the RecyclerView immediately
                                    }
                                });
                            } else {
                                // Handle the case when the child doesn't exist
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error if necessary
                        }
                    });
                }
                // Assign tempList to list and call show() after iterating through all the data
                list = tempList;
                //show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if necessary
            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchuser){
            EditText t1 = findViewById(R.id.textuser);
            value = t1.getText().toString();
            if(value.equals(null) || value.equals("")){
                def();
                //show();
                return;
            }
            list = new ArrayList<>();
            //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class).trim();
                        String dateOfJoining = userSnapshot.child("Date").getValue(String.class).trim();
                        String UID = userSnapshot.getKey();
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(FollowActivity.userId).child(FollowActivity.node);
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.hasChild(UID)) {
                                    DatabaseReference logsReference = databaseReference.child(UID).child("Logs");
                                    logsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                            long count = dataSnapshot1.getChildrenCount();
                                            if (find(username, value)) {
                                                list.add(new User(username, count, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
                                                show();
                                            }
                                            //list.add(new User(username, count, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError1) {
                                            if (find(username, value)) {
                                                list.add(new User(username, 0, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
                                                show();
                                            }
                                            //list.add(new User(username, 0, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
                                        }
                                    });
                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error if necessary
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
    }

    @Override
    public void OnItemClick(int position, int source) {
        ProfileStalking.userId = list.get(position).getID();
        if(ProfileStalking.userId.equals(MainActivity.userId)){

        }
        else {
            Intent intent = new Intent(FollowActivity.this, ProfileStalking.class);
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
}
