package org.gstsuvidhakendra.mygsk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.gstsuvidhakendra.mygsk.utils.NetworkInformation;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileInput extends AppCompatActivity {


    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_input);
    }

    public void checkInternet(View view){
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
            setMobile();
    }

    public void setMobile(){
        EditText mobileNumberView = findViewById(R.id.editText3);
        String mobileNumber = mobileNumberView.getText().toString();

        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();

        String url = "https://crm.gstsuvidhakendra.org.in/app_leads.php?mobile="+mobileNumber+"&action=send";

        /**/
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(this, "Receiving data from app...", Toast.LENGTH_SHORT).show();
                try {
                    Log.d("myapp", "The response is" + response.getString("type"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });

        if(mobileNumber.isEmpty()){
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("mobileNumber",mobileNumber);
            editor.apply();

            requestQueue.add(jsonObjectRequest);
            Intent intent = new Intent(this, OtpInput.class);
            startActivity(intent);
        }
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this, MobileInput.class);
//        startActivity(intent);
//        finish();
//
//    }

}
