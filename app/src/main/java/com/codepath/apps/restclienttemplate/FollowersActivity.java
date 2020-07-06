package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class FollowersActivity extends AppCompatActivity {

    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1; // Indicates that only the current fragment will be in the Lifecycle

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FollowersAdapter followersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        // setting up followers fragments
        tabLayout = (TabLayout) findViewById(R.id.tablayoutId);
        viewPager = (ViewPager) findViewById(R.id.viewpagerId);
        FragmentManager fm = this.getSupportFragmentManager();
        followersAdapter = new FollowersAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // adding followers fragments to the adapter
        followersAdapter.addFragment(new FragmentFollowers(), "Followers");
        followersAdapter.addFragment(new FragmentFollowing(), "Following");
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(followersAdapter);
    }
}