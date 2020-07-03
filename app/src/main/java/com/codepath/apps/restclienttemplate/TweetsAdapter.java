package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

import okhttp3.Headers;

// RecyclerView.Adapter<TweetsAdapter.ViewHolder>
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";
    public static final int RADIUS = 30;

    Context context;
    List<Tweet> tweets;

    // Pass in contex and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with the viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvRelativeTime;
        ImageView ivMedia;
        ImageButton btnReply;
        ImageButton btnFavorite;

        // this itemView represents one row in the recycler view (item_tweet)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);

            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            final String screenName = tweet.user.screenName;
            final long id = tweet.id;

            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + screenName);
            tvRelativeTime.setText(tweet.relativeTime);

            Glide.with(context).load(tweet.user.profileImageUrl).circleCrop().into(ivProfileImage);
            if (tweet.mediaImageUrl != null)
                Glide.with(context).load(tweet.mediaImageUrl).transform(new RoundedCorners(RADIUS)).into(ivMedia);
            else
                ivMedia.setVisibility(View.GONE);

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComposeFragment dialog = ComposeFragment.newInstance("@" + screenName);
                    FragmentManager fragmentManager = ((TimelineActivity) context).getSupportFragmentManager();
                    dialog.show(fragmentManager, "ComposeFragment");
                }
            });

            if (tweet.favorited) {
                btnFavorite.setImageResource(R.drawable.ic_vector_heart);
                btnFavorite.setTag(R.drawable.ic_vector_heart);
            } else {
                btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                btnFavorite.setTag(R.drawable.ic_vector_heart_stroke);
            }

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TwitterClient client = TwitterApp.getRestClient(context);

                    if ((int) btnFavorite.getTag() == R.drawable.ic_vector_heart_stroke) {
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
                        btnFavorite.setImageResource(R.drawable.ic_vector_heart);
                        btnFavorite.setTag(R.drawable.ic_vector_heart);
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
                        btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                        btnFavorite.setTag(R.drawable.ic_vector_heart_stroke);
                    }
                }
            });
        }

        // when the user clicks on a row, show TweetDetailActivity for the selected movie
        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();

            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailActivity.class);
                // serialize the tweet using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}