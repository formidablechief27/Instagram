package com.example.metagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metagram.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Saved extends AppCompatActivity implements View.OnClickListener, listener {


    List<Post> list = new ArrayList<>();
    RecyclerView recyclerView;
    PostAdapter adapter;
    Post p;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved);
        recyclerView = findViewById(R.id.list);
        fill();
    }

    public void sort(){
        Comparator<Post> dateComparator = new Comparator<Post>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @Override
            public int compare(Post post1, Post post2) {
                try {
                    Date date1 = sdf.parse(post1.getDate());
                    Date date2 = sdf.parse(post2.getDate());

                    // Compare the dates
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // Handle parsing exception
                }
            }
        };
        Collections.sort(list, dateComparator);
        //adapter.notifyDataSetChanged();
    }

    public void show(){
        adapter = new PostAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fill(){
        list.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    try{
                        String ID = userSnapshot.getKey();
                        String url = userSnapshot.child("post_image").getValue(String.class).trim();
                        String caption = userSnapshot.child("post_caption").getValue(String.class).trim();
                        String UID = ID.substring(0, ID.indexOf(" "));
                        String date = ID.substring(ID.indexOf(" ") + 1, ID.length());
                        String name = userSnapshot.child("post_uploader").getValue(String.class).trim();
                        DataSnapshot ch = userSnapshot.child("post_likes");
                        int count = 0;
                        for(DataSnapshot it : ch.getChildren()){
                            count++;
                        }
                        DataSnapshot childs = userSnapshot.child("saved");
                        for(DataSnapshot items : childs.getChildren()){
                            if(items.getKey().equals(MainActivity.userId)){
                                list.add(new Post(R.drawable.ic_launcher_foreground, name, url, caption, date, ID, Integer.toString(count)));
                                break;
                            }
                        }
                    }
                    catch(Exception e){
                    }
                }
                sort();
                show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnItemClick(int position, int source) {
        if(source == 3){
            ProfileStalking.userId = list.get(position).getID().substring(0, list.get(position).getID().indexOf(' '));
            Intent intent = new Intent(Saved.this, ProfileStalking.class);
            startActivity(intent);
        }
        if(source == 0){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts").child(list.get(position).getID());
            ref.child("post_likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(MainActivity.userId)){
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("posts").child(list.get(position).getID());
                        ref.child(    "post_likes").child(MainActivity.userId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        adapter.notifyItemChanged(position);
                                        // The specific node has been removed successfully
                                        // You can add your code here for handling success
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                    else{
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").child(list.get(position).getID()).child("post_likes");
                        databaseReference.child(MainActivity.userId).setValue("0")
                                .addOnSuccessListener(aVoid -> {
                                    adapter.notifyItemChanged(position);
                                    // Image URL updated successfully in the database
                                })
                                .addOnFailureListener(e1 -> {
                                    // Handle failure to update image URL
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if(source == 1){
            Comment.postId = list.get(position).getID();
            Intent intent = new Intent(Saved.this, Comment.class);
            startActivity(intent);
        }
        if (source == 2) {
            String Id = list.get(position).getID();
            Comment.postId = Id;
            adapter.notifyItemChanged(position);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(Id).child("saved");
            databaseReference.child(MainActivity.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("posts").child(Comment.postId).child("saved");
                        databaseReference1.child(MainActivity.userId).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {

                                    // Handle the error
                                    Log.e("Firebase", "Removal failed: " + databaseError.getMessage());
                                } else {
                                    // Key has been removed successfully
                                    //Log.d("Firebase", keyToCheck + " removed successfully.");
                                }
                            }
                        });
                    } else {
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("posts").child(Comment.postId).child("saved");
                        databaseReference1.child(MainActivity.userId).setValue("..", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {

                                } else {
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error checking key: " + databaseError.getMessage());
                }
            });
        }
        if(source == 4){
            Image.link = list.get(position).getPost();
            Image.caption = list.get(position).getCaption();
            Intent intent = new Intent(Saved.this, Image.class);
            startActivity(intent);
        }
    }
}
