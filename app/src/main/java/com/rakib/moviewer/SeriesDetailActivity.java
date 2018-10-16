package com.rakib.moviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.rakib.moviewer.app.AppController;
import com.rakib.moviewer.model.Cast;
import com.rakib.moviewer.model.Genre;
import com.rakib.moviewer.model.Results;
import com.rakib.moviewer.model.SingleSeriesData;


import com.rakib.moviewer.model.series.single.CreatedBy;
import com.rakib.moviewer.model.series.single.Network;
import com.rakib.moviewer.util.CustomTab;
import com.rakib.moviewer.util.FixAppBarLayoutBehavior;
import com.rakib.moviewer.util.SharedPref;
import com.rany.albeg.wein.springfabmenu.SpringFabMenu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class SeriesDetailActivity extends AppCompatActivity {


    Gson gson;
    GsonBuilder gsonBuilder;
    String url;
    String firestoreMovieId;

    TextView mTitleTextView;
    TextView mGenreTextView;
    TextView mOverviewTextView;
    TextView mRunTimeTextView;
    TextView mCreatedByTextView;
    TextView mRatingTextView;
    TextView mNetworkTextView;
    TextView mLangugaeTextView;
    TextView mCountryTextView;
    TextView mNumberOfEpisodesTextView;
    TextView mNumberOfSeasonsTextView;
    TextView mFirstAirDateTextView;
    TextView mLastAirDateTextView;
    ImageView mPosterImageView;
    CardView mDetailCardView;

    ImageButton mIMDBImageButton;
    ImageButton mFacebookImageButton;
    ImageButton mInstagramemageButton;
    ImageButton mWebmageButton;
    ImageButton mYoutubeImageButton;

    Button reviewButton;

    CustomTab customTab;
    Toolbar toolbar;
    FloatingActionButton watchFab;
    FloatingActionButton watchListedFab;
    SpringFabMenu sfm;


    JsonObjectRequest jsObjRequest;
    SingleSeriesData singleSeriesData;

    String posterUrl;
    String title;
    String overview;
    int runtime;
    int id;
    String budget;
    String revenue;
    String releaseDate;
    String poster = "https://image.tmdb.org/t/p/w154";
    String userId;
    String imdbId;
    String status;
    String homePage;
    double rating;
    String castNames = "";

    String imdbLink = "http://www.imdb.com/title/";
    String facebookLink = "https://www.facebook.com/";
    String youtubeLink = "https://www.youtube.com/watch/";
    String instagramLink = "https://www.instagram.com/";
    String videoKey;

    ArrayList<Genre> genreArrayList;
    ArrayList<Network> networkArrayList;
    ArrayList<String> countryArrayList;
    ArrayList<CreatedBy> createdByArrayList;
    ArrayList<Cast> castArrayList;
    ArrayList<Results> resultsArrayList;

    FirebaseFirestore db;
    ProgressBar progressBar;

    boolean watched;
    boolean watchlisted;

    Snackbar snackbar;

    Integer watchedSeriesNumber = 0;
    Integer watchListedSeriesNumber = 0;
    String firebaseUserId;

    ExpandableTextView expTv1;

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);

        firebaseUserId = FirebaseAuth.getInstance().getUid();

        initializeViews();
        showSnackBar();
        setSupportActionBar(toolbar);

        SharedPref.init(getApplicationContext());
        watchedSeriesNumber = SharedPref.read(firebaseUserId + "watched_series",0);
        watchListedSeriesNumber = SharedPref.read(firebaseUserId + "watch_listed_series",0);

        mDetailCardView.setVisibility(View.INVISIBLE);
        Integer series_id = getIntent().getIntExtra("series_id", 0);
        status = getIntent().getStringExtra("status");
        title = getIntent().getStringExtra("title");
        firestoreMovieId = getIntent().getStringExtra("doc_id");
        db = FirebaseFirestore.getInstance();


        checkStatus();
        isWatched();
        isWatchListed();
        sfm = (SpringFabMenu) findViewById(R.id.series_sfm);

        id = getIntent().getIntExtra("series_id", 0);

        AppBarLayout abl = findViewById(R.id.app_bar);
        ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).setBehavior(new FixAppBarLayoutBehavior());

        sfm.setOnSpringFabMenuItemClickListener(new SpringFabMenu.OnSpringFabMenuItemClickListener() {
            @Override
            public void onSpringFabMenuItemClick(View view) {
                switch (view.getId()) {
                    case R.id.series_fab_1:
                        if (!watched && !watchlisted)
                            addToWatched();
                        else if (!watched && watchlisted) {
                            showConfirmDialog();
                        } else
                            removeFromWatched();
                        break;
                    case R.id.series_fab_2:
                        if (!watchlisted)
                            addToWatchListed();
                        else
                            removeFromWatchListed();
                        break;
                }
            }
        });

        String seriesId = String.valueOf(series_id);

        url = "https://api.themoviedb.org/3/tv/" + seriesId + "?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&append_to_response=credits,external_ids,videos";

        posterUrl = "https://image.tmdb.org/t/p/w780" + getIntent().getStringExtra("poster");


        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        customTab = new CustomTab();


        makeRequest();


    }

    private void showSnackBar() {
        snackbar = Snackbar.make(findViewById(R.id.detail_series_coordinate_layout),
                "Loading data", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    private void makeRequest() {
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        snackbar.dismiss();
                        mDetailCardView.setVisibility(View.VISIBLE);
                        sfm.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        singleSeriesData = gson.fromJson(response.toString(), SingleSeriesData.class);
                        id = singleSeriesData.getId();
                        title = singleSeriesData.getName();
                        Log.d("SSS", title);
                        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        poster = singleSeriesData.getPosterPath();
                        mTitleTextView.setText(singleSeriesData.getName());
                        overview = singleSeriesData.getOverview();
                        rating = singleSeriesData.getVoteAverage();
                        mOverviewTextView.setText(overview);
                        mOverviewTextView.setBackgroundColor(Color.parseColor("#FAFAFA"));
                        Glide.with(getApplicationContext())
                                .load(posterUrl)
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(mPosterImageView);

                        String runtime = String.valueOf(singleSeriesData.getEpisodeRunTime() + " minutes");
                        mRunTimeTextView.setText(runtime);

                        mNumberOfSeasonsTextView.setText(String.valueOf(singleSeriesData.getNumberOfSeasons()));

                        mNumberOfEpisodesTextView.setText(String.valueOf(singleSeriesData.getNumberOfEpisodes()));

                        mFirstAirDateTextView.setText(String.valueOf(singleSeriesData.getFirstAirDate()));

                        mLastAirDateTextView.setText(String.valueOf(singleSeriesData.getLastAirDate()));

                        mRatingTextView.setText(String.valueOf(singleSeriesData.getVoteAverage()));

                        mLangugaeTextView.setText(singleSeriesData.getOriginalLanguage());


                        createdByArrayList = new ArrayList<>();
                        createdByArrayList = singleSeriesData.getCreatedBy();
                        for (int i = 0; i < createdByArrayList.size(); i++) {
                            mCreatedByTextView.append(createdByArrayList.get(i).getName());
                            if (i != createdByArrayList.size() - 1)
                                mCreatedByTextView.append(", ");
                        }

                        genreArrayList = new ArrayList<>();
                        genreArrayList = singleSeriesData.getGenres();
                        for (int i = 0; i < genreArrayList.size(); i++) {
                            mGenreTextView.append(genreArrayList.get(i).getName());
                            if (i != genreArrayList.size() - 1)
                                mGenreTextView.append(", ");
                        }

                        networkArrayList = new ArrayList<>();
                        networkArrayList = singleSeriesData.getNetworks();
                        for (int i = 0; i < networkArrayList.size(); i++) {
                            mNetworkTextView.append(networkArrayList.get(i).getName());
                            if (i != networkArrayList.size() - 1)
                                mNetworkTextView.append(", ");
                        }

                        countryArrayList = new ArrayList<>();
                        countryArrayList = singleSeriesData.getOriginCountry();
                        for (int i = 0; i < countryArrayList.size(); i++) {
                            mCountryTextView.append(countryArrayList.get(i));
                            if (i != countryArrayList.size() - 1)
                                mCountryTextView.append(", ");
                        }

                        castArrayList = new ArrayList<>();
                        castArrayList = singleSeriesData.getCredits().getCast();
                        for (int i = 0; i < castArrayList.size(); i++) {
                            //mCastTextView.append(castArrayList.get(i).getName() + "\n");
                            castNames += castArrayList.get(i).getName() + " as " + castArrayList.get(i).getCharacter() + "\n";
                            // characterNames += castArrayList.get(i).getCharacter() + "\n";
                        }
                        expTv1.setText((castNames));

                        toolbar.setTitle(singleSeriesData.getName());

                        final String imdbId = singleSeriesData.getExternalIds().getImdbId();
                        final String facebookId = singleSeriesData.getExternalIds().getFacebookId();
                        final String instagramId = singleSeriesData.getExternalIds().getInstagramId();
                        homePage = singleSeriesData.getHomepage();

                        Log.d("Homepage",homePage);

                        mIMDBImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (imdbId == null){
                                    Toast.makeText(getApplicationContext(), "No Imdb link available", Toast.LENGTH_SHORT).show();
                                }else {
                                    customTab.open_link(getApplicationContext(), imdbLink + imdbId);

                                }
                            }
                        });

                        mFacebookImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (facebookId == null){
                                    Toast.makeText(getApplicationContext(), "No Facebook link available", Toast.LENGTH_SHORT).show();
                                } else {
                                    customTab.open_link(getApplicationContext(), facebookLink + facebookId);

                                }
                            }
                        });

                        mWebmageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (homePage.equals(""))
                                {
                                    Snackbar.make(findViewById(R.id.detail_series_coordinate_layout), R.string.no_official_website,
                                            Snackbar.LENGTH_SHORT)
                                            .show();
                                }

                                else {
                                    customTab.open_link(getApplicationContext(), homePage);
                                }
                            }
                        });

                        mInstagramemageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (instagramId == null){
                                    Toast.makeText(getApplicationContext(), "No Instagram link available", Toast.LENGTH_SHORT).show();
                                } else {
                                    String link = instagramLink + instagramId + "/?hl=en";
                                    customTab.open_link(getApplicationContext(), link);
                                }

                            }
                        });

                        resultsArrayList = new ArrayList<>();
                        resultsArrayList = singleSeriesData.getVideos().getResults();

                        mYoutubeImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (resultsArrayList.size() == 0){
                                    Toast.makeText(getApplicationContext(), "No Youtube link available", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String link = singleSeriesData.getVideos().getResults().get(0).getKey();
                                    customTab.open_link(getApplicationContext(), youtubeLink+link);
                                }
                            }
                        });

                        if (resultsArrayList.size() == 0){

                        }

                        reviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SeriesDetailActivity.this, ReviewActivity.class);
                                intent.putExtra("title", title);
                                Log.d("title", title);
                                startActivity(intent);
                            }
                        });

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void initializeViews() {
        mPosterImageView = findViewById(R.id.detail_series_collapsing_poster);
        mTitleTextView = findViewById(R.id.detail_series_title);
        mGenreTextView = findViewById(R.id.detail_series_genre);
        mOverviewTextView = findViewById(R.id.detail_series_overview);
        mRunTimeTextView = findViewById(R.id.detail_series_runtime);
        mCreatedByTextView = findViewById(R.id.detail_series_created_by);
        mNetworkTextView = findViewById(R.id.detail_series_networks);
        mCountryTextView = findViewById(R.id.detail_series_country);
        mLangugaeTextView = findViewById(R.id.detail_series_spoken_language);
        mRatingTextView = findViewById(R.id.detail_series_rating);
        mNumberOfEpisodesTextView = findViewById(R.id.detail_series_number_of_episodes);
        mNumberOfSeasonsTextView = findViewById(R.id.detail_series_number_of_seasons);
        mFirstAirDateTextView = findViewById(R.id.detail_series_first_air_date);
        mLastAirDateTextView = findViewById(R.id.detail_series_last_air_date);
        mDetailCardView = findViewById(R.id.series_detail_card_view);
        reviewButton = findViewById(R.id.series_review_button);



        mIMDBImageButton = findViewById(R.id.series_imdb_image_button);
        mFacebookImageButton = findViewById(R.id.series_facebook_image_button);
        mInstagramemageButton = findViewById(R.id.series_instagram_image_button);
        mWebmageButton = findViewById(R.id.series_web_image_button);
        mYoutubeImageButton = findViewById(R.id.series_youtube_image_button);

        toolbar = findViewById(R.id.series_toolbar);
        toolbar.setTitle("");



        progressBar = findViewById(R.id.detail_series_progressbar);

        watchFab = findViewById(R.id.series_fab_1);
        watchListedFab = findViewById(R.id.series_fab_2);

        expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
    }

    private void addToWatched() {
        status = "watched";
        Date date = new Date();
        db.collection("Watched Series")
                .add(new Series(title,rating, id, date, poster, userId, status))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        Toast.makeText(getApplicationContext(), "Successfully added ", Toast.LENGTH_SHORT).show();
        watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));
        watchListedFab.setEnabled(false);
        watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        watched = true;
        watchedSeriesNumber++;
        SharedPref.write(firebaseUserId + "watched_series",watchedSeriesNumber);
        isWatched();
    }


    private void addToWatchListed() {
        status = "watchlisted";
        Date date = new Date();
        db.collection("WatchListed Series")
                .add(new Series(title,rating, id, date, poster, userId, status))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        watchListedFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_visibility_off_black_24dp));
        watchlisted = true;
        watchFab.setEnabled(false);
        watchFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        Toast.makeText(getApplicationContext(), "Added to watchlisted collection", Toast.LENGTH_SHORT).show();
        watchListedSeriesNumber++;
        SharedPref.write(firebaseUserId + "watch_listed_series",watchListedSeriesNumber);
        isWatchListed();
    }

    private void checkStatus() {
        if (status != null) {
            if (status.equals("watched")) {
                watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));
                watched = true;
                watchListedFab.setEnabled(false);
                watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                Log.d("MMM", "watched");
            } else if (status.equals("watchlisted")) {
                watchListedFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_visibility_off_black_24dp));
                watchlisted = true;
                watchFab.setEnabled(false);
                watchFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            }

        }
    }

    private void isWatched() {
        db.collection("Watched Series")
                .whereEqualTo("title", title)
                .whereEqualTo("userId",firebaseUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("BBB", document.getId() + " => " + document.getData());
                                watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));
                                watchListedFab.setEnabled(false);
                                watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                                watched = true;
                                firestoreMovieId = document.getId();
                                Log.d("XXX", firestoreMovieId);

                            }

                        } else {
                            Log.d("CCC", "Error getting documents: ", task.getException());
                            watched = false;
                        }
                    }
                });
    }

    private void isWatchListed() {
        db.collection("WatchListed Series")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("BBB", document.getId() + " => " + document.getData());
                                watchListedFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_visibility_off_black_24dp));
                                watchlisted = true;
                                watchFab.setEnabled(false);
                                watchFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                                firestoreMovieId = document.getId();
                                Log.d("ZZZ", firestoreMovieId);

                            }

                        } else {
                            Log.d("CCC", "Error getting documents: ", task.getException());
                            watchlisted = false;
                        }
                    }
                });
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.do_you_want_to_delete_it)
                .setTitle(R.string.delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addToWatched();
                removeFromWatchListed();

                Toast.makeText(getApplicationContext(), R.string.added_to_watched, Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeFromWatchListed() {
        Log.d("ZZZ", "1");
        db.collection("WatchListed Series")
                .document(firestoreMovieId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("ZZZ", "2");
                    }
                });
        watchListedFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_watch_later_black_24dp));
        watchlisted = false;
        watchFab.setEnabled(true);
        watchFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff9800")));
        Toast.makeText(getApplicationContext(), "Removed from watchlisted collection", Toast.LENGTH_SHORT).show();
        watchListedSeriesNumber--;
        SharedPref.write(firebaseUserId + "watch_listed_series",watchListedSeriesNumber);
        Log.d("ZZZ", "3");
        isWatchListed();
    }

    private void removeFromWatched() {
        db.collection("Watched Series")
                .document(firestoreMovieId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
        watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_black_24dp));
        watchListedFab.setEnabled(true);
        watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff9800")));
        watched = false;
        Toast.makeText(getApplicationContext(), "Removed from watched collection", Toast.LENGTH_SHORT).show();
        watchedSeriesNumber--;
        SharedPref.write(firebaseUserId + "watched_series",watchedSeriesNumber);
        isWatched();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
