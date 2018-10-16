package com.rakib.moviewer.model;

/**
 * Created by Rakib on 1/24/2018.
 */

public class SpokenLanguage {
    private String iso_639_1;

    public String getIso6391() {
        return this.iso_639_1;
    }

    public void setIso6391(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
