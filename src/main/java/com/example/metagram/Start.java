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
import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Start extends AppCompatActivity implements View.OnClickListener, listener {

    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;

    public static FirebaseUser user;
    public static String userId;

    public static String uname;
    String date1;
    String date2;
    TextView display;
    String username;
    String password;
    String email;
    String date;
    Button secret;
    Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        secret = findViewById(R.id.secret);
        log = findViewById(R.id.submit);
        secret.setOnClickListener(this);
        log.setOnClickListener(this);
        String filename = "User Details";
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(filename);
        }
        catch(FileNotFoundException e){
            Intent intent = new Intent(Start.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.secret){
            Intent intent = new Intent(Start.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId() == R.id.submit){
            String filename = "User Details";
            try {
                FileInputStream fileInputStream = getApplicationContext().openFileInput(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                username = bufferedReader.readLine();
                uname = username;
                email = bufferedReader.readLine();
                password = bufferedReader.readLine();
                EditText text = findViewById(R.id.password);
                String p = text.getText().toString();
                if(p.equals(password)){
                    Intent intent = new Intent(Start.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Start.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
            catch(FileNotFoundException e){
                //return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void OnItemClick(int position, int source) {

    }
}
