package com.rakib.moviewer;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.rakib.moviewer.util.SharedPref;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    String firebaseUserId;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUserId = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=  inflater.inflate(R.layout.fragment_progress, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        SharedPref.init(getActivity().getApplicationContext());

        final TextView watchedMoviesNumberTextView = view.findViewById(R.id.watched_movies_number);
        final TextView watchListedMoviesNumberTextView = view.findViewById(R.id.watch_listed_movies_number);
        final TextView watchedSeriesNumberTextView = view.findViewById(R.id.watched_series_number);
        final TextView watchListedSeriesNumberTextView = view.findViewById(R.id.watch_listed_series_number);

        final Integer watchedMoviesNumber = SharedPref.read(firebaseUserId + "watched_movies",0);
        final Integer watchListedMoviesNumber = SharedPref.read(firebaseUserId + "watch_listed_movies",0);
        final Integer watchedSeriesNumber = SharedPref.read(firebaseUserId + "watched_series",0);
        final Integer watchListedSeriesNumber = SharedPref.read(firebaseUserId + "watch_listed_series",0);

        animateWatchedMoviesNumberTextView(0,watchedMoviesNumber,watchedMoviesNumberTextView);
        animateWatchListedMoviesNumberTextView(0,watchListedMoviesNumber,watchListedMoviesNumberTextView);
        animateWatchedSeriesNumberTextView(0,watchedSeriesNumber,watchedSeriesNumberTextView);
        animateWatchListedSeriesNumberTextView(0,watchListedSeriesNumber,watchListedSeriesNumberTextView);

        return view;


    }


    public void animateWatchedMoviesNumberTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    public void animateWatchListedMoviesNumberTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    public void animateWatchedSeriesNumberTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    public void animateWatchListedSeriesNumberTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPref.init(getActivity().getApplicationContext());
        SharedPref.write("fragment", "F5");
    }
}
