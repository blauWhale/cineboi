package ch.bbcag.cineboi.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ItemDAO getItemDAO();

}