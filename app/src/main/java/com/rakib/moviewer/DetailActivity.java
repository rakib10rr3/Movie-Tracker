package com.rakib.moviewer;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
import com.rakib.moviewer.model.*;
import com.rakib.moviewer.util.CustomTab;
import com.rakib.moviewer.util.FixAppBarLayoutBehavior;
import com.rakib.moviewer.util.SharedPref;
import com.rany.albeg.wein.springfabmenu.SpringFabMenu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class DetailActivity extends AppCompatActivity {

    Gson gson;
    GsonBuilder gsonBuilder;
    String url;
    String firestoreMovieId;

    TextView mTitleTextView;
    TextView mGenreTextView;
    TextView mOverviewTextView;
    TextView mProductionCompaniesTextView;
    TextView mProductionCountryTextView;
    TextView mBudgetTextView;
    TextView mRevenueTextView;
    TextView mReleaseDateTextView;
    TextView mSpokenLanguageTextView;
    TextView mDirectorTextView;
    TextView mRunTimeTextView;
    ExpandableTextView expTv1;
    ExpandableTextView expTv2;
    // TextView mCastTextView;
    //ExpandableTextView mCastTextView;
    Button buttonToggle;
    Button reviewButton;
    CollapsingToolbarLayout collapsingToolbarLayout;

    CardView cardView;

    Toolbar toolbar;
    FloatingActionButton watchFab;
    FloatingActionButton watchListedFab;
    ImageView mPosterImageView;
    SpringFabMenu sfm;

    JsonObjectRequest jsObjRequest;
    StringRequest stringRequest;
    String responsed;

    SingleMovieData singleMovieData;
    CustomTab customTab;
    ProgressBar progressBar;

    ArrayList<Genre> genreArrayList;
    ArrayList<ProductionCompany> productionCompanyArrayList;
    ArrayList<SpokenLanguage> spokenLanguageArrayList;
    ArrayList<Cast> castArrayList;
    ArrayList<ProductionCountry> productionCountryArrayList;
    ArrayList<Crew> crewArrayList;
    ArrayList<Results> resultsArrayList;

    String castNames = "";
    String directorName;
    String characterNames = "";
    String posterUrl;
    int id;
    double rating;

    String title;
    String overview;
    int runtime;
    String budget;
    String revenue;
    String releaseDate;
    String poster = "https://image.tmdb.org/t/p/w154";
    String userId;
    String imdbId;
    String status;

    String homePage;

    String imdbLink = "http://www.imdb.com/title/";
    String facebookLink = "https://www.facebook.com/";
    String youtubeLink = "https://www.youtube.com/watch/";
    String videoKey;

    FirebaseFirestore db;

    ImageButton imdbImageButton;
    ImageButton facebookImageButton;
    ImageButton webImageButton;
    ImageButton youtubeImageButton;

    boolean watched;
    boolean watchlisted;

    Snackbar snackbar;

    Integer watchedMoviesNumber = 0;
    Integer watchListedMoviesNumber = 0;

    String firebaseUserId;
    AppBarLayout abl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        setSupportActionBar(toolbar);

        firebaseUserId = FirebaseAuth.getInstance().getUid();

        SharedPref.init(getApplicationContext());
        watchedMoviesNumber = SharedPref.read(firebaseUserId + "watched_movies", 0);
        watchListedMoviesNumber = SharedPref.read(firebaseUserId + "watch_listed_movies", 0);


        Integer movie_id = getIntent().getIntExtra("movie_id", 0);
        status = getIntent().getStringExtra("status");
        title = getIntent().getStringExtra("title");
        firestoreMovieId = getIntent().getStringExtra("doc_id");
        db = FirebaseFirestore.getInstance();

        checkStatus();
        isWatched();
        isWatchListed();
        sfm = (SpringFabMenu) findViewById(R.id.sfm);


        id = getIntent().getIntExtra("movie_id", 0);

        abl = findViewById(R.id.app_bar);

        ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).setBehavior(new FixAppBarLayoutBehavior());


        sfm.setOnSpringFabMenuItemClickListener(new SpringFabMenu.OnSpringFabMenuItemClickListener() {
            @Override
            public void onSpringFabMenuItemClick(View view) {
                switch (view.getId()) {
                    case R.id.fab_1:
                        if (!watched && !watchlisted)
                            addToWatched();
                        else if (!watched && watchlisted) {
                            showConfirmDialog();
                        } else
                            removeFromWatched();
                        break;
                    case R.id.fab_2:
                        if (!watchlisted)
                            addToWatchListed();
                        else
                            removeFromWatchListed();
                        break;
                }
            }
        });

