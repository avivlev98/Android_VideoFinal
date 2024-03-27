package com.example.videogamecatalog.adapters;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videogamecatalog.R;
import com.example.videogamecatalog.Classes.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private Context context;
    private List<Game> gameList;


    private NavController navController;

    public GameAdapter(Context context, List<Game> gameList, NavController navController) {
        this.context = context;
        this.gameList = gameList;
        this.navController = navController;
    }
    public void filterList(List<Game> filteredList) {
        gameList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        int firstGameIndex = position * 2;
        int secondGameIndex = firstGameIndex + 1;

        Game game1 = gameList.get(firstGameIndex);
        Game game2 = null;

        if (secondGameIndex < gameList.size()) {
            game2 = gameList.get(secondGameIndex);
        }

        holder.bind(game1, game2);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                navController.navigate(R.id.action_homePage_to_gameDetail);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(gameList.size() / 2.0);
    }


    public class GameViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView1, imageView2;
        private TextView title1, title2, undertitle1, undertitle2;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
            undertitle1 = itemView.findViewById(R.id.undertitle1);
            undertitle2 = itemView.findViewById(R.id.undertitle2);
        }

        public void bind(Game game1, Game game2) {
            if (game1 != null) {
                // Set game1 data
                ArrayList<String> genres = game1.getGenres();
                title1.setText(game1.getName());
                undertitle1.setText(genres.get(0) +" • " + game1.getRelease_date());
                // Load image using your image loading library (e.g., Picasso, Glide)
                // Example with Picasso:
                Picasso.get().load(game1.getUrl_picture()).into(imageView1);
                String game1Name = game1.getName();
                String imageUrl1 = game1.getUrl_picture();
                String game1Summery = game1.getSummary();
                imageView1.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putString("game1Name", game1Name);
                    args.putString("imageUrl1", imageUrl1);
                    args.putString("game1Summery", game1Summery);
                    // Handle click for game1
                    navController.navigate(R.id.action_homePage_to_gameDetail, args);
                });
            }

            if (game2 != null) {
                // Set game2 data
                ArrayList<String> genres = game2.getGenres();
                title2.setText(game2.getName());
                undertitle2.setText(genres.get(0) +" • " + game2.getRelease_date());
                // Load image using your image loading library (e.g., Picasso, Glide)
                // Example with Picasso:
                Picasso.get().load(game2.getUrl_picture()).into(imageView2);
                String game2Name = game2.getName();
                String imageUrl2 = game2.getUrl_picture();
                String game2Summery = game2.getSummary();
                imageView2.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putString("game1Name", game2Name);
                    args.putString("imageUrl1", imageUrl2);
                    args.putString("game1Summery", game2Summery);
                    // Handle click for game2
                    navController.navigate(R.id.action_homePage_to_gameDetail, args);
                });
            }
        }
    }
}


