package com.rakib.moviewer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Rakib on 1/22/2018.
 */

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.ViewHolder> {
    List<Result> resultList;
    Context context;
    String url = "https://image.tmdb.org/t/p/w500";
    int value;
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dbr;


    public MovieSearchAdapter(Context context, List<Result> list) {
        this.resultList = list;
        this.context = context;
        //value = mValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_movie_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(url + resultList.get(position).getPosterPath())
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(holder.mPosterImageView);
        holder.mNameTextView.setText(resultList.get(position).getTitle());
        holder.mDateTextView.setText(resultList.get(position).getReleaseDate());
        holder.mRatingTextView.setText(String.valueOf(resultList.get(position).getVoteAverage()));
//        holder.mOptionImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PopupMenu popup = new PopupMenu(context, view);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.popup, popup.getMenu());
//                popup.show();
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        // your stuff here (one frequently uses a switch-case,
//                        // the cases depending on item.getId() )
//                        switch (item.getItemId()) {
//                            case R.id.watchlist:
//                                Toast.makeText(context, String.valueOf(resultList.get(position).getTitle()) + " will be added to watchlist", Toast.LENGTH_SHORT).show();
//
//                            case R.id.watched:
//                                Toast.makeText(context, String.valueOf(resultList.get(position).getTitle()) + " will be added to watched", Toast.LENGTH_SHORT).show();
//                                return true;
//                        }
//                        return false;
//                    }
//                });
//
//            }
//
//
//        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("movie_id",resultList.get(position).getId());
                intent.putExtra("poster",resultList.get(position).getPosterPath());
                intent.putExtra("title",resultList.get(position).getTitle());
                context.startActivity(intent);
                //Toast.makeText(context, "yoooooo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNameTextView;
        TextView mDateTextView;
        ImageView mPosterImageView;
        ImageButton mOptionImageButton;
        TextView mRatingTextView;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.search_movie_name);
            mDateTextView = (TextView) view.findViewById(R.id.search_release_date);
            mPosterImageView = (ImageView) view.findViewById(R.id.search_info_image);
            mRatingTextView = view.findViewById(R.id.search_movie_rating);
            cardView =  view.findViewById(R.id.search_card);
        }
    }
}
