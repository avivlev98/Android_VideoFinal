package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videogamecatalog.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameDetail extends Fragment {

    private TextView titleTextView;
    private ImageView imageView;
    private TextView descTextView;

    public GameDetail() {
        // Required empty public constructor
    }


    public static GameDetail newInstance(String param1, String param2) {
        GameDetail fragment = new GameDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);
        // Initialize views
        titleTextView = view.findViewById(R.id.detailTitle);
        imageView = view.findViewById(R.id.detailImage);
        descTextView = view.findViewById(R.id.detailDesc);

        // Retrieve arguments
        String gameName = getArguments().getString("game1Name");
        String gameSummary = getArguments().getString("game1Summery");
        String imageUrl = getArguments().getString("imageUrl1");
        titleTextView.setText(gameName);
        descTextView.setText(gameSummary);
        // Inflate the layout for this fragment
        return view;
    }
}