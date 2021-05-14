package com.example.lqasocialmedia.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.example.lqasocialmedia.model.User;
import com.example.lqasocialmedia.repository.UserRepository;

public class UserDataSource extends ItemKeyedDataSource<String, User> {
    private UserRepository userRepository;

    public UserDataSource() {
        userRepository = new UserRepository();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<User> callback) {
        userRepository.getAll(callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<User> callback) {
        userRepository.getAll(callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<User> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull User item) {
        return item.getId();
    }
}
