package ch.bbcag.cineboi.model;

import ch.bbcag.cineboi.R;

public class Film {
    private int id;
    private String name, poster_Path;

    public Film(int id, String name, String poster_Path) {
        this.id = id;
        this.name = name;
        this.poster_Path = R.string.poster_url + poster_Path;
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
}
