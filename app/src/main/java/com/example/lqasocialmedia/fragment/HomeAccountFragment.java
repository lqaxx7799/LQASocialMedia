package com.example.lqasocialmedia.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.activity.AccountListActivity;
import com.example.lqasocialmedia.adapter.AccountPostGridAdapter;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.model.Post;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAccountFragment extends Fragment {
    private TextView txtPostCount, txtFollowersCount, txtFollowingCount;
    private ShapeableImageView imgProfilePicture;
    private TextView lblUserName, lblBio;
    private LinearLayout layoutFollowers, layoutFollowing;
    private RecyclerView postGridRecyclerView;
    private AccountPostGridAdapter accountPostGridAdapter;
    private View v;

    private Session session;
    private SocialMediaService socialMediaService;

    public HomeAccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_account, container, false);

        session = new Session(getContext());
        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        initView();
        resetData();
        loadData();

        postGridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        accountPostGridAdapter = new AccountPostGridAdapter(getActivity());
        postGridRecyclerView.setAdapter(accountPostGridAdapter);

        initListeners();

        return v;
    }

    private void initView() {
        txtPostCount = v.findViewById(R.id.txtPostCount);
        txtFollowersCount = v.findViewById(R.id.txtFollowersCount);
        txtFollowingCount = v.findViewById(R.id.txtFollowingCount);
        imgProfilePicture = v.findViewById(R.id.imgProfilePicture);
        lblUserName = v.findViewById(R.id.lblUserName);
        lblBio = v.findViewById(R.id.lblBio);
        layoutFollowers = v.findViewById(R.id.layoutFollowers);
        layoutFollowing = v.findViewById(R.id.layoutFollowing);

        postGridRecyclerView = v.findViewById(R.id.postGridRecyclerView);
    }

    private void resetData() {
        txtPostCount.setText("0");
        txtFollowersCount.setText("0");
        txtFollowingCount.setText("0");
        lblUserName.setText("");
        lblBio.setText("");
        Glide.with(getActivity()).load(getResources().getString(R.string.default_profile_picture_url)).into(imgProfilePicture);
    }

    private void loadData() {
        socialMediaService.getAccountById(session.getAccountId()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if (account == null) {
                    return;
                }
                lblUserName.setText(account.getUserName());
                lblBio.setText(account.getBio());
                if (account.getProfilePictureUrl() == null || account.getProfilePictureUrl().equals("")) {
                    Glide.with(getActivity()).load(getResources().getString(R.string.default_profile_picture_url)).into(imgProfilePicture);
                } else {
                    String profilePictureUrl = getResources().getString(R.string.api_root) + "/images/" + account.getProfilePictureUrl();
                    Glide.with(getActivity()).load(profilePictureUrl).into(imgProfilePicture);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });

        socialMediaService.getFollowing(session.getAccountId()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                txtFollowingCount.setText(String.valueOf(response.body().size()));

                layoutFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AccountListActivity.class);
                        intent.putExtra("title", "Following");
                        intent.putExtra("data",  new Gson().toJson(response.body()));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });

        socialMediaService.getFollowed(session.getAccountId()).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                txtFollowersCount.setText(String.valueOf(response.body().size()));

                layoutFollowers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AccountListActivity.class);
                        intent.putExtra("title", "Followers");
                        intent.putExtra("data",  new Gson().toJson(response.body()));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });

        socialMediaService.getPostsByAccountId(session.getAccountId()).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                txtPostCount.setText(String.valueOf(response.body().size()));
                accountPostGridAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    private void initListeners() {

    }
}