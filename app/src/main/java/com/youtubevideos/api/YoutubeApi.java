package com.youtubevideos.api;


import com.youtubevideos.api.model.VideoResponse;
import com.youtubevideos.api.model.YoutubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApi {
    @GET("search")
    Call<YoutubeResponse> searchVideo(@Query("type") String type,
                                      @Query("videoCategoryId") String categoryId,
                                      @Query("key") String key,
                                      @Query("part") String part,
                                      @Query("order") String order,
                                      @Query("maxResults") String maxResults,
                                      @Query("pageToken") String pageToken);

    @GET("videos")
    Call<VideoResponse> videoDetails(@Query("part") String type,
                                     @Query("id") String categoryId,
                                     @Query("key") String key);
}
