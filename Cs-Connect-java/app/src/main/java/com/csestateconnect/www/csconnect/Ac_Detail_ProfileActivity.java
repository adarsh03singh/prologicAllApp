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
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Ac_Detail_ProfileActivity extends AppCompatActivity {

    ImageLoader imageLoader = SingletonQueue.getInstance(getBaseContext()).getImageLoader();
    Button back_button;
    Button submitButton;
    String token;
    Integer status_code;
    String errorMessage;

    ImageView pancardImageView;
    ImageView cancelChequeImageview;
    ConstraintLayout loader;
    TextView userName;
    TextView user_ac_number;
    TextView user_bankName;
    TextView user_ifsc_code;
    TextView user_pancardNumber;
    TextView user_adderss;
    TextView user_verified;
    TextView user_verifiedStatus;
    TextView user_StatusReason;

    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac__detail__profile);
        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else


            getValuesFromSharedPreferences();
        accountdetail();

        back_button = findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToAc_Detail_updateFragment();
                submitButton.setClickable(false);
            }
        });
    }

    public void getIdforTextAndButton() {

        loader = findViewById(R.id.progress_bar_layout);
        submitButton = (Button) findViewById(R.id.ac_detail_profile_updateAccount_button);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
        userName = (TextView) findViewById(R.id.ac_detail_profile_user_name);
        user_ac_number = (TextView) findViewById(R.id.ac_detail_profile_ac_number);
        user_bankName = (TextView) findViewById(R.id.ac_detail_profile_bank_name);
        user_ifsc_code = (TextView) findViewById(R.id.ac_detail_profile_ifsc_code);
        user_pancardNumber = (TextView) findViewById(R.id.ac_detail_profile_panCard_number);
        user_adderss = (TextView) findViewById(R.id.ac_detail_profile_address);
        user_verified = (TextView) findViewById(R.id.ac_detail_profile_verified);
        user_verifiedStatus = (TextView) findViewById(R.id.ac_detail_profile_verifiedStatus);
        user_StatusReason = (TextView) findViewById(R.id.ac_detail_profile_status_reason);

        pancardImageView = (ImageView) findViewById(R.id.ac_detail_profile_panCard_image);
        cancelChequeImageview = (ImageView) findViewById(R.id.ac_detail_profile_cancelCheque_image);

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

    public void accountdetail() {

        loader.setVisibility(View.VISIBLE);
        String URL_GET_USER_ACCOUNT_DETAIL = getString(R.string.api_domain) + getString(R.string.account_detail_api_path);
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_GET_USER_ACCOUNT_DETAIL
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response ", response);
                loader.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jObj = new JSONObject(response);

                    String user = jObj.getString("user");
                    String account_holder_name = jObj.getString("account_holder_name");
                    String account_number = jObj.getString("account_number");
                    String address = jObj.getString("address");
                    String ifsc_code = jObj.getString("ifsc_code");
                    String pan_card_number = jObj.getString("pan_card_number");
                    String verified = jObj.getString("verified");
                    String verified_status = jObj.getString("account_status");
                    String status_reason = jObj.getString("status_reason");
                    String bank_name = jObj.getString("bank_name");


                    userName.setText(account_holder_name);
                    user_ac_number.setText(account_number);
                    user_bankName.setText(bank_name);
                    user_ifsc_code.setText(ifsc_code);
                    user_pancardNumber.setText(pan_card_number);
                    user_adderss.setText(address);
                    Glide.with(getApplicationContext()).load(jObj.getString("pan_card_image"))
                            .into(pancardImageView);
                    if (pancardImageView != null) {
                        pancardImageView.setBackground(null);
                    }

                    Glide.with(getApplicationContext()).load(jObj.getString("canceled_cheque_image"))
                            .into(cancelChequeImageview);
                    if (cancelChequeImageview != null) {
                        cancelChequeImageview.setBackground(null);
                    }

                    user_verified.setText(verified);
                    user_verifiedStatus.setText(verified_status);
                    user_StatusReason.setText(status_reason);

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("shared_preference_key", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("account_holder_name", userName.getText().toString());
                    editor.putString("account_number", user_ac_number.getText().toString());
                    editor.putString("bank_name", user_bankName.getText().toString());
                    editor.putString("ifsc_code", user_ifsc_code.getText().toString());
                    editor.putString("pan_card_number", user_pancardNumber.getText().toString());
                    editor.putString("address", user_adderss.getText().toString());
                    editor.putString("pan_card_image", jObj.getString("pan_card_image"));
                    editor.putString("cancel_cheque_image", jObj.getString("canceled_cheque_image"));

                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response ", error.toString());
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

                try {
                    if (error.networkResponse.statusCode == 403) {
                        errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        removeTokenFromSharedPreferences();//will be go SplashActivity
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
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

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    private void goToAc_DetailActivity() {
        Intent intent = new Intent(getApplicationContext(), Ac_DetailActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToAc_Detail_updateFragment() {
        Intent intent = new Intent(getApplicationContext(), Ac_Detail_UpdateActivity.class);
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
