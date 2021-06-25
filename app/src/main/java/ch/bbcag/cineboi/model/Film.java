package ch.bbcag.cineboi.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Film {

    private int id;
    private String name, poster_Path, overview, backdrop, info;
    private static final String POSTER_PATH_URL = "https://www.themoviedb.org/t/p/w500";
    private int rating;

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
        return POSTER_PATH_URL+poster_Path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return POSTER_PATH_URL+backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }
}
