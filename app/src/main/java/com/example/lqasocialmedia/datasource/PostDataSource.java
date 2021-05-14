package com.example.lqasocialmedia.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.model.User;
import com.example.lqasocialmedia.repository.PostRepository;
import com.example.lqasocialmedia.repository.UserRepository;

public class PostDataSource extends ItemKeyedDataSource<String, Post> {
    private PostRepository postRepository;

    public PostDataSource() {
        postRepository = new PostRepository();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Post> callback) {
        postRepository.getAll(params.requestedInitialKey, params.requestedLoadSize, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {
        postRepository.getAll(params.key, params.requestedLoadSize, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Post item) {
        return item.getId();
    }
}
