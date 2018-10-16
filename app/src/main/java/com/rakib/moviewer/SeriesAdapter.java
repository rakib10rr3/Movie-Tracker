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
import com.google.firebase.firestore.FirebaseFirestore;
import com.rakib.moviewer.model.series.Result;

import java.util.List;

/**
 * Created by Rakib on 03/18/2018.
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {
    List<Result> resultList;
    Context context;
    String url = "https://image.tmdb.org/t/p/w500";
    int value;
    View view;
    FirebaseFirestore db;
    Intent intent;
    Activity activity;

    private Listener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_series_single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(url + resultList.get(position).getPosterPath()).thumbnail(0.5f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(holder.mPosterView);

        holder.mNameTextView.setText(resultList.get(position).getName());
        holder.mPosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent = new Intent(context, SeriesDetailActivity.class);
                        intent.putExtra("poster", resultList.get(position).getPosterPath());
                        intent.putExtra("title", resultList.get(position).getName());
                        intent.putExtra("series_id", resultList.get(position).getId());
                        context.startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                }, 100);
            }
        });
        holder.mRatingTextView.setText(String.valueOf(resultList.get(position).getVoteAverage()));

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    interface Listener {
        void onClick(int position);
    }

    public SeriesAdapter(Context context, List<Result> list) {
        this.resultList = list;
        this.context = context;
        activity = (Activity) context;    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mNameTextView;
        TextView mRatingTextView;
        ImageView mPosterView;
        LinearLayout mLinear;


        public ViewHolder(View view) {
            super(view);
            mPosterView = view.findViewById(R.id.series_info_image);
            mNameTextView = view.findViewById(R.id.series_name);
            mLinear = view.findViewById(R.id.lin_view_series);
            mRatingTextView = view.findViewById(R.id.series_rating);
        }

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


}