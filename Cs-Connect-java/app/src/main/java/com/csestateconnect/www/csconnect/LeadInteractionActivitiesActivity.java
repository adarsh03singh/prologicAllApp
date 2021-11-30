package com.csestateconnect.www.csconnect;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.csestateconnect.www.csconnect.adapters.LeadActivitiesListAdapter;
import com.csestateconnect.www.csconnect.adapters.LeadListAdapter;
import com.csestateconnect.www.csconnect.adapters.ProjectWowFactorAdapter;
import com.csestateconnect.www.csconnect.models.lead.GetActivity;
import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.csestateconnect.www.csconnect.models.lead_activitied.ActivitiesList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeadInteractionActivitiesActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    Toolbar toolbar;
    String leadActivityDate;
    String leadActivity;
    String token;
    String id;
    Lead lead;
    private ConstraintLayout loader;

    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    Button support_issue_button;
    TextView noActivityTxt;

    private List<GetActivity> activitiesList;
    private RecyclerView mRecyclerView;
    private LeadActivitiesListAdapter mAdapter;
NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_interaction_activities);
        loader = findViewById(R.id.progress_bar_layout);
        noActivityTxt = findViewById(R.id.noActivities_text);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.interaction_activities_menu);
        toolbar.setOnMenuItemClickListener(this);
        mRecyclerView = findViewById(R.id.leadActivitylistView);
//        mAdapter = new LeadActivitiesListAdapter(activitiesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(LeadInteractionActivitiesActivity.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mAdapter);

        getStatusValuesFromSharedPreferences();

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getLeadData();
        getLeadDetail();

        back_button = findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLeadsDetail();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.interact_activity_create) {
            goToCreateLeadActivities();

        }
        return true;
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

    private void getLeadDetail() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.leads_list_path) + "/" + id;
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_PROJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject allData = new JSONObject(response);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Lead>() {
                    }.getType();
                    lead = gson.fromJson(allData.toString(), type);

                    activitiesList = lead.getActivities;
                    mAdapter = new LeadActivitiesListAdapter(activitiesList);
                    mRecyclerView.setAdapter(mAdapter);

                    if (mAdapter.getItemCount() == 0) {
                        noActivityTxt.setVisibility(View.VISIBLE);
                    } else
                        noActivityTxt.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ContentValues.TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);

                return params;
            }
        };
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);

    }

    private void goToLeadsDetail() {
        Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
        finish();
        super.onBackPressed();
    }

    private void goToCreateLeadActivities() {
        Intent intent = new Intent(getApplicationContext(), CreateLeadInteractionActivitiesActivity.class);
        intent.putExtra("leadIdbyLeadAdapter", id);
        startActivity(intent);
        finish();
    }


    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void getLeadData() {
        final Intent myintent = getIntent();
        if (null != myintent.getExtras()) {

            try {
                id = myintent.getExtras().getString("leadIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void getStatusValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
        finish();
        super.onBackPressed();
    }


}

