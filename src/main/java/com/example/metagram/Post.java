package com.example.metagram;

public class Post {
    int dp;
    String name;
    String post;
    String caption;
    String date;
    String ID;
    String likes;

    public Post(int dp, String name, String post, String caption, String date, String ID, String likes) {
        this.dp = dp;
        this.name = name;
        this.post = post;
        this.caption = caption;
        this.date = date;
        this.ID = ID;
        this.likes = likes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
