package com.example.lqasocialmedia.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.activity.CreatePostActivity;
import com.example.lqasocialmedia.adapter.PostRecyclerViewAdapter;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePostFragment extends Fragment {
    public static final String TAG = "HomePostFragment";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView postRecyclerView;
    private FloatingActionButton fabCreatePost;
    private LinearLayoutManager postLayoutManager;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private View v;
    private SocialMediaService socialMediaService;
    private Session session;

    private List<Post> data;

    private boolean isLoading = false;
    private int currentPage = 0;
    private boolean isLastPage = false;

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

        initView();

        data = new ArrayList<>();

        postLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        postRecyclerView.setLayoutManager(postLayoutManager);

        postRecyclerViewAdapter = new PostRecyclerViewAdapter(getActivity(), data);
        postRecyclerView.setAdapter(postRecyclerViewAdapter);

        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        session = new Session(getContext());

        fabCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            }
        });

        initData();
        initScrollListener();
        initRefreshListener();

        return v;
    }

    private void initData() {
        loadFeed();
    }

    private void initScrollListener() {
        postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastPage) {
                    return;
                }

                if (!isLoading) {
                    if (postLayoutManager.findLastCompletelyVisibleItemPosition() == data.size() - 1) {
                        //bottom of list!
                        currentPage += 1;
                        loadFeed();
                    }
                }
            }
        });
    }

    private void initRefreshListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reset state
                data.removeAll(data);
                postRecyclerViewAdapter.notifyDataSetChanged();
                currentPage = 0;
                initData();
            }
        });
    }

    private void initView() {
        postRecyclerView = v.findViewById(R.id.postRecyclerView);
        swipeContainer = v.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fabCreatePost = v.findViewById(R.id.fabCreatePost);
    }

    private void loadFeed() {
        isLoading = true;
        data.add(null);
        postRecyclerViewAdapter.notifyItemInserted(data.size() - 1);

        socialMediaService.getFeed(session.getAccountId(), currentPage)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        data.remove(data.size() - 1);
                        int scrollPosition = data.size();
                        postRecyclerViewAdapter.notifyItemRemoved(scrollPosition);

                        isLoading = false;
                        swipeContainer.setRefreshing(false);

                        if (response.body() == null || response.body().isEmpty()) {
                            isLastPage = true;
                        } else {
                            data.addAll(response.body());
                            postRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        Log.i("LOAD ERROR", "Error at page " + currentPage + ", " + t.getMessage());
                        data.remove(data.size() - 1);
                        int scrollPosition = data.size();
                        postRecyclerViewAdapter.notifyItemRemoved(scrollPosition);;
                        isLoading = false;
                    }
                });

        // int id, String thumbnailUrl, String caption, boolean isDeleted, int accountId, Date createdAt
//        List<Post> data = new ArrayList<>();
//        data.add(new Post(1, "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg", "This a caption", false, 1, new Date()));
//        data.add(new Post(2, "https://lokeshdhakar.com/projects/lightbox2/images/image-3.jpg", "This is a caption", true, 1, new Date()));
//        data.add(new Post(3, "https://cdn.wallpapersafari.com/38/45/KftFVL.jpg", "This a caption", true, 1, new Date()));
//        return data;
    }
}