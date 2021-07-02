package ch.bbcag.cineboi;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import ch.bbcag.cineboi.helper.AlertDialogHelper;
import ch.bbcag.cineboi.helper.ImageListAdapter;
import ch.bbcag.cineboi.helper.TMDB_Parser;
import ch.bbcag.cineboi.model.Film;

public class DiscoverFragment extends Fragment {
    private static final String API_KEY = "?api_key=fa11728f6e81c5f05fb42f521fb71283";
    private static final String API_HEAD = "https://api.themoviedb.org/3/";
    private static final String API_URL = API_HEAD + "discover/movie"+API_KEY;
    private static final String API_URL_GENRE = API_HEAD + "genre/movie/list"+API_KEY;
    private static final String API_URL_COUNTRIES = API_HEAD + "watch/providers/regions"+API_KEY;
    private static final String API_ADDITION_GENRE = "&with_genres=";
    private static final String API_ADDITION_COUNTRY = "&region=";
    private static final String API_URL_SEARCH = API_HEAD + "search/movie"+API_KEY+"&query=";
    private String api_query = "&sort_by=popularity.desc";
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout bottomsheetcontainer;
    private AlertDialogHelper alertDialogHelper = new AlertDialogHelper();
    private SearchView simpleSearchView;

    @Override
    public void onStart() {
        super.onStart();
        getFilmPosters(API_URL + api_query);
        simpleSearchView = getActivity().findViewById(R.id.searchView);
        simpleSearchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        simpleSearchView = getActivity().findViewById(R.id.searchView);
        simpleSearchView.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_persistent);
        bottomsheetcontainer = bottomSheetDialog.findViewById(R.id.bottom_sheet);
        getActivity().setTitle("Discover");
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        Button btnCountry = v.findViewById(R.id.country_filter);
        Button btnGenre = v.findViewById(R.id.genre_filter);
        Button btnYear = v.findViewById(R.id.release_filter);
        Button btnReset = v.findViewById(R.id.reset_button);
        btnCountry.setOnClickListener(this::filterCountries);
        btnGenre.setOnClickListener(this::filterGenres);
        btnYear.setOnClickListener(this::filterRelease);
        btnReset.setOnClickListener(this::filterReset);
        simpleSearchView = getActivity().findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getFilmPosters(API_URL_SEARCH + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    getFilmPosters(API_URL + api_query);
                } else {
                    getFilmPosters(API_URL_SEARCH + newText);
                }
                return false;
            }
        });

        return v;
    }

    private void getFilmPosters(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Film> films = TMDB_Parser.createFilmFromJsonString(response);
                        GridView gridView = getActivity().findViewById(R.id.gridview);
                        ImageListAdapter filmAdapter = new ImageListAdapter(getContext(), films);
                        gridView.setAdapter(filmAdapter);
                        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                            Film selected = (Film) parent.getItemAtPosition(position);
                            intent.putExtra("FilmId", selected.getId());
                            intent.putExtra("Filmname", selected.getName());
                            startActivity(intent);
                        };
                        gridView.setOnItemClickListener(mListClickedHandler);
                    } catch (JSONException e) {
                        alertDialogHelper.generateAlertDialog(getActivity());
                        e.printStackTrace();
                    }
                }, error -> alertDialogHelper.generateAlertDialog(getActivity()));
        queue.add(stringRequest);
    }


    public void filterGenres(View view) {
        bottomsheetcontainer.removeAllViews();
        getGenres(API_URL_GENRE);
        bottomSheetDialog.show();
    }

    public void filterCountries(View view) {
        bottomsheetcontainer.removeAllViews();
        getCountries(API_URL_COUNTRIES);
        bottomSheetDialog.show();

    }

    public void filterRelease(View view) {
        ConstraintLayout constraintLayout = getActivity().findViewById(R.id.numberpicker_constraint);
        constraintLayout.setVisibility(View.VISIBLE);
        Button resetbtn = getActivity().findViewById(R.id.reset_button);
        resetbtn.setVisibility(View.INVISIBLE);
        NumberPicker np = getActivity().findViewById(R.id.numberPicker);
        np.setMinValue(1900);
        np.setMaxValue(2100);
        np.setValue(2021);
        Button ok = getActivity().findViewById(R.id.yearpicker_button);
        np.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);
        ok.setOnClickListener(v -> {
            int yearValue = np.getValue();
            setApi_query(getApi_query() + "&year=" + yearValue);
            Button btn = getActivity().findViewById(R.id.release_filter);
            btn.setText(Integer.toString(np.getValue()));
            getFilmPosters(API_URL + api_query);
            np.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.INVISIBLE);
            Button resetbtn1 = getActivity().findViewById(R.id.reset_button);
            constraintLayout.setVisibility(View.INVISIBLE);
            resetbtn1.setVisibility(View.VISIBLE);
        });
    }


    public void filterReset(View view) {
        setApi_query("&sort_by=popularity.desc");
        getFilmPosters(API_URL + api_query);

        Button genreFilterBtn = getActivity().findViewById(R.id.genre_filter);
        genreFilterBtn.setText(R.string.button_genre);

        Button countryFilterBtn = getActivity().findViewById(R.id.country_filter);
        countryFilterBtn.setText(R.string.button_countries);

        Button yearFilterBtn = getActivity().findViewById(R.id.release_filter);
        yearFilterBtn.setText(R.string.button_release_year);

        Button resetBtn = getActivity().findViewById(R.id.reset_button);
        resetBtn.setVisibility(View.INVISIBLE);
    }


    private void getGenres(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Map<String, String> genres = TMDB_Parser.getFilmGenresFromJsonString(response);
                        generateView(genres, API_ADDITION_GENRE, R.id.genre_filter);

                    } catch (JSONException e) {
                        alertDialogHelper.generateAlertDialog(getActivity());
                        e.printStackTrace();
                    }
                }, error -> alertDialogHelper.generateAlertDialog(getActivity()));
        queue.add(stringRequest);
    }

    private void getCountries(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Map<String, String> countries = TMDB_Parser.getFilmCountriesFromJsonString(response);
                        generateView(countries, API_ADDITION_COUNTRY, R.id.country_filter);

                    } catch (JSONException e) {
                        alertDialogHelper.generateAlertDialog(getActivity());
                        ;
                        e.printStackTrace();
                    }
                }, error -> alertDialogHelper.generateAlertDialog(getActivity()));
        queue.add(stringRequest);
    }

    private void generateView(Map<String, String> map, String searchItem, int idButton) {
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setPaddingRelative(8, 8, 8, 8);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(getActivity());
            textView.setText((String) pair.getValue());
            textView.setPaddingRelative(8, 8, 8, 8);
            textView.setTextSize(25);
            linearLayout.addView(textView);
            linearLayout.setOnClickListener(v -> {
                setApi_query(getApi_query() + searchItem + pair.getKey());
                getFilmPosters(API_URL + api_query);
                bottomSheetDialog.hide();
                Button btn = getActivity().findViewById(idButton);
                btn.setText(pair.getValue().toString());
                Button resetBtn = getActivity().findViewById(R.id.reset_button);
                resetBtn.setVisibility(View.VISIBLE);
            });
            bottomsheetcontainer.addView(linearLayout);
        }
    }

    public String getApi_query() {
        return api_query;
    }

    public void setApi_query(String api_query) {
        this.api_query = api_query;
    }
}