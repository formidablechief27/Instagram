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

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.Holder> implements View.OnClickListener{
    Context context;
    List<Story> list;
    static listener object;

    static String URL = "";

    public StoryAdapter(Context context, List<Story> list, listener object) {
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
        View view = inflater.inflate(R.layout.story, parent, false);
        return new Holder(view, object);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position){
        holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        holder.textView.setText(list.get(position).getID());
        String UID = list.get(position).getID().substring(0, list.get(position).getID().indexOf(' '));
        DatabaseReference refer1 = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("username");
        refer1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePictureUrl = dataSnapshot.getValue(String.class);
                    holder.textView.setText(profilePictureUrl);
                    //System.out.println("Profile Picture URL: " + profilePictureUrl);
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("profilePictureUrl");
        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePictureUrl = dataSnapshot.getValue(String.class);
                    Glide.with(context)
                            .load(profilePictureUrl) // Use the profile picture URL from the User model
                            .into(holder.imageView);
                    //System.out.println("Profile Picture URL: " + profilePictureUrl);
                } else {
                    holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
//        Glide.with(context)
//                .load(getProfilePictureUrl(list.get(position).getID())) // Use the profile picture URL from the User model
//                .placeholder(R.drawable.ic_launcher_foreground) // Optional: Placeholder image while loading
//                .error(R.drawable.ic_launcher_foreground) // Optional: Image to display if loading fails
//                .into(holder.imageView);
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
        TextView textView;
        public Holder(@NonNull View itemView, listener object) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 5);
                        }
                    }
                }
            });
        }
    }
}
