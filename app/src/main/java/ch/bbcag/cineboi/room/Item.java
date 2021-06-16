package ch.bbcag.cineboi.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.bbcag.cineboi.model.Film;

@Entity(tableName = "favorit_films")
public class Item {

    @PrimaryKey
    @NonNull
    private Long id;

    private int filmID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }
}
