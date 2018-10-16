package com.rakib.moviewer.model.series.single;

/**
 * Created by Rakib on 03/18/2018.
 */

public class Season {
    private String air_date;

    public String getAirDate() { return this.air_date; }

    public void setAirDate(String air_date) { this.air_date = air_date; }

    private int episode_count;

    public int getEpisodeCount() { return this.episode_count; }

    public void setEpisodeCount(int episode_count) { this.episode_count = episode_count; }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String overview;

    public String getOverview() { return this.overview; }

    public void setOverview(String overview) { this.overview = overview; }

    private String poster_path;

    public String getPosterPath() { return this.poster_path; }

    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    private int season_number;

    public int getSeasonNumber() { return this.season_number; }

    public void setSeasonNumber(int season_number) { this.season_number = season_number; }
}
