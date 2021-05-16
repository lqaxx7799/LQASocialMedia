package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.adapter.AccountListRecyclerViewAdapter;
import com.example.lqasocialmedia.model.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccountListActivity extends AppCompatActivity {
    private TextView lblAccountListTitle;
    private RecyclerView accountListRecyclerView;
    private AccountListRecyclerViewAdapter accountListRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        lblAccountListTitle = findViewById(R.id.lblAccountListTitle);
        accountListRecyclerView = findViewById(R.id.accountListRecyclerView);

        Intent intent = getIntent();
        lblAccountListTitle.setText(intent.getStringExtra("title"));

        accountListRecyclerViewAdapter = new AccountListRecyclerViewAdapter(this);
        accountListRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        accountListRecyclerView.setAdapter(accountListRecyclerViewAdapter);

        String dataJson = intent.getStringExtra("data");
        List<Account> data = new ArrayList<>();
        Type type = new TypeToken<List<Account>>() {
        }.getType();
        data = new Gson().fromJson(dataJson, type);
        accountListRecyclerViewAdapter.setData(data);
    }
}