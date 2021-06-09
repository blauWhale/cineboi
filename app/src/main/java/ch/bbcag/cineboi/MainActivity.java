package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;

import ch.bbcag.cineboi.helper.ImageListAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=fa11728f6e81c5f05fb42f521fb71283&";
    private String api_query = "year=1998";
    private static final String IMAGE_PATH = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2";
    public static String[] FilmImages = {
            "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/xrAaJAn3hqkInq5kE1AGJqIGXyT.jpg",
            "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/z2UtGA1WggESspi6KOXeo66lvLx.jpg",
            "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/h8Rb9gBr48ODIwYUttZNYeMWeUU.jpg",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Discover");
        getFilmPosters(API_URL + api_query);
//        GridView gridView = (GridView) findViewById(R.id.gridview);
//        gridView.setAdapter(
//                new ImageListAdapter(MainActivity.this, FilmImages)
//        );
    }


    private void getFilmPosters(String url)
    {
        //final ArrayAdapter<Film> filmAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_gallery_item);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        // verwende die gemerkte Id auf der folgenden Seite
                        ArrayList<String> posterPaths = new ArrayList<>();
                        for (Film film: films) {
                            posterPaths.add(IMAGE_PATH + film.getPoster_Path());
                        }
                        String posterPathArray[] = new String[posterPaths.size()];
                        for(int j =0;j<posterPaths.size();j++){
                            posterPathArray[j] = posterPaths.get(j);
                        }

                        //ListView filmList = findViewById(R.id.filmlist);
                        GridView gridView = (GridView) findViewById(R.id.gridview);
                        gridView.setAdapter(
                                new ImageListAdapter(MainActivity.this, posterPathArray)
                        );
                    } catch (JSONException e) {
                        generateAlertDialog();
                        e.printStackTrace();
                    }
                }, error -> generateAlertDialog());
        queue.add(stringRequest);
    }

    private void generateAlertDialog() {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
            finish();
        });
        dialogBuilder.setMessage("Die Filme konnten nicht geladen werden. Versuche es später nochmals.").setTitle("Fehler");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }


    public String getApi_query() {
        return api_query;
    }

    public void setApi_query(String api_query) {
        this.api_query = api_query;
    }
}