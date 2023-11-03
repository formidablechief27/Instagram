package com.example.metagram;

public class Story {
    String ID;
    String link;
    String caption;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Story(String ID, String link, String caption) {
        this.ID = ID;
        this.link = link;
        this.caption = caption;
    }
}
