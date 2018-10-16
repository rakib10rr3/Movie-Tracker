package com.rakib.moviewer.model;

import java.util.Date;

/**
 * Created by Rakib on 03/13/2018.
 */

public class Review {
    private String title;
    private String review;
    private String userName;
    private String userId;
    private Date added;

    public Review(){

    }

    public Review(String title, String review, String userName, String userId, Date added) {
        this.title = title;
        this.review = review;
        this.userName = userName;
        this.userId = userId;
        this.added = added;
    }

    public String getTitle() {
        return title;
    }

    public String getReview() {
        return review;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public Date getAdded() {
        return added;
    }
}
