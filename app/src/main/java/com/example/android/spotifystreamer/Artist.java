package com.example.android.spotifystreamer;

/*
Class for each Artist with his name id and image url.
 */
public class Artist {
    private String name;
    private String id;
    private String image;

    public Artist (String name, String id, String image) {
        this.name = name;
        this.id = id;
        this. image = image;
    }
    public String getName() {
        return name;
    }
    public String geetId() {
        return id;
    }
    public String getImage() {
        return image;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setId (String id) {
        this.id = id;
    }
    public void setImage (String image) {
        this.image = image;
    }
}
