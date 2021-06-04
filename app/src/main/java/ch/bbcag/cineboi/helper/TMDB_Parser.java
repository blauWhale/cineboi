package ch.bbcag.cineboi.helper;

import org.json.JSONException;
import org.json.JSONObject;
import ch.bbcag.cineboi.model.Film;

public class TMDB_Parser {

    public static Film createFilmFromJsonString(String filmJsonString) throws JSONException {
        Film film = new Film();
        JSONObject jsonObj = new JSONObject(filmJsonString);
        film.setId(Integer.parseInt(jsonObj.getString("id")));
        film.setName(jsonObj.getString("original_title"));
        film.setPoster_Path(jsonObj.getString("poster_path"));
        return film;
    }
}
