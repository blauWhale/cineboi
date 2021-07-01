package ch.bbcag.cineboi.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ch.bbcag.cineboi.R;
import ch.bbcag.cineboi.model.Film;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    private List<Film> favoriteList;
    private Context context;
    private FavoriteClickListener callback;

    public FavoriteRecyclerAdapter(List<Film> filmList, Context context, FavoriteClickListener callback) {
        favoriteList = filmList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_text_overlay, viewGroup, false);

        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Film film = favoriteList.get(position);
        viewHolder.getFilmTitle().setText(film.getName());
        Glide.with(context).load(film.getBackdrop()).centerCrop().into((ImageView) viewHolder.getFilmPoster());
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public Film getItemAt(int pos) {
        return favoriteList.get(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView filmTitle;
        private final ImageView filmPoster;
        private FavoriteClickListener callback;

        public ViewHolder(View view, FavoriteClickListener callback) {
            super(view);
            this.callback = callback;
            filmTitle = (TextView) view.findViewById(R.id.favorite_title);
            filmPoster = (ImageView) view.findViewById(R.id.favorite_image);

            filmPoster.setOnClickListener(v -> callback.onClick(getAdapterPosition()));
        }

        public TextView getFilmTitle() {
            return filmTitle;
        }

        public ImageView getFilmPoster() {
            return filmPoster;
        }

    }

    public interface FavoriteClickListener {
        void onClick(int pos);
    }
}

