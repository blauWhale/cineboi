package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.bbcag.cineboi.helper.ImageListAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=fa11728f6e81c5f05fb42f521fb71283&";
    private static final String API_URL_GENRE = "https://api.themoviedb.org/3/genre/movie/list?api_key=fa11728f6e81c5f05fb42f521fb71283";
    private static final String API_URL_COUNTRIES = "https://api.themoviedb.org/3/watch/providers/regions?api_key=fa11728f6e81c5f05fb42f521fb71283";
    private static final String API_ADDITION_GENRE = "&with_genres=";
    private static final String API_ADDITION_COUNTRY = "&region=";
    private String api_query = "sort_by=popularity.desc";
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout bottomsheetcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getFilmPosters(API_URL + api_query);
        setTitle("Discover");

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_persistent);
        bottomsheetcontainer = bottomSheetDialog.findViewById(R.id.bottom_sheet);

        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                api_query = "&query=" + simpleSearchView.getQuery();
                getFilmPosters(API_URL + api_query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });
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


    private void getGenres(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        HashMap<String, String> genres = TMDB_Parser.getFilmGenresFromJsonString(response);
                        generateView(genres, API_ADDITION_GENRE, R.id.genre_filter);

                    } catch (JSONException e) {
                        generateAlertDialog();
                        e.printStackTrace();
                    }
                }, error -> generateAlertDialog());
        queue.add(stringRequest);
    }

    private void getCountries(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        HashMap<String, String> countries = TMDB_Parser.getFilmCountriesFromJsonString(response);
                        generateView(countries, API_ADDITION_COUNTRY, R.id.country_filter);

                    } catch (JSONException e) {
                        generateAlertDialog();
                        e.printStackTrace();
                    }
                }, error -> generateAlertDialog());
        queue.add(stringRequest);
    }

    private void generateView(HashMap<String, String> map, String searchItem, int idButton) {
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            LinearLayout linearLayout  = new LinearLayout(MainActivity.this);
            linearLayout.setPaddingRelative(8,8,8,8);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView = new TextView(this);
            textView.setText((String) pair.getValue());
            textView.setPaddingRelative(8,8,8,8);
            textView.setTextSize(25);
            linearLayout.addView(textView);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setApi_query(getApi_query() + searchItem + pair.getKey());
                    getFilmPosters(API_URL + api_query);
                    bottomSheetDialog.hide();
                    Button btn = (Button) findViewById(idButton);
                    btn.setText(pair.getValue().toString());

                    Button resetbtn = (Button) findViewById(R.id.reset_button);
                    resetbtn.setVisibility(View.VISIBLE);
                }

            });
            bottomsheetcontainer.addView(linearLayout);
        }
    }


    public void filterGenres(View view) {
        bottomsheetcontainer.removeAllViews();
        getGenres(API_URL_GENRE);
        bottomSheetDialog.show();

    }

    public void filterCountries(View view) {
        bottomsheetcontainer.removeAllViews();
        getCountries(API_URL_COUNTRIES);
        bottomSheetDialog.show();

    }

    public void filterRelease(View view) {
        setApi_query("year=2021");
        getFilmPosters(API_URL + api_query);
    }

    public void addOnTabSelectedListener (TabLayout.OnTabSelectedListener listener){


    }
    public void initializeTabNavigation(){
        TabItem discover = findViewById(R.id.discover);
        TabItem watchlist = findViewById(R.id.watchlist);
        TabItem favorite = findViewById(R.id.favorite);


    }

    public void filterReset(View view) {
        setApi_query("sort_by=popularity.desc");
        getFilmPosters(API_URL + api_query);

        Button btn = (Button) findViewById(R.id.genre_filter);
        btn.setText(R.string.button_genre);

        Button btn2 = (Button) findViewById(R.id.country_filter);
        btn2.setText(R.string.button_countries);

        Button resetbtn = (Button) findViewById(R.id.reset_button);
        resetbtn.setVisibility(View.INVISIBLE);
    }

}