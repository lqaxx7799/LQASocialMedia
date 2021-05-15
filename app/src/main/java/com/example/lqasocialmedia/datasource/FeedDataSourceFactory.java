package com.example.lqasocialmedia.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.concurrent.Executor;

public class FeedDataSourceFactory extends DataSource.Factory {
    MutableLiveData<ItemKeyedFeedDataSource> mutableLiveData;
    ItemKeyedFeedDataSource itemKeyedFeedDataSource;
    Executor executor;

    public FeedDataSourceFactory(Executor executor) {
        this.mutableLiveData = new MutableLiveData<ItemKeyedFeedDataSource>();
        this.executor = executor;
    }


    @Override
    public DataSource create() {
        itemKeyedFeedDataSource = new ItemKeyedFeedDataSource(executor);
        mutableLiveData.postValue(itemKeyedFeedDataSource);
        return itemKeyedFeedDataSource;
    }

    public MutableLiveData<ItemKeyedFeedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
