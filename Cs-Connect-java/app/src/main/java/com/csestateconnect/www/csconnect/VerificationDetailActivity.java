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
import android.widget.Button;
import android.widget.TextView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class VerificationDetailActivity extends AppCompatActivity {

    ImageLoader imageLoader = SingletonQueue.getInstance(getBaseContext()).getImageLoader();
    private NavigationDrawerToggle drawerInterface;
    private BottomNavigationToggle bottomNavigationInterface;
    NetworkImageView id_proofImage;
    ConstraintLayout loader;

    String errorMessage;
    String token;
    Button back_button;
    Button update_button;
    TextView statusText;
    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_detail);
        getIdforTextAndButton();

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

            getValuesFromSharedPreferences();
        getVerificationDetail();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateVerification();
                update_button.setClickable(false);
            }
        });
    }

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        statusText = findViewById(R.id.submitted);
        back_button = findViewById(R.id.left_side_bar_button);
        update_button = findViewById(R.id.verification_detail_submit_button);
        id_proofImage = (NetworkImageView) findViewById(R.id.verification_detail_idProof_image);
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


    public void getVerificationDetail() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_KYC_DETAIL = getString(R.string.api_domain) + getString(R.string.kyc_detail_api_path);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_KYC_DETAIL
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response ", response.toString());
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("kyc_status");

                    id_proofImage.setImageUrl(jObj.getString("id_card_image"), imageLoader);
                    statusText.setText(status);

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("shared_preference_key", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("verification_image", jObj.getString("id_card_image"));
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("Error Response ", error.toString());
                loader.setVisibility(View.INVISIBLE);

                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

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

                        if (error instanceof ServerError) {
                            errorMessage = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                        } else if (error instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        } else if (networkResponse.statusCode == 404) {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "multipart/form-data");
                params.put("Authorization", token);

                return params;
            }

        };
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    private void goToUpdateVerification() {
        Intent intent = new Intent(getApplicationContext(), VerificationUpdateActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        super.onBackPressed();
    }

}
