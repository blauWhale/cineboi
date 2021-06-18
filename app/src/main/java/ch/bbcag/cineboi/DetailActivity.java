package ch.bbcag.cineboi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.FavFilmDAO;
import ch.bbcag.cineboi.room.FavoriteFilm;

public class DetailActivity extends AppCompatActivity {

    private int id;
    private Film film;
    private FavFilmDAO favFilmDAO;

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        favFilmDAO = AppDatabase.getInstance(getApplicationContext()).getFavFilmDAO();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFavorite();
    }

    private void getFilmDetails(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, create_API_URL(this.id), response -> {
                    try {
                        film = TMDB_Parser.getFilmDetailFromJsonString(response);
                        TextView title_l = findViewById(R.id.title_l);
                        TextView info = findViewById(R.id.info);
                        TextView overview = findViewById(R.id.overview);
                        title_l.setText(film.getName());
                        overview.setText(film.getOverview());
                        info.setText(film.getInfo());
                        ImageView imageView2 = findViewById(R.id.poster2);
                        ImageView imageView1 = findViewById(R.id.poster1);
                        Glide.with(this).load(film.getPoster_Path()).into(imageView2);
                        Glide.with(this).load(film.getBackdrop()).centerCrop().into(imageView1);
                    } catch (JSONException e) {
                        generateAlertDialog();
                        e.printStackTrace();
                    }
                }, error -> generateAlertDialog());
        queue.add(stringRequest);
    }

    public  String create_API_URL(int id){
        return "https://api.themoviedb.org/3/movie/" + id + "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    }

    private void generateAlertDialog() {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
            finish();
        });
        dialogBuilder.setMessage("Die Filmdetails konnten nicht geladen werden. Versuche es später nochmals.").setTitle("Fehler");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void addToFavFilm(View view) {

        favFilmDAO.insert(new FavoriteFilm(this.id));
        onStart();
    }

    public void RemoveToFavFilm(View view) {
        favFilmDAO.removeFilmFromFavorite(this.id);
        onStart();
    }

    public void isFavorite(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        if (favFilmDAO.checkFilmInFavorite(this.id)){
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 0, 0)));
            fab.setOnClickListener(this::RemoveToFavFilm);
        }else{
            fab.setOnClickListener(this::addToFavFilm);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 218, 197)));
        }
    }
}