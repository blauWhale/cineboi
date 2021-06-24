package ch.bbcag.cineboi;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import ch.bbcag.cineboi.helper.FavoriteRecyclerAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.FavFilmDAO;
import ch.bbcag.cineboi.room.FavoriteFilm;

public class FavoriteFragment extends Fragment {
    AppDatabase database;
    ArrayList<Film> favoriteFilms = new ArrayList<>();
    View v;
    private FavoriteRecyclerAdapter filmAdapter;
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
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_favoritelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filmAdapter = new FavoriteRecyclerAdapter(favoriteFilms,getActivity(),pos -> {
            Film film = filmAdapter.getItemAt(pos);
            Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
            int fid = film.getId();
            intent.putExtra("FilmId", fid);
            intent.putExtra("Filmname", film.getName());
            startActivity(intent);
        });
        recyclerView.setAdapter(filmAdapter);
    }

    public String create_API_URL(int id) {
        return "https://api.themoviedb.org/3/movie/" + id + "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    }

}