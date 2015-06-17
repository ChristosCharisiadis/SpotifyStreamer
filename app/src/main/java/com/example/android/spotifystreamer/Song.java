package com.example.android.spotifystreamer;

/**
 Class for each song with it's name, album, url and images.
 */
public class Song {
    private String trackName;
    private String albumName;
    private String bigImage;
    private String smallImage;
    private String url;

    public Song(String trackName, String albumName, String bigImage, String smallImage, String url) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.bigImage = bigImage;
        this.smallImage = smallImage;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public String getBigImage() {
        return bigImage;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
