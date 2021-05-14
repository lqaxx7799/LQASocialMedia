package com.example.lqasocialmedia.datasource;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.model.User;

public class PostDataSourceFactory extends DataSource.Factory<String, Post>{
    @NonNull
    @Override
    public DataSource<String, Post> create() {
        return new PostDataSource();
    }
}
