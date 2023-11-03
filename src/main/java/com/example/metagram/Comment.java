package com.example.metagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Comment extends AppCompatActivity implements View.OnClickListener, listener {

    static String postId = "";
    List<Message> list = new ArrayList<>();
    MessageAdapter adapter;
    RecyclerView recyclerView;

    String message = "";
    Button send;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        recyclerView = findViewById(R.id.list);
        send = findViewById(R.id.send);
        text = findViewById(R.id.msg);
        send.setOnClickListener(this);
        fill();
    }

    @Override
    public void OnItemClick(int position, int source) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send){
            EditText text = findViewById(R.id.msg);
            String msg = text.getText().toString().trim();
            text.setHint("Add a Comment ...");
            if(!msg.equals("") && !msg.equals(null)){
                //gmsg();
                KARO(msg);
                //list.clear();
                Intent intent = new Intent(Comment.this, Comment.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void KARO(String message){
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10); // Generates a random digit from 0 to 9
            codeBuilder.append(randomDigit);
        }
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(Comment.postId).child("comments");
        userRef.child(codeBuilder.toString()).setValue(MainActivity.uname + ":" + message)
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


    public void show(){
        adapter = new MessageAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fill(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    String value = childSnapshot.getValue(String.class);
                    String nm = value.substring(0, value.indexOf(':'));
                    String ms = value.substring(value.indexOf(':') + 1, value.length());
                    list.add(new Message(nm, ms));
                    show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here.
            }
        });
    }
}
