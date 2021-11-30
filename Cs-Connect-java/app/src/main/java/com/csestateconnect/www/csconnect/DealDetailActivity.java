package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csestateconnect.www.csconnect.models.deal.Deal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class DealDetailActivity extends AppCompatActivity {


    Integer REQUEST_CAMERA = 1;
    Integer SELECT_FILE = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;

    String URL_FOR_GET_PROJECT;
    private String errorMessage;
    private String token;
    private Integer dealId;
    private Deal deal;
    private Integer dealIdByLead;
    private String dealimage;
    private ConstraintLayout loader;
    private TextView dealIdView;
    private TextView dealDateView;
    private Spinner dealStatusView;
    private TextView dealnameView;
    private TextView dealProjectViewView;
    private TextView emailViewView;
    private TextView phoneNumberView;
    private Button back_button;
    private Button uploadImageButton;
    private ImageView chequeImageView;
    JSONArray status;
    Integer slectedStatusId = 0;

NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);

        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        getValuesFromSharedPreferences();
        getLeadData();
        getDealDetail();
//        getStatusValue();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommissionFragment();
            }
        });

        emailViewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + emailViewView.getText().toString());
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });
        phoneNumberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumberView.getText().toString()));
                startActivity(intent);
            }
        });

        chequeImageView.setOnClickListener(new View.OnClickListener() {
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
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
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


    public void getIdforTextAndButton() {

        loader = findViewById(R.id.progress_bar_layout);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
        uploadImageButton = (Button) findViewById(R.id.upload_image_button);
        chequeImageView = (ImageView) findViewById(R.id.deal_cheque_image);
        emailViewView = (TextView) findViewById(R.id.deal_email);
        dealIdView = (TextView) findViewById(R.id.deal_id);
        dealDateView = (TextView) findViewById(R.id.deal_created_date);
        dealStatusView = (Spinner) findViewById(R.id.deal_status);
        dealnameView = (TextView) findViewById(R.id.deal_name);
        dealProjectViewView = (TextView) findViewById(R.id.deal_project);
        phoneNumberView = (TextView) findViewById(R.id.deal_phone_number);

    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String status_array_string = (sharedPref.getString("dealStatusCode", null));

        try {
            status = new JSONArray(status_array_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void getStatusValue() {
//
//    }

    public void updateDealStatus() {
        String GET_DEAL_STATUS_URL = getString(R.string.api_domain) + getString(R.string.update_deal_api_path) + dealIdView.getText() + "/";
        JSONObject json1 = new JSONObject();

        try {
            json1.put("deal_status", slectedStatusId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PATCH, GET_DEAL_STATUS_URL, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", token);
                return headers;
            }


        };
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

    private void getDealDetail() {

        loader.setVisibility(View.VISIBLE);
        if (dealId != 0) {
            URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.deals_list_path) + "/" + dealId;
        } else
            URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.deals_list_path) + "/" + dealIdByLead;
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_PROJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject allData = new JSONObject(response);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Deal>() {
                    }.getType();
                    deal = gson.fromJson(allData.toString(), type);

                    SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
                    Date date = null;
                    try {
                        date = inputFormatter.parse(deal.getCreatedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String deid = deal.getId().toString();
                    String name = deal.getLead().getName();
                    String email = deal.getLead().getEmail();
                    String mobile = deal.getLead().getPhoneNumber();
                     String statusView = deal.getDealStatus().getName();

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
                            android.R.layout.simple_spinner_item,
                            statusSpinnerNameArray
                    );

                    dealStatusView.setAdapter(statusAdapter);

                    for(int i=0; i< statusSpinnerNameArray.size(); i++){
                        if( statusSpinnerNameArray.get(i).equals(statusView)){
                            dealStatusView.setSelection(i);
                        }
                    }

                    dealStatusView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            slectedStatusId = statusSpinnerIdArray.get(position);
                            if (position > 0) {
                                updateDealStatus();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    String projectName = deal.getProject().getName();
                    try {
                        if (deal.getChequeImage() != null) {
                            dealimage = deal.getChequeImage();
                            Glide.with((getApplicationContext())).load(dealimage).into(chequeImageView);
                            chequeImageView.setVisibility(View.VISIBLE);
                            uploadImageButton.setVisibility(View.GONE);
                        } else {
                            uploadImageButton.setVisibility(View.VISIBLE);
                            uploadImageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goToDealChequeImageActivity();
                                }
                            });
                            chequeImageView.setVisibility(View.GONE);
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    dealIdView.setText(deid);
                    dealDateView.setText(outputFormatter.format(date));
                    dealnameView.setText(name);
                    emailViewView.setText(email);
                    phoneNumberView.setText(mobile);
                    dealProjectViewView.setText(projectName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ContentValues.TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse == null) {

                    String errorMessage = errorMessage = null;
                    if (error instanceof NetworkError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        errorMessage = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
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
                    Glide.with(getApplicationContext()).load(profile_image).into(chequeImageView);
                    loader.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.INVISIBLE);
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

    private void goToCommissionFragment() {
        super.onBackPressed();
        finish();
    }

    private void goToDealChequeImageActivity() {

            Intent intent = new Intent(getApplicationContext(), DealChequeImageActivity.class);
        if(dealId != 0) {
            intent.putExtra("dealIdbyDealAdapter", dealId);
        } else
            intent.putExtra("dealIdbyDealAdapter", dealIdByLead);

            startActivity(intent);
            finish();
    }

    public void getLeadData() {
        final Intent myintent = getIntent();
        if (null != myintent.getExtras()) {

                try {
                    dealId = myintent.getExtras().getInt("dealIdbyDealAdapter");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            if (0 == dealId) {
                try {
                    dealIdByLead = myintent.getExtras().getInt("dealIdbyLeadAdapter");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}

