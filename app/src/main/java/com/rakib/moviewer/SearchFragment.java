package com.rakib.moviewer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rakib.moviewer.app.AppController;
import com.rakib.moviewer.model.series.SeriesResponse;
import com.rakib.moviewer.util.SharedPref;

import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    ProgressBar progressBar;

    private static String TAG = MainActivity.class.getSimpleName();

    Gson gson;
    JsonObjectRequest jsonObjectRequest;
    MoviesResponse moviesResponse;
    SeriesResponse seriesResponse;
    List<Result> resultList;
    SharedPreferences SP;
    String choice;
    String url="";
    View view;

    String keyword;
    TextView mSearcTextView;
    TextView mNoResultTextView;
    RelativeLayout mNoResulRelativeLayout;

    public void setKeyword(String mKeyword) {
        keyword = mKeyword;
    }

    private String getKeyword() {
        return keyword;
    }




    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);



        progressBar = view.findViewById(R.id.search_progress_bar);
        //mNoResultTextView = view.findViewById(R.id.no_result_found_text_view);
        mNoResulRelativeLayout = view.findViewById(R.id.no_result_found_relative);
        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        if (keyword == null){
            keyword = SharedPref.read("search_movie_keyword","");
        }
        loadPref(view);


        return view;

    }


    public void makeMovieSearchRequest(final View view) {
        Log.d(TAG, url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                //Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                List<Result> resultMoviesResponse = moviesResponse.getResults();
                if (moviesResponse.getTotalResults() == 0){
                    Log.d(TAG, "no");
                    //mNoResultTextView.setVisibility(View.VISIBLE);
                    mNoResulRelativeLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    animation.setInterpolator(interpolator);
                    mNoResulRelativeLayout.startAnimation(animation);
                }
                // Log.d(TAG, "resultmoviesResponse" + String.valueOf(resultMoviesResponse.get(0).getTitle() + String.valueOf(resultMoviesResponse.get(5).getTitle())));
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler);
                //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                int resId = R.anim.layout_animation_fall_down;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
                recyclerView.setLayoutAnimation(animation);

                MovieSearchAdapter adapter = new MovieSearchAdapter(getContext(), resultMoviesResponse);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void makeSeriesSearchRequest(final View view){
        Log.d(TAG, url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("response" , response.toString());
                seriesResponse = gson.fromJson(response.toString(), SeriesResponse.class);
                Log.d("pages", String.valueOf(seriesResponse.getTotalPages()));
                List<com.rakib.moviewer.model.series.Result> resultSeriesResponse = seriesResponse.getResults();
                if (seriesResponse.getTotalResults() == 0){
                    Log.d(TAG, "no");
                    mNoResulRelativeLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                    animation.setInterpolator(interpolator);
                    mNoResulRelativeLayout.startAnimation(animation);
                }
                // Log.d(TAG, "resultmoviesResponse" + String.valueOf(resultMoviesResponse.get(0).getTitle() + String.valueOf(resultMoviesResponse.get(5).getTitle())));
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler);
                //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                int resId = R.anim.layout_animation_fall_down;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
                recyclerView.setLayoutAnimation(animation);

                SeriesSearchAdapter adapter = new SeriesSearchAdapter(getContext(), resultSeriesResponse);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mNoResultTextView.setVisibility(View.VISIBLE);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void loadPref(View view) {
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        choice = SP.getString("choice", "1");
        if (choice.equals("1"))
        {
            url = "https://api.themoviedb.org/3/search/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&page=1&include_adult=false&sort_by=popularity.desc&query=";

            url += getKeyword();
            makeMovieSearchRequest(view);
        }

        else
        {
            url = "https://api.themoviedb.org/3/search/tv?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&page=1&query=";

            url += getKeyword();
            makeSeriesSearchRequest(view);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPref.init(getActivity().getApplicationContext());
        SharedPref.write("fragment", "F6");
        SharedPref.write("search_movie_keyword",keyword);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (keyword.equals("null"))
        {
            //Toast.makeText(getActivity().getApplicationContext(), "yoo", Toast.LENGTH_SHORT).show();


        }
        //keyword = SharedPref.read("search_movie_keyword","");
       // loadPref(view);
    }

    private class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}
