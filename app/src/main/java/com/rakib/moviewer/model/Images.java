package com.rakib.moviewer.model;

import java.util.ArrayList;

/**
 * Created by Rakib on 03/10/2018.
 */

public class Images {
    private ArrayList<Backdrop> backdrops;

    public ArrayList<Backdrop> getBackdrops() { return this.backdrops; }

    public void setBackdrops(ArrayList<Backdrop> backdrops) { this.backdrops = backdrops; }

    private ArrayList<Poster> posters;

    public ArrayList<Poster> getPosters() { return this.posters; }

    public void setPosters(ArrayList<Poster> posters) { this.posters = posters; }
}
