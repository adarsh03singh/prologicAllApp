package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getValuesFromSharedPreferences();
        inflateSplashFragment();
        getLocationData();
        getStatusCode();
        getDealStatus();
        getProjectData();
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().findFragmentById(R.id.splash_fragment_container).getClass().getName().toString().equals("com.csestateconnect.www.csconnect.SplashFragment")){
            super.onBackPressed();
        }
    }

    private void inflateSplashFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.splash_fragment_container, new SplashFragment(), "SPLASH_FRAGMENT");
        ft.commit();
    }

    public void getLocationData(){
        String location_url = getString(R.string.api_domain) + getString(R.string.locations_api_path);
        JsonArrayRequest locationsRequest = new JsonArrayRequest(
                Request.Method.GET, location_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setLocationsDataInSharedPrefrences(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("locations", error.toString());
                    }
                }
        );
        SingletonQueue.getInstance(this).addToRequestQueue(locationsRequest);
    }

    public void setLocationsDataInSharedPrefrences(String locationsDataString){
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.locations_data_key), locationsDataString);
        editor.commit();
    }
    public void getStatusCode() {
        String URL_FOR_STATUS = getString(R.string.api_domain) + getString(R.string.get_status_api_path);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,URL_FOR_STATUS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
                        SharedPreferences.Editor editors = pref.edit();
                        editors.putString("statusCode", response.toString());
                        editors.commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",  token);
                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    public void getDealStatus() {
        String URL_FOR_STATUS = getString(R.string.api_domain) + getString(R.string.get_deal_status_api_path);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,URL_FOR_STATUS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
                        SharedPreferences.Editor editors = pref.edit();
                        editors.putString("dealStatusCode", response.toString());
                        editors.commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",  token);
                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(req);
    }


    public void getProjectData(){
        String project_url = getString(R.string.api_domain) + getString(R.string.projects_search_path);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, project_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject projects = new JSONObject(response);
                            JSONArray projectName = projects.getJSONArray("results");
                            Log.e("Response", ">>>>" + projectName.toString());

                            SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
                            SharedPreferences.Editor editors = pref.edit();
                            editors.putString("projectDetails", projectName.toString());
                            editors.commit();

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
                SharedPreferences.Editor editors = pref.edit();
                editors.putString("projectData", response.toString());
                editors.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Country",">>>"+error);
            }
        }
        );
        SingletonQueue.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }
}
