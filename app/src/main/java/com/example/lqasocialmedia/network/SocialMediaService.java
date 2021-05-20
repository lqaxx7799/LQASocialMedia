package com.example.lqasocialmedia.network;

import com.example.lqasocialmedia.dto.LogInDTO;
import com.example.lqasocialmedia.dto.SignUpDTO;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.model.Post;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SocialMediaService {
    @GET("/feed")
    Call<List<Post>> getFeed(@Query("id") int accountId, @Query("page") int page);

    @GET("/account/{id}")
    Call<Account> getAccountById(@Path("id") int accountId);

    @GET("/accounts/following/{id}")
    Call<List<Account>> getFollowing(@Path("id") int accountId);

    @GET("/accounts/followed/{id}")
    Call<List<Account>> getFollowed(@Path("id") int accountId);

    @GET("/posts/byAccount/{id}")
    Call<List<Post>> getPostsByAccountId(@Path("id") int accountId);

    @POST("/authentication/logIn")
    Call<String> logIn(@Body LogInDTO logInDTO);

    @POST("/authentication/signUp")
    Call<Account> signUp(@Body RequestBody data);
}
