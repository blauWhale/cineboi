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

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    private List<Film> localDataSet;
    private Context context;
    FavoriteClickListener callback;

    public FavoriteRecyclerAdapter(List<Film> dataSet, Context context, FavoriteClickListener callback) {
        localDataSet = dataSet;
        this.context = context;
        this.callback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_text_overlay, viewGroup, false);

        return new ViewHolder(view, callback);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Film film = localDataSet.get(position);
        viewHolder.getFilmTitle().setText(film.getName());
        Glide.with(context).load(film.getBackdrop()).centerCrop().into((ImageView) viewHolder.getFilmPoster());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public Film getItemAt(int pos) {
        return localDataSet.get(pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView filmTitle;
        private final ImageView filmPoster;
        private FavoriteClickListener callback;

        public ViewHolder(View view, FavoriteClickListener callback) {
            super(view);
            // Define click listener for the ViewHolder's View
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

