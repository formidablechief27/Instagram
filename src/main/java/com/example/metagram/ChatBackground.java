package com.example.metagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metagram.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatBackground extends AppCompatActivity implements View.OnClickListener, listener{

    List<ChatBG> list = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbg);
        recyclerView = findViewById(R.id.list);
        fill();
    }

    public void show(){
        adapter = new ChatAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fill(){
        list.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(MainActivity.userId).child("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    try{
                        String ID = userSnapshot.getKey();
                        //String name = userSnapshot.child("name").getValue(String.class).trim();
                        list.add(new ChatBG(R.drawable.ic_launcher_foreground, ID, ID));
                        show();
                    }
                    catch(Exception e){
                    }
                }
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
        if(source == 0){
            Pooja.recieverID = list.get(position).getID();
            Pooja.senderID = MainActivity.userId;
            Intent intent = new Intent(ChatBackground.this, Pooja.class);
            startActivity(intent);
        }
    }
}