//        try {
//            if (watchFab != null && fabMenu != null) {
//
//                View customView = View.inflate(this, R.layout.layout_custom_menu, null);
//                setupCustomFilterView(customView);
//                fabMenu.setCustomView(customView);
//                fabMenu.bindAnchorView(watchFab);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        watchFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String id = String.valueOf(movie_id);
        Log.d("movie id", id);

        posterUrl = "https://image.tmdb.org/t/p/w780" + getIntent().getStringExtra("poster");
        Log.d("poster", posterUrl);


        url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&append_to_response=credits,external_ids,videos,images";
        Log.d("url", url);

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        customTab = new CustomTab();

        makeRequest();

//        mPosterImageView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                revealEffectImage();
//            }
//        }, 1000);
//
//        sfm.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                revealEffectFab();
//            }
//        }, 3000);


        showSnackBar();

        //mTitleTextView.setText(singleMovieData.getOriginalTitle());

        //String title = singleMovieData.getTitle();
        //Log.d("title",title);
        ArrayList<Genre> genreArrayList = new ArrayList<>();

        // mGenreTextView.setText(genreArrayList.get(0).getName());


        //mOverviewTextView.setText(singleMovieData.getOverview());

    }

    private void showSnackBar() {
        snackbar = Snackbar.make(findViewById(R.id.detail_coordinate_layout),
                "Loading data", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

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

    private void checkStatus() {
        if (status != null) {
            if (status.equals("watched")) {
                watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));
                watched = true;
                watchListedFab.setEnabled(false);
                watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

            } else if (status.equals("watchlisted")) {
                watchListedFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_visibility_off_black_24dp));
                watchlisted = true;
                watchFab.setEnabled(false);
                watchFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

            }
        }
    }


    public void makeRequest() {

        progressBar.setVisibility(View.VISIBLE);
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        // collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                        progressBar.setVisibility(View.INVISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        sfm.setVisibility(View.VISIBLE);
                        snackbar.dismiss();
                        singleMovieData = gson.fromJson(response.toString(), SingleMovieData.class);
                        title = singleMovieData.getTitle();
                        rating = singleMovieData.getVoteAverage();
                        mTitleTextView.setText(title);
                        overview = singleMovieData.getOverview();
                        mOverviewTextView.setText(singleMovieData.getOverview());
                        mOverviewTextView.setBackgroundColor(Color.parseColor("#FAFAFA"));
                        Glide.with(getApplicationContext())
                                .load(posterUrl)
                                .thumbnail(0.5f)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                ).into(mPosterImageView);
                        Glide.with(getApplicationContext()).asBitmap().load(posterUrl).listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                                //Bitmap bitmap = ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap();

                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        if (textSwatch != null) {
                                            collapsingToolbarLayout.setContentScrimColor(textSwatch.getRgb());

                                            int colorFrom = getResources().getColor(R.color.colorPrimaryDark);
                                            final int colorTo = textSwatch.getRgb();
                                            Log.d("Col",String.valueOf(textSwatch.getPopulation()));
                                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                                            colorAnimation.setDuration(2250); // milliseconds
                                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                                @Override
                                                public void onAnimationUpdate(ValueAnimator animator) {
                                                    Window window = getWindow();
                                                    window.setStatusBarColor(colorTo);
                                                }

                                            });
                                            colorAnimation.start();

                                        } else {
                                            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                                            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
                                        }

                                    }
                                });
                                return false;
                            }
                        }).into(mPosterImageView);
                        toolbar.setTitle(singleMovieData.getTagline());

                        //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        genreArrayList = new ArrayList<>();
                        genreArrayList = singleMovieData.getGenres();
