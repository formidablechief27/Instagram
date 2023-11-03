package com.example.metagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private EditText email;
    private EditText password;
    private Button login;
    private TextView register;

    public static boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.submit);
        register = findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        }

        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();
                            MainActivity.userId = userId;
                            Toast.makeText(Login.this, "Logging in ....", Toast.LENGTH_SHORT).show();
                            done = true;
                            getUserDetails(currentUser, email, password, userId);
                        } else {
                            Toast.makeText(Login.this, "Incorrect Email or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getUserDetails(FirebaseUser user, String email, String password, String userId) {
        if (user != null) {
            DatabaseReference userRef = mDatabase.child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String date = dataSnapshot.child("Date").getValue(String.class);
                        saveUserDetailsToFile(email, username, password, date);
                    }
                    GoBack();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void saveUserDetailsToFile(String email, String username, String password, String date) {
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

    public void GoBack() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
