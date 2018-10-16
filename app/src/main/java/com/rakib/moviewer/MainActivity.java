package com.rakib.moviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView; // not the default !


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakib.moviewer.util.BottomNavigationViewHelper;
import com.rakib.moviewer.util.SharedPref;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    RequestQueue requestQueue;
    String url;
    Fragment fragment;
    TextView resultText;
    FrameLayout frameLayout;
    SearchView searchView = null;
    MenuItem myActionMenuItem;
    BottomSheetBehavior bottomSheetBehavior;
    ProgressBar progressBar;
    HomeFragment homeFragment;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId;
    SharedPreferences SP;
    String choice;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);


        FirebaseAuth auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            // already signed in
//            startActivity(new Intent(MainActivity.this,SignInActivity.class));
//            finish();
//        } else {
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setIsSmartLockEnabled(false)
//                            .setAvailableProviders(Arrays.asList(
//                                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
//                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
//                            .build(),
//                    RC_SIGN_IN);
//        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("movfsfsies");
        databaseReference.setValue("hello");
//        View bottomSheet = findViewById(R.id.bottom_sheet);
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        frameLayout = (FrameLayout) findViewById(R.id.frame_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //startActivity(new Intent(this,HomeActivity.class));

        //homeFragment = new HomeFragment();
        //loadFragment(homeFragment);

        loadPref();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //vollye();
                        loadPref();
                        return true;
                    case R.id.nav_movies:
                        loadFragment(new MoviesFragment());
                        return true;
                    case R.id.nav_series:
                        loadFragment(new SeriesFragment());
                        return true;
                    case R.id.nav_progress:
                        loadFragment(new ProgressFragment());
                        return true;

                }
                return false;
            }

        });


//        requestQueue = Volley.newRequestQueue(this);
//
//        url = "https://api.themoviedb.org/3/search/movie?api_key=6c6ba266be0e6c4acdf15d39afccb023&language=en-US&page=1&include_adult=false&query=batman";
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                        Log.d("Response", response);
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", error.toString());
//                    }
//                }
//        );
//        requestQueue.add(getRequest);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                if (!query.isEmpty()) {
                    myActionMenuItem.expandActionView();
                    SearchFragment searchFragment = new SearchFragment();

                    searchFragment.setKeyword(query);
                    loadFragment(searchFragment);
                }
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
//                onSearchRequested();
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
                return true;
            case R.id.action_option_settings:
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 0);
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;

        }
        return false;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Movie Tracker\n\nDeveloped by Rakibul Huda\n\nCo-Founder Team Shunno")
                .setTitle("About").setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if(getFragmentManager().getBackStackEntryCount() > 0){
//            getFragmentManager().popBackStackImmediate();
//        }
//        else{
//            super.onBackPressed();
//        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    //showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    //showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            //showSnackbar(R.string.unknown_sign_in_response);
        } else if (requestCode == 0) {
            loadPref();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPref();
        //Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        SharedPref.init(getApplicationContext());
        String fragmentName = SharedPref.read("fragment", "F1");
        switch (fragmentName) {
            case "F2":
                loadFragment(new MoviesFragment());
                break;
            case "F3":
                loadFragment(new SeriesFragment());
                break;
            case "F5":
                loadFragment(new ProgressFragment());
                break;
            case "F6":
                loadFragment(new SearchFragment());
                break;

        }

    }

    private void loadPref() {
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        choice = SP.getString("choice", "1");
        if (choice.equals("1"))
            loadFragment(new HomeFragment());
        else loadFragment(new HomeSeriesFragment());
        Log.d("GGG ", choice);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
        SharedPref.write("fragment","");
    }
}
