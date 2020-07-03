package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {

    public interface OnFinishEditDialog {
        void sendTweet(Tweet tweet);
    }

    public OnFinishEditDialog onFinishEditDialog;

    public static final String TAG = "ComposeFragment";
    public static final int MAX_TWEET_LENGTH = 280;
    public static final int RESULT_CODE = 1;

    private EditText etCompose;
    private Button btnTweet;
    private TextView tvCount;
    private TwitterClient client;
    private String replyScreenName;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance(String screenName) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);

        etCompose = view.findViewById(R.id.etCompose);
        btnTweet = view.findViewById(R.id.btnTweet);
        tvCount = view.findViewById(R.id.tvCount);

        client = TwitterApp.getRestClient(getContext());

        etCompose.setText(getArguments().getString("screen_name"));
        updateCharsLeft();
        etCompose.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateCharsLeft();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                // Error handling first
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getContext(), "Sorry, your tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH ) {
                    Toast.makeText(getContext(), "Sorry, your tweet is too long!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), tweetContent, Toast.LENGTH_LONG).show();
                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet!");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet.body);

                            onFinishEditDialog.sendTweet(tweet);
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet!", throwable);
                    }
                });

            }
        });

        return view;
    }

    private void updateCharsLeft() {
        int tweetLength = etCompose.getText().toString().length();
        int charsLeft = MAX_TWEET_LENGTH - tweetLength;
        if (charsLeft >= 0)
            tvCount.setTextColor(Color.BLACK);
        else
            tvCount.setTextColor(Color.RED);
        String charsLeftMessage = Integer.toString(charsLeft) + " characters left";
        tvCount.setText(charsLeftMessage);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onFinishEditDialog = (OnFinishEditDialog) getActivity();
        } catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}