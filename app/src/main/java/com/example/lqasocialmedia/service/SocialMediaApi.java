package com.example.lqasocialmedia.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SocialMediaApi {
    public static SocialMediaService createSocialMediaService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost:8888");

        return builder.build().create(SocialMediaService.class);
    }
}
