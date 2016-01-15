package com.hsns.appdemo;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "MainActivity";
    private ArrayList<Product> mProducts = new ArrayList<>();
//    private SearchProductSuggestionAdapter mSearchProductSuggestionAdapter;
    private GoogleStringSuggestionAdapter mGoogleStringSuggestionAdapter;
    private ArrayList<String> mString = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        initProducts();

        initViews();
    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initProducts() {
        mProducts.add(new Product(1, "Coca Cola", "http://wiseheartdesign.com/images/articles/default-avatar.png"));
        mProducts.add(new Product(2, "Anchor", "http://reface.me/wp-content/uploads/default-facebook-avatar-female.gif"));
        mProducts.add(new Product(3, "ABC", "http://reface.me/wp-content/uploads/default-facebook-avatar-male.gif"));
        mProducts.add(new Product(4, "Soda", "http://dreamatico.com/data_images/girl/girl-8.jpg"));
        mProducts.add(new Product(5, "Sting", "http://wiseheartdesign.com/images/articles/default-avatar.png"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), SearchableActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
//        mSearchProductSuggestionAdapter = new SearchProductSuggestionAdapter(this, mProducts);
        mGoogleStringSuggestionAdapter = new GoogleStringSuggestionAdapter(this, mString);
        searchView.setSuggestionsAdapter(mGoogleStringSuggestionAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                requestSuggestions(newText);

                return true;
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery(mString.get(position), false);
                searchView.clearFocus();
                return true;
            }
        });

        return true;
    }

    private void requestSuggestions(String newText) {
        mString.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "http://suggestqueries.google.com/complete/search?client=chrome&q=" + newText;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            jsonArray = jsonArray.getJSONArray(1);
                            TreeSet<String> strings = new TreeSet<>();
                            for(int i = 0; i < jsonArray.length(); i++){
                                strings.add(jsonArray.getString(i));
                            }
                            mString.addAll(strings);
                            mGoogleStringSuggestionAdapter.notifyDataSetChanged();
                            Log.i(TAG, jsonArray.toString());

                            mProgressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.setVisibility(View.GONE);
                        Log.e(TAG, error.getMessage());
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest, "GET");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
