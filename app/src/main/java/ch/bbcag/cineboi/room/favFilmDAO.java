package ch.bbcag.cineboi.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface favFilmDAO {

    @Query("SELECT * FROM favorit_films ")
    int[] getAll();

    //@Insert
    //void insert(int fid);
}
