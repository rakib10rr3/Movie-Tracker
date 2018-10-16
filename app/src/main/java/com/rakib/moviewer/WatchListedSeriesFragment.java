package com.rakib.moviewer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.view.animation.LayoutAnimationController;
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

public class WatchListedSeriesFragment extends Fragment {

    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ImageView imageView;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;

    String firebaseUserId;

    public WatchListedSeriesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_watch_listed_series, container, false);
        recyclerView = view.findViewById(R.id.watch_listed_series_recycler);

        imageView = view.findViewById(R.id.no_series_image);
        relativeLayout = view.findViewById(R.id.no_series);
        progressBar = view.findViewById(R.id.watch_listed_series_progress_bar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

//        int resId = R.anim.grid_layout_animation_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), resId);
//        recyclerView.setLayoutAnimation(animation);


        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        db = FirebaseFirestore.getInstance();
        loadWatchListedSeries();
        return view;
    }

    private void loadWatchListedSeries() {

        Log.d("ttaagg", "came here");
        final Query query = db.collection("WatchListed Series").orderBy("title").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirestoreRecyclerOptions<Series> response = new FirestoreRecyclerOptions.Builder<Series>()
                .setQuery(query, Series.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Series, MySeriesViewHolder>(response) {
            @Override
            protected void onBindViewHolder(final MySeriesViewHolder holder, int position, final Series model) {
                Log.d("qwerty", "came here");
                holder.mySeriesTitleTextView.setText(model.getTitle());
                holder.mySeriesRatingTextView.setText(String.valueOf(model.getRating()));
                Glide.with(getActivity().getApplicationContext())
                        .load("https://image.tmdb.org/t/p/w154" + model.getPosterPath())
                        .thumbnail(0.5f)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                        ).into(holder.mySeriesPosterImageView);
                Log.d("model", model.getTitle());
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                final String doc_id = snapshot.getId();
                holder.mySeriesPosterImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showDeleteDialog(doc_id);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(),SeriesDetailActivity.class);
                                intent.putExtra("status",model.getStatus());
                                intent.putExtra("series_id",model.getId());
                                intent.putExtra("poster",model.getPosterPath());
                                intent.putExtra("doc_id",doc_id);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            }
                        },100);
                    }
                });
                holder.mySeriesPosterImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText(getActivity(), "Long Clicked", Toast.LENGTH_SHORT).show();

                        showDeleteDialog(doc_id);
                        return true;
                    }
                });

            }

            @Override
            public MySeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_series_single_item, parent, false);

                return new MySeriesViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Integer number = adapter.getItemCount();
                SharedPref.init(getActivity().getApplicationContext());
                SharedPref.write(firebaseUserId + "watch_listed_series",number);
                Log.d("LLL",String.valueOf(number));


                progressBar.setVisibility(View.INVISIBLE);

                if (adapter.getItemCount() == 0){
                    //Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                    //emptyTextView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bounce);
                    relativeLayout.startAnimation(animation);
                }

            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    public void delete(String id) {
        db.collection("WatchListed Series")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
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

    private static class MySeriesViewHolder extends RecyclerView.ViewHolder {

        private TextView mySeriesTitleTextView;
        private ImageView mySeriesPosterImageView;
        private TextView mySeriesRatingTextView;



        public MySeriesViewHolder(View view) {
            super(view);
            mySeriesTitleTextView = (TextView) view.findViewById(R.id.my_series_title);
            mySeriesPosterImageView = (ImageView) view.findViewById(R.id.my_series_poster);
            mySeriesRatingTextView = view.findViewById(R.id.my_series_rating);

        }
    }


}
