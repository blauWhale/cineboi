package ch.bbcag.cineboi.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteFilm.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavFilmDAO getFavFilmDAO();

    
    private static AppDatabase cineboiDB;
    public static AppDatabase getInstance(Context context) {
        if (null == cineboiDB) {
            cineboiDB = buildDatabaseInstance(context);
        }
        return cineboiDB;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "cineboiDB")
                .allowMainThreadQueries()
                .build();
    }
}



