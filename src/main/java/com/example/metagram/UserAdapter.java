package com.example.metagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.platforminfo.UserAgentPublisher;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> implements View.OnClickListener{
    Context context;
    List<User> list;
    static listener object;

    static String URL = "";

    public UserAdapter(Context context, List<User> list, listener object) {
        this.context = context;
        this.list = list;
        this.object = object;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.showuser, parent, false);
        return new Holder(view, object);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position){
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        Glide.with(context)
                .load(getProfilePictureUrl(list.get(position).getID())) // Use the profile picture URL from the User model
                .placeholder(R.drawable.ic_launcher_foreground) // Optional: Placeholder image while loading
                .error(R.drawable.ic_launcher_foreground) // Optional: Image to display if loading fails
                .into(holder.imageView);
        holder.textView1.setText(list.get(position).getUsername());
        holder.textView2.setText("Posts  = " + list.get(position).getLogs());
        holder.textView3.setText("Member Since " + list.get(position).getDate());
    }

    public String getProfilePictureUrl(String ID){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(ID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                    URL = imageUrl;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                URL = "";
            }
        });
        return URL;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1, textView2, textView3;
        public Holder(@NonNull View itemView, listener object) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView1 = itemView.findViewById(R.id.name);
            textView2 = itemView.findViewById(R.id.logs);
            textView3 = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 0);
                        }
                    }
                }
            });
        }
    }
}
