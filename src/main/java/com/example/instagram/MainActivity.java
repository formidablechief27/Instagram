package com.example.instagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, listener {
    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;

    public static FirebaseUser user;
    public static String userId;
    String date1;
    String date2;
    TextView display;
    String username;
    String password;
    String email;
    String date;
    TextView bug;
    static String s1="";
    static String s2="";
    static String s3="";
    static boolean flag = false;
    static int i = 0;
//    List<HomeLog> list = new ArrayList<>();

    RecyclerView recyclerView;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (Login.done == false) {
            CheckUser();
            Login.done = true;
        }
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date1 = currentDate;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String yesterdayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(yesterday);
        date2 = yesterdayDate;
        recyclerView = findViewById(R.id.list);
        bug = findViewById(R.id.bug);
        //fill();
        binding.bottomnav.setBackground(null);
        binding.bottomnav.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
//        list.clear();
//        if(Login.done == true)
//            fill();
        super.onResume();
//        //fill();
    }

    public void show(){
//        if(list.size() == 0) {
//            bug.setText("None of your following have logged in the past 2 days, Start my logging a workout or follow someone");
//        }
//        HomeAdapter adapter = new HomeAdapter(this, list, this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fill(){
//        //list.clear();
//        try{
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    list.clear();
//                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                        String username = userSnapshot.child("username").getValue(String.class).trim();
//                        String dateOfJoining = userSnapshot.child("Date").getValue(String.class).trim();
//                        String UID = userSnapshot.getKey();
//                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("Following");
//                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
//                                if (dataSnapshot1.hasChild(UID) || UID.equals(MainActivity.userId)) {
//                                    DatabaseReference logsReference = databaseReference.child(UID).child("Logs");
//                                    logsReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                                            if(dataSnapshot2.hasChild(date1)){
//                                                retrieveLogData(logsReference.child(date1), username, date1, UID);
////                                            list.add(new HomeLog(R.drawable.ic_launcher_foreground, username, date1, val1.size()>0?val1.get(0):"", val1.size()>1?val1.get(1):"", val1.size()>2?val1.get(2):"", UID));
////                                            show();
//                                                //bug.setText("Pooja Pooja Pooja");
//                                            }
//                                            if(dataSnapshot2.hasChild(date2)){
//                                                retrieveLogData(logsReference.child(date2), username, date2, UID);
////                                            list.add(new HomeLog(R.drawable.ic_launcher_foreground, username, date2, val1.size()>0?val1.get(0):"", val1.size()>1?val1.get(1):"", val1.size()>2?val1.get(2):"", UID));
////                                            show();
//                                            }
//                                            //list.add(new User(username, count, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError2) {
//                                            // bug.setText("Pooja Pooja Pooja");
//                                            //list.add(new User(username, 0, dateOfJoining, R.drawable.ic_launcher_foreground, UID));
//                                        }
//                                    });
//                                } else {
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                // Handle the error if necessary
//                            }
//                        });
//                    }
//                    //show();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle the error if necessary
//                }
//            });
//        }
//        catch(NullPointerException e){
//            Intent intent = new Intent(MainActivity.this, Welcome.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
    }
    public void CheckUser(){
        String filename = "User Details";
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            username = bufferedReader.readLine();
            email = bufferedReader.readLine();
            password = bufferedReader.readLine();
            date = bufferedReader.readLine();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();
                                userId = user.getUid();
                                Toast.makeText(MainActivity.this, "Logging in ....", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                //return;
                            }
                        }
                    });
        }
        catch(FileNotFoundException e){
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            //return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            Intent profileIntent = new Intent(MainActivity.this, MainActivity.class);
            profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(profileIntent);
        }
        else if (item.getItemId() == R.id.profile) {
            // Use Intent to navigate to ProfileActivity (change ProfileActivity with your desired activity class)
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }
//        else if (item.getItemId() == R.id.log) {
//            // Use Intent to navigate to SearchActivity (change SearchActivity with your desired activity class)
//            Intent searchIntent = new Intent(MainActivity.this, LogActivity.class);
//            startActivity(searchIntent);
//            return true;
//        }
        else if (item.getItemId() == R.id.search){
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        return false;
    }

    @Override
    public void OnItemClick(int position, int source) {
//        if(source == 0){
//            ReadLogs.userId = list.get(position).getUserId();
//            ReadLogs.date = list.get(position).getDate();
//            Intent intent = new Intent(MainActivity.this, ReadLogs.class);
//            startActivity(intent);
//            //finish();
//        }
//        if(source == 1){
//            ProfileStalking.userId = list.get(position).getUserId();
//            if(ProfileStalking.userId.equals(MainActivity.userId)){
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }
//            else {
//                Intent intent = new Intent(MainActivity.this, ProfileStalking.class);
//                startActivity(intent);
//            }
//            //finish();
//        }
    }

//    public void retrieveLogData(DatabaseReference logReference, String username, String date, String UID) {
//        ArrayList<String> val1 = new ArrayList<>();
//        ArrayList<String> val2 = new ArrayList<>();
//        logReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                int i = 0;
//                while (dataSnapshot2.hasChild(Integer.toString(i))) {
//                    String l = dataSnapshot2.child(Integer.toString(i)).getValue(String.class).trim();
//                    String[] logValues = l.split(" ");
//                    String name = "";
//                    for (int j = 0; j <= logValues.length - 4; j++) {
//                        name += logValues[j] + " ";
//                    }
//                    name = name.trim();
//                    String weight = logValues[logValues.length - 3];
//                    String reps = logValues[logValues.length - 2];
//                    if (val1.indexOf(name) == -1) {
//                        val1.add(name);
//                        val2.add(weight);
//                    }
//                    i++;
//                }
//                String val = val1.size()>2?val1.get(2) + " " + val2.get(2):"";
//                if(val1.size() > 3){
//                    val += "  ...";
//                }
//                list.add(new HomeLog(R.drawable.ic_launcher_foreground, username, date, val1.size()>0?val1.get(0)+" " + val2.get(0):"", val1.size()>1?val1.get(1)+" " + val2.get(1):"", val, UID));
//                show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error if necessary
//            }
//        });
//    }
}
