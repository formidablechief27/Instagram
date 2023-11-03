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

public class Pooja extends AppCompatActivity implements View.OnClickListener, listener {

    static String senderID = "";
    static String recieverID = "";

    TextView name;
    ImageView image;

    List<Message> list = new ArrayList<>();
    MessageAdapter adapter;
    RecyclerView recyclerView;

    String message = "";
    Button send;
    EditText text;

    TextView hext;
    TextView l1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pooja);
        name = findViewById(R.id.name);
        l1 = findViewById(R.id.l1);
        find(recieverID);
        image = findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher_foreground);
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
            text.setHint("Type a message ...");
            if(!msg.equals("") || !msg.equals(null)){
                //gmsg();
                KARO(senderID, msg);
                //list.clear();
                Intent intent = new Intent(Pooja.this, Pooja.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void KARO(String senderID, String message){
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10); // Generates a random digit from 0 to 9
            codeBuilder.append(randomDigit);
        }
        String userId = MainActivity.userId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(senderID).child("chats").child(recieverID);
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
        DatabaseReference userRef1 = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(recieverID).child("chats").child(senderID);
        userRef1.child(codeBuilder.toString()).setValue(MainActivity.uname + ":" + message)
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
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(senderID).child("chats").child(recieverID);
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

    public void find(String key) {
        String ans = "";
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(key).child("username");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                name.setText(value);//future.complete(value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                //future.completeExceptionally(error.toException());
            }
        });
    }

}
