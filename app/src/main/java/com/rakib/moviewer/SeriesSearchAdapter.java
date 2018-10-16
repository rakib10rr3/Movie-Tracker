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
import com.rakib.moviewer.model.series.single.*;

import java.util.List;

/**
 * Created by Rakib on 03/20/2018.
 */

public class SeriesSearchAdapter extends RecyclerView.Adapter<SeriesSearchAdapter.ViewHolder> {

    List<com.rakib.moviewer.model.series.Result> resultList;
    Context context;
    String url = "https://image.tmdb.org/t/p/w500";
    int value;
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dbr;

    public SeriesSearchAdapter(Context context, List<com.rakib.moviewer.model.series.Result> list){
        this.resultList = list;
        this.context = context;
    }

    @Override
    public SeriesSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_series_search, parent, false);
        return new SeriesSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeriesSearchAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(url + resultList.get(position).getPosterPath())
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(holder.mPosterImageView);
        holder.mNameTextView.setText(resultList.get(position).getName());
        holder.mFirstAirDateTextView.setText(resultList.get(position).getFirstAirDate());
        holder.mRatingTextView.setText(String.valueOf(resultList.get(position).getVoteAverage()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SeriesDetailActivity.class);
                intent.putExtra("series_id",resultList.get(position).getId());
                intent.putExtra("poster",resultList.get(position).getPosterPath());
                intent.putExtra("title",resultList.get(position).getName());
                context.startActivity(intent);
            }
        });
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
//                                Toast.makeText(context, String.valueOf(resultList.get(position).getName()) + " will be added to watchlist", Toast.LENGTH_SHORT).show();
//
//                            case R.id.watched:
//                                Toast.makeText(context, String.valueOf(resultList.get(position).getName()) + " will be added to watched", Toast.LENGTH_SHORT).show();
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
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,SeriesDetailActivity.class);
//                intent.putExtra("series_id",resultList.get(position).getId());
//                intent.putExtra("poster",resultList.get(position).getPosterPath());
//                intent.putExtra("title",resultList.get(position).getName());
//                context.startActivity(intent);
//                Toast.makeText(context, "yoooooo", Toast.LENGTH_SHORT).show();
//            }
//        });

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
        CardView cardView;
        TextView mFirstAirDateTextView;
        TextView mRatingTextView;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.search_series_name);
            mPosterImageView = (ImageView) view.findViewById(R.id.search_series_info_image);
            cardView = view.findViewById(R.id.search_card);
            mFirstAirDateTextView = view.findViewById(R.id.search_series_release_date);
            mRatingTextView = view.findViewById(R.id.search_series_rating);
        }
    }
}
