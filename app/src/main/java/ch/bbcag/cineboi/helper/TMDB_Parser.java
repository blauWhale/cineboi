package ch.bbcag.cineboi.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

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
            setStandardValue(film, subObj);
            filmlist.add(film);
        }
        return filmlist;
    }


    public static Film getFilmDetailFromJsonString(String filmJsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(filmJsonString);
        Film film = new Film();
        setStandardValue(film, jsonObj);
        film.setOverview(jsonObj.getString("overview"));
        film.setBackdrop(jsonObj.getString("backdrop_path"));
        film.setInfo("Length:"+jsonObj.getString("runtime")+"min\nLanguage:" +jsonObj.getString("original_language"));
        return film;
    }

    public static void setStandardValue(Film film, JSONObject jsonObj) throws JSONException {
        film.setId(Integer.parseInt(jsonObj.getString("id")));
        film.setName(jsonObj.getString("original_title"));
        film.setPoster_Path(jsonObj.getString("poster_path"));
    }

    public static HashMap<String, String> getFilmGenresFromJsonString(String filmJsonString) throws JSONException{
        JSONObject jsonObj = new JSONObject(filmJsonString);
        JSONArray results = jsonObj.getJSONArray("genres");
        HashMap<String, String> genres = new HashMap<String, String>();
        for(int i = 0; i < results.length(); i++)
        {
            JSONObject subObj = results.getJSONObject(i);
            genres.put(subObj.getString("id"), subObj.getString("name"));
        }
        return genres;
    }
    public static HashMap<String, String> getFilmCountriesFromJsonString(String filmJsonString) throws JSONException{
        JSONObject jsonObj = new JSONObject(filmJsonString);
        JSONArray results = jsonObj.getJSONArray("results");
        HashMap<String, String> countries = new HashMap<>();
        for(int i = 0; i < results.length(); i++)
        {
            JSONObject subObj = results.getJSONObject(i);
            countries.put(subObj.getString("iso_3166_1"), subObj.getString("native_name"));
        }
        return countries;
    }

}
