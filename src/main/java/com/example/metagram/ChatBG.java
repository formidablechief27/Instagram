package com.example.metagram;

public class ChatBG {
    String username;
    int image;

    String ID;

    public ChatBG(int image, String username, String ID) {
        this.image = image;
        this.username = username;
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
