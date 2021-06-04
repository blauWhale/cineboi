package ch.bbcag.cineboi.model;

import java.util.ArrayList;
import java.util.List;

import ch.bbcag.cineboi.R;

public class Film {
    private int id;
    private String name, poster_Path;
    private static final String POSTER_PATH_URL = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/";

    public Film(int id, String name, String poster_Path) {
        this.id = id;
        this.name = name;
        this.poster_Path = POSTER_PATH_URL + poster_Path;
    }

    public Film() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoster_Path(String poster_Path) {
        this.poster_Path = poster_Path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPoster_Path() {
        return poster_Path;
    }

    public static String getPosterPathUrl() {
        return POSTER_PATH_URL;
    }

    @Override
    public String toString() {
        return name;
    }
}
