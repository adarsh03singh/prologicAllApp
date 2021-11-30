package com.csestateconnect.www.csconnect;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateOtpFragment extends Fragment {
    private String otp;
    private String pk;
    private String phonenumber;
    String token;
    String staff;
    Button otp_verify_button;
    TextView validate_otp_resend_button;
    TextView validate_otp_timer_text;
    TextView not_recieved_yet_text;
    ConstraintLayout loader;

    public Pattern otpPattern = Pattern.compile("(|^)\\d{6}");

    NetworkInformation networkInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        pk = getArguments().getString("pk");
        phonenumber = getArguments().getString("phone_number");
        setPhoneNumberInSharedPrefrences(phonenumber);
        return inflater.inflate(R.layout.fragment_validateotp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }

        otp_verify_button = getView().findViewById(R.id.validate_otp_verify_Button);
        validate_otp_resend_button = getView().findViewById(R.id.validate_otp_resend_textButton);
        not_recieved_yet_text = getView().findViewById(R.id.not_recieved_yet_text);
        loader = getView().findViewById(R.id.progress_bar_layout);
        buttonVisibility();

        otp_verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });

        checkAndRequestMessagePermissions();
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
                    getFragmentManager().beginTransaction().detach(ValidateOtpFragment.this)
                            .attach(ValidateOtpFragment.this).commit();
                }

            }
        });
        builder.show();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                EditText otpTextField = getView().findViewById(R.id.validate_otp_textView);

                if (message != null) {
                    Matcher m = otpPattern.matcher(message);
                    if (m.find()) {
                        otpTextField.setText(m.group(0));

                        Button verifyButton = (Button) getView().findViewById(R.id.validate_otp_verify_Button);
                        verifyButton.performClick();

                    }
                }
            }
        }
    };

    private boolean checkAndRequestMessagePermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        }
        return true;
    }


    public void buttonVisibility() {
        validate_otp_resend_button.setVisibility(View.GONE);
        not_recieved_yet_text.setVisibility(View.GONE);
        startTimer(30000, 1000);
    }


    public void startTimer(final Integer milliseconds, final Integer interval) {
        validate_otp_timer_text = getView().findViewById(R.id.validate_otp_timer_textView);
        new CountDownTimer(milliseconds, interval) {
            public void onTick(long millisUntilFinished) {
                validate_otp_timer_text.setText("00:" + millisUntilFinished / interval);
            }

            public void onFinish() {
                validate_otp_timer_text.setText("00:00");
                validate_otp_resend_button.setVisibility(View.VISIBLE);
                not_recieved_yet_text.setVisibility(View.VISIBLE);
                validate_otp_resend_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendOTP();
                        validate_otp_resend_button.setVisibility(View.INVISIBLE);
                        not_recieved_yet_text.setVisibility(View.INVISIBLE);
                        startTimer(milliseconds, interval);
                        loader.setVisibility(View.VISIBLE);
                    }
                });
            }

        }.start();
    }


    public void getToken() {
        EditText validate_otp_text = getView().findViewById(R.id.validate_otp_textView);

        otp = validate_otp_text.getText().toString();

        loader.setVisibility(View.VISIBLE);

        if (otp.length() != 6) {
            loader.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_validateotp_root), "Invalid OTP", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
            snackbar.show();
        } else {
            final JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("pk", pk);
                requestBody.put("otp", otp);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String validate_otp_url = getString(R.string.api_domain) + getString(R.string.validate_otp_api_path);
            JsonObjectRequest validate_otp_request = new JsonObjectRequest(
                    Request.Method.POST, validate_otp_url, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        try {
                            boolean staff = Boolean.parseBoolean(response.getString("staff"));

                            if (!staff == true) {
                                token = response.getString("token");
                                setTokenInSharedPrefrences(token);
                                loader.setVisibility(View.INVISIBLE);
                                goToMainActivity(response.getBoolean("is_existing_user"));
                            } else {
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "Number Already Registerd", Toast.LENGTH_LONG).show();
                                goToGenerateOtpFragment();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    loader.setVisibility(View.INVISIBLE);
                    String errorMessage = "Unknown error";

                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

                    } else if (error.networkResponse.statusCode == 403) {
                        try {
                            String error_message = new JSONObject(new String(error.networkResponse.data))
                                    .getString("error");
                            showError(error_message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            SingletonQueue.getInstance(getActivity()).addToRequestQueue(validate_otp_request);
        }

    }

    public void resendOTP() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("phone_number", phonenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String generate_otp_url = getString(R.string.api_domain) + getString(R.string.generate_otp_api_path);
        JsonObjectRequest generate_otp_request = new JsonObjectRequest(
                Request.Method.POST, generate_otp_url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pk = response.getString("pk");
                    loader.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
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
                } else if (error instanceof ServerError) {
                    errorMessage = "The server could not be found. Please try again after some time!!";
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
                } else if (error.networkResponse.statusCode == 400) {
                    try {
                        String error_message = new JSONObject(new String(error.networkResponse.data))
                                .getJSONArray("phone_number").getString(0);
                        showError(error_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pk", pk);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(generate_otp_request);
    }

    public void showError(String error_message) {
        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_validateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
    }


    public void setTokenInSharedPrefrences(String TokenString) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.token_data_key), "Token " + TokenString);
        editor.commit();
    }

    public void setPhoneNumberInSharedPrefrences(String PhoneString) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.phonenumber_data), PhoneString);
        editor.commit();
    }

    public void goToMainActivity(Boolean isExistingUser) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("is_existing_user", isExistingUser);
        startActivity(intent);
        getActivity().finish();
    }

    public void goToGenerateOtpFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        ft.replace(R.id.splash_fragment_container, new GenerateOtpFragment());
        ft.commit();
    }


    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
}