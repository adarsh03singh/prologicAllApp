package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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

public class ProfileEditActivity extends AppCompatActivity {

    Integer slectedCountryId = 0;
    Integer slectedCityId = 0;
    ConstraintLayout loader;
    JSONArray cities;
    JSONArray countries;
    String token;
    String selectedCity;


    TextView profile_edit_back_button;
    TextView firstNameText;
    TextView lastnameText;
    TextView stateText;
    TextView emailText;
    TextView emailCheckLength;
    Spinner countrySpinner;
    Spinner citySpinner;
    Button save_Button;
    String errorMessage;
    final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }

        getIdforTextAndButton();
        getValuesFromSharedPreferences();
        getAndSetOldDataFromSharedPreferences();
        getAndSetOldDataFromSharedPreferences();
        getLocationdata();


try {
    profile_edit_back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToProfileActivityByback();

        }
    });

    save_Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fieldValidation();

        }
    });
}catch (NullPointerException e){
    e.printStackTrace();
}
    }

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        profile_edit_back_button = (Button) findViewById(R.id.left_side_bar_button);
        firstNameText = (EditText) findViewById(R.id.edit_profile_firstname);
        lastnameText = (EditText) findViewById(R.id.edit_profile_lastname);
        stateText = (EditText) findViewById(R.id.edit_profile_state_text);
        emailText = (EditText) findViewById(R.id.edit_profile_email);
        emailCheckLength = (TextView) findViewById(R.id._profileEdit_email_hasCode);
        countrySpinner = (Spinner) findViewById(R.id.edit_profile_spinner_country);
        citySpinner = (Spinner) findViewById(R.id.edit_profile_spinner_city);
        save_Button = (Button) findViewById(R.id.edit_profile_save_button);
    }

    public void showError(String error_message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_validateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
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

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String phone_number = sharedPref.getString(getString(R.string.phonenumber_data), null);
        String defaultValue = getString(R.string.locations_default_empty_value);
        String locationsData = sharedPref.getString(getString(R.string.locations_data_key), defaultValue);

        try {
            countries = new JSONArray(locationsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getLocationdata() {

        final List<String> countrySpinnerNameArray = new ArrayList<>();
        final List<Integer> countrySpinnerIdArray = new ArrayList<>();

        for (Integer index_of_country = 0; index_of_country < countries.length(); index_of_country++) {
            try {
                countrySpinnerNameArray.add(new JSONObject(countries.get(index_of_country).toString()).getString("name"));
                countrySpinnerIdArray.add(new JSONObject(countries.get(index_of_country).toString()).getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                countrySpinnerNameArray
        );
try {
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

                    for (Integer index_of_city = 0; index_of_city < cities.length(); index_of_city++) {
                        try {
                            citySpinnerNameArray.add(new JSONObject(cities.get(index_of_city).toString()).getString("name"));
                            citySpinnerIdArray.add(new JSONObject(cities.get(index_of_city).toString()).getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    final ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            citySpinnerNameArray);

                    citySpinner.setAdapter(cityAdapter);

                    for(int i=0; i< citySpinnerNameArray.size() ;i++){
                       if( citySpinnerNameArray.get(i).equalsIgnoreCase(selectedCity)){
                           citySpinner.setSelection(i);
                       }
                    }
                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            slectedCityId = citySpinnerIdArray.get(position);

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
}catch (NullPointerException e){
    e.printStackTrace();
}
    }

    public void fieldValidation() {

        if (emailText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();

        } else if (!emailText.getText().toString().isEmpty()) {
            pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(emailText.getText().toString());
            if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "Please enter  valid email", Toast.LENGTH_LONG).show();
            } else {
                updateProfile();
                save_Button.setClickable(false);
            }
        }
    }

    private void updateProfile() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_UPDATE_PROFILE = getString(R.string.api_domain) + getString(R.string.update_profile_api_path);

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("first_name", firstNameText.getText().toString());
            jsonObject.put("last_name", lastnameText.getText().toString());
            jsonObject.put("country", slectedCountryId);
            jsonObject.put("city", slectedCityId);
            jsonObject.put("state", stateText.getText().toString());
            jsonObject.put("email", emailText.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> profileResponseListner = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response ", response.toString());
                goToProfileActivity();
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileEditActivity.this, "Successfully Updated", Toast.LENGTH_LONG).show();
            }
        };

        Response.ErrorListener profileErrorResponseListner = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response ", error.toString());
                loader.setVisibility(View.INVISIBLE);
                save_Button.setClickable(true);

                errorMessage = null;
                if (error instanceof NetworkError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    errorMessage = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    errorMessage = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
               else if (error.networkResponse.statusCode == 500){
                    Toast.makeText(ProfileEditActivity.this, "Error Occerred", Toast.LENGTH_LONG).show();
            } else if (error.networkResponse.statusCode == 400) {
                    try {
                        String error_message = new JSONObject(new String(error.networkResponse.data))
                                .getJSONArray("email").getString(0);
                        showError(error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (error.networkResponse.statusCode == 403) {
                    errorMessage = null;
                    try {
                        errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeTokenFromSharedPreferences();//will be go SplashActivity
                }

            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL_FOR_UPDATE_PROFILE,
                jsonObject,
                profileResponseListner,
                profileErrorResponseListner
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token);
                return params;
            }
        };

        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }


    private void goToProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToProfileActivityByback() {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void removeTokenFromSharedPreferences() {
        SharedPreferences pref = getBaseContext().getSharedPreferences("shared_preference_key", 0);
        pref.edit().remove("token").commit();
        goToSplashActivity();
    }

    public void goToSplashActivity() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        startActivity(intent);
        finish();
    }

    public void getAndSetOldDataFromSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("shared_preference_key", 0);
        String first_name = sharedPref.getString("first_name", null);
        String last_name = sharedPref.getString("last_name", null);
        String email = sharedPref.getString("email", null);
        String emailCheck = sharedPref.getString("emailCheck", null);
        String state = sharedPref.getString("state", null);
        String selectedCountry = sharedPref.getString("country", null);
         selectedCity = sharedPref.getString("city", null);



        //setOldData
try {

    firstNameText.setText(first_name);
    lastnameText.setText(last_name);
    stateText.setText(state);
}catch (NullPointerException e){
    e.printStackTrace();
}
        try {
    if (email.length() > 55) {
        emailText.setText(email);
        emailCheckLength.setVisibility(View.VISIBLE);
    } else {
        emailText.setText(email);
        emailCheckLength.setVisibility(View.INVISIBLE);
    }
    emailText.setText(email);
    emailCheckLength.setText(emailCheck);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        JSONObject countryObjectInLoop, cityObjectInLoop;



        for (Integer countryIndex = 0; countryIndex < countries.length(); countryIndex++) {
            try {
                countryObjectInLoop = new JSONObject(countries.get(countryIndex).toString());
                if (selectedCountry.equals(countryObjectInLoop.getString("name").toString())) {
                    countrySpinner.setSelection(countryIndex);
                    cities = countryObjectInLoop.getJSONArray("cities");

                    for (Integer cityIndex = 0; cityIndex < cities.length(); cityIndex++) {
                        try {
                            cityObjectInLoop = new JSONObject(cities.get(cityIndex).toString());
                            if (selectedCity.equals(cityObjectInLoop.getString("name").toString())) {
                                citySpinner.setSelection(cityIndex);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
