package com.tronpc.audiorecorder;
import java.util.List;


public class Music {
    List<OnlineFile> music;

    public Music(List<OnlineFile> music) {
        this.music = music;
    }

    public List<OnlineFile> getMusic() {
        return music;
    }
}
