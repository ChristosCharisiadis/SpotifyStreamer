package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Artist.java - a class representing an artist
 *
 */
public class Artist implements Parcelable{
    private String name;
    private String id;
    private String image;

    /**
     * Default Constructor
     * @param name
     * @param id
     * @param image
     */
    public Artist (String name, String id, String image) {
        this.name = name;
        this.id = id;
        this. image = image;
    }

    /**
     * Constructor for parcelable interface
     * @param in
     */
    public Artist(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        this.name = data[0];
        this.id = data[1];
        this.image = data[2];
    }
    public String getName() {
        return name;
    }
    public String getId() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {name, id, image});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Artist createFromParcel (Parcel in) {
            return new Artist(in);
        }
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
