package ch.bbcag.cineboi.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorit_films")
public class favoriteFilm {

    @PrimaryKey
    @NonNull
    public int id;

    @ColumnInfo(name = "film_id")
    public int fid;
}
