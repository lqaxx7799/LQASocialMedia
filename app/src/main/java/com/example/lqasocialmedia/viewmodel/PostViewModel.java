package com.example.lqasocialmedia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.lqasocialmedia.datasource.FeedDataSourceFactory;
import com.example.lqasocialmedia.datasource.ItemKeyedFeedDataSource;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.repository.NetworkState;

import java.util.concurrent.Executor;

public class PostViewModel extends ViewModel {
    public LiveData<PagedList<Post>> posts;
    public LiveData<NetworkState> networkState;
    Executor executor;
    LiveData<ItemKeyedFeedDataSource> tDataSource;

    public PostViewModel() {
        FeedDataSourceFactory feedDataSourceFactory = new FeedDataSourceFactory(executor);

        tDataSource = feedDataSourceFactory.getMutableLiveData();

        networkState = Transformations.switchMap(feedDataSourceFactory.getMutableLiveData(), dataSource -> {
            return dataSource.getNetworkState();
        });

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        posts = (new LivePagedListBuilder<>(feedDataSourceFactory, pagedListConfig))
                .build();
    }
}
