package com.example.metagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder> implements View.OnClickListener{

    Context context;
    List<Post> list;
    static listener object;
    static String URL = "";

    public PostAdapter(Context context, List<Post> list, listener listener) {
        this.context = context;
        this.list = list;
        object = listener;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.homepagepost, parent, false);
        return new Holder(view, object);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.like.setImageResource(R.drawable.like_default);
        holder.saved.setImageResource(R.drawable.baseline_save_25);
        String ID = list.get(position).getID().substring(0, list.get(position).getID().indexOf(' '));
        DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("users").child(ID).child("profilePictureUrl");
        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePictureUrl = dataSnapshot.getValue(String.class);
                    Glide.with(context)
                            .load(profilePictureUrl) // Use the profile picture URL from the User model
                            .into(holder.dp);
                    //System.out.println("Profile Picture URL: " + profilePictureUrl);
                } else {
                    holder.dp.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                holder.dp.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts").child(ID);
        ref.child("post_likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(MainActivity.userId)){
                    holder.like.setImageResource(R.drawable.like_liked);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("saved");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(ID)) {
                    holder.saved.setImageResource(R.drawable.baseline_save_24);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Glide.with(context)
                .load(list.get(position).getPost()) // Use the profile picture URL from the User model
                .into(holder.post);
        holder.name.setText(list.get(position).getName());
        holder.caption.setText(list.get(position).getCaption());
        holder.likes.setText(list.get(position).getLikes());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        ImageView dp, post;
        TextView name, caption, likes;

        ImageView like, comment, saved;
        public Holder(@NonNull View itemView, listener object) {
            super(itemView);
            dp = itemView.findViewById(R.id.image);
            post = itemView.findViewById(R.id.showimage);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            saved = itemView.findViewById(R.id.save);
            likes = itemView.findViewById(R.id.likes);
            like.setOnClickListener(new View.OnClickListener(){
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
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 1);
                        }
                    }
                }
            });
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 2);
                        }
                    }
                }
            });
            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 3);
                        }
                    }
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 3);
                        }
                    }
                }
            });
            post.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 4);
                        }
                    }
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(object != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            object.OnItemClick(position, 0);
//                        }
//                    }
//                }
//            });
        }
    }
}
