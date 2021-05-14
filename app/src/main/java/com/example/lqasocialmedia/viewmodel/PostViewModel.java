package com.example.lqasocialmedia.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.example.lqasocialmedia.datasource.PostDataSourceFactory;
import com.example.lqasocialmedia.datasource.UserDataSourceFactory;

import io.reactivex.Observable;

public class PostViewModel extends ViewModel {
    private PostDataSourceFactory dataSourceFactory;
    private PagedList.Config config;
    public PostViewModel() {
        dataSourceFactory = new PostDataSourceFactory();

        config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(25).build();
    }

    public Observable<PagedList> getPagedListObservable(){
        return new RxPagedListBuilder(dataSourceFactory, config).buildObservable();
    }
}
