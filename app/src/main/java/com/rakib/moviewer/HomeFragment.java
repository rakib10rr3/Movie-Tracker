package com.rakib.moviewer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rakib.moviewer.app.AppController;
import com.rakib.moviewer.util.SharedPref;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static String TAG = MainActivity.class.getSimpleName();

    String movieBasePath = "https://image.tmdb.org/t/p/w500";
    TextView resulText;
    Gson gson;
    JsonObjectRequest jsonObjectRequest;
    MoviesResponse moviesResponse;
    List<Result> resultList;
    ViewGroup bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    int index;

    List<Result> resultMoviesResponse;
    List<Result> responseForUpcomingMoviesList;
    List<Result> responseForActionMoviesList;
    List<Result> responseForAnimationMoviesList;
    List<Result> responseForWarMoviesList;
    List<Result> responseForRomanceMoviesList;
    List<Result> responseForSciFiMoviesList;
    List<Result> responseForHorrorMoviesList;
    List<Result> responseForMusicMoviesList;

    CardView mCardView1, mCardView2, mCardView3, mCardView4, mCardView5, mCardView6, mCardView7, mCardView8;
    BottomNavigationView bottomNavigationView;
    LinearLayout linearLayout;
    String url = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&page=1&include_adult=false&sort_by=popularity.desc";

    String upcomingMoviesUrl = "http://api.themoviedb.org/3/movie/upcoming?api_key=6c6ba266be0e6c4acdf15d39afccb023&language-en-US&region=CA|US";
    String actionMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=28,12";
    String animationMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=16";
    String warMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=10752";
    String romanceMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=10749";
    String scienceFictionMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=878";
    String horrorMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=27";
    String musicMoviesUrl = "https://api.themoviedb.org/3/discover/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=10402,";

    ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mCardView1 = view.findViewById(R.id.card1);
        mCardView2 = view.findViewById(R.id.card2);
        mCardView3 = view.findViewById(R.id.card3);
        mCardView4 = view.findViewById(R.id.card4);
        mCardView5 = view.findViewById(R.id.card5);
        mCardView6 = view.findViewById(R.id.card6);
        mCardView7 = view.findViewById(R.id.card7);
        mCardView8 = view.findViewById(R.id.card8);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);

        linearLayout = (LinearLayout) getActivity().findViewById(R.id.lin);

        bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Log.d("dsdsds", String.valueOf(bottomSheet));
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();


        makeUpComingMoviesRequest(view);
        makeActionMoviesRequest(view);
        makeAnimationMoviesRequest(view);
        makeWarMoviesRequest(view);
        makeRomanticMoviesRequest(view);
        makeScienceFictionMoviesRequest(view);
        makeMusicalMoviesRequest(view);
        makeHorrorMoviesRequest(view);


        return view;
    }

    public void makeUpComingMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                upcomingMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // mCardView1.setVisibility(View.VISIBLE);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForUpcomingMoviesList = moviesResponse.getResults();
                Log.d(TAG, "responseForUpcomingMoviesList" + String.valueOf(responseForUpcomingMoviesList.get(0).getTitle() + String.valueOf(responseForUpcomingMoviesList.get(5).getTitle())));

                recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

                MovieAdapter adapter = new MovieAdapter(getContext(), responseForUpcomingMoviesList, 2);
                recyclerView.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
//                index = 0;
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // this code will be executed after 2 seconds
//                        recyclerView.scrollToPosition(index++);
//                    }
//                }, 2000);
                runTest(new Runnable() {
                    @Override
                    public void run() {

//stuff that updates ui

                    }
                });
                recyclerView.scrollToPosition(2);

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


                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                        //layoutParams.setBehavior(new BottomNavigationBehavior());

                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
                        Glide.with(getContext())
                                .load(movieBasePath + responseForUpcomingMoviesList.get(position).getPosterPath())
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(imageView);
                        textView.setText(responseForUpcomingMoviesList.get(position).getTitle());

                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
                        mOverViewTextView.setText(responseForUpcomingMoviesList.get(position).getOverview());
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

    public void makeActionMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                actionMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //mCardView2.setVisibility(View.VISIBLE);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForActionMoviesList = moviesResponse.getResults();
                Log.d(TAG, "responseForActionMoviesList" + String.valueOf(responseForActionMoviesList.get(0).getTitle() + String.valueOf(responseForActionMoviesList.get(5).getTitle())));

                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForActionMoviesList, 2);
                //recyclerView.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView.setLayoutManager(gridLayoutManager);

                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
                recyclerView1.setAdapter(adapter);
                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView1.setLayoutManager(gridLayoutManager1);

