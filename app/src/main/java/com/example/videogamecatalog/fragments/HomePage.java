package com.example.videogamecatalog.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private Spinner yearSpinner;
    private Spinner genreSpinner;
    private Spinner platformSpinner;
    private SearchView searchView;
    private List<Game> gameList;
    private List<Game> filteredList;
    private List<String> genreList = new ArrayList<>();
    private List<String> yearsList = new ArrayList<>();

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
        searchView = view.findViewById(R.id.search);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        genreSpinner = view.findViewById(R.id.genreSpinner);
        platformSpinner = view.findViewById(R.id.platformSpinner);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gamesRef = database.getReference("games");

        // List to store Game objects
        gameList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // Attach a ValueEventListener to the "games" node
        gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
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
                    if (counter < 300) {
                        gameList.add(game);
                        System.out.println("Game date: " + game.getRelease_date());
                    }
                    counter++;
                    //System.out.println("Game url: " + game.getUrl_picture());
                }
                NavController navController = Navigation.findNavController(requireView());
                adapter = new GameAdapter(getContext(), gameList, navController);
                recyclerView.setAdapter(adapter);
                setupYearSpinner(); // Call setupYearSpinner() here to ensure yearsList is populated
                setupGenreSpinner();
                setupPlatformSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error getting game data: " + databaseError.getMessage());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void setupYearSpinner() {
        List<String> years = new ArrayList<>();
        // Get unique release years from the game list
        for (Game game : gameList) {
            String year = game.getRelease_date();
            if (!years.contains(year)) {
                years.add(year);
            }
        }
        Collections.sort(years, new Comparator<String>() {
            @Override
            public int compare(String year1, String year2) {
                if (year1 == null && year2 == null) {
                    return 0;
                } else if (year1 == null) {
                    return -1;
                } else if (year2 == null) {
                    return 1;
                } else {
                    int yearInt1 = Integer.parseInt(year1.substring(0, 4));
                    int yearInt2 = Integer.parseInt(year2.substring(0, 4));
                    return Integer.compare(yearInt1, yearInt2);
                }
            }
        });

        // Ensure that years list does not contain null elements
        yearsList = new ArrayList<>();
        yearsList.add("All Years");
        for (String year : years) {
            if (year != null) {
                yearsList.add(year);
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearsList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(spinnerAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                applyFilters(); // Apply filters when year selection changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void setupGenreSpinner() {
        List<String> genres = new ArrayList<>();
        genres.add("All Genres");
        // Get unique genres from the game list
        for (Game game : gameList) {
            for (String genre : game.getGenres()) {
                if (!genres.contains(genre)) {
                    genres.add(genre);
                }
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genres);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(spinnerAdapter);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                applyFilters(); // Apply filters when genre selection changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void setupPlatformSpinner() {
        List<String> platforms = new ArrayList<>();
        platforms.add("All Platforms");
        // Get unique platforms from the game list
        for (Game game : gameList) {
            for (String platform : game.getPlatforms()) {
                if (!platforms.contains(platform)) {
                    platforms.add(platform);
                }
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, platforms);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        platformSpinner.setAdapter(spinnerAdapter);
        platformSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                applyFilters(); // Apply filters when platform selection changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    // Method to filter games by selected year
    private void filterByYear(String year) {
        filteredList.clear();
        for (Game game : gameList) {
            String releaseDate = game.getRelease_date();
            if (releaseDate != null && releaseDate.startsWith(year)) {
                filteredList.add(game);
            }
        }
        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }

    private void filterByGenre(String genre) {
        filteredList.clear();
        for (Game game : gameList) {
            // Check if the game's genres contain the selected genre
            if (game.getGenres() != null && game.getGenres().contains(genre)) {
                filteredList.add(game);
            }
        }
        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }

    private void filterByPlatform(String platform) {
        filteredList.clear();
        for (Game game : gameList) {
            // Check if the game's genres contain the selected genre
            if (game.getPlatforms() != null && game.getPlatforms().contains(platform)) {
                filteredList.add(game);
            }
        }
        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }

    
    private void filterByYearGenreAndPlatform(String year, String genre, String platform) {
        filteredList.clear();
        for (Game game : gameList) {
            // Check if the game's genre matches the selected genre
            if (game.getGenres() != null && game.getGenres().contains(genre)) {
                // Check if the game's release year matches the selected year
                if (year.equals("All Years") || game.getRelease_date().startsWith(year)) {
                    // Check if the game's platforms contain the selected platform
                    if (platform.equals("All Platforms") || game.getPlatforms().contains(platform)) {
                        filteredList.add(game);
                    }
                }
            }
        }
        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }
    private void filter(String query) {
        filteredList.clear();
        for (Game game : gameList) {
            // Filter by game name containing the query
            if (game.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(game);
            }
        }
        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }

    private void applyFilters() {
        String selectedYear = yearSpinner.getSelectedItem().toString();
        String selectedGenre = genreSpinner.getSelectedItem().toString();
        String selectedPlatform = platformSpinner.getSelectedItem().toString();
        String query = searchView.getQuery().toString().toLowerCase();

        List<Game> filteredList = new ArrayList<>();

        for (Game game : gameList) {
            String releaseDate = game.getRelease_date();
            if (releaseDate != null) {
                boolean matchesYear = selectedYear.equals("All Years") || releaseDate.startsWith(selectedYear);
                boolean matchesGenre = selectedGenre.equals("All Genres") || game.getGenres().contains(selectedGenre);
                boolean matchesPlatform = selectedPlatform.equals("All Platforms") || game.getPlatforms().contains(selectedPlatform);
                boolean matchesQuery = query.isEmpty() || game.getName().toLowerCase().contains(query);

                if (matchesYear && matchesGenre && matchesPlatform && matchesQuery) {
                    filteredList.add(game);
                }
            }
        }

        // Update the adapter with the filtered list
        adapter.filterList(filteredList);
    }



}
