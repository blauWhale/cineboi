package ch.bbcag.cineboi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import ch.bbcag.cineboi.helper.AlertDialogHelper;
import ch.bbcag.cineboi.helper.ApiHelper;
import ch.bbcag.cineboi.helper.WatchListRecyclerAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;
import ch.bbcag.cineboi.room.AppDatabase;
import ch.bbcag.cineboi.room.WatchlistFilm;
import ch.bbcag.cineboi.room.WatchlistFilmDAO;

public class WatchlistFragment extends Fragment {
    private View v;
    private AppDatabase database;
    private ArrayList<Film> watchlistFilms = new ArrayList<>();
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
        v = inflater.inflate(R.layout.fragment_watchlist, container, false);
        return v;
    }

    private void getWatchlistFilmPosters() {
        AlertDialogHelper alertDialogHelper = new AlertDialogHelper();
        ApiHelper apiHelper = new ApiHelper();
        watchlistFilmDAO = database.getWatchlistFilmDAO();
        List<WatchlistFilm> watchlistFilmList = watchlistFilmDAO.getAll();
        watchlistFilms.clear();
        TextView message = v.findViewById(R.id.message2);
        if (watchlistFilmList.size() != 0) {
            message.setVisibility(View.INVISIBLE);
            for (WatchlistFilm watchlistFilm : watchlistFilmList) {
                String url = apiHelper.createAPIURL(watchlistFilm.getFilmID());
                RequestQueue queue = Volley.newRequestQueue(requireActivity().getApplicationContext());
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
                if (view == R.id.remove_watchlistbtn) {
                    removeFromWatchlist(film.getId());
                }
                if (view == R.id.watchlist_poster) {
                    Intent intent = new Intent(requireActivity().getApplicationContext(), DetailActivity.class);
                    int fid = film.getId();
                    intent.putExtra("FilmId", fid);
                    intent.putExtra("Filmname", film.getName());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(filmAdapter);
        } else {
            if (filmAdapter != null) {
                filmAdapter.notifyDataSetChanged();
            }
            message.setVisibility(View.VISIBLE);
        }
    }

    public void removeFromWatchlist(int id) {
        watchlistFilmDAO.removeFilmFromWatchlist(id);
        onStart();
    }
}