package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
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
    private String api_query = "year=2004";
    private static final String IMAGE_PATH = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Discover");
        getSupportActionBar().hide();
        getFilmPosters(API_URL + api_query);
    }



    private void getFilmPosters(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        ArrayList<String> posterPaths = new ArrayList<>();
                        for (Film film: films) {
                            posterPaths.add(IMAGE_PATH + film.getPoster_Path());
                        }
                        String[] posterPathArray = new String[posterPaths.size()];
                        for(int j =0;j<posterPaths.size();j++){
                            posterPathArray[j] = posterPaths.get(j);
                        }
                        GridView gridView = (GridView) findViewById(R.id.gridview);
                        //ImageListAdapter posts = new ImageListAdapter(MainActivity.this, posterPathArray);
                        gridView.setAdapter(new ImageListAdapter(MainActivity.this, posterPathArray));
                        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            Film selected = (Film)parent.getItemAtPosition(position);
                            intent.putExtra("FilmId", selected.getId());
                            intent.putExtra("Filmname", selected.getName());
                            startActivity(intent);
                        };
                        //posts.setOnItemClickListener(mListClickedHandler);
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