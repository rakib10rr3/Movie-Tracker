package com.rakib.moviewer.model;

/**
 * Created by Rakib on 03/10/2018.
 */

public class Backdrop {
    private double aspect_ratio;

    public double getAspectRatio() { return this.aspect_ratio; }

    public void setAspectRatio(double aspect_ratio) { this.aspect_ratio = aspect_ratio; }

    private String file_path;

    public String getFilePath() { return this.file_path; }

    public void setFilePath(String file_path) { this.file_path = file_path; }

    private int height;

    public int getHeight() { return this.height; }

    public void setHeight(int height) { this.height = height; }

    private String iso_639_1;

    public String getIso6391() { return this.iso_639_1; }

    public void setIso6391(String iso_639_1) { this.iso_639_1 = iso_639_1; }

    private double vote_average;

    public double getVoteAverage() { return this.vote_average; }

    public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }

    private int vote_count;

    public int getVoteCount() { return this.vote_count; }

    public void setVoteCount(int vote_count) { this.vote_count = vote_count; }

    private int width;

    public int getWidth() { return this.width; }

    public void setWidth(int width) { this.width = width; }
}
