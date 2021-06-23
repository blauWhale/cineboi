package ch.bbcag.cineboi.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WatchlistFilmDAO {

    @Query("SELECT * FROM watchlist_films")
    List<WatchlistFilm> getAll();

    @Insert
    void insert(WatchlistFilm film);

    @Query("SELECT * FROM watchlist_films WHERE filmID = :id")
    boolean checkFilmInWatchlist(int id);

    @Query("DELETE FROM watchlist_films WHERE filmID = :id")
    void removeFilmFromWatchlist(int id);
}
