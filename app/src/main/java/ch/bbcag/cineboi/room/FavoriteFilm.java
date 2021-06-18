package ch.bbcag.cineboi.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ch.bbcag.cineboi.model.Film;

@Entity(tableName = "favorite_films")
public class FavoriteFilm {

    public FavoriteFilm(int id){
        this.filmID = id;
    }
    public FavoriteFilm(){}

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int filmID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }
}
