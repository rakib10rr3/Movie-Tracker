package com.rakib.moviewer;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.rakib.moviewer.model.Review;


import java.util.Date;

public class ReviewActivity extends AppCompatActivity {

    EditText reviewEditText;
    Button writeButton;

    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;

    String title;
    String review;
    String userId;
    String userName;

    RecyclerView recyclerView;
    final static String TAG = "AAA";
    ImageView imageView;
    RelativeLayout relativeLayout;
    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        db = FirebaseFirestore.getInstance();
        initializeViews();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        title = getIntent().getStringExtra("title");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        loadReviews();


        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review = reviewEditText.getText().toString();
                if (TextUtils.isEmpty(review)) {
                    Toast.makeText(ReviewActivity.this, R.string.write_review, Toast.LENGTH_SHORT).show();
                } else {
                    addReview(review);
                }
            }
        });

    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_review);
        reviewEditText = (EditText) findViewById(R.id.edit_text_write_your_review);
        writeButton = (Button) findViewById(R.id.button_write);
        imageView = findViewById(R.id.no_review_image);
        relativeLayout = findViewById(R.id.no_reviews);
        progressBar = findViewById(R.id.no_reviews_progress_bar);
        textView = findViewById(R.id.no_review_text);


    }

    private void addReview(String review) {
        Date date = new Date();
        db.collection("Reviews")
                .add(new Review(title, review, userName, userId, date))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        reviewEditText.setText("");


    }

    private void loadReviews() {

        final Query query = db.collection("Reviews").orderBy("added").whereEqualTo("title", title);
        Log.d(TAG, "1");
        FirestoreRecyclerOptions<Review> response = new FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .build();

        //Log.d(TAG, "hi:"  + response.getSnapshots().size());

        Log.d(TAG, "2");
        adapter = new FirestoreRecyclerAdapter<Review, ReviewsViewHolder>(response) {


            @Override
            public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d(TAG, "5");
                View view = getLayoutInflater()
                        .inflate(R.layout.single_item_review, parent, false);
                Log.d(TAG, "6");
                return new ReviewsViewHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            protected void onBindViewHolder(ReviewsViewHolder holder, int position, final Review model) {

                holder.nameTextView.setText(model.getUserName());
                holder.reviewTextView.setText(model.getReview());

                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                final String doc_id = snapshot.getId();

                holder.reviewLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (FirebaseAuth.getInstance().getUid().equals(model.getUserId())) {
                            //Toast.makeText(ReviewActivity.this, doc_id, Toast.LENGTH_SHORT).show();
                            showDeleteDialog(doc_id);
                            return true;
                        }
                        return false;
                    }

                });


            }


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(View.INVISIBLE);

                if (adapter.getItemCount() == 0) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                    relativeLayout.startAnimation(animation);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }

            }
        };
        Log.d(TAG, "7");
        adapter.notifyDataSetChanged();
        Log.d(TAG, "8");
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "9");

    }

    private void showDeleteDialog(final String doc_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.do_you_want_to_delete_it)
                .setTitle(R.string.delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete(doc_id);
                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();

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

    public void delete(String id) {
        db.collection("Reviews")
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

    private class ReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView reviewTextView;
        private LinearLayout reviewLinearLayout;

        private ReviewsViewHolder(View itemView) {
            super(itemView);
            Log.d("AAA", "10");
            nameTextView = (TextView) itemView.findViewById(R.id.review_user_name);
            reviewTextView = (TextView) itemView.findViewById(R.id.review_user_review);
            reviewLinearLayout = itemView.findViewById(R.id.review_linear_layout);

        }
    }

}
