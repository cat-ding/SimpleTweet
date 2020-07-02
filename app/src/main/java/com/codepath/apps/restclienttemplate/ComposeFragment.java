package com.codepath.apps.restclienttemplate;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {

    public static final int MAX_TWEET_LENGTH = 280;

    private EditText etCompose;
    private Button btnTweet;
    private TextView tvCount;
    private TwitterClient client;

    public ComposeFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ComposeFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    String param1, String param2
    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "TWEEET", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}