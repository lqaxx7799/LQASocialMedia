package com.example.lqasocialmedia.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.lqasocialmedia.fragment.HomeAccountFragment;
import com.example.lqasocialmedia.fragment.HomeNotiFragment;
import com.example.lqasocialmedia.fragment.HomePostFragment;
import com.example.lqasocialmedia.fragment.HomeSearchFragment;

public class HomeBottomNavigationAdapter extends FragmentStatePagerAdapter {
    private int numberOfPages = 4;
    public HomeBottomNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomePostFragment();
            case 1:
                return new HomeSearchFragment();
            case 2:
                return new HomeNotiFragment();
            case 3:
                return new HomeAccountFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.numberOfPages;
    }
}
