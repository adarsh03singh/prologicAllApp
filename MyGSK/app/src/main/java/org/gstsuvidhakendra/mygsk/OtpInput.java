package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.gstsuvidhakendra.mygsk.utils.NetworkInformation;
import org.json.JSONException;
import org.json.JSONObject;

public class OtpInput extends AppCompatActivity {
    TextView textView;
    TextView phoneNumberView;
    ProgressBar roundLoader;
    TextView validate_otp_timer_text, resendTextbutton;
    NetworkInformation networkInformation;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_input);
        SharedPreferences getShared = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        final String leadName = getShared.getString("leadName","nO nAME");
        final String leadMobileNumber = getShared.getString("mobileNumber","No mobile number");
        final String leadPincode = getShared.getString("pincode","No pincode");

        roundLoader = findViewById(R.id.progressBar2);
        phoneNumberView = findViewById(R.id.textView6);
        phoneNumberView.setText(leadMobileNumber);
        resendTextbutton = findViewById(R.id.resendTextbutton );
        //textView.setText(value1 + value2 + value3);
        EditText otpNumberView = findViewById(R.id.editText4);
        new MyReceiver().setEditText(otpNumberView);
        buttonVisibility(view);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        roundLoader.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void checkInternet(View view){
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
            putOtp();
    }
    public void putOtp(){
        SharedPreferences getShared = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        final String leadName = getShared.getString("leadName","nO nAME");
        final String leadMobileNumber = getShared.getString("mobileNumber","No mobile number");
        final String leadPincode = getShared.getString("pincode","No pincode");


        EditText otpNumberView = findViewById(R.id.editText4);
        final String otpToVerify = otpNumberView.getText().toString();

        String url = "https://crm.gstsuvidhakendra.org.in/app_leads.php?mobile="+leadMobileNumber+"&action=verify&otp_to_verify="+otpToVerify+"";

        /**/
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Toast.makeText(OtpInput.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("MyGSK", "The response is" + response.getString("type"));
                    Log.d("MyGSK", "The Mobile" + leadMobileNumber);

                    Log.d("MyGSK", "Response type: " + response.getString("message").getClass().getName());

                    final String otp_verified = "otp_verified";
                    final String otp_verified2 = "already_verified";
                    //Log.d("MyGSK", "The URL " + url);

                    if(response.getString("message").equals(otp_verified)||
                            response.getString("message").equals(otp_verified2)){
                        Toast.makeText(OtpInput.this,"OTP verified", Toast.LENGTH_SHORT).show();

                        //Toast.makeText(OtpInput.this, "OTP: "+otpToVerify, Toast.LENGTH_SHORT).show();
                        pushLead(leadName,leadPincode,leadMobileNumber);

                        roundLoader.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Intent intent = new Intent(OtpInput.this, GskList.class);
                        startActivity(intent);
                    }else{
                        roundLoader.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(OtpInput.this, "OTP not verified", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(OtpInput.this, "OTP not verified", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(OtpInput.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    roundLoader.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                roundLoader.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("myapp", "Something went wrong");
            }
        });



        if(otpToVerify.isEmpty()){
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
        }else{
            roundLoader.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            requestQueue.add(jsonObjectRequest);
        }
    }
    public void pushLead(String leadName,String leadPincode,String leadMobileNumber){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://gstsuvidhakendra.org.in/api/mygsk_lead.php?lead_name="+leadName+"&lead_pincode="+leadPincode+"&lead_mobile="+leadMobileNumber+"&lead_action=create";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("result").equals("successful")){
                        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shrd.edit();

                        editor.putString("lead_status", "added");
                        editor.apply();

                        //do something
                    }else{
                        //Toast.makeText(OtpInput.this, "lead not inserted ", Toast.LENGTH_LONG).show();
                    }
                    //Log.d("myapp", "The response is" + response.getString("title"));
                } catch (JSONException e) {
                    Log.d("myapp", "push lead caught error");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    /*public void goToInnerMain(){
        Intent intent = new Intent(this, InnerMainScreen.class);
        startActivity(intent);
    }*/
    public void resendOtp(){
        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        final String leadMobileNumber = shrd.getString("mobileNumber","");
        roundLoader.setVisibility(View.VISIBLE);

        String url = "https://crm.gstsuvidhakendra.org.in/app_leads.php?mobile="+leadMobileNumber+"&action=send";
        Log.d("myapp", "lead mobile" + leadMobileNumber);
        /**/
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(OtpInput.this, "Receiving data from app...", Toast.LENGTH_SHORT).show();
                try {
                    Log.d("myapp", "The response is" + response.getString("type"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                roundLoader.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });
        requestQueue.add(jsonObjectRequest);
        Toast.makeText(this, "Resending OTP", Toast.LENGTH_SHORT).show();
        roundLoader.setVisibility(View.GONE);
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

    public void buttonVisibility(View view) {
        resendTextbutton.setVisibility(View.GONE);
        startTimer(45000, 1000);
    }

    public void startTimer(final Integer milliseconds, final Integer interval) {
        validate_otp_timer_text = findViewById(R.id.validate_otp_timer_textView);
        new CountDownTimer(milliseconds, interval) {
            public void onTick(long millisUntilFinished) {
                validate_otp_timer_text.setText("00:" + millisUntilFinished / interval);
            }

            public void onFinish() {
                validate_otp_timer_text.setText("00:00");
                resendTextbutton.setVisibility(View.VISIBLE);
                resendTextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendOtp();
                        resendTextbutton.setVisibility(View.GONE);
                        startTimer(milliseconds, interval);

                    }
                });
            }

        }.start();
    }

}
