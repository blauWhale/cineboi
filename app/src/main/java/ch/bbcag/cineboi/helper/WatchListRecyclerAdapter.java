package ch.bbcag.cineboi.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ch.bbcag.cineboi.R;
import ch.bbcag.cineboi.model.Film;

public class WatchListRecyclerAdapter extends RecyclerView.Adapter<WatchListRecyclerAdapter.ViewHolder> {

    private List<Film> watchlistList;
    private Context context;
    WatchlistClickListener callback;

    public WatchListRecyclerAdapter(List<Film> filmList, Context context, WatchlistClickListener callback) {
        watchlistList = filmList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item, viewGroup, false);

        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Film film = watchlistList.get(position);
        viewHolder.getFilmTitle().setText(film.getName());
        viewHolder.getFilmDescription().setText(film.getOverview());
        Glide.with(context).load(film.getPoster_Path()).centerCrop().into((ImageView) viewHolder.getFilmPoster());
    }

    @Override
    public int getItemCount() {
        return watchlistList.size();
    }

    public Film getItemAt(int pos) {
        return watchlistList.get(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView filmTitle;
        private final TextView filmDescription;
        private final ImageView filmPoster;
        private WatchlistClickListener callback;

        public ViewHolder(View view, WatchlistClickListener callback) {
            super(view);
            this.callback = callback;
            filmTitle = view.findViewById(R.id.watchlist_title);
            filmDescription = view.findViewById(R.id.watchlist_overview);
            filmPoster = view.findViewById(R.id.watchlist_poster);
            Button btn = view.findViewById(R.id.remove_watchlistbtn);
            btn.setOnClickListener(v -> callback.onClick(getAdapterPosition(), R.id.remove_watchlistbtn));

            filmPoster.setOnClickListener(v -> callback.onClick(getAdapterPosition(), R.id.watchlist_poster));
        }

        public TextView getFilmTitle() {
            return filmTitle;
        }

        public TextView getFilmDescription() {
            return filmDescription;
        }

        public ImageView getFilmPoster() {
            return filmPoster;
        }

    }

    public interface WatchlistClickListener {
        void onClick(int pos, int view);
    }

}

