package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.gstsuvidhakendra.mygsk.utils.NetworkInformation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GskList extends AppCompatActivity {
    ProgressBar roundLoader;
    SwipeRefreshLayout swipeRefreshLayout;

    String leadPincode;
    String leadMobileNumber;
    String leadName;
    //    private RecyclerView gskListRecyclerView;
//    private GskListAdapter gAdapter;
//     ArrayList<GetGskAddressListdata> gskList = new ArrayList<GetGskAddressListdata>();
    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsk_list);
        /**/
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshlayout);
//        gskListRecyclerView = findViewById(R.id.gskListrecyclerview);

        GskList.this.setTitle("Gsk Locations");

        roundLoader = findViewById(R.id.progressBar);
        SharedPreferences getShared = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        leadPincode = getShared.getString("pincode", "");
        leadName = getShared.getString("leadName", "null");
        leadMobileNumber = getShared.getString("mobileNumber", "");
        //final String leadPincode="201301";
        /**/
        TextView pincodeView = findViewById(R.id.pincodeView);
        pincodeView.setText(leadPincode);

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else {
            getLocation();
        }

//       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//           @Override
//           public void onRefresh() {
//               getLocation();
//               swipeRefreshLayout.setRefreshing(false);
//               swipeRefreshLayout.canChildScrollUp();
//           }
//       });


    }


    public void getLocation() {


        String url = "https://gstsuvidhakendra.org.in/api/get_gsk.php?pincode=" + leadPincode + "";

        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(this);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                roundLoader.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                try {
                    Log.d("myapp", "The response is " + response.toString());
                    final LinearLayout lm = findViewById(R.id.container_main);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 30, 0, 0);

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);
                        Log.d("myapp", "loop " + i);

                        String gskId = jresponse.getString("ID");
                        String gskCompany = jresponse.getString("billing_company");
                        String gskPhone = jresponse.getString("billing_phone");
                        String gskAddress1 = jresponse.getString("billing_address_1");
                        String gskAddress2 = jresponse.getString("billing_address_2");
                        String gskState = jresponse.getString("billing_state");
                        String gskCity = jresponse.getString("billing_city");
                        String gskPostcode = jresponse.getString("billing_postcode");

                        //showGskList(gskCompany,gskAddress1);
                        if (!gskCity.isEmpty()) {
                            LinearLayout ll = new LinearLayout(GskList.this);
                            //ll.setClickable(true);

                            ll.setOrientation(LinearLayout.VERTICAL);
                            CardView cv = new CardView(GskList.this);
                            //cv.setLayoutParams(new CardView.LayoutParams(
                            //CardView.LayoutParams.MATCH_PARENT, 150));

                            ll.addView(cv);

                            LinearLayout ll2 = new LinearLayout(GskList.this);
                            ll2.setOrientation(LinearLayout.VERTICAL);
                            cv.addView(ll2);

                            float density = GskList.this.getResources().getDisplayMetrics().density;
                            int paddingDp = 20;
                            int paddingPixel = (int) (paddingDp * density);

                            TextView gskAddressView1 = new TextView(GskList.this);
                            gskAddressView1.setPadding(15, paddingPixel, 10, 0);
                            gskAddressView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            gskAddressView1.setText(gskAddress1);
                            ll2.addView(gskAddressView1);

                            //paddingDp += 20;
                            paddingPixel = (int) (paddingDp * density);

                            if (!gskAddress2.isEmpty()) {
                                TextView gskAddressView2 = new TextView(GskList.this);
                                gskAddressView2.setPadding(15, paddingPixel, 10, 0);
                                gskAddressView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                gskAddressView2.setText(gskAddress2);
                                ll2.addView(gskAddressView2);
                            }
                            paddingDp = 10;
                            paddingPixel = (int) (paddingDp * density);

                            TextView gskAddressView3 = new TextView(GskList.this);
                            gskAddressView3.setPadding(15, 0, 10, paddingPixel);
                            gskAddressView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            gskAddressView3.setText(gskCity);
                            ll2.addView(gskAddressView3);
                            ll2.setPadding(15, paddingPixel, 10, paddingPixel);
                            ll.setLayoutParams(params);
                            ll.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    assignGsk(v, leadName, leadPincode, leadMobileNumber);
                                }
                            });
                            ll.setContentDescription(gskId + "<!>" + gskCompany + "<!>" + gskPhone + "<!>" + gskAddress1 + ", " + gskAddress2 + ", " + gskCity + ", " + gskState + ", " + gskPostcode);
                            lm.addView(ll);
                        }
                    }
                } catch (JSONException e) {
                    roundLoader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                roundLoader.setVisibility(View.GONE);
            }
        });
        roundLoader.setVisibility(View.VISIBLE);
        requestQueue.add(jsonObjectRequest);
    }

    public void assignGsk(View v, String leadName, String leadPincode, String leadMobileNumber) {
        //Toast.makeText(GskList.this, "Hello", Toast.LENGTH_LONG).show();
        final String userGskData = (String) v.getContentDescription();
        final String[] userGskDataArray = userGskData.split("<!>");
        final String userGskId = userGskDataArray[0];

        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.putString("GskCompany", userGskDataArray[1]);
        editor.putString("GskPhone", userGskDataArray[2]);
        editor.putString("GSkAddress", userGskDataArray[3]);
        editor.apply();
        //Toast.makeText(this, userGskData, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, leadMobileNumber, Toast.LENGTH_LONG).show();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://gstsuvidhakendra.org.in/api/mygsk_lead.php?assigned_gsk=" + userGskId + "&lead_mobile=" + leadMobileNumber + "&lead_action=update";
        Log.d("myapp", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("result").equals("successful")) {
                        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shrd.edit();

                        editor.putString("lead_status", "assigned");
                        editor.apply();
                        //Toast.makeText(GskList.this, userGskId+" GSK assigned "+leadMobileNumber, Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(GskList.this, "GSK not assigned", Toast.LENGTH_LONG).show();
                    }
                    //Log.d("myapp", "The response is" + response.getString("title"));
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                roundLoader.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(GskList.this, InnerMainScreen.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                roundLoader.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("myapp", "Something went wrong");
            }
        });
        roundLoader.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        requestQueue.add(jsonObjectRequest);
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

    /*public void showGskList(String gskCompany,String gskAddress1){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView gskCompanyView = new TextView(this);
        gskCompanyView.setText(gskCompany);
        ll.addView(gskCompanyView);

        TextView gskAddressView = new TextView(this);
        gskAddressView.setText(gskAddress1);
        ll.addView(gskAddressView);

        this.lm.addView(ll);
        //ll = null;
    }*/

  /*  private void buildRecyclerView() {

        // initializing our adapter class.
        gAdapter = new GskListAdapter(this, gskList );
        LinearLayoutManager manager = new LinearLayoutManager(this);
        gskListRecyclerView.setHasFixedSize(true);
        gskListRecyclerView.setLayoutManager(manager);
        gskListRecyclerView.setAdapter(gAdapter);


        gskListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, gskListRecyclerView ,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        assignGsk(view, leadName, leadPincode, leadMobileNumber);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
*/


   /* public void getLocation(){


        String url = "https://gstsuvidhakendra.org.in/api/get_gsk.php?pincode="+leadPincode;

        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(this);


        JsonArrayRequest strRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    // loop through each json object
                    roundLoader.setVisibility(View.GONE);

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject data = (JSONObject)response.get(i);

                        int id = data.getInt("ID");
                        String billing_phone = data.getString("billing_phone");
                        String billing_company = data.getString("billing_company");
                        String address1 = data.getString("billing_address_1");
                        String address2 = data.getString("billing_address_2");
                        String state = data.getString("billing_state");
                        String city = data.getString("billing_city");
                        String postCode = data.getString("billing_postcode");
                        try {
                            gskList.add(new GetGskAddressListdata(id,billing_phone,billing_company,address1, address2, state, city,postCode));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        buildRecyclerView();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                              e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Error: " + error,
                        Toast.LENGTH_LONG).show();
                roundLoader.setVisibility(View.GONE);
            }
        });
        roundLoader.setVisibility(View.VISIBLE);
        requestQueue.add(strRequest);
    }*/

}
