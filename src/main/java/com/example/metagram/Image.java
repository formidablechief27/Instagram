package com.example.metagram;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Image extends AppCompatActivity implements View.OnClickListener, listener{

    ImageView image;
    TextView text;
    static String link = "";
    static String caption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewimage);
        image = findViewById(R.id.image);
        text = findViewById(R.id.caption);
        fill();
    }

    public void fill(){
        Glide.with(Image.this)
                .load(link) // Use the profile picture URL from the User model
                .into(image);
        text.setText(caption);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnItemClick(int position, int source) {

    }
}
