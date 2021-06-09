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

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=fa11728f6e81c5f05fb42f521fb71283&year=2021";
    public static String[] eatFoodyImages = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Discover");
        getFilmPosters();
        GridView listView = (GridView) findViewById(R.id.usage_example_gridview);
        listView.setAdapter(
                new ImageListAdapter(MainActivity.this, eatFoodyImages)
        );
    }


    private void getFilmPosters()
    {
        final ArrayAdapter<Film> filmAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_gallery_item);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        filmAdapter.addAll(films);
                        // verwende die gemerkte Id auf der folgenden Seite
                        ListView filmList = findViewById(R.id.filmlist);
                        filmList.setAdapter(filmAdapter);
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
}