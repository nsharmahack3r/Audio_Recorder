package com.tronpc.audiorecorder;

public class AudioItem {
    public String title;
    public String source;

    //The type can be either "sample" or "recording".
    public String type;

    public AudioItem(String title, String source, String type) {
        this.title = title;
        this.source = source;
        this.type = type;
    }
}
