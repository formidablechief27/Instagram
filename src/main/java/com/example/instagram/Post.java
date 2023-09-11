package com.example.instagram;

public class Post {
    int dp;
    String name;
    String post;
    String caption;
    String date;

    public Post(int dp, String name, String post, String caption, String date) {
        this.dp = dp;
        this.name = name;
        this.post = post;
        this.caption = caption;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDp() {
        return dp;
    }

    public void setDp(int dp) {
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
