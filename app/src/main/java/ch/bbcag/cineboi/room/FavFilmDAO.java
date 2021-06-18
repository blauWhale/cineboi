package ch.bbcag.cineboi.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.bbcag.cineboi.model.Film;

@Dao
public interface FavFilmDAO {

    @Query("SELECT * FROM favorite_films")
    List<FavoriteFilm> getAll();

    @Insert
    void insert(FavoriteFilm film);
}
