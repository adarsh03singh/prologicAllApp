package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.csestateconnect.www.csconnect.adapters.SearchResultListAdapter;
import com.csestateconnect.www.csconnect.models.search.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
NetworkInformation networkInformation;
    private RecyclerView mRecyclerView;
    private SearchResultListAdapter mAdapter;

    private LinkedList<SearchResult> searchResultList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_field);
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else




        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Projects, Cities..");


        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.search_result_recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new SearchResultListAdapter(this, searchResultList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchResultList.clear();
                mAdapter.notifyDataSetChanged();
                getSearchResults(s);
                return false;
            }
        });



    }

    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press Retry to Connect.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (networkInformation.isConnectingToInternet() == false) {
//no connection
                    networkErrorPopup();
                } else
                    recreate();
            }
        });
        builder.show();
    }

    private void getSearchResults(String query) {

        String url_for_suggestion = getString(R.string.api_domain) + "/projects/suggest/?name_suggest__completion=" + query +"&city_name_suggest__completion="+ query;

        SingletonQueue.getInstance(getBaseContext()).getRequestQueue().cancelAll(SearchActivity.class);

        StringRequest strReq = new StringRequest(Request.Method.GET, url_for_suggestion, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jObj = new JSONObject(response);

                    ArrayList<String> cities = new ArrayList<String>();
                    JSONArray citySuggestionsArray = jObj.getJSONArray("city_name_suggest__completion");
                    JSONObject firstSuggestions = citySuggestionsArray.getJSONObject(0);
                    JSONArray firstSuggestionsArray = firstSuggestions.getJSONArray("options");

                    for (Integer i = 0; i < firstSuggestionsArray.length(); i++) {
                        JSONObject suggestion = firstSuggestionsArray.getJSONObject(i);
                        String city = suggestion.getString("text");
                        if (!cities.contains(city)) {
                            cities.add(city);
                            searchResultList.add(new SearchResult(city, "CITY"));
                        }
                    }


                    JSONArray projectSuggestionsArray = jObj.getJSONArray("name_suggest__completion");
                    JSONObject projectFirstSuggestions = projectSuggestionsArray.getJSONObject(0);
                    JSONArray projectFirstSuggestionsArray = projectFirstSuggestions.getJSONArray("options");

                    for (Integer i = 0; i < projectFirstSuggestionsArray.length(); i++) {
                        JSONObject suggestion = projectFirstSuggestionsArray.getJSONObject(i);
                        JSONObject project = suggestion.getJSONObject("_source");
                        Integer id = project.getInt("id");
                        String name = project.getString("name");
                        String location = project.getJSONObject("location").getString("name");
                        String city = project.getJSONObject("city").getString("name");
                        String locationName = location + ", " + city;
                        searchResultList.add(new SearchResult(id, name, "PROJECT", locationName));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        strReq.setTag(SearchActivity.class);
        SingletonQueue.getInstance(getBaseContext()).addToRequestQueue(strReq);

    }
}
