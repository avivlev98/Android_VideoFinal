package com.example.videogamecatalog.Classes;

import java.util.ArrayList;

public class Game {
    private String id;
    private String url_picture;
    private String release_date;
    private ArrayList<String> genres;
    private ArrayList<String> platforms;
    private String summary;
    private String name;

    public Game(String id, String url_picture, String release_date, ArrayList<String> genres, ArrayList<String> platforms, String summary, String name) {
        this.id = id;
        this.url_picture = url_picture;
        this.release_date = release_date;
        this.genres = genres;
        this.platforms = platforms;
        this.summary = summary;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getUrl_picture() {
        return url_picture;
    }

    public String getRelease_date() {
        return release_date;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public String getSummary() {
        return summary;
    }

    public String getName() {
        return name;
    }
}
