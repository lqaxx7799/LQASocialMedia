package com.example.lqasocialmedia.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.adapter.PostRecyclerViewAdapter;
import com.example.lqasocialmedia.viewmodel.PostViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class HomePostFragment extends Fragment {
    public static final String TAG = "HomePostFragment";
    private ProgressBar loadingPost;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView postRecyclerView;
    private LinearLayoutManager postLayoutManager;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private View v;

    private PostViewModel viewModel;

    public HomePostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_post, container, false);

        loadingPost = v.findViewById(R.id.loadingPost);

        postRecyclerView = v.findViewById(R.id.postRecyclerView);
        postLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        postRecyclerView.setLayoutManager(postLayoutManager);

        postRecyclerViewAdapter = new PostRecyclerViewAdapter(getActivity());
        postRecyclerView.setAdapter(postRecyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        viewModel.posts.observe(this, pagedList -> {
            postRecyclerViewAdapter.submitList(pagedList);
        });
        viewModel.networkState.observe(this, networkState -> {
            postRecyclerViewAdapter.setNetworkState(networkState);
            Log.d(TAG, "Network State Change");
        });



//        viewModel.getPagedListObservable().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(pagedList -> postRecyclerViewAdapter.submitList(pagedList));

//        postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (loadingPost.getVisibility() == View.VISIBLE) {
//                    return;
//                }
//                if (!recyclerView.canScrollVertically(1)) { //1 for down
//                    loadingPost.setVisibility(View.VISIBLE);
////                    postLayoutManager.scrollToPosition(postRecyclerViewAdapter.getItemCount());
//                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                            new Runnable() {
//                                public void run() {
//                                    postRecyclerViewAdapter.appendData(getData());
//                                    loadingPost.setVisibility(View.GONE);
//                                }
//                            },
//                            2000
//                    );
//                }
//            }
//        });

//        swipeContainer = v.findViewById(R.id.swipeContainer);
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                        new Runnable() {
//                            public void run() {
//                                List<Post> data = getData();
//                                postRecyclerViewAdapter.setData(data);
//                                swipeContainer.setRefreshing(false);
//                            }
//                        },
//                        1000
//                );
//            }
//        });

//        new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                new Runnable() {
//                    public void run() {
//                        List<Post> data = getData();
//                        postRecyclerViewAdapter.setData(data);
//                        loadingPost.setVisibility(View.GONE);
//                    }
//                },
//                1000
//        );

        return v;
    }

//    private List<Post> getData() {
//        List<Post> data = new ArrayList<>();
//        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg", true, "This a caption"));
//        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://lokeshdhakar.com/projects/lightbox2/images/image-3.jpg", true, "This a caption"));
//        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://cdn.wallpapersafari.com/38/45/KftFVL.jpg", true, "This a caption"));
//        return data;
//    }
}