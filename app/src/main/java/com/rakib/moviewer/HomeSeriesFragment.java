package com.rakib.moviewer;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rakib.moviewer.app.AppController;
import com.rakib.moviewer.model.series.*;
import com.rakib.moviewer.util.SharedPref;

import org.json.JSONObject;

import java.util.List;

public class HomeSeriesFragment extends Fragment {

    private static String TAG = MainActivity.class.getSimpleName();
    JsonObjectRequest jsonObjectRequest;
    Gson gson;
    String seriesUrl = "https://api.themoviedb.org/3/discover/tv?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false";

    List<com.rakib.moviewer.model.series.Result> responseForSeriesList;

    SeriesResponse seriesResponse;

    CardView cardView;

    Snackbar mySnackbar;

    ProgressBar progressBar;

    public HomeSeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflatedView = inflater.inflate(R.layout.fragment_home_series, container, false);
        Toolbar toolbar = (Toolbar) inflatedView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //loadSeriesLoadingDataSnackBar(inflatedView);

        cardView = inflatedView.findViewById(R.id.home_series_card);

        progressBar = inflatedView.findViewById(R.id.series_loading_progress_bar);

        makeSeriesRequest(inflatedView);
        return inflatedView;
    }

    private void loadSeriesLoadingDataSnackBar(View view) {
        mySnackbar = Snackbar.make(view.findViewById(R.id.home_series_coordinator),
                R.string.loading_series, Snackbar.LENGTH_INDEFINITE);
        mySnackbar.show();
    }

    private void makeSeriesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                seriesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                seriesResponse = gson.fromJson(response.toString(), SeriesResponse.class);
                Log.d(TAG, String.valueOf(seriesResponse.getTotalPages()));
                responseForSeriesList = seriesResponse.getResults();


                cardView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_series_recycler);
                SeriesAdapter adapter = new SeriesAdapter(getContext(), responseForSeriesList );
                recyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);

//                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
//                recyclerView1.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView1.setLayoutManager(gridLayoutManager1);

//                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
//                recyclerView2.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView2.setLayoutManager(gridLayoutManager2);
//
//                RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler3);
//                recyclerView3.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView3.setLayoutManager(gridLayoutManager3);


                adapter.setListener(new SeriesAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);


                        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                        //layoutParams.setBehavior(new BottomNavigationBehavior());



                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();

                    }
                });

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

    @Override
    public void onPause() {
        super.onPause();
        SharedPref.init(getActivity().getApplicationContext());
        SharedPref.write("fragment","F4");
    }
}
