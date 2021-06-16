package ch.bbcag.cineboi;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.ItemDAO;

public class FavoriteFragment extends Fragment {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=fa11728f6e81c5f05fb42f521fb71283&";
    AppDatabase database = Room.databaseBuilder(getActivity(), AppDatabase.class, "cineboi.db")
            .allowMainThreadQueries()
            .build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Favorite");
        getFavoriteFilmPosters(API_URL);
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }

    private void getFavoriteFilmPosters(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        GridView gridView = getActivity().findViewById(R.id.gridview);
                        ImageListAdapter filmAdapter = new ImageListAdapter(getActivity(), films);
                        gridView.setAdapter(filmAdapter);
                        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
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
        dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
            getActivity().finish();
        });
        dialogBuilder.setMessage("Die Filme konnten nicht geladen werden. Versuche es später nochmals.").setTitle("Fehler");
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}