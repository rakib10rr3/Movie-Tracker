package com.rakib.moviewer.model;

import com.rakib.moviewer.model.series.single.CreatedBy;
import com.rakib.moviewer.model.series.single.Network;
import com.rakib.moviewer.model.series.single.Season;

import java.util.ArrayList;

/**
 * Created by Rakib on 03/18/2018.
 */

public class SingleSeriesData {
    private String backdrop_path;

    public String getBackdropPath() { return this.backdrop_path; }

    public void setBackdropPath(String backdrop_path) { this.backdrop_path = backdrop_path; }

    private ArrayList<CreatedBy> created_by;

    public ArrayList<CreatedBy> getCreatedBy() { return this.created_by; }

    public void setCreatedBy(ArrayList<CreatedBy> created_by) { this.created_by = created_by; }

    private ArrayList<Integer> episode_run_time;

    public ArrayList<Integer> getEpisodeRunTime() { return this.episode_run_time; }

    public void setEpisodeRunTime(ArrayList<Integer> episode_run_time) { this.episode_run_time = episode_run_time; }

    private String first_air_date;

    public String getFirstAirDate() { return this.first_air_date; }

    public void setFirstAirDate(String first_air_date) { this.first_air_date = first_air_date; }

    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() { return this.genres; }

    public void setGenres(ArrayList<Genre> genres) { this.genres = genres; }

    private String homepage;

    public String getHomepage() { return this.homepage; }

    public void setHomepage(String homepage) { this.homepage = homepage; }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private boolean in_production;

    public boolean getInProduction() { return this.in_production; }

    public void setInProduction(boolean in_production) { this.in_production = in_production; }

    private ArrayList<String> languages;

    public ArrayList<String> getLanguages() { return this.languages; }

    public void setLanguages(ArrayList<String> languages) { this.languages = languages; }

    private String last_air_date;

    public String getLastAirDate() { return this.last_air_date; }

    public void setLastAirDate(String last_air_date) { this.last_air_date = last_air_date; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private ArrayList<Network> networks;

    public ArrayList<Network> getNetworks() { return this.networks; }

    public void setNetworks(ArrayList<Network> networks) { this.networks = networks; }

    private int number_of_episodes;

    public int getNumberOfEpisodes() { return this.number_of_episodes; }

    public void setNumberOfEpisodes(int number_of_episodes) { this.number_of_episodes = number_of_episodes; }

    private int number_of_seasons;

    public int getNumberOfSeasons() { return this.number_of_seasons; }

    public void setNumberOfSeasons(int number_of_seasons) { this.number_of_seasons = number_of_seasons; }

    private ArrayList<String> origin_country;

    public ArrayList<String> getOriginCountry() { return this.origin_country; }

    public void setOriginCountry(ArrayList<String> origin_country) { this.origin_country = origin_country; }

    private String original_language;

    public String getOriginalLanguage() { return this.original_language; }

    public void setOriginalLanguage(String original_language) { this.original_language = original_language; }

    private String original_name;

    public String getOriginalName() { return this.original_name; }

    public void setOriginalName(String original_name) { this.original_name = original_name; }

    private String overview;

    public String getOverview() { return this.overview; }

    public void setOverview(String overview) { this.overview = overview; }

    private double popularity;

    public double getPopularity() { return this.popularity; }

    public void setPopularity(double popularity) { this.popularity = popularity; }

    private String poster_path;

    public String getPosterPath() { return this.poster_path; }

    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    private ArrayList<ProductionCompany> production_companies;

    public ArrayList<ProductionCompany> getProductionCompanies() { return this.production_companies; }

    public void setProductionCompanies(ArrayList<ProductionCompany> production_companies) { this.production_companies = production_companies; }

    private ArrayList<Season> seasons;

    public ArrayList<Season> getSeasons() { return this.seasons; }

    public void setSeasons(ArrayList<Season> seasons) { this.seasons = seasons; }

    private String status;

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    private String type;

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    private double vote_average;

    public double getVoteAverage() { return this.vote_average; }

    public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }

    private int vote_count;

    public int getVoteCount() { return this.vote_count; }

    public void setVoteCount(int vote_count) { this.vote_count = vote_count; }

    private Credits credits;

    public Credits getCredits() { return this.credits; }

    public void setCredits(Credits credits) { this.credits = credits; }

    private ExternalIds external_ids;

    public ExternalIds getExternalIds() { return this.external_ids; }

    public void setExternalIds(ExternalIds external_ids) { this.external_ids = external_ids; }

    private Videos videos;

    public Videos getVideos() { return this.videos; }

    public void setVideos(Videos videos) { this.videos = videos; }

    private Images images;

    public Images getImages() { return this.images; }

    public void setImages(Images images) { this.images = images; }
}
