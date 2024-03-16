package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videogamecatalog.Classes.Game;
import com.example.videogamecatalog.R;
import com.example.videogamecatalog.adapters.GameAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter adapter;



    public HomePage() {
        // Required empty public constructor
    }


    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gamesRef = database.getReference("games");

// List to store Game objects
        List<Game> gameList = new ArrayList<>();

// Attach a ValueEventListener to the "games" node
        gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter =0;
                // Iterate through each child node under "games"
                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                    // Extract game data
                    String gameId = gameSnapshot.getKey();
                    String gameName = gameSnapshot.child("name").getValue(String.class);
                    String imageUrl = gameSnapshot.child("url_picture").getValue(String.class);
                    String summary = gameSnapshot.child("summary").getValue(String.class);
                    String releaseDate = gameSnapshot.child("release_date").getValue(String.class);
                    GenericTypeIndicator<ArrayList<String>> genreListType = new GenericTypeIndicator<ArrayList<String>>() {};
                    ArrayList<String> genres = gameSnapshot.child("genres").getValue(genreListType);
                    GenericTypeIndicator<ArrayList<String>> platformListType = new GenericTypeIndicator<ArrayList<String>>() {};
                    ArrayList<String> platforms = gameSnapshot.child("platforms").getValue(platformListType);
                    // Parse other game attributes similarly
                    imageUrl = imageUrl.replaceFirst("^//", "https://");
                    // Create a Game object
                    Game game = new Game(gameId, imageUrl, releaseDate, genres, platforms, summary, gameName);

                    // Add the Game object to the list
                    if (counter<50) {
                        gameList.add(game);
                        System.out.println("Game date: " + game.getRelease_date());
                    }
                    counter++;
                    //System.out.println("Game url: " + game.getUrl_picture());
                }
                adapter = new GameAdapter(getContext(),gameList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error getting game data: " + databaseError.getMessage());
            }
        });

//        for (int i = 0; i < gameList.size(); i++) {
//            Game game = gameList.get(i);
//            System.out.println("Game name: " + game.getName());
//        }
        // Inflate the layout for this fragment
        return view;
    }
}