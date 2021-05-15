package com.example.lqasocialmedia.datasource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.repository.NetworkState;
import com.example.lqasocialmedia.repository.Status;
import com.example.lqasocialmedia.service.SocialMediaApi;
import com.example.lqasocialmedia.service.SocialMediaService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemKeyedFeedDataSource extends ItemKeyedDataSource<Integer, Post> {
    public static final String TAG = "ItemKeyedFeedDataSource";
    SocialMediaService socialMediaService;
    LoadInitialParams<Integer> initialParams;
    LoadParams<Integer> afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;

    public ItemKeyedFeedDataSource(Executor retryExecutor) {
        socialMediaService = SocialMediaApi.createSocialMediaService();
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.retryExecutor = retryExecutor;
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Post> callback) {
        Log.i(TAG, "Loading Rang " + 1 + " Count " + params.requestedLoadSize);
        List<Post> posts = new ArrayList<>();
        initialParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        socialMediaService.getFeed(1, 1).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    posts.addAll(response.body());
                    callback.onResult(posts);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    initialParams = null;
                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Post> callback) {
        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        List<Post> posts = new ArrayList<>();
        afterParams = params;

        networkState.postValue(NetworkState.LOADING);
        socialMediaService.getFeed(1, params.key).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    posts.addAll(response.body());
                    callback.onResult(posts);
                    networkState.postValue(NetworkState.LOADED);
                    afterParams = null;
                } else {
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                    Log.e("API CALL", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Post> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull Post item) {
        return item.getId();
    }

}
