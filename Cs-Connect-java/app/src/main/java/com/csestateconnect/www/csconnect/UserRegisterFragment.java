package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class UserRegisterFragment extends Fragment {

    ImageLoader imageLoader=SingletonQueue.getInstance(getActivity()).getImageLoader();
    private NavigationDrawerToggle drawerInterface;
    private BottomNavigationToggle bottomNavigationInterface;
    Integer slectedCountryId = 0;
    Integer slectedCityId = 0;

    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 2;
    String token;
    ConstraintLayout loader;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;

    JSONArray cities;
    JSONArray countries;
    ImageView profileImage;
    TextView register_back_button;
    TextView firstNameText;
    TextView lastnameText;
    TextView stateText;
    TextView emailText;
    //    TextView termsConditionTextButton;
    Spinner countrySpinner;
    Spinner citySpinner;
    Button registerButton;
    String errorMessage;
    final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;
NetworkInformation networkInformation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            drawerInterface = (NavigationDrawerToggle) context;
            bottomNavigationInterface = (BottomNavigationToggle) context;
            getValuesFromSharedPreferences();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerInterface.lockDrawer();
        bottomNavigationInterface.hideBottomNavigation();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getIdforTextAndButton();
        loader = getView().findViewById(R.id.progress_bar_layout);
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getLocationdata();



        register_back_button = getView().findViewById(R.id.register_skip_button);
        register_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();

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
                    getFragmentManager().beginTransaction().detach(UserRegisterFragment.this)
                            .attach(UserRegisterFragment.this).commit();
                }

            }
        });
        builder.show();
    }

    public void selectImages(){

        checkPermissions();
        if (checkPermissions() == true) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Upload Image");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    if (items[i].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);

                    } else if (items[i].equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                    } else if (items[i].equals("Cancel")) {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                updateProfileImage(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    updateProfileImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean checkPermissions() {
        int camera = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
        bottomNavigationInterface.showBottomNavigation();
    }

    public void getIdforTextAndButton() {
        profileImage = (ImageView) getView().findViewById(R.id.register_profile_image);
        firstNameText = (EditText) getView().findViewById(R.id.register_firstname_text);
        lastnameText = (EditText) getView().findViewById(R.id.register_lastname_text);
        stateText = (EditText) getView().findViewById(R.id.register_state_text);
        emailText = (EditText) getView().findViewById(R.id._register_email_text);
//        termsConditionTextButton = (TextView) getView().findViewById(R.id.register_terms_condition_text);
        countrySpinner = (Spinner) getView().findViewById(R.id.register_spinner_country);
        citySpinner = (Spinner) getView().findViewById(R.id.register_spinner_city);
        registerButton = (Button) getView().findViewById(R.id.register_save_button);
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String phone_number = sharedPref.getString(getString(R.string.phonenumber_data), null);
        String defaultValue = getString(R.string.locations_default_empty_value);
        String locationsData = sharedPref.getString(getString(R.string.locations_data_key), defaultValue);

        try {
            countries = new JSONArray(locationsData);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLocationdata() {

        final List<String> countrySpinnerNameArray = new ArrayList<>();
        final List<Integer> countrySpinnerIdArray = new ArrayList<>();

        for(Integer index_of_country = 0; index_of_country < countries.length(); index_of_country++) {
            try {
                countrySpinnerNameArray.add(new JSONObject(countries.get(index_of_country).toString()).getString("name"));
                countrySpinnerIdArray.add(new JSONObject(countries.get(index_of_country).toString()).getInt("id"));
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(
                getActivity(),
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
                            getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            citySpinnerNameArray);

                    citySpinner.setAdapter(cityAdapter);

                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            slectedCityId = citySpinnerIdArray.get(position);

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

    public void fieldValidation(){

        if (  firstNameText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your first name", Toast.LENGTH_SHORT).show();

        } else if (lastnameText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your last name", Toast.LENGTH_SHORT).show();


        } else if (slectedCountryId == 0) {
            Toast.makeText(getActivity(), "Please enter country name", Toast.LENGTH_SHORT).show();

        } else if (slectedCityId == 0) {
            Toast.makeText(getActivity(), "Please enter city name", Toast.LENGTH_SHORT).show();

        } else if (stateText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter state", Toast.LENGTH_SHORT).show();

        } else if (emailText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();

        } else if (!emailText.getText().toString().isEmpty()) {
            pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(emailText.getText().toString());
            if (!matcher.matches()){
                Toast.makeText(getActivity(), "Please enter  valid email", Toast.LENGTH_SHORT).show();
            } else {
                CreateProfile();

                registerButton.setClickable(false);
            }
        }
    }
    public void showError(String error_message){
        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_validateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
    }

    private void CreateProfile() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_CREATE_PROFILE = getString(R.string.api_domain) + getString(R.string.update_profile_api_path);

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("first_name", firstNameText.getText().toString());
            jsonObject.put("last_name", lastnameText.getText().toString());
            jsonObject.put("country",slectedCountryId);
            jsonObject.put("city", slectedCityId);
            jsonObject.put("state", stateText.getText().toString());
            jsonObject.put("email", emailText.getText().toString());




        } catch (JSONException e){
            e.printStackTrace();
        }

        Response.Listener<JSONObject> profileResponseListner = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response ", response.toString());
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Successfully Profile Saved", Toast.LENGTH_SHORT).show();
                goToProfileFragment();
            }
        };

        Response.ErrorListener profileErrorResponseListner = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response ", error.toString());
                registerButton.setClickable(true);
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
                } else if (error.networkResponse.statusCode == 500){
                        Toast.makeText(getActivity(), "Error Occerred", Toast.LENGTH_LONG).show();

                } else if (error.networkResponse.statusCode == 403) {
                    errorMessage = null;
                    try {
                        errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeTokenFromSharedPreferences();//will be go SplashActivity
                }
                else if (error.networkResponse.statusCode == 400) {
                    try {
                        String error_message = new JSONObject(new String(error.networkResponse.data))
                                .getJSONArray("email").getString(0);
                        showError(error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL_FOR_CREATE_PROFILE,
                jsonObject,
                profileResponseListner,
                profileErrorResponseListner
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", token );
                return params;
            }
        };

        SingletonQueue.getInstance(getActivity()).addToRequestQueue(jsonRequest);
    }


    public void updateProfileImage(final Bitmap bitmap_data) {
        String URL_FOR_UPDATE_PROFILEIMAGE = getString(R.string.api_domain) + getString(R.string.update_profileImage_api_path);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                URL_FOR_UPDATE_PROFILEIMAGE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    String profile_image = new JSONObject(new String(response.data)).get("profile_image").toString();
                    Glide.with(getActivity()).load(profile_image).apply(RequestOptions.circleCropTransform()).into(profileImage);
                    loader.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                String errorMessage = "Unknown error";

                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                try {
                    if ( error.networkResponse.statusCode == 403) {

                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");

                        removeTokenFromSharedPreferences();//will be go SplshActivity
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap_data.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageInByte1 = stream.toByteArray();

                    params.put("profile_image", new DataPart("file_cover.jpg", imageInByte1, "image/jpeg"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(retryPolicy);
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }
    private void goToProfileFragment(){
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    private void removeTokenFromSharedPreferences(){
        SharedPreferences pref = getActivity().getBaseContext().getSharedPreferences("shared_preference_key", 0);
        pref.edit().remove("token").commit();
        goToSplashActivity();
    }
    public void goToSplashActivity() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        startActivity(intent);
        getActivity().finish();
    }
}