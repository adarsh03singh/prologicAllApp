package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csestateconnect.www.csconnect.models.lead.Lead;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class LeadFeedbackActivity extends AppCompatActivity {

    Button back_button;
    Button submit_button;
    Button support_issue_button;
    EditText feedbackView;
    RatingBar mratingBar;

    String token;
    Lead lead;
    String leadId;
    Integer dealId;
    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_feedback);
        getTokenFromSharedPreferences();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getLeadData();

        mratingBar = (RatingBar) findViewById(R.id.ratingBar);
        submit_button = (Button) findViewById(R.id.feedback_submit_button);
        feedbackView = (EditText) findViewById(R.id.feedback_view);
        back_button = findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
                intent.putExtra("leadIdbyLeadAdapter",leadId);
                intent.putExtra("dealIdbyLeadAdapter",dealId);
                startActivity(intent);
                finish();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLeadFeedback();
                submit_button.setClickable(false);
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

    public void getLeadFeedback() {
        String GET_LEAD_STATUS_URL = getString(R.string.api_domain) + getString(R.string.get_feedback_api_path)+leadId+"/";
        JSONObject json1 = new JSONObject();

        try {
            json1.put("quality", mratingBar.getRating());
            json1.put("feedback_comment", feedbackView.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PATCH, GET_LEAD_STATUS_URL, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        goToLeadDetail();
                        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
                submit_button.setClickable(true);
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse == null) {

                    String errorMessage = errorMessage = null;
                    if (error instanceof NetworkError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        errorMessage = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }


        };
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

    public void getTokenFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    private void goToLeadDetail() {
        Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
        intent.putExtra("leadIdbyLeadAdapter",leadId);
        intent.putExtra("dealIdbyLeadAdapter",dealId);
        startActivity(intent);
        finish();
    }

    public void getLeadData() {
        final Intent myintent = getIntent();
        if (null != myintent.getExtras()) {

            try {
                leadId = myintent.getExtras().getString("leadIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                dealId = myintent.getExtras().getInt("dealIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

        @Override
        public void onBackPressed() {
            Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
            intent.putExtra("leadIdbyLeadAdapter",leadId);
            intent.putExtra("dealIdbyLeadAdapter",dealId);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
}
