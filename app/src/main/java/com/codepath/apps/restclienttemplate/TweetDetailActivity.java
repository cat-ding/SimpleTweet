package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";
    public static final int RADIUS = 30;

    TextView tvBody;
    TextView tvScreenName;
    TextView tvRelativeTime;
    ImageView ivProfileImage;
    ImageView ivMedia;
    ImageButton btnReply;
    ImageButton btnRetweet;
    ImageButton btnFavorite;
    long id;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply View Binding library
        final ActivityTweetDetailBinding binding = ActivityTweetDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        id = tweet.id;
        binding.tvBody.setText(tweet.body);
        binding.tvRelativeTime.setText(tweet.relativeTime);
        binding.tvScreenName.setText("@" + tweet.user.screenName);

        if (tweet.favorited) {
            binding.btnFavorite.setImageResource(R.drawable.ic_vector_heart);
            binding.btnFavorite.setTag(R.drawable.ic_vector_heart);
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
            binding.btnFavorite.setTag(R.drawable.ic_vector_heart_stroke);
        }

        if (tweet.retweeted) {
            binding.btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
            binding.btnRetweet.setTag(R.drawable.ic_vector_retweet);
        } else {
            binding.btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
            binding.btnRetweet.setTag(R.drawable.ic_vector_retweet_stroke);
        }

        Glide.with(this).load(tweet.user.profileImageUrl).circleCrop().into(binding.ivProfileImage);
        if (tweet.mediaImageUrl != null)
            Glide.with(this).load(tweet.mediaImageUrl).transform(new RoundedCorners(RADIUS)).into(binding.ivMedia);
        else
            binding.ivMedia.setVisibility(View.GONE);

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposeFragment dialog = ComposeFragment.newInstance("@" + tweet.user.screenName);
                dialog.show(getSupportFragmentManager(), "ComposeFragment");
            }
        });

        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            TwitterClient client = TwitterApp.getRestClient(getApplicationContext());
            @Override
            public void onClick(View view) {
                if ((int) binding.btnFavorite.getTag() == R.drawable.ic_vector_heart_stroke) {
                    client.favoriteTweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess: in favoriting!");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: in favoriting!", throwable);
                        }
                    });
                    tweet.favorited = true;
                    binding.btnFavorite.setImageResource(R.drawable.ic_vector_heart);
                    binding.btnFavorite.setTag(R.drawable.ic_vector_heart);
                } else {
                    client.unfavoriteTweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess: in unfavoriting!");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: in unfavoriting!", throwable);
                        }
                    });
                    tweet.favorited = false;
                    binding.btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                    binding.btnFavorite.setTag(R.drawable.ic_vector_heart_stroke);
                }
            }
        });

        binding.btnRetweet.setOnClickListener(new View.OnClickListener() {
            TwitterClient client = TwitterApp.getRestClient(getApplicationContext());
            @Override
            public void onClick(View view) {
                if ((int) binding.btnRetweet.getTag() == R.drawable.ic_vector_retweet_stroke) {
                    client.retweetTweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess: in retweeting!");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: in retweeting!", throwable);
                        }
                    });
                    tweet.retweeted = true;
                    binding.btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    binding.btnRetweet.setTag(R.drawable.ic_vector_retweet);
                } else {
                    client.unretweetTweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess: in unretweeting!");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure: in unretweeting!", throwable);
                        }
                    });
                    tweet.retweeted = false;
                    binding.btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                    binding.btnRetweet.setTag(R.drawable.ic_vector_retweet_stroke);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // transmitting tweet object back to TimelineActivity
        Intent intent = new Intent();
        intent.putExtra("updatedTweet", Parcels.wrap(tweet));
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}