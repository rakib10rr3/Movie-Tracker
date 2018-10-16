package com.rakib.moviewer;

import java.util.ArrayList;

/**
 * Created by Rakib on 1/5/2018.
 */

public class Result {
    private int vote_count;

    public int getVoteCount() { return this.vote_count; }

    public void setVoteCount(int vote_count) { this.vote_count = vote_count; }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private boolean video;

    public boolean getVideo() { return this.video; }

    public void setVideo(boolean video) { this.video = video; }

    private double vote_average;

    public double getVoteAverage() { return this.vote_average; }

    public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }

    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private double popularity;

    public double getPopularity() { return this.popularity; }

    public void setPopularity(double popularity) { this.popularity = popularity; }

    private String poster_path;

    public String getPosterPath() { return this.poster_path; }

    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    private String original_language;

    public String getOriginalLanguage() { return this.original_language; }

    public void setOriginalLanguage(String original_language) { this.original_language = original_language; }

    private String original_title;

    public String getOriginalTitle() { return this.original_title; }

    public void setOriginalTitle(String original_title) { this.original_title = original_title; }

    private ArrayList<Integer> genre_ids;

    public ArrayList<Integer> getGenreIds() { return this.genre_ids; }

    public void setGenreIds(ArrayList<Integer> genre_ids) { this.genre_ids = genre_ids; }

    private String backdrop_path;

    public String getBackdropPath() { return this.backdrop_path; }

    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }

    private boolean adult;

    public boolean getAdult() { return this.adult; }

    public void setAdult(boolean adult) { this.adult = adult; }

    private String overview;

    public String getOverview() { return this.overview; }

    public void setOverview(String overview) { this.overview = overview; }

    private String release_date;

    public String getReleaseDate() { return this.release_date; }

    public void setReleaseDate(String release_date) { this.release_date = release_date; }
}
