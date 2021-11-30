package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificationUpdateActivity extends AppCompatActivity {

    ImageView id_proofImage;
    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 2;
    String errorMessage;
    ConstraintLayout loader;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;

    Button back_button;
    Button update_button;
    String token;
NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_update);
        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else


            getValuesFromSharedPreferences();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        id_proofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKYCDetail();
                update_button.setClickable(false);
            }
        });
    }

    public void selectImages() {
        checkPermissions();
        if (checkPermissions() == true) {

            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationUpdateActivity.this);
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
                id_proofImage.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                id_proofImage.setImageURI(selectedImageUri);
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


    public void updateKYCDetail() {
        loader.setVisibility(View.VISIBLE);

        String URL_FOR_get_kycDetail = getString(R.string.api_domain) + getString(R.string.update_kyc_api_path);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT,
                URL_FOR_get_kycDetail, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.setVisibility(View.INVISIBLE);
                Log.e("NetworkResponse", response.toString());
                Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                goToVerification_detail();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                update_button.setClickable(true);
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("Error Response ", error.toString());
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
                        errorMessage = null;
                    } else if (error instanceof NetworkError) {
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

                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) id_proofImage.getDrawable());
                    Bitmap bitmap1 = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageInByte1 = stream.toByteArray();


                    params.put("id_card_image", new DataPart("file_cover.jpg", imageInByte1, "image/jpeg"));

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


    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String verification_image =  sharedPref.getString("verification_image",null);

        //setOldData
        Glide.with(getApplicationContext()).load(verification_image).into(id_proofImage);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), VerificationDetailActivity.class);
        startActivity(intent);
        finish();
    }

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        back_button = findViewById(R.id.left_side_bar_button);
        update_button = findViewById(R.id.update_verification_submit_button);
        id_proofImage = (ImageView) findViewById(R.id.update_verification_idProof_image);
    }

    private void removeTokenFromSharedPreferences() {
        SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
        pref.edit().remove("token").commit();
        goToSplashActivity();
    }

    public void goToSplashActivity() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        startActivity(intent);
        finish();
    }

    private void goToVerification_detail() {
        Intent intent = new Intent(getApplicationContext(), VerificationDetailActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), VerificationDetailActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
