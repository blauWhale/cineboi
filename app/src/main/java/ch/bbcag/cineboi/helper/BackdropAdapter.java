package ch.bbcag.cineboi.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ch.bbcag.cineboi.R;
import ch.bbcag.cineboi.model.Film;

public class BackdropAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Film> imageUrls;
    private View view;

    public BackdropAdapter(Context context, ArrayList<Film> films, View view) {
        super(context, R.layout.image_text_overlay, films);
        this.context = context;
        this.imageUrls = films;
        inflater = LayoutInflater.from(context);
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }
        Glide.with(context).load(imageUrls.get(position).getBackdrop()).centerCrop().into((ImageView) convertView);

        return convertView;
    }
}