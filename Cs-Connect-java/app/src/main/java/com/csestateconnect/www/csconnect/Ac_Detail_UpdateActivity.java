package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ac_Detail_UpdateActivity extends AppCompatActivity {


    ConstraintLayout pancard_uploder_layout;
    ConstraintLayout cancelCheq_uploder_layout;
    Integer REQUEST_CAMERA_CLICK_CHEQUE = 1;
    Integer REQUEST_CAMERA_CLICK_PAN = 2;

    Integer SELECT_FILE_CLICK_CHEQUE = 3;
    Integer SELECT_FILE_CLICK_PAN = 4;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;
    String token;
    String errorMessage;

    Button back_button;
    Button Continue_button;

    TextInputLayout userName_text;
    TextInputLayout user_ac_number_text;
    TextInputLayout user_bank_name_text;
    TextInputLayout ifsc_code_text;
    TextInputLayout pan_cardNumber_text;
    TextInputLayout address_text;
    ImageView pan_caredImage;
    ImageView cancell_chequedImage;
    ConstraintLayout loader;
NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac__detail__update);

        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        getValuesFromSharedPreferences();
        getAndSetOldDataFromSharedPreferences();

        pancard_uploder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("PAN");
            }
        });
        cancelCheq_uploder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("CHEQUE");
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAc_Detail_Profile();
            }
        });
        Continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount();
                Continue_button.setClickable(false);
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

    private void selectImage(final String button_name) {
        checkPermissions();
        if (checkPermissions() == true) {

            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Ac_Detail_UpdateActivity.this);
            builder.setTitle("Upload Image");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    if (items[i].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (button_name.equals("CHEQUE")) {
                            startActivityForResult(intent, REQUEST_CAMERA_CLICK_CHEQUE);
                        } else if (button_name.equals("PAN")) {
                            startActivityForResult(intent, REQUEST_CAMERA_CLICK_PAN);
                        }


                    } else if (items[i].equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");

                        if (button_name.equals("CHEQUE")) {
                            startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE_CLICK_CHEQUE);
                        } else if (button_name.equals("PAN")) {
                            startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE_CLICK_PAN);
                        }

                 /*   else if(items[i].equals("Remove Image")){
                        dialog
                    }*/

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

            if (requestCode == REQUEST_CAMERA_CLICK_CHEQUE) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                cancell_chequedImage.setImageBitmap(bitmap);
            } else if (requestCode == REQUEST_CAMERA_CLICK_PAN) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                pan_caredImage.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE_CLICK_CHEQUE) {
                Uri selectedImageUri = data.getData();
                cancell_chequedImage.setImageURI(selectedImageUri);
            } else if (requestCode == SELECT_FILE_CLICK_PAN) {
                Uri selectedImageUri = data.getData();
                pan_caredImage.setImageURI(selectedImageUri);
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
        pancard_uploder_layout = (ConstraintLayout) findViewById(R.id.ac_detail_update_pancard_uploder_layout);
        cancelCheq_uploder_layout = (ConstraintLayout) findViewById(R.id.ac_deatil_update_CancelCheq_uploder_layout);
        pan_caredImage = (ImageView) findViewById(R.id.ac_detail_update_pancard_image);
        cancell_chequedImage = (ImageView) findViewById(R.id.ac_detail_update_cancelCheq_image);
        userName_text = (TextInputLayout) findViewById(R.id.ac_detail_update_user_name);
        user_ac_number_text = (TextInputLayout) findViewById(R.id.ac_detail_update_acount_number);
        user_bank_name_text = (TextInputLayout) findViewById(R.id.ac_detail_update_bank_name);
        ifsc_code_text = (TextInputLayout) findViewById(R.id.ac_detail_update_ifsc_code);
        pan_cardNumber_text = (TextInputLayout) findViewById(R.id.ac_detail_update_pan_card_number);
        address_text = (TextInputLayout) findViewById(R.id.ac_detail_update_address);
        Continue_button = (Button) findViewById(R.id.ac_detail_update_Continue_button);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
    }


    public void updateAccount() {
        loader.setVisibility(View.VISIBLE);
        String URL_USER_UPDATE_ACCOUNT = getString(R.string.api_domain) + getString(R.string.update_account_api_path);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT,
                URL_USER_UPDATE_ACCOUNT, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("NetworkResponse", "onResponse: ");
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(Ac_Detail_UpdateActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                goToAc_Detail_Profile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("VolleyError", "onErrorResponse: ");
                Continue_button.setClickable(true);
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
                    Integer statusCode = error.networkResponse.statusCode;
                    if (statusCode == 403) {
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
                params.put("account_holder_name", userName_text.getEditText().getText().toString());
                params.put("account_number", user_ac_number_text.getEditText().getText().toString());
                params.put("bank_name", user_bank_name_text.getEditText().getText().toString());
                params.put("ifsc_code", ifsc_code_text.getEditText().getText().toString());
                params.put("pan_card_number", pan_cardNumber_text.getEditText().getText().toString());
                params.put("address", address_text.getEditText().getText().toString());

                return params;
            }



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",token);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                try {


                    BitmapDrawable bitmapDrawable1 = ((BitmapDrawable) pan_caredImage.getDrawable());
                    BitmapDrawable bitmapDrawable2 = ((BitmapDrawable) cancell_chequedImage.getDrawable());
                    Bitmap bitmap1 = bitmapDrawable1.getBitmap();
                    Bitmap bitmap2 = bitmapDrawable2.getBitmap();
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    byte[] imageInByte1 = stream1.toByteArray();
                    byte[] imageInByte2 = stream2.toByteArray();


                    params.put("pan_card_image", new DataPart("file_cover.jpg", imageInByte1, "image/png"));
                    params.put("canceled_cheque_image", new DataPart("file_cheque.jpg", imageInByte2, "image/png"));

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

    private void goToAc_Detail_Profile() {
        Intent intent = new Intent(getApplicationContext(), Ac_Detail_ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void getAndSetOldDataFromSharedPreferences() {
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("shared_preference_key", 0);
        String account_holder_name = sharedPref.getString("account_holder_name",null);
        String account_number = sharedPref.getString("account_number",null);
        String bank_name = sharedPref.getString("bank_name",null);
        String ifsc_code = sharedPref.getString("ifsc_code",null);
        String pan_card_number =  sharedPref.getString("pan_card_number",null);
        String address =  sharedPref.getString("address",null);
        String panCard =  sharedPref.getString("pan_card_image",null);
        String cancelCheque =  sharedPref.getString("cancel_cheque_image",null);

        //setOldData
        Glide.with(getApplicationContext()).load(panCard).into(pan_caredImage);
        Glide.with(getApplicationContext()).load(cancelCheque).into(cancell_chequedImage);

        userName_text.getEditText().setText(account_holder_name);
        user_ac_number_text.getEditText().setText(account_number);
        user_bank_name_text.getEditText().setText(bank_name);
        ifsc_code_text.getEditText().setText(ifsc_code);
        pan_cardNumber_text.getEditText().setText(pan_card_number);
        address_text.getEditText().setText(address);

    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    private void removeTokenFromSharedPreferences(){
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Ac_Detail_ProfileActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
