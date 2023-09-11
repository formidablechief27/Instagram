package com.example.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    public PostAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.homepagepost, parent, false);
        return new PostAdapter.Holder(view, object);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.Holder holder, int position) {
        holder.dp.setImageResource(R.drawable.ic_launcher_foreground);
        Glide.with(context)
                .load(list.get(position).getPost()) // Use the profile picture URL from the User model
                .into(holder.post);
        holder.name.setText(list.get(position).getName() + " on : " + list.get(position).getDate());
        holder.caption.setText(list.get(position).getCaption());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        ImageView dp, post;
        TextView name, caption;
        public Holder(@NonNull View itemView, listener object) {
            super(itemView);
            dp = itemView.findViewById(R.id.image);
            post = itemView.findViewById(R.id.showimage);
            name = itemView.findViewById(R.id.name);
            caption = itemView.findViewById(R.id.caption);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            object.OnItemClick(position, 0);
                        }
                    }
                }
            });
        }
    }
}
