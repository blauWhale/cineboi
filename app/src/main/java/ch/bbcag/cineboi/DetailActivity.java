package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;

import ch.bbcag.cineboi.helper.ImageListAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;

public class DetailActivity extends AppCompatActivity {

    private final String API_URL = "https://api.themoviedb.org/3/movie/635302?api_key=fa11728f6e81c5f05fb42f521fb71283";
    private static final String IMAGE_PATH = "https://www.themoviedb.org/t/p/original";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Filmname");
        this.id = intent.getIntExtra("Film", 0);
        setTitle(name);

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFilmDetails(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, create_API_URL(this.id), response -> {
                    try {
                        Film film = TMDB_Parser.getFilmDetailFromJsonString(response);
                        TextView title_s = findViewById(R.id.title_l);
                        title_s.setText(film.getName());
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
}