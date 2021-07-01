package ch.bbcag.cineboi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import ch.bbcag.cineboi.helper.AlertDialogHelper;
import ch.bbcag.cineboi.helper.ApiHelper;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.FavFilmDAO;
import ch.bbcag.cineboi.room.FavoriteFilm;
import ch.bbcag.cineboi.room.WatchlistFilm;
import ch.bbcag.cineboi.room.WatchlistFilmDAO;

public class DetailActivity extends AppCompatActivity {

    private int id;
    private Film film;
    private FavFilmDAO favFilmDAO;
    private WatchlistFilmDAO watchlistFilmDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Filmname");
        this.id = intent.getIntExtra("FilmId", 0);
        getFilmDetails();
        setTitle(name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        favFilmDAO = AppDatabase.getInstance(getApplicationContext()).getFavFilmDAO();
        watchlistFilmDAO = AppDatabase.getInstance(getApplicationContext()).getWatchlistFilmDAO();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFavorite();
        isOnWatchlist();
    }

    private void getFilmDetails() {
        AlertDialogHelper alertDialogHelper = new AlertDialogHelper();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ApiHelper apiHelper = new ApiHelper();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiHelper.createAPIURL(this.id), response -> {
            try {
                film = TMDB_Parser.getFilmDetailFromJsonString(response);
                TextView filmTitle = findViewById(R.id.filmTitle);
                TextView info = findViewById(R.id.info);
                TextView overview = findViewById(R.id.overview);
                filmTitle.setText(film.getName());
                overview.setText(film.getOverview());
                info.setText(film.getInfo());
                ImageView filmPoster = findViewById(R.id.filmPoster);
                ImageView filmBackdrop = findViewById(R.id.filmBackdrop);
                Glide.with(this).load(film.getPoster_Path()).into(filmPoster);
                Glide.with(this).load(film.getBackdrop()).centerCrop().into(filmBackdrop);
            } catch (JSONException e) {
                alertDialogHelper.generateAlertDialog(this);
                e.printStackTrace();
            }
        }, error -> alertDialogHelper.generateAlertDialog(this));
        queue.add(stringRequest);
    }


    public void addToFavFilm(View view) {
        favFilmDAO.insert(new FavoriteFilm(this.id));
        onStart();
    }

    public void RemoveToFavFilm(View view) {
        favFilmDAO.removeFilmFromFavorite(this.id);
        onStart();
    }

    public void isFavorite() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        if (favFilmDAO.checkFilmInFavorite(this.id)) {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 0, 0)));
            fab.setOnClickListener(this::RemoveToFavFilm);
        } else {
            fab.setOnClickListener(this::addToFavFilm);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 218, 197)));
        }
    }

    public void addToWatchlist(View view) {
        watchlistFilmDAO.insert(new WatchlistFilm(this.id));
        onStart();
    }

    public void removeFromWatchlist(View view) {
        watchlistFilmDAO.removeFilmFromWatchlist(this.id);
        onStart();
    }

    public void isOnWatchlist() {
        Button watchlistBtn = findViewById(R.id.watchlist_btn);
        if (watchlistFilmDAO.checkFilmInWatchlist(this.id)) {
            watchlistBtn.setText(R.string.in_watchlist);
            watchlistBtn.setOnClickListener(this::removeFromWatchlist);
        } else {
            watchlistBtn.setOnClickListener(this::addToWatchlist);
            watchlistBtn.setText(R.string.add_to_watchlist);
        }
    }
}