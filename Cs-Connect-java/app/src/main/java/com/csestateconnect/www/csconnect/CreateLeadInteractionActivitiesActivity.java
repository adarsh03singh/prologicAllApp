package com.csestateconnect.www.csconnect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateLeadInteractionActivitiesActivity extends AppCompatActivity {

    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    String token;
    ConstraintLayout loader;
    EditText activityTxt;
    String leadId;

    Lead lead;

    Button submitButton;
    EditText dateView;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String date_time = "";
NetworkInformation networkInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead_interaction_activities);
        getValuesFromSharedPreferences();
        getIdforTextAndButton();

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getLeadData();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeadInteractionActivitiesActivity.class);
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInteractionDates();
                submitButton.setClickable(false);
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

    }

    private void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth ;
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        dateView.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
        submitButton = (Button) findViewById(R.id.createActivities_save_button);
        dateView = (EditText) findViewById(R.id.create_activities_date_txt);
        activityTxt = (EditText) findViewById(R.id.create_activity_description_txt);


    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);

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
    public void createInteractionDates() {
        loader.setVisibility(View.VISIBLE);
        String URL_FOR_CREATE_INTERACTION_ACTIVITIES = getString(R.string.api_domain) + getString(R.string.create_interaction_activities_api_path);
        JSONObject json1 = new JSONObject();

        try {

            json1.put("lead", leadId);
            json1.put("short_description", activityTxt.getText());
            json1.put("date", dateView.getText());

            goToLeadsActivities();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_FOR_CREATE_INTERACTION_ACTIVITIES, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loader.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Successfully Created", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.INVISIBLE);
                submitButton.setClickable(true);
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

    private void goToLeadsActivities() {
        Intent intent = new Intent(getApplicationContext(), LeadInteractionActivitiesActivity.class);
        intent.putExtra("leadIdbyLeadAdapter",leadId);
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
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), LeadInteractionActivitiesActivity.class);
        super.onBackPressed();
        finish();
    }
}
