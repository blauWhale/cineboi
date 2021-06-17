package ch.bbcag.cineboi.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {favoriteFilm.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract favFilmDAO getFavFilmDAO();

}