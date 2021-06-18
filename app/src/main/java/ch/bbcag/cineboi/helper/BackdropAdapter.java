package ch.bbcag.cineboi.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ch.bbcag.cineboi.R;
import ch.bbcag.cineboi.model.Film;

public class BackdropAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Film> imageUrls;

    public BackdropAdapter(Context context, ArrayList<Film> films) {
        super(context, R.layout.listview_item_image, films);
        this.context = context;
        this.imageUrls = films;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }
        Glide.with(context).load(imageUrls.get(position).getBackdrop()).into((ImageView) convertView);
        return convertView;
    }
}