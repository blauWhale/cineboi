package ch.bbcag.cineboi;

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
import java.util.ArrayList;
import java.util.List;
import ch.bbcag.cineboi.helper.AlertDialogHelper;
import ch.bbcag.cineboi.helper.BackdropAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.FavFilmDAO;
import ch.bbcag.cineboi.room.FavoriteFilm;

public class FavoriteFragment extends Fragment {
    AppDatabase database;
    ArrayList<Film> favoriteFilms = new ArrayList<>();
    View v;
    private BackdropAdapter filmAdapter;
    private AlertDialogHelper alertDialogHelper;

    @Override
    public void onStart() {
        super.onStart();
        getFavoriteFilmPosters();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        v = inflater.inflate(R.layout.fragment_favorite, container, false);
        alertDialogHelper = new AlertDialogHelper();
        return v;
    }

    private void getFavoriteFilmPosters() {
        FavFilmDAO favFilmDAO = database.getFavFilmDAO();
        List<FavoriteFilm> favoriteFilmList = favFilmDAO.getAll();
        favoriteFilms.clear();
        for (FavoriteFilm favoriteFilm : favoriteFilmList) {
            String url = create_API_URL(favoriteFilm.getFilmID());
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                        try {
                            Film film = TMDB_Parser.getFilmDetailFromJsonString(response);
                            favoriteFilms.add(film);
                            filmAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            alertDialogHelper.generateAlertDialog(getActivity());
                            e.printStackTrace();
                        }
                    }, error -> alertDialogHelper.generateAlertDialog(getActivity()));
            queue.add(stringRequest);
        }
        GridView gridView = v.findViewById(R.id.backdropList);
        filmAdapter = new BackdropAdapter(getActivity(), favoriteFilms);
        gridView.setAdapter(filmAdapter);
        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
            Film selected = (Film) parent.getItemAtPosition(position);
            int fid = selected.getId();
            intent.putExtra("FilmId", fid);
            intent.putExtra("Filmname", selected.getName());
            startActivity(intent);
        };
        gridView.setOnItemClickListener(mListClickedHandler);
    }

    public String create_API_URL(int id) {
        return "https://api.themoviedb.org/3/movie/" + id + "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    }

}