package com.tronpc.audiorecorder;

public class OnlineFile {

    private String title;
    private String album;
    private String artist;
    private String genre;
    private String source;
    private String image;
    private int trackNumber;
    private int totalTrackCount;
    private int duration;
    private String site;

    public OnlineFile(String title, String album, String artist, String genre, String source, String image, int trackNumber, int totalTrackCount, int duration, String site) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.genre = genre;
        this.source = source;
        this.image = image;
        this.trackNumber = trackNumber;
        this.totalTrackCount = totalTrackCount;
        this.duration = duration;
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getSource() {
        return source;
    }

    public String getImage() {
        return image;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getTotalTrackCount() {
        return totalTrackCount;
    }

    public int getDuration() {
        return duration;
    }

    public String getSite() {
        return site;
    }

    //    "title" : "Jazz in Paris",
//            "album" : "Jazz & Blues",
//            "artist" : "Media Right Productions",
//            "genre" : "Jazz & Blues",
//            "source" : "Jazz_In_Paris.mp3",
//            "image" : "album_art.jpg",
//            "trackNumber" : 1,
//            "totalTrackCount" : 6,
//            "duration" : 103,
//            "site" : "https://www.youtube.com/audiolibrary/music"
}
