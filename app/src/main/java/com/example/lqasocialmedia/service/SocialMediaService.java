package com.example.lqasocialmedia.service;

import com.example.lqasocialmedia.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SocialMediaService {
    @GET("/feed")
    Call<List<Post>> getFeed(@Query("accountId") int accountId, @Query("page") int page);
}
