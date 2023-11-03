package com.example.metagram;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.TaskCompletionSource;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    EditText email;
    EditText username;
    EditText password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.register);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email= this.email.getText().toString().trim();
        String password= this.password.getText().toString().trim();
        String username= this.username.getText().toString().trim();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String filename = currentDate;
        Task<Boolean> validationTask = valid(username);
        validationTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isUnique = task.getResult();
                if (isUnique) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();
                                        String userId = user.getUid();
                                        MainActivity.userId = userId;
                                        MainActivity.uname = username;
                                        DatabaseReference userRef = mDatabase.child("users").child(userId);
                                        userRef.child("username").setValue(username);
                                        userRef.child("Date").setValue(filename);
                                        userRef.child("mail").setValue(email);
                                        FileEntry(email, username, password, filename);
                                        GoBack();
                                    } else {
                                        // Registration failed, display an error message
                                        Toast.makeText(Register.this, "Email already in Use or Weak Password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Register.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the task failure if necessary
            }
        });

    }

    public void FileEntry(String email, String username, String password, String date){
        File filesDir = getApplicationContext().getFilesDir();
        File myFile = new File(filesDir, "User Details");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(myFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(username);
            printWriter.println(email);
            printWriter.println(password);
            printWriter.println(date);
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void GoBack(){
        Intent intent = new Intent(Register.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public Task<Boolean> valid(String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isUnique = true;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class).trim();
                    if (username.equals(name)) {
                        isUnique = false;
                        break;
                    }
                }
                completionSource.setResult(isUnique);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                completionSource.setException(databaseError.toException());
            }
        });

        return completionSource.getTask();
    }



}
