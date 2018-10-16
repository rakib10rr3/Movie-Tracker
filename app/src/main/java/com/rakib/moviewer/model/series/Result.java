package com.rakib.moviewer.model.series;

import java.util.ArrayList;

/**
 * Created by Rakib on 03/18/2018.
 */

public class Result {
    private String original_name;

    public String getOriginalName() { return this.original_name; }

    public void setOriginalName(String original_name) { this.original_name = original_name; }

    private ArrayList<Integer> genre_ids;

    public ArrayList<Integer> getGenreIds() { return this.genre_ids; }

    public void setGenreIds(ArrayList<Integer> genre_ids) { this.genre_ids = genre_ids; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private double popularity;

    public double getPopularity() { return this.popularity; }

    public void setPopularity(double popularity) { this.popularity = popularity; }

    private ArrayList<String> origin_country;

    public ArrayList<String> getOriginCountry() { return this.origin_country; }

    public void setOriginCountry(ArrayList<String> origin_country) { this.origin_country = origin_country; }

    private int vote_count;

    public int getVoteCount() { return this.vote_count; }

    public void setVoteCount(int vote_count) { this.vote_count = vote_count; }

    private String first_air_date;

    public String getFirstAirDate() { return this.first_air_date; }

    public void setFirstAirDate(String first_air_date) { this.first_air_date = first_air_date; }

    private String backdrop_path;

    public String getBackdropPath() { return this.backdrop_path; }

    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }

    private String original_language;

    public String getOriginalLanguage() { return this.original_language; }

    public void setOriginalLanguage(String original_language) { this.original_language = original_language; }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private double vote_average;

    public double getVoteAverage() { return this.vote_average; }

    public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }

    private String overview;

    public String getOverview() { return this.overview; }

    public void setOverview(String overview) { this.overview = overview; }

    private String poster_path;

    public String getPosterPath() { return this.poster_path; }

    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }
}
