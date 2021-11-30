package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UserProfileActivity extends AppCompatActivity {

    NetworkInformation networkInformation;

    ImageLoader imageLoader = SingletonQueue.getInstance(getBaseContext()).getImageLoader();
    private NavigationDrawerToggle drawerInterface;
    String token;
    String errorMessage;
    String imagePath, imageFileName;

    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 2;

    ConstraintLayout loader;
    ImageView profileImage;
    Button back_button;
    TextView update_text_button;
    TextView phoneNumberText;
    TextView firstNameText;
    TextView lastnameText;
    TextView stateText;
    TextView emailText;
    TextView emailCheckLength;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;
    TextView countryText;
    TextView cityText;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getIdforTextAndButton();

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

            getValuesFromSharedPreferences();
        getProfiles();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        update_text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileEditActivity();
                update_text_button.setClickable(false);
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


    public void selectImages() {
        checkPermissions();
        if (checkPermissions() == true) {
            final CharSequence[] items = {"Camera", "Gallery", "Remove Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                    } else if (items[i].equals("Remove Image")) {
                        Glide.with(getApplicationContext()).load(R.drawable.android_profile_icon_round).apply(RequestOptions.circleCropTransform()).into(profileImage);
                        removeImageData();

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

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                updateProfileImage(bitmap);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    updateProfileImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private boolean checkPermissions() {
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public void showError(String error_message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_validateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
    }

    private void getProfiles() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.get_profile_api_path);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :", response.toString());
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jObj = new JSONObject(response);


                    String first_name = jObj.getString("first_name");
                    String last_name = jObj.getString("last_name");

                    String countryName;
                    try {
                        JSONObject country = jObj.getJSONObject("country");
                        countryName = country.getString("name");
                    } catch (Exception e) {
                        countryName = "";
                    }


                    String cityName;
                    try {
                        JSONObject city = jObj.getJSONObject("city");
                        cityName = city.getString("name");
                    } catch (Exception e) {
                        cityName = "";
                    }

                    String state = jObj.getString("state");
                    String phone_number = jObj.getString("phone_number");
                    String email = jObj.getString("email");

//                    JSONObject Rm = jObj.getJSONObject("rm");
//
//                    String RmId = Rm.getString("id");
//                    String RmFirstName = Rm.getString("first_name");
//                    String RmLastname = Rm.getString("last_name");
//                    String RmEmail = Rm.getString("email");
//                    String RmNumber = Rm.getString("phone_number");

                    Glide.with(getApplicationContext()).load(jObj.getString("profile_image")).apply(RequestOptions.circleCropTransform()).into(profileImage);

                    if (!jObj.getString("profile_image").isEmpty()) {
                        profileImage.setBackground(null);
                    }
//                    profileImage.setImageUrl(jObj.getString("profile_image"),imageLoader);
                    firstNameText.setText(first_name);
                    lastnameText.setText(last_name);
                    countryText.setText(countryName);
                    cityText.setText(cityName);
                    phoneNumberText.setText(phone_number);
                    stateText.setText(state);


                    if (email.length() >= 56) {
                        emailText.setText(email);
                        emailCheckLength.setVisibility(View.VISIBLE);
                    } else {
                        emailText.setText(email);
                        emailCheckLength.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                String errorMessage = null;
                 if (error instanceof ServerError) {
                    errorMessage = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                } else if (error instanceof ParseError) {
                    errorMessage = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    errorMessage = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }

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

    public void updateProfileImage(final Bitmap bitmap_data) {
        loader.setVisibility(View.VISIBLE);

        String URL_FOR_UPDATE_PROFILEIMAGE = getString(R.string.api_domain) + getString(R.string.update_profileImage_api_path);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                URL_FOR_UPDATE_PROFILEIMAGE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("Response", response.toString());
                Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                try {
                    String profile_image = new JSONObject(new String(response.data)).get("profile_image").toString();
                    Glide.with(getApplicationContext()).load(profile_image).apply(RequestOptions.circleCropTransform()).into(profileImage);
                    loader.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                loader.setVisibility(View.INVISIBLE);
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }

                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                         errorMessage = null;
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

                       else if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (error.networkResponse.statusCode == 403) {
                        errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        removeTokenFromSharedPreferences();//will be go SplshActivity
                    }

                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
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
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private void removeImageData() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.remove_profile_image_api_path);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :", response.toString());
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jObj = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                String errorMessage = "Unknown error";

                  if (networkResponse == null) {

                        errorMessage = null;
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
                        } else if (error.networkResponse.statusCode == 403) {
                        errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        removeTokenFromSharedPreferences();//will be go SplshActivity
                    }

                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
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


    public void getIdforTextAndButton() {

        loader = findViewById(R.id.progress_bar_layout);
        update_text_button = findViewById(R.id.profile_update_text_button);
        back_button = findViewById(R.id.left_side_bar_button);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        firstNameText = (TextView) findViewById(R.id.profile_firstname);
        lastnameText = (TextView) findViewById(R.id.profile_lastname);
        phoneNumberText = (TextView) findViewById(R.id.profile_phoneNumber_text);
        stateText = (TextView) findViewById(R.id.profile_state_text);
        emailText = (TextView) findViewById(R.id._profile_email_text);
        emailCheckLength = (TextView) findViewById(R.id._profile_email_hasCode);
        countryText = (TextView) findViewById(R.id.profile_country_text);
        cityText = (TextView) findViewById(R.id.profile_city_text);

    }

    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToProfileEditActivity() {
        Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
        setDataInSharedPreferences();
        startActivity(intent);
        finish();

    }

    public void setDataInSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("first_name", firstNameText.getText().toString());
        editor.putString("last_name", lastnameText.getText().toString());
        editor.putString("email", emailText.getText().toString());
        editor.putString("emailCheck", emailCheckLength.getText().toString());
        editor.putString("state", stateText.getText().toString());
        editor.putString("country", countryText.getText().toString());
        editor.putString("city", cityText.getText().toString());
        editor.commit();
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
