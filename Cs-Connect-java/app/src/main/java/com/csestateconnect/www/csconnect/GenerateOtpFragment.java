package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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


public class GenerateOtpFragment extends Fragment {

    private String selectedCountryCode;
    private Spinner country_code_spinner;
    private  EditText generate_otp_phoneNumber_text;
    private final List<String> countryCodeNames = new ArrayList<>();
    private final List<String> countryNumberCodes = new ArrayList<>();
    private String phonenumber;
    private CheckBox termsConditionCheckBox;
    private CheckBox privacyPolicyCheckBox;
    private TextView termsConditionTxt;
    private TextView privacyPolicyTxt;
    private ConstraintLayout loader;

    NetworkInformation networkInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generateotp, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkInformation = new NetworkInformation(getContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }
        final Button go_to_validate_otp_button = getView().findViewById(R.id.get_otp_button);
            go_to_validate_otp_button.setVisibility(View.VISIBLE);
            populateSpinner();

            generate_otp_phoneNumber_text = getView().findViewById(R.id.generate_otp_phoneNumber_text);
            loader = getView().findViewById(R.id.progress_bar_layout);
            privacyPolicyCheckBox = getView().findViewById(R.id.privacy_policy_chechBox);
            termsConditionCheckBox = getView().findViewById(R.id.termCondition_Checkbox);
            privacyPolicyTxt = getView().findViewById(R.id.privacy_policy_text);
            termsConditionTxt = getView().findViewById(R.id.termCondition_text);

            termsConditionTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TermsConditionsActivity.class);
                    startActivity(intent);

                }
            });
            privacyPolicyTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                    startActivity(intent);
                }
            });

            go_to_validate_otp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (termsConditionCheckBox.isChecked()) {
                        getOTP();
                        loader.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(getActivity(), "Please check Terms & Conditions", Toast.LENGTH_SHORT).show();
                }
            });
        }


    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.Press Retry to Connect.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (networkInformation.isConnectingToInternet() == false) {
//no connection
                    networkErrorPopup();
                } else{
                    getFragmentManager().beginTransaction().detach(GenerateOtpFragment.this)
                            .attach(GenerateOtpFragment.this).commit();
                }

            }
        });
        builder.show();
    }

    public void getOTP(){
        phonenumber = generate_otp_phoneNumber_text.getText().toString();
        phonenumber = selectedCountryCode + phonenumber;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("phone_number", phonenumber);

        } catch (JSONException e){
            e.printStackTrace();
        }

        String generate_otp_url = getString(R.string.api_domain) + getString(R.string.generate_otp_api_path);
        JsonObjectRequest generate_otp_request = new JsonObjectRequest(
            Request.Method.POST, generate_otp_url, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        goToValidateOtpFragment(response.getString("pk"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loader.setVisibility(View.INVISIBLE);
                    String errorMessage = null;
                    if (error instanceof NetworkError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error.networkResponse.statusCode == 400){
                        try {
                            String error_message = new JSONObject(new String(error.networkResponse.data))
                                    .getJSONArray("phone_number").getString(0);
                            showError(error_message);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(generate_otp_request);
    }

    public void populateSpinner(){
        country_code_spinner=getView().findViewById(R.id.generate_otp_countryCode_spinner);

        JSONArray countries = getLocationsDataFromSharedPrefrences();

        String countryCodeName;
        JSONObject country;

        for(Integer index_of_country = 0; index_of_country < countries.length(); index_of_country++) {
            try {
                country = new JSONObject(countries.get(index_of_country).toString());
                countryCodeName = country.getString("two_digit_code")  + " " + country.getString("number_code");
                countryCodeNames.add(countryCodeName);
                countryNumberCodes.add(country.getString("number_code"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        final ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countryCodeNames);

        country_code_spinner.setAdapter(countriesAdapter);

        country_code_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryCode = countryNumberCodes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToValidateOtpFragment(String pk) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        Bundle args = new Bundle();
        args.putString("pk", pk);
        args.putString("phone_number",phonenumber);
        Fragment vof = new ValidateOtpFragment();
        vof.setArguments(args);
        ft.replace(R.id.splash_fragment_container, vof);
        ft.commit();
    }

    public void showError(String error_message){
        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_generateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
    }


    public JSONArray getLocationsDataFromSharedPrefrences(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        // TODO: Set Default value for country if data is not received
        String defaultValue = getString(R.string.locations_default_empty_value);
        String locationsData = sharedPref.getString(getString(R.string.locations_data_key), defaultValue);
        try {
            return new JSONArray(locationsData);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

}