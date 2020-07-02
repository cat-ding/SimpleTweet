package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    public static final int RADIUS = 30;

    TextView tvBody;
    TextView tvScreenName;
    TextView tvRelativeTime;
    ImageView ivProfileImage;
    ImageView ivMedia;
    ImageView btnReply;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply View Binding library
        ActivityTweetDetailBinding binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        binding.tvBody.setText(tweet.body);
        binding.tvRelativeTime.setText(tweet.relativeTime);
        binding.tvScreenName.setText("@" + tweet.user.screenName);

        Glide.with(this).load(tweet.user.profileImageUrl).circleCrop().into(binding.ivProfileImage);
        if (tweet.mediaImageUrl != null)
            Glide.with(this).load(tweet.mediaImageUrl).transform(new RoundedCorners(RADIUS)).into(binding.ivMedia);
        else
            binding.ivMedia.setVisibility(View.GONE);

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(TweetDetailActivity.class, ComposeActivity.class);
//                intent.putExtra("replyScreenName", tvScreenName);
//                startActivity(intent);
                Toast.makeText(TweetDetailActivity.this, "clicked on reply!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}