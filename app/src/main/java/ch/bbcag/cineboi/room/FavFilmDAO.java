package ch.bbcag.cineboi.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface FavFilmDAO {

    @Query("SELECT * FROM favorite_films")
    List<FavoriteFilm> getAll();

    @Insert
    void insert(FavoriteFilm film);

    @Query("SELECT * FROM favorite_films WHERE filmID = :id")
    boolean checkFilmInFavorite(int id);

    @Query("DELETE FROM favorite_films WHERE filmID = :id")
    void removeFilmFromFavorite(int id);
}
