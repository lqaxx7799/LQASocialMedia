package com.example.lqasocialmedia.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.adapter.HomeBottomNavigationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private Session session;

    private ViewPager homePager;
    private BottomNavigationView homeBottomNav;
    private HomeBottomNavigationAdapter homeBottomNavigationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session = new Session(this);

        initView();

        homeBottomNavigationAdapter = new HomeBottomNavigationAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        homePager.setAdapter(homeBottomNavigationAdapter);

        homeBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeMenuPost:
                        homePager.setCurrentItem(0);
                        break;
                    case R.id.homeMenuSearch:
                        homePager.setCurrentItem(1);
                        break;
                    case R.id.homeMenuAdd:
                        homePager.setCurrentItem(2);
                        break;
                    case R.id.homeMenuNoti:
                        homePager.setCurrentItem(3);
                        break;
                    case R.id.homeMenuAccount:
                        homePager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeOptionSettings:
                break;
            case R.id.homeOptionLogOut:
                session.setToken("");
                session.setAccountId(0);
                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        homePager = findViewById(R.id.homePager);
        homeBottomNav = findViewById(R.id.homeBottomNav);
    }
}