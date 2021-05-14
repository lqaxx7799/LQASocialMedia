package com.example.lqasocialmedia.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.adapter.AccountPostGridAdapter;
import com.example.lqasocialmedia.viewmodel.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeAccountFragment extends Fragment {
    private RecyclerView postGridRecyclerView;
    private AccountPostGridAdapter accountPostGridAdapter;
    private View v;

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
        View v = inflater.inflate(R.layout.fragment_home_account, container, false);

        postGridRecyclerView = v.findViewById(R.id.postGridRecyclerView);
        postGridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        accountPostGridAdapter = new AccountPostGridAdapter(getActivity());
        postGridRecyclerView.setAdapter(accountPostGridAdapter);

        List<Post> data = new ArrayList<>();
        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg", true, "This a caption"));
        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://lokeshdhakar.com/projects/lightbox2/images/image-3.jpg", true, "This a caption"));
        data.add(new Post(1, 1, "https://www.w3schools.com/howto/img_avatar.png", "qanh99", "https://cdn.wallpapersafari.com/38/45/KftFVL.jpg", true, "This a caption"));
        accountPostGridAdapter.setData(data);

        return v;
    }
}