package ch.bbcag.cineboi.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

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
            convertView = inflater.inflate(R.layout.image_text_overlay, parent, false);
        }
        ImageView backdrop = view.findViewById(R.id.imageView);
        Glide.with(context).load(imageUrls.get(position).getBackdrop()).centerCrop().into(backdrop);
        TextView title = view.findViewById(R.id.textView);
        title.setText(imageUrls.get(position).getName());
        return convertView;
    }
}