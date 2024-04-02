package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videogamecatalog.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameDetail extends Fragment {

    private TextView titleTextView;
    private ImageView imageView;
    private TextView descTextView;

    private TextView releaseDateTextView;
    private TextView genereTextView;

    private TextView platformTextView;

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
        releaseDateTextView = view.findViewById(R.id.detailRelease);
        genereTextView = view.findViewById(R.id.detailGenre);
        platformTextView = view.findViewById(R.id.detailPlatform);
        descTextView = view.findViewById(R.id.detailDesc);
        //ImageButton backButton = view.findViewById(R.id.backButton);
        ImageView backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        // Retrieve arguments
        String gameName = getArguments().getString("game1Name");
        String gameSummary = getArguments().getString("game1Summery");
        String imageUrl = getArguments().getString("imageUrl1");
        String gameRelease = getArguments().getString("game1releaseDate");
        ArrayList<String> gameGenre = getArguments().getStringArrayList("game1genre");
        ArrayList<String> gamePlatform = getArguments().getStringArrayList("game1platform");
        StringBuilder genreStringBuilder = new StringBuilder();
        for (String genre : gameGenre) {
            if (genreStringBuilder.length() > 0) {
                genreStringBuilder.append(", "); // Add a comma and space before each genre
            }
            genreStringBuilder.append(genre);
        }
        StringBuilder platformStringBuilder = new StringBuilder();
        for (String platform : gamePlatform) {
            if (platformStringBuilder.length() > 0) {
                platformStringBuilder.append(", "); // Add a comma and space before each genre
            }
            platformStringBuilder.append(platform);
        }
        titleTextView.setText(gameName);
        releaseDateTextView.setText(gameRelease);
        genereTextView.setText(genreStringBuilder.toString());
        descTextView.setText(gameSummary);
        platformTextView.setText(platformStringBuilder.toString());
        Picasso.get().load(imageUrl).into(imageView);
        // Inflate the layout for this fragment
        return view;
    }
}