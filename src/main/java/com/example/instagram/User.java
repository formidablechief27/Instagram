package com.example.instagram;

public class User {
    String username;
    long logs;
    String date;
    String ID;
    int image;

    public User(String username, long logs, String date, int image, String ID) {
        this.username = username;
        this.logs = logs;
        this.date = date;
        this.image = image;
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getLogs() {
        return logs;
    }

    public void setLogs(long logs) {
        this.logs = logs;
    }

}
