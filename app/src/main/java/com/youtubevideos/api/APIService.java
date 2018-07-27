package com.youtubevideos.api;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class APIService {
    private static <LPL> LPL createService(Class<LPL> cls) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.BASE_URL).build();
        return retrofit.create(cls);
    }

    public static final YoutubeApi youtubeApi = createService(YoutubeApi.class);
}