//                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
//                recyclerView2.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView2.setLayoutManager(gridLayoutManager2);
//
//                RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler3);
//                recyclerView3.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView3.setLayoutManager(gridLayoutManager3);


                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                        //layoutParams.setBehavior(new BottomNavigationBehavior());

                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
                        Glide.with(getContext())
                                .load(movieBasePath + responseForActionMoviesList.get(position).getPosterPath())
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(imageView);
                        textView.setText(responseForActionMoviesList.get(position).getTitle());

                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
                        mOverViewTextView.setText(responseForActionMoviesList.get(position).getOverview());
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

    public void makeAnimationMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                animationMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //mCardView3.setVisibility(View.VISIBLE);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForAnimationMoviesList = moviesResponse.getResults();
                Log.d(TAG, "responseForAnimationMoviesList" + String.valueOf(responseForAnimationMoviesList.get(0).getTitle() + String.valueOf(responseForAnimationMoviesList.get(5).getTitle())));


                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForAnimationMoviesList, 2);
                //recyclerView.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView.setLayoutManager(gridLayoutManager);

//                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
//                recyclerView1.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView1.setLayoutManager(gridLayoutManager1);

                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
                recyclerView2.setAdapter(adapter);
                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView2.setLayoutManager(gridLayoutManager2);


//
//                RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler3);
//                recyclerView3.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView3.setLayoutManager(gridLayoutManager3);


                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        //CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                        //layoutParams.setBehavior(new BottomNavigationBehavior());

                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
                        Glide.with(getContext())
                                .load(movieBasePath + responseForAnimationMoviesList.get(position).getPosterPath())
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(imageView);
                        textView.setText(responseForAnimationMoviesList.get(position).getTitle());

                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
                        mOverViewTextView.setText(responseForAnimationMoviesList.get(position).getOverview());
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

    public void makeWarMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                warMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //swipeRefreshLayout.setRefreshing(false);

                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForWarMoviesList = moviesResponse.getResults();
                Log.d(TAG, "responseForWarMoviesList" + String.valueOf(responseForWarMoviesList.get(0).getTitle() + String.valueOf(responseForWarMoviesList.get(5).getTitle())));

                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForWarMoviesList, 2);
                //recyclerView.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView.setLayoutManager(gridLayoutManager);

//                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
//                recyclerView1.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView1.setLayoutManager(gridLayoutManager1);

//                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
//                recyclerView2.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView2.setLayoutManager(gridLayoutManager2);

                RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recycler3);
                recyclerView3.setAdapter(adapter);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView3.setLayoutManager(gridLayoutManager3);
                progressBar.setVisibility(View.INVISIBLE);

                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
                        Glide.with(getContext())
                                .load(movieBasePath + responseForWarMoviesList.get(position).getPosterPath())
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(imageView);
                        textView.setText(responseForWarMoviesList.get(position).getTitle());
//
//                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
                        mOverViewTextView.setText(responseForWarMoviesList.get(position).getOverview());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie_id", responseForWarMoviesList.get(position).getId());
                        intent.putExtra("poster", responseForWarMoviesList.get(position).getPosterPath());
                        startActivity(intent);
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

    public void makeRomanticMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                romanceMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //swipeRefreshLayout.setRefreshing(false);


                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForRomanceMoviesList = moviesResponse.getResults();
                Log.d(TAG, "responseForRomanceMoviesList" + String.valueOf(responseForRomanceMoviesList.get(0).getTitle() + String.valueOf(responseForRomanceMoviesList.get(5).getTitle())));

                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForRomanceMoviesList, 2);


                RecyclerView recyclerView4 = (RecyclerView) view.findViewById(R.id.recycler4);
                recyclerView4.setAdapter(adapter);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView4.setLayoutManager(gridLayoutManager3);
                progressBar.setVisibility(View.INVISIBLE);

                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
                        Glide.with(getContext())
                                .load(movieBasePath + responseForRomanceMoviesList.get(position).getPosterPath())
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(imageView);
                        textView.setText(responseForRomanceMoviesList.get(position).getTitle());

//                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
                        mOverViewTextView.setText(responseForRomanceMoviesList.get(position).getOverview());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie_id", responseForRomanceMoviesList.get(position).getId());
                        intent.putExtra("poster", responseForRomanceMoviesList.get(position).getPosterPath());
                        startActivity(intent);
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

    public void makeScienceFictionMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                scienceFictionMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForSciFiMoviesList = moviesResponse.getResults();


                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForSciFiMoviesList, 2);

                RecyclerView recyclerView5 = (RecyclerView) view.findViewById(R.id.recycler5);
                recyclerView5.setAdapter(adapter);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView5.setLayoutManager(gridLayoutManager3);
                progressBar.setVisibility(View.INVISIBLE);

                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
