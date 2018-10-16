package com.rakib.moviewer;

import com.rakib.moviewer.model.Cast;
import com.rakib.moviewer.model.Crew;
import com.rakib.moviewer.model.Genre;
import com.rakib.moviewer.model.ProductionCompany;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rakib on 1/22/2018.
 */

public class Movie {
    private int id;
    private String title;
    private Date added;
    private double rating;
    private String posterPath;
    private String userId;
    private String status;

    public Movie() {

    }

    public Movie(String title, double rating ,int id, Date date, String posterPath, String userId, String status) {
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

    public double getRating(){return rating;}

    public Date getAdded() {
        return added;
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
}
