package com.rakib.moviewer;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.rakib.moviewer.util.SharedPref;



/**
 * A simple {@link Fragment} subclass.
 */
public class WatchListedMoviesFragment extends Fragment {

    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ImageView imageView;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    String firebaseUserId;

    public WatchListedMoviesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_watch_listed_movies, container, false);
        //final TextView textView = view.findViewById(R.id.watched);

        recyclerView = view.findViewById(R.id.watch_listed_movies_recycler);
        imageView = view.findViewById(R.id.no_movie_image);
        relativeLayout = view.findViewById(R.id.no_movie);
        progressBar = view.findViewById(R.id.watch_listed_movies_progress_bar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        db = FirebaseFirestore.getInstance();
        SharedPref.init(getActivity().getApplicationContext());
        loadWatchListedMovies();
        return view;
    }

    private void loadWatchListedMovies() {
        Log.d("ttaagg","came here");
        final Query query = db.collection("WatchListed Movies").orderBy("title").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirestoreRecyclerOptions<Movie> response = new FirestoreRecyclerOptions.Builder<Movie>()
                .setQuery(query, Movie.class)
                .build();

        Log.d("responseDD",response.toString());
        adapter = new FirestoreRecyclerAdapter<Movie, MyMoviesViewHolder>(response) {
            @Override
            protected void onBindViewHolder(final MyMoviesViewHolder holder, int position, final Movie model) {
                //final Note note = notesList.get(position);
                Log.d("qwerty","came here");
                holder.myMoviesTitleTextView.setText(model.getTitle());
                holder.myMoviesRatingTextView.setText(String.valueOf(model.getRating()));
                Glide.with(getActivity().getApplicationContext())
                        .load("https://image.tmdb.org/t/p/w154"+model.getPosterPath())
                        .thumbnail(0.5f)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        ).into(holder.myMoviesPosterImageView);
                Log.d("model",model.getTitle());
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                final String doc_id = snapshot.getId();
                holder.myMoviesPosterImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showDeleteDialog(doc_id);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(),DetailActivity.class);
                                intent.putExtra("status",model.getStatus());
                                intent.putExtra("movie_id",model.getId());
                                intent.putExtra("poster",model.getPosterPath());
                                intent.putExtra("doc_id",doc_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            }
                        },100);
                    }
                });
                holder.myMoviesPosterImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText(getActivity(), "Long Clicked", Toast.LENGTH_SHORT).show();
                        showDeleteDialog(doc_id);
                        return true;
                    }
                });


            }

            @Override
            public MyMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_movies_single_item, parent, false);

                return new MyMoviesViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                SharedPref.write(firebaseUserId + "watch_listed_movies",adapter.getItemCount());
                progressBar.setVisibility(View.INVISIBLE);

                if (adapter.getItemCount() == 0){
                    //Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                    //emptyTextView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

                    animation.setInterpolator(interpolator);


                    relativeLayout.startAnimation(animation);
                }

            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }

    private void showDeleteDialog(final String doc_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.do_you_want_to_delete_it)
                .setTitle(R.string.delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete(doc_id);
                Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();

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

    public void delete(String id){
        db.collection("WatchListed Movies")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private static class MyMoviesViewHolder extends RecyclerView.ViewHolder {

        private TextView myMoviesTitleTextView;
        private ImageView myMoviesPosterImageView;
        private TextView myMoviesRatingTextView;

        public MyMoviesViewHolder(View view){
            super(view);
            myMoviesTitleTextView = (TextView) view.findViewById(R.id.my_movies_title);
            myMoviesPosterImageView = (ImageView) view.findViewById(R.id.my_movies_poster);
            myMoviesRatingTextView = view.findViewById(R.id.my_movies_rating);
        }
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
