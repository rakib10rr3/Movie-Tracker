package com.rakib.moviewer.model.series.single;

/**
 * Created by Rakib on 03/18/2018.
 */

public class Network {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String logo_path;

    public String getLogoPath() {
        return this.logo_path;
    }

    public void setLogoPath(String logo_path) {
        this.logo_path = logo_path;
    }

    private String origin_country;

    public String getOriginCountry() {
        return this.origin_country;
    }

    public void setOriginCountry(String origin_country) {
        this.origin_country = origin_country;
    }
}
