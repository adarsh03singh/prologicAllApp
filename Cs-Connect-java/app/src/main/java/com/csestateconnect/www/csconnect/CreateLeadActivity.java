package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateLeadActivity extends AppCompatActivity {

    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    Button support_issue_button;
    String token;
    String errorMessage;
    String leadId;
    ConstraintLayout loader;

    Integer slectedCountryId = 0;
    Integer slectedCityId = 0;
    Integer slectedLocationId = 0;
    Integer slectedStatusId = 0;
    EditText clientNameText;
    EditText clientNumberText;
    EditText clientEmailText;
    EditText clientBudgetText;
    EditText clientCommenttext;
    Spinner countrySpinner;
    Spinner locationSpinner;
    Spinner citySpinner;
    Spinner statusSpinner;
    Button submitButton;

    JSONArray locations;
    JSONArray cities;
    JSONArray countries;
    JSONArray status;

    final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;
    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead);
        getValuesFromSharedPreferences();
        getIdforTextAndButton();

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

            getLocationdata();
        getStatusValue();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();

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

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
        clientNameText = (EditText) findViewById(R.id.create_lead_clientNameText);
        clientNumberText = (EditText) findViewById(R.id.create_lead_clientNumberText);
        clientEmailText = (EditText) findViewById(R.id.create_lead_clientEmailText);
        clientBudgetText = (EditText) findViewById(R.id.create_lead_budgetText);
        clientCommenttext = (EditText) findViewById(R.id.create_lead_clientCommentText);
        countrySpinner = (Spinner) findViewById(R.id.create_lead_spinner_country);
        citySpinner = (Spinner) findViewById(R.id.create_lead_spinner_city);
        statusSpinner = (Spinner) findViewById(R.id.create_lead_spinner_status);
        locationSpinner = (Spinner) findViewById(R.id.create_lead_spinner_location);
        submitButton = (Button) findViewById(R.id.create_lead_submit_button);
    }


    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String phone_number = sharedPref.getString(getString(R.string.phonenumber_data), null);
        String status_array_string = (sharedPref.getString("statusCode", null));
        String defaultValue = getString(R.string.locations_default_empty_value);
        String locationsData = sharedPref.getString(getString(R.string.locations_data_key), defaultValue);

        try {
            countries = new JSONArray(locationsData);
            status = new JSONArray(status_array_string);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLocationdata() {

        final List<String> countrySpinnerNameArray = new ArrayList<>();
        final List<Integer> countrySpinnerIdArray = new ArrayList<>();
        try {
        for(Integer index_of_country = 0; index_of_country < countries.length(); index_of_country++) {


            try {
                countrySpinnerNameArray.add(new JSONObject(countries.get(index_of_country).toString()).getString("name"));
                countrySpinnerIdArray.add(new JSONObject(countries.get(index_of_country).toString()).getInt("id"));
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                countrySpinnerNameArray
        );

        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slectedCountryId = countrySpinnerIdArray.get(position);
                try {
                    JSONObject country = new JSONObject(countries.get(position).toString());

                    cities = country.getJSONArray("cities");

                    final List<String> citySpinnerNameArray = new ArrayList<>();
                    final List<Integer> citySpinnerIdArray = new ArrayList<>();

                    for(Integer index_of_city = 0; index_of_city < cities.length(); index_of_city++) {
                        try {
                            citySpinnerNameArray.add(new JSONObject(cities.get(index_of_city).toString()).getString("name"));
                            citySpinnerIdArray.add(new JSONObject(cities.get(index_of_city).toString()).getInt("id"));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    final ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            citySpinnerNameArray);

                    citySpinner.setAdapter(cityAdapter);

                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            slectedCityId = citySpinnerIdArray.get(position);

                            try {
                                JSONObject city = new JSONObject(cities.get(position).toString());


                                locations = city.getJSONArray("locations");

                                final List<String> locationSpinnerNameArray = new ArrayList<>();
                                final List<Integer> locationSpinnerIdArray = new ArrayList<>();

                                for (Integer index_of_location = 0; index_of_location < locations.length(); index_of_location++) {
                                    try {
                                        locationSpinnerNameArray.add(new JSONObject(locations.get(index_of_location).toString()).getString("name"));
                                        locationSpinnerIdArray.add(new JSONObject(locations.get(index_of_location).toString()).getInt("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                final ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        android.R.layout.simple_spinner_dropdown_item,
                                        locationSpinnerNameArray);

                                locationSpinner.setAdapter(locationsAdapter);

                                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        slectedLocationId = locationSpinnerIdArray.get(position);
                                        Log.d("locationValue", slectedLocationId.toString());
                                        //     Toast.makeText(getApplicationContext(),String.valueOf(slectedLocationId),+12).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getStatusValue(){
        final List<String> statusSpinnerNameArray = new ArrayList<>();
        final List<Integer> statusSpinnerIdArray = new ArrayList<>();

        for (Integer index_of_status = 0; index_of_status < status.length(); index_of_status++) {
            try {
                statusSpinnerNameArray.add(new JSONObject(status.get(index_of_status).toString()).getString("name"));
                statusSpinnerIdArray.add(new JSONObject(status.get(index_of_status).toString()).getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statusSpinnerNameArray
        );

        statusSpinner.setAdapter(statusAdapter);
        for(int i=0; i< statusSpinnerNameArray.size(); i++){
            if( statusSpinnerNameArray.get(i).equals("Fresh")){
                statusSpinner.setSelection(i);
            }
        }

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slectedStatusId = statusSpinnerIdArray.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fieldValidation() {
        if (clientNameText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter client name", Toast.LENGTH_LONG).show();

        } else if (clientNumberText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your client number", Toast.LENGTH_LONG).show();

        } else if (clientNumberText.length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter 10 digits number", Toast.LENGTH_LONG).show();

        } else if (clientEmailText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();

        } else if (clientBudgetText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter client budget", Toast.LENGTH_LONG).show();

//        } else if (clientCommenttext.getText().toString().isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();

        } else if (!clientEmailText.getText().toString().isEmpty()) {
            pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(clientEmailText.getText().toString());
            if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "Please enter  valid email", Toast.LENGTH_LONG).show();

        } else {
            createLeads();
                submitButton.setClickable(false);
        }
    }
    }

    public void createLeads() {
        loader.setVisibility(View.VISIBLE);
        String URL_FOR_CREATE_LEADS = getString(R.string.api_domain) + getString(R.string.create_lead_api_path);
        JSONObject json1 = new JSONObject();

        try {

            json1.put("name", clientNameText.getText().toString());
            json1.put("phone_number", "+91"+clientNumberText.getText().toString());
            json1.put("country", slectedCountryId);
            json1.put("city", slectedCityId);
            json1.put("location", slectedLocationId);
            json1.put("lead_status", slectedStatusId);
            json1.put("email", clientEmailText.getText().toString());
            json1.put("budget", clientBudgetText.getText().toString());
            json1.put("feedback_comment", clientCommenttext.getText().toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_FOR_CREATE_LEADS, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loader.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Successfully Created Lead", Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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

                    } else if (error.networkResponse.statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Error Occerred", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }) {

            /**
             * Passing some request headers
             * */
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


    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
      finish();
    }


}
