package ch.bbcag.cineboi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import ch.bbcag.cineboi.model.Film;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Discover");
        addFilmposterToClickableList();
    }

    private void addFilmposterToClickableList(){
        ListView films =  findViewById(R.id.filmlist);
        ArrayAdapter<Film> filmAdapter =  new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_gallery_item);
        badiAdapter.addAll(BadiDao.getAll());
        badis.setAdapter(badiAdapter);


        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)     {
                Intent intent = new Intent(getApplicationContext(), BadiDetailsActivity.class);
                Badi selected = (Badi)parent.getItemAtPosition(position);
                intent.putExtra("badiId", selected.getId());
                intent.putExtra("badiName", selected.getName());
                startActivity(intent);
            }
        };
        badis.setOnItemClickListener(mListClickedHandler);
    }
}