package ch.bbcag.cineboi;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.bbcag.cineboi.helper.AlertDialogHelper;
import ch.bbcag.cineboi.helper.WatchListRecyclerAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.FavFilmDAO;
import ch.bbcag.cineboi.room.FavoriteFilm;
import ch.bbcag.cineboi.room.WatchlistFilm;
import ch.bbcag.cineboi.room.WatchlistFilmDAO;


public class WatchlistFragment extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    private AppDatabase database;
    private ArrayList<Film> watchlistFilms = new ArrayList<>();
    private AlertDialogHelper alertDialogHelper;
    private WatchListRecyclerAdapter filmAdapter;
    private WatchlistFilmDAO watchlistFilmDAO;

    @Override
    public void onStart() {
        super.onStart();
        getWatchlistFilmPosters();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Watchlist");
        database = AppDatabase.getInstance(getActivity().getApplicationContext());
        v =  inflater.inflate(R.layout.fragment_watchlist, container, false);


        return v;
    }

    private void getWatchlistFilmPosters() {
        watchlistFilmDAO = database.getWatchlistFilmDAO();
        List<WatchlistFilm> watchlistFilmList = watchlistFilmDAO.getAll();
        watchlistFilms.clear();
        for (WatchlistFilm watchlistFilm : watchlistFilmList) {
            String url = create_API_URL(watchlistFilm.getFilmID());
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                try {
                    Film film = TMDB_Parser.getFilmDetailFromJsonString(response);
                    watchlistFilms.add(film);
                    filmAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    alertDialogHelper.generateAlertDialog(getActivity());
                    e.printStackTrace();
                }
            }, error -> alertDialogHelper.generateAlertDialog(getActivity()));
            queue.add(stringRequest);
        }
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_watchlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filmAdapter = new WatchListRecyclerAdapter(watchlistFilms, getActivity(), (pos, view) -> {
            Film film = filmAdapter.getItemAt(pos);
            if (view == R.id.remove_watchlistbtn){
                removeFromWatchlist(v, film.getId());
            }
            if (view == R.id.watchlist_poster){
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                int fid = film.getId();
                intent.putExtra("FilmId", fid);
                intent.putExtra("Filmname", film.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(filmAdapter);

    }

    public String create_API_URL(int id) {
        return "https://api.themoviedb.org/3/movie/" + id + "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    }

    public void removeFromWatchlist(View view, int id) {
        watchlistFilmDAO.removeFilmFromWatchlist(id);
        onStart();
    }
}