//                        for (Genre name : genreArrayList){
//                            mGenreTextView.append(name.getName()+ ",");
//                        }
                        for (int i = 0; i < genreArrayList.size(); i++) {
                            mGenreTextView.append(genreArrayList.get(i).getName());
                            if (i != genreArrayList.size() - 1)
                                mGenreTextView.append(", ");
                        }
                        productionCompanyArrayList = new ArrayList<>();
                        productionCompanyArrayList = singleMovieData.getProductionCompanies();
                        for (int i = 0; i < productionCompanyArrayList.size(); i++) {
                            mProductionCompaniesTextView.append(productionCompanyArrayList.get(i).getName());
                            if (i != productionCompanyArrayList.size() - 1)
                                mProductionCompaniesTextView.append(", ");
                        }

                        productionCountryArrayList = new ArrayList<>();
                        productionCountryArrayList = singleMovieData.getProductionCountries();
                        for (int i = 0; i < productionCountryArrayList.size(); i++) {
                            mProductionCountryTextView.append(productionCountryArrayList.get(i).getName());
                            if (i != productionCountryArrayList.size() - 1)
                                mProductionCountryTextView.append(", ");
                        }

                        budget = String.valueOf(singleMovieData.getBudget());
                        mBudgetTextView.setText(String.valueOf(singleMovieData.getBudget()));

                        releaseDate = singleMovieData.getRelease_date();
                        mReleaseDateTextView.setText(String.valueOf(singleMovieData.getRelease_date()));


                        spokenLanguageArrayList = new ArrayList<>();
                        spokenLanguageArrayList = singleMovieData.getSpokenLanguages();

                        if (spokenLanguageArrayList.size() > 0)
                            mSpokenLanguageTextView.setText(String.valueOf(spokenLanguageArrayList.get(0).getName()));
                        else
                            mSpokenLanguageTextView.setText(R.string.english);

                        revenue = String.valueOf(singleMovieData.getRevenue());
                        mRevenueTextView.setText(String.valueOf(singleMovieData.getRevenue()));
                        //mRevenueTextView.setPaintFlags(mRevenueTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                        castArrayList = new ArrayList<>();
                        castArrayList = singleMovieData.getCredits().getCast();
                        for (int i = 0; i < castArrayList.size(); i++) {
                            //mCastTextView.append(castArrayList.get(i).getName() + "\n");
                            castNames += castArrayList.get(i).getName() + " as " + castArrayList.get(i).getCharacter() + "\n";
                            // characterNames += castArrayList.get(i).getCharacter() + "\n";
                        }
                        expTv1.setText((castNames));
                        //expTv2.setText(characterNames);
//                        characterArrayList = new ArrayList<>();
//                        characterArrayList = singleMovieData.getCredits().getCast();
//                        for (int i = 0; i<characterArrayList.size();i++){
//                            characterNames += characterArrayList.get(i).getCharacter() + "\n";
//                        }


                        crewArrayList = new ArrayList<>();
                        crewArrayList = singleMovieData.getCredits().getCrew();
                        for (int i = 0; i < crewArrayList.size(); i++) {
                            if (crewArrayList.get(i).getDepartment().equals("Directing")) {
                                directorName = crewArrayList.get(i).getName();
                                mDirectorTextView.setText(directorName);
                                break;
                            }
                        }
                        runtime = singleMovieData.getRuntime();
                        mRunTimeTextView.setText(String.valueOf(singleMovieData.getRuntime() + " minutes"));

                        poster = singleMovieData.getPosterPath();

                        id = singleMovieData.getId();

                        imdbId = singleMovieData.getExternal_ids().getImdbId();

                        homePage = singleMovieData.getHomepage();

                        imdbImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (imdbId == null) {
                                    Toast.makeText(getApplicationContext(), "No Imdb link available", Toast.LENGTH_SHORT).show();
                                } else {
                                    customTab.open_link(getApplicationContext(), imdbLink + imdbId);
                                }
                            }
                        });

                        facebookImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (singleMovieData.getExternal_ids().getFacebookId() == null) {
                                    Toast.makeText(getApplicationContext(), "No Facebook link available", Toast.LENGTH_SHORT).show();
                                } else {
                                    customTab.open_link(getApplicationContext(), facebookLink + singleMovieData.getExternal_ids().getFacebookId());

                                }
                            }
                        });

                        resultsArrayList = singleMovieData.getVideos().getResults();
                        if (resultsArrayList != null) {
                            if (resultsArrayList.size() != 0) {
                                videoKey = resultsArrayList.get(0).getKey();
                            }

                        }


                        youtubeImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customTab.open_link(getApplicationContext(), youtubeLink + videoKey);
                            }
                        });

                        webImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (homePage != null)
                                    customTab.open_link(getApplicationContext(), homePage);
                                else {
                                    Snackbar.make(findViewById(R.id.detail_series_coordinate_layout), R.string.no_official_website,
                                            Snackbar.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        });

                        reviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
                                intent.putExtra("title", title);
                                Log.d("title", title);
                                startActivity(intent);
                            }
                        });
//                        mCastTextView.append("...");
//
//                        mCastTextView.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(final View v)
//                            {
//                                if (mCastTextView.isExpanded())
//                                {
//                                    mCastTextView.collapse();
//                                    //buttonToggle.setText("Show full cast");
//                                }
//                                else
//                                {
//                                    mCastTextView.expand();
//                                    for (int i=5; i<castArrayList.size();i++)
//                                    {
//                                        mCastTextView.append(castArrayList.get(i).getName()+"\n");
//                                    }
//                                  // buttonToggle.setText("hide full cast");
//                                }
//                            }
//
//                        });
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    public void initializeViews() {

        mTitleTextView = (TextView) findViewById(R.id.detail_movie_title);
        mGenreTextView = (TextView) findViewById(R.id.detail_movie_genre);
        mOverviewTextView = (TextView) findViewById(R.id.detail_movie_overview);
        mProductionCompaniesTextView = (TextView) findViewById(R.id.detail_movie_production_company);
        mProductionCountryTextView = (TextView) findViewById(R.id.detail_movie_production_country);
        mPosterImageView = (ImageView) findViewById(R.id.detail_collapsing_poster);
        mBudgetTextView = (TextView) findViewById(R.id.detail_budget);
        mRevenueTextView = (TextView) findViewById(R.id.detail_revenue);
        mReleaseDateTextView = (TextView) findViewById(R.id.detail_release_date);
        mSpokenLanguageTextView = (TextView) findViewById(R.id.detail_spoken_language);
        mRunTimeTextView = (TextView) findViewById(R.id.detail_runtime);
        //mCastTextView = (TextView) findViewById(R.id.detail_cast);
        // mCastTextView = (ExpandableTextView) findViewById(R.id.detail_cast);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //watchFab = (FloatingActionButton) findViewById(R.id.fab_button);
        progressBar = (ProgressBar) findViewById(R.id.detail_progressbar);
        //fabMenu = findViewById(R.id.fabMenu);
        // buttonToggle = (Button)findViewById(R.id.button_toggle);
        expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        //expTv2 = (ExpandableTextView) findViewById(R.id.expandable_text_character);
        mDirectorTextView = (TextView) findViewById(R.id.detail_director);

        watchFab = (FloatingActionButton) findViewById(R.id.fab_1);
        watchListedFab = findViewById(R.id.fab_2);

        imdbImageButton = (ImageButton) findViewById(R.id.imdb_image_button);
        facebookImageButton = (ImageButton) findViewById(R.id.facebook_image_button);
        webImageButton = (ImageButton) findViewById(R.id.web_image_button);
        reviewButton = (Button) findViewById(R.id.review_button);
        youtubeImageButton = findViewById(R.id.youtube_image_button);

        cardView = findViewById(R.id.detail_content_card);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
    }

    private void isWatched() {
        db.collection("Watched Movies")
                .whereEqualTo("title", title)
                .whereEqualTo("userId", firebaseUserId)
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

    private void addToWatched() {
        status = "watched";
        Date date = new Date();
        db.collection("Watched Movies")
                .add(new Movie(title, rating, id, date, poster, userId, status))
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
        Toast.makeText(getApplicationContext(), "Added to watched collection", Toast.LENGTH_SHORT).show();
        watchFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_black_24dp));
        watchListedFab.setEnabled(false);
        watchListedFab.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        watched = true;
        watchedMoviesNumber++;
        SharedPref.write(firebaseUserId + "watched_movies", watchedMoviesNumber);
        isWatched();
    }

    private void removeFromWatched() {
        db.collection("Watched Movies")
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
        watchedMoviesNumber--;
        SharedPref.write(firebaseUserId + "watched_movies", watchedMoviesNumber);
        isWatched();
    }


    private void isWatchListed() {
        db.collection("WatchListed Movies")
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


    private void addToWatchListed() {
        status = "watchlisted";
        Date date = new Date();
        db.collection("WatchListed Movies")
                .add(new Movie(title, rating, id, date, poster, userId, status))
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
        watchListedMoviesNumber++;
        SharedPref.write(firebaseUserId + "watch_listed_movies", watchListedMoviesNumber);
        isWatchListed();
    }

    private void removeFromWatchListed() {
        Log.d("ZZZ", "1");
        db.collection("WatchListed Movies")
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
        Log.d("ZZZ", "3");
        watchListedMoviesNumber--;
        SharedPref.write(firebaseUserId + "watch_listed_movies", watchListedMoviesNumber);
        isWatchListed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    void revealEffect() {
        if (Build.VERSION.SDK_INT > 20) {
            int cx = sfm.getMeasuredWidth() / 2;
            int cy = sfm.getMeasuredHeight() / 2;
            int finalRadius = Math.max(sfm.getWidth(), sfm.getHeight());
            Animator a = ViewAnimationUtils.createCircularReveal(sfm, cx,
                    cy, 0, finalRadius);
            a.setDuration(1000);
            sfm.setVisibility(View.VISIBLE);
            a.start();
        }
    }


    void revealEffectImage() {
        if (Build.VERSION.SDK_INT > 20) {
            int cx = (mPosterImageView.getLeft() + mPosterImageView.getRight()) / 2;
            int cy = (mPosterImageView.getTop() + mPosterImageView.getBottom()) / 2;
            int finalRadius = (int) Math.hypot(mPosterImageView.getWidth(),
                    mPosterImageView.getHeight());
            Animator imageReveal = ViewAnimationUtils.createCircularReveal(mPosterImageView, cx,
                    cy, 0, finalRadius);
            imageReveal.setDuration(1000);
            imageReveal.setInterpolator(new DecelerateInterpolator());
            mPosterImageView.setVisibility(View.VISIBLE);
            imageReveal.start();
        }
    }


    void revealEffectFab() {
        if (Build.VERSION.SDK_INT > 20) {
            int cx = sfm.getMeasuredWidth() / 2;
            int cy = sfm.getMeasuredHeight() / 2;
            int finalRadius = Math.max(sfm.getWidth(), sfm.getHeight());
            Animator fabReveal = ViewAnimationUtils.createCircularReveal(sfm, cx,
                    cy, 0, finalRadius);
            fabReveal.setDuration(1000);
            fabReveal.setInterpolator(new DecelerateInterpolator());
            sfm.setVisibility(View.VISIBLE);
            fabReveal.start();
        }
    }


}
