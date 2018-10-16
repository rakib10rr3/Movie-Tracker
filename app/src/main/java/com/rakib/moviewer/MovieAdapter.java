package com.rakib.moviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Rakib on 1/6/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    List<Result> resultList;
    Context context;
    String url = "https://image.tmdb.org/t/p/w500";
    int value;
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dbr;
    FirebaseFirestore db;
    Intent intent;
    Activity activity;


    private Listener listener;

    interface Listener {
        void onClick(int position);
    }


    public MovieAdapter(Context context, List<Result> list, int mValue) {
        this.resultList = list;
        this.context = context;
        value = mValue;
        activity = (Activity) context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mLayerCardView;
        CardView mCardVIew;
        TextView mNameTextView;
        TextView mRatingTextView;
        ImageView mPosterView;
        LinearLayout mLinear;
        ImageButton imageButton;


        public ViewHolder(View view) {
            super(view);
            mPosterView = (ImageView) view.findViewById(R.id.info_image);
            mNameTextView = (TextView) view.findViewById(R.id.movie_name);
            mRatingTextView = view.findViewById(R.id.movie_rating);
            mLinear = (LinearLayout) view.findViewById(R.id.lin_view);
            //imageButton = (ImageButton) view.findViewById(R.id.option);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (value == 2)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_movie_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(url + resultList.get(position).getPosterPath())
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(holder.mPosterView);

        holder.mNameTextView.setText(resultList.get(position).getTitle());
        holder.mRatingTextView.setText(String.valueOf(resultList.get(position).getVoteAverage()));
        //holder.mRatingTextView.setText(String.valueOf(resultList.get(position).getId()));
        if (value == 2) {
            holder.mPosterView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onClick(position);
                        view.findViewById(R.id.bottom_sheet);
                    }
                    return true;
                }
            });

            holder.mPosterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(context, DetailActivity.class);
                            intent.putExtra("poster", resultList.get(position).getPosterPath());
                            intent.putExtra("title", resultList.get(position).getTitle());
                            intent.putExtra("movie_id", resultList.get(position).getId());
                            context.startActivity(intent);
                            activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        }
                    }, 100);
                }
            });

        } else {
            holder.mCardVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, resultList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

}
