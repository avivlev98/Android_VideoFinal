package com.example.videogamecatalog.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videogamecatalog.R;
import com.example.videogamecatalog.Classes.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> gameList;
    private Context context;

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
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
                imageView1.setOnClickListener(v -> {
                    // Handle click for game1
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
                imageView2.setOnClickListener(v -> {
                    // Handle click for game2
                });
            }
        }
    }
}


//public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
//
//    private Context context;
//    private List<Game> gameList;
//
//    public GameAdapter(Context context, List<Game> gameList) {
//        this.context = context;
//        this.gameList = gameList;
//    }
//
//    @NonNull
//    @Override
//    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
//        return new GameViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
//        Game game = gameList.get(position);
//        holder.bind(game);
//    }
//
//    @Override
//    public int getItemCount() {
//        return gameList.size();
//    }
//
//    public class GameViewHolder extends RecyclerView.ViewHolder {
//        private TextView game_name;
//        private TextView release_date;
//
//        private TextView genres;
//        private TextView platforms;
//
//        private TextView summery;
//        private ImageView ImageView;
//
//        public GameViewHolder(@NonNull View itemView) {
//            super(itemView);
//            game_name = itemView.findViewById(R.id.game_name);
//            release_date = itemView.findViewById(R.id.release_date);
//            genres = itemView.findViewById(R.id.genres);
//            platforms = itemView.findViewById(R.id.platforms);
//            summery = itemView.findViewById(R.id.summery);
//            ImageView = itemView.findViewById(R.id.gameImage);
//        }
//
//        public void bind(Game game) {
//            game_name.setText(game.getName());
//            summery.setText(game.getSummary());
//            release_date.setText(game.getRelease_date());
//
//            // Convert ArrayList to String
//            String genresStr = android.text.TextUtils.join(", ", game.getGenres());
//            String platformsStr = android.text.TextUtils.join(", ", game.getPlatforms());
//
//            genres.setText(genresStr);
//            platforms.setText(platformsStr);
//
//            // Load image using Picasso, Glide, or any other image loading library
//            // For example, with Picasso:
//            Picasso.get().load(game.getUrl_picture()).into(ImageView);
//        }
//    }
//}

