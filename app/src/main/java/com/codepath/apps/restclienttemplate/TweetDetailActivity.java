package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    public static final int RADIUS = 30;

    TextView tvBody;
    TextView tvScreenName;
    TextView tvRelativeTime;
    ImageView ivProfileImage;
    ImageView ivMedia;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvRelativeTime = findViewById(R.id.tvRelativeTime);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivMedia = findViewById(R.id.ivMedia);

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvRelativeTime.setText(tweet.relativeTime);

        Glide.with(this).load(tweet.user.profileImageUrl).transform(new RoundedCorners(RADIUS)).into(ivProfileImage);
        if (tweet.mediaImageUrl != null)
            Glide.with(this).load(tweet.mediaImageUrl).transform(new RoundedCorners(RADIUS)).into(ivMedia);
        else
            ivMedia.setVisibility(View.GONE);
    }
}