package com.rakib.moviewer.model;

import java.util.ArrayList;

/**
 * Created by Rakib on 1/24/2018.
 */

public class SingleMovieData {
    private boolean adult;

    public boolean getAdult() {
        return this.adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    private String backdrop_path;

    public String getBackdropPath() {
        return this.backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    private BelongsToCollection belongs_to_collection;

    public BelongsToCollection getBelongsToCollection() {
        return this.belongs_to_collection;
    }

    public void setBelongsToCollection(BelongsToCollection belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    private int budget;

    public int getBudget() {
        return this.budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    private String homepage;

    public String getHomepage() {
        return this.homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String imdb_id;

    public String getImdbId() {
        return this.imdb_id;
    }

    public void setImdbId(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    private String original_language;

    public String getOriginalLanguage() {
        return this.original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    private String original_title;

    public String getOriginalTitle() {
        return this.original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    private String overview;

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    private double popularity;

    public double getPopularity() {
        return this.popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    private String poster_path;

    public String getPosterPath() {
        return this.poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    private ArrayList<ProductionCompany> production_companies;

    public ArrayList<ProductionCompany> getProductionCompanies() {
        return this.production_companies;
    }

    public void setProductionCompanies(ArrayList<ProductionCompany> production_companies) {
        this.production_companies = production_companies;
    }

    private ArrayList<ProductionCountry> production_countries;

    public ArrayList<ProductionCountry> getProductionCountries() {
        return this.production_countries;
    }

    public void setProductionCountries(ArrayList<ProductionCountry> production_countries) {
        this.production_countries = production_countries;
    }

    private String release_date;

    public String getRelease_date() {
        return this.release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    private int revenue;

    public int getRevenue() {
        return this.revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    private int runtime;

    public int getRuntime() {
        return this.runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    private ArrayList<SpokenLanguage> spoken_languages;

    public ArrayList<SpokenLanguage> getSpokenLanguages() {
        return this.spoken_languages;
    }

    public void setSpokenLanguages(ArrayList<SpokenLanguage> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String tagline;

    public String getTagline() {
        return this.tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private boolean video;

    public boolean getVideo() {
        return this.video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    private double vote_average;

    public double getVoteAverage() {
        return this.vote_average;
    }

    public void setVoteAverage(double vote_average) {
        this.vote_average = vote_average;
    }

    private int vote_count;

    public int getVoteCount() {
        return this.vote_count;
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    private Credits credits;

    public Credits getCredits() {
        return this.credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    private ExternalIds external_ids;

    public ExternalIds getExternal_ids() {
        return this.external_ids;
    }

    public void setExternal_ids(ExternalIds externalIds) {
        this.external_ids = externalIds;
    }

    private Videos videos;

    public Videos getVideos() { return this.videos; }

    public void setVideos(Videos videos) { this.videos = videos; }

    private Images images;
    public Images getImages ()
    {
        return images;
    }

    public void setImages (Images images)
    {
        this.images = images;
    }
}