//                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
//                        Glide.with(getContext())
//                                .load(movieBasePath + responseForWarMoviesList.get(position).getPosterPath())
//                                .thumbnail(0.5f)
//                                .apply(new RequestOptions()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                ).into(imageView);
//                        textView.setText(responseForWarMoviesList.get(position).getTitle());
//
//                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
//                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
//                        mOverViewTextView.setText(responseForWarMoviesList.get(position).getOverview());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie_id", responseForWarMoviesList.get(position).getId());
                        intent.putExtra("poster", responseForWarMoviesList.get(position).getPosterPath());
                        startActivity(intent);
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

    public void makeMusicalMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                musicMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForMusicMoviesList = moviesResponse.getResults();


                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForMusicMoviesList, 2);
                //recyclerView.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView.setLayoutManager(gridLayoutManager);

//                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
//                recyclerView1.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView1.setLayoutManager(gridLayoutManager1);

//                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
//                recyclerView2.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView2.setLayoutManager(gridLayoutManager2);

                RecyclerView recyclerView6 = (RecyclerView) view.findViewById(R.id.recycler6);
                recyclerView6.setAdapter(adapter);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView6.setLayoutManager(gridLayoutManager3);
                progressBar.setVisibility(View.INVISIBLE);

                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
//                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
//                        Glide.with(getContext())
//                                .load(movieBasePath + responseForWarMoviesList.get(position).getPosterPath())
//                                .thumbnail(0.5f)
//                                .apply(new RequestOptions()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                ).into(imageView);
//                        textView.setText(responseForWarMoviesList.get(position).getTitle());
//
//                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
//                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
//                        mOverViewTextView.setText(responseForWarMoviesList.get(position).getOverview());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie_id", responseForWarMoviesList.get(position).getId());
                        intent.putExtra("poster", responseForWarMoviesList.get(position).getPosterPath());
                        startActivity(intent);
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

    public void makeHorrorMoviesRequest(final View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                horrorMoviesUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                //swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, response.toString());
                moviesResponse = gson.fromJson(response.toString(), MoviesResponse.class);
                Log.d(TAG, String.valueOf(moviesResponse.getTotalPages()));
                responseForHorrorMoviesList = moviesResponse.getResults();


                //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                MovieAdapter adapter = new MovieAdapter(getContext(), responseForHorrorMoviesList, 2);
                //recyclerView.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView.setLayoutManager(gridLayoutManager);

//                RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler1);
//                recyclerView1.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//                recyclerView1.setLayoutManager(gridLayoutManager1);

//                RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler2);
//                recyclerView2.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,false);
//                recyclerView2.setLayoutManager(gridLayoutManager2);

                RecyclerView recyclerView7 = (RecyclerView) view.findViewById(R.id.recycler7);
                recyclerView7.setAdapter(adapter);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                recyclerView7.setLayoutManager(gridLayoutManager3);
                progressBar.setVisibility(View.INVISIBLE);
                mCardView1.setVisibility(View.VISIBLE);
                mCardView2.setVisibility(View.VISIBLE);
                mCardView3.setVisibility(View.VISIBLE);
                mCardView4.setVisibility(View.VISIBLE);
                mCardView5.setVisibility(View.VISIBLE);
                mCardView6.setVisibility(View.VISIBLE);
                mCardView7.setVisibility(View.VISIBLE);
                mCardView8.setVisibility(View.VISIBLE);

                adapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
//                        Toast.makeText(getContext(),String.valueOf(resultMoviesResponse.get(position).getId()),Toast.LENGTH_SHORT).show();
//                        View myV = myView(bottomSheet,position);
//                        TextView textView = (TextView)myV.findViewById(R.id.bottomsheet_text);
//                        textView.setText(R.string.bottom_sheet_text1);
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                        TextView textView = bottomSheet.findViewById(R.id.bottomsheet_text);
//                        ImageView imageView = bottomSheet.findViewById(R.id.bottom_movie_image);
//                        Glide.with(getContext())
//                                .load(movieBasePath + responseForWarMoviesList.get(position).getPosterPath())
//                                .thumbnail(0.5f)
//                                .apply(new RequestOptions()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                ).into(imageView);
//                        textView.setText(responseForWarMoviesList.get(position).getTitle());
//
//                        //Toast.makeText(getContext(),resultMoviesResponse.get(position).getTitle() ,Toast.LENGTH_SHORT).show();
//                        TextView mOverViewTextView = bottomSheet.findViewById(R.id.bottom_movie_overview);
//                        mOverViewTextView.setText(responseForWarMoviesList.get(position).getOverview());
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie_id", responseForWarMoviesList.get(position).getId());
                        intent.putExtra("poster", responseForWarMoviesList.get(position).getPosterPath());
                        startActivity(intent);
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
        SharedPref.write("fragment", "F1");
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}



