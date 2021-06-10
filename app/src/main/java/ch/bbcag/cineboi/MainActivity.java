package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
    private static final String API_ADDITION_GENRE = "&with_genres=";
    private String api_query = "year=2004";
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getFilmPosters(API_URL + api_query);
        getGenres(API_URL_GENRE);
        setTitle("Discover");

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_persistent);
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
        bottomSheetDialog.show();
    }

    private void getGenres(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        HashMap<String, String> genres = TMDB_Parser.getFilmGenresFromJsonString(response);
                        generateView(genres, API_ADDITION_GENRE);

                    } catch (JSONException e) {
                        generateAlertDialog();
                        e.printStackTrace();
                    }
                }, error -> generateAlertDialog());
        queue.add(stringRequest);
    }

    private void generateView(HashMap<String, String> map, String searchItem) {
        Iterator it = map.entrySet().iterator();
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_persistent);
        LinearLayout bottomsheetcontainer = bottomSheetDialog.findViewById(R.id.bottom_sheet);
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
                }

            });

            bottomsheetcontainer.addView(linearLayout);
        }
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