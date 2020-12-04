package com.tronpc.audiorecorder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "http://storage.googleapis.com/automotive-media/";
    @GET("music.json")
    Call<Music>getOnlineMusic();
}
