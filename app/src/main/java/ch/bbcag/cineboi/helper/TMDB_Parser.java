package ch.bbcag.cineboi.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import ch.bbcag.cineboi.model.Film;

public class TMDB_Parser {

    public static ArrayList<Film> createFilmFromJsonString(String filmJsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(filmJsonString);
        JSONArray results = jsonObj.getJSONArray("results");
        ArrayList<Film> filmlist = new ArrayList<>();
        for(int i = 0; i < results.length(); i++)
        {
            Film film = new Film();
            JSONObject subObj = results.getJSONObject(i);
            film.setId(Integer.parseInt(subObj.getString("id")));
            film.setName(subObj.getString("original_title"));
            film.setPoster_Path(subObj.getString("poster_path"));
            filmlist.add(film);
        }
        return filmlist;
    }


    public static Film getFilmDetailFromJsonString(String filmJsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(filmJsonString);
        Film film = new Film();
        film.setName(jsonObj.getString("original_title"));
        film.setPoster_Path(jsonObj.getString("poster_path"));
        film.setOverview(jsonObj.getString("overview"));
        film.setBackdrop(jsonObj.getString("backdrop_path"));
        return film;
    }
}
