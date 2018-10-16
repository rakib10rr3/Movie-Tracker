package com.rakib.moviewer.model;

import java.util.ArrayList;

/**
 * Created by Rakib on 2/10/2018.
 */

public class Credits {
    private ArrayList<Cast> cast;

    public ArrayList<Cast> getCast() { return this.cast; }

    public void setCast(ArrayList<Cast> cast) { this.cast = cast; }

    private ArrayList<Crew> crew;

    public ArrayList<Crew> getCrew() { return this.crew; }

    public void setCrew(ArrayList<Crew> crew) { this.crew = crew; }
}
