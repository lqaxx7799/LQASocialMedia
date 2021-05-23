package com.example.lqasocialmedia.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.activity.AccountListActivity;
import com.example.lqasocialmedia.adapter.AccountListRecyclerViewAdapter;
import com.example.lqasocialmedia.model.Account;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSearchFragment extends Fragment {
    private EditText txtSearch;
    private Button btnSearch;
    private RecyclerView searchListRecyclerView;
    private AccountListRecyclerViewAdapter accountListRecyclerViewAdapter;
    private View v;

    private SocialMediaService socialMediaService;

    public HomeSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home_search, container, false);

        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        txtSearch = v.findViewById(R.id.txtSearch);
        btnSearch = v.findViewById(R.id.btnSearch);
        searchListRecyclerView = v.findViewById(R.id.searchListRecyclerView);

        accountListRecyclerViewAdapter = new AccountListRecyclerViewAdapter(getActivity());
        searchListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        searchListRecyclerView.setAdapter(accountListRecyclerViewAdapter);

        accountListRecyclerViewAdapter.setData(new ArrayList<>());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtSearch.getText().toString();
                if (text.equals("")) {
                    accountListRecyclerViewAdapter.setData(new ArrayList<>());
                    return;
                }
                trySearch(text);
            }
        });

        return v;
    }

    private void trySearch(String text) {
        socialMediaService.getByName(text).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                accountListRecyclerViewAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }
}