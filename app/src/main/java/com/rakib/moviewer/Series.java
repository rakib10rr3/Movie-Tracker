package com.rakib.moviewer;

import java.util.Date;

/**
 * Created by Rakib on 03/18/2018.
 */

public class Series {

    private int id;
    private String title;
    private double rating;
    private Date added;
    private String posterPath;
    private String userId;
    private String status;

    public Series() {

    }

    public Series(String title, double rating, int id, Date date, String posterPath, String userId, String status) {
        this.title = title;
        this.rating = rating;
        this.id = id;
        this.added = date;
        this.posterPath = posterPath;
        this.userId = userId;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public Date getAdded() {
        return this.added;
    }

}
