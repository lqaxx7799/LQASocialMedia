package com.example.lqasocialmedia.datasource;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.example.lqasocialmedia.model.User;

public class UserDataSourceFactory  extends DataSource.Factory<String, User>{
    @NonNull
    @Override
    public DataSource<String, User> create() {
        return new UserDataSource();
    }
}
