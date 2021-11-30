package org.gstsuvidhakendra.mygsk;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GskListScroll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsk_list_scroll);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String url = "https://gstsuvidhakendra.org.in/api/get_gsk.php?pincode=110032";
        /**/
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(this, "Receiving data from app...", Toast.LENGTH_SHORT).show();
                //Log.d("myapp", "The response is" );
                //Log.d("myapp", "The response is " + response.toString());
                try {
                    //Log.d("myapp", "The response is " + response.getString(0));
                    //Log.d("myapp", "The response is " + response.length());
                    for(int i = 0; i < response.length(); i++){
                        JSONObject jresponse = response.getJSONObject(i);
                        Log.d("myapp", "The response is " + jresponse.toString());
                        String gskId = jresponse.getString("ID");
                        String gskCompany = jresponse.getString("billing_company");
                        String gskPhone = jresponse.getString("billing_postcode");
                        String gskAddress1 = jresponse.getString("billing_address_1");
                        String gskAddress2 = jresponse.getString("billing_address_2");
                        String gskState = jresponse.getString("billing_state");
                        String gskCity = jresponse.getString("billing_city");
                        String gskPostcode = jresponse.getString("billing_postcode");




                    }
                    //Log.d("myapp", "The response is" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", String.valueOf(error));
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}
