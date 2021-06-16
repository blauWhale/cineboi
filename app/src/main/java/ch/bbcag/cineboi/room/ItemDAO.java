package ch.bbcag.cineboi.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import ch.bbcag.cineboi.model.Film;

@Dao
public interface ItemDAO {

    @Insert
    public void insert(Item... items);

    @Update
    public void update(Item... items);

    @Delete
    public void delete(Item item);

    @Query("SELECT * FROM favorit_films")
    public ArrayList<Item> getItems();

    @Query("SELECT filmID FROM favorit_films")
    public ArrayList<Film> getFilms();

    @Query("SELECT * FROM favorit_films WHERE id = :id")
    public Item getItemById(Long id);
}
