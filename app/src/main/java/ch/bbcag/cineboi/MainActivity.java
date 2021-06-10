package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import java.util.ArrayList;
import ch.bbcag.cineboi.helper.ImageListAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=fa11728f6e81c5f05fb42f521fb71283&";
    private String api_query = "year=2004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getFilmPosters(API_URL + api_query);
        setTitle("Discover");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFilmPosters(API_URL + api_query);
    }

    private void getFilmPosters(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        GridView gridView = findViewById(R.id.gridview);
                        ImageListAdapter filmAdapter = new ImageListAdapter(MainActivity.this, films);
                        gridView.setAdapter(filmAdapter);
                        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            Film selected = (Film) parent.getItemAtPosition(position);
                            intent.putExtra("FilmId", selected.getId());
                            intent.putExtra("Filmname", selected.getName());
                            startActivity(intent);
                        };
                        gridView.setOnItemClickListener(mListClickedHandler);
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

    public void filterGenres(View view) {
        setApi_query(getApi_query() + "&with_genres=28");
        onStart();
    }

    public void filterCountries(View view) {
        setApi_query(getApi_query() + "&region=ch");
        onStart();
    }

    public void filterRelease(View view) {
        setApi_query("year=2020");
        onStart();
    }

    public void addOnTabSelectedListener (TabLayout.OnTabSelectedListener listener){


    }
    public void initializeTabNavigation(){
        TabItem discover = findViewById(R.id.discover);
        TabItem watchlist = findViewById(R.id.watchlist);
        TabItem favorite = findViewById(R.id.favorite);


    }
}