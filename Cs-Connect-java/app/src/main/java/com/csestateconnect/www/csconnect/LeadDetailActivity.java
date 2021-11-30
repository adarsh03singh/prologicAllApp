package com.csestateconnect.www.csconnect;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class LeadDetailActivity extends AppCompatActivity {

    private NavigationDrawerToggle drawerInterface;
    private String leadStatusSpinerValue;
    private ConstraintLayout loader;
    private String interactionDate;
    private String token;
    private String id;
    private String URL_FOR_GET_PROJECT;
    private Integer dealId;
    private String errorMessage;
    private Lead lead;
    private String getIdByCreateLead;

    private TextView leadId;
    private TextView leadDate;
//    private TextView leadStatus;
    private TextView leadname;
    private TextView leadBudget;
    private TextView leadLocation;
    private TextView emailView;
    private TextView phoneNumberView;

    private Button back_button;
    private Button interactionDateButton;
    private Button leadActivityButton;
    private Button leadFeedbackbutton;
    private Button dealButton;
    private Spinner statusSpinnerView;
NetworkInformation networkInformation;

    JSONArray status;
    Integer slectedStatusId = 0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lead_detail);

        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        getStatusValuesFromSharedPreferences();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLeadFragment();
            }
        });
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + emailView.getText().toString());
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


    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onPostResume() {
        super.onPostResume();
        getLeadData();
        getLeadDetail();

        interactionDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInteractionDateFragment();
                interactionDateButton.setClickable(false);
            }
        });

        leadFeedbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLeadFeedbackActivity();
                leadFeedbackbutton.setClickable(false);
            }
        });

        leadActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLeadActivities();
                leadActivityButton.setClickable(false);
            }
        });

        try {
            if (dealId == 0) {

                dealButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToCreateDealFragment();
                        dealButton.setClickable(false);
                    }
                });
            } else if (dealId != 0) {
                dealButton.setBackgroundColor(R.color.colorPrimary);
                dealButton.setText("Go To Deal");
                dealButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDealDetailFragment();
                        dealButton.setClickable(false);
                    }
                });
            } else
                dealButton.setBackgroundColor(R.color.colorButton);
            dealButton.setClickable(true);

        } catch (NullPointerException e) {
            e.printStackTrace();
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
    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        statusSpinnerView = findViewById(R.id.lead_status_spinner);
        dealButton = (Button) findViewById(R.id.deal_button);
        back_button = findViewById(R.id.left_side_bar_button);
        interactionDateButton = findViewById(R.id.interaction_date_button);
        leadActivityButton = findViewById(R.id.lead_activity_button);
        leadFeedbackbutton = findViewById(R.id.lead_feedback_button);
        emailView = (TextView) findViewById(R.id.lead_email);
        leadId = (TextView) findViewById(R.id.lead_id);
        leadDate = (TextView) findViewById(R.id.lead_created_date);
        leadname = (TextView) findViewById(R.id.lead_name);
        leadBudget = (TextView) findViewById(R.id.lead_budget);
        leadLocation = (TextView) findViewById(R.id.lead_location);
        phoneNumberView = (TextView) findViewById(R.id.lead_phone_number);
    }

    public void getStatusValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String status_array_string = (sharedPref.getString("statusCode", null));

        try {
            status = new JSONArray(status_array_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateLeadStatus() {
        String GET_LEAD_STATUS_URL = getString(R.string.api_domain) + getString(R.string.get_feedback_api_path) + leadId.getText() + "/";
        JSONObject json1 = new JSONObject();

        try {

            json1.put("lead_status", slectedStatusId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PATCH, GET_LEAD_STATUS_URL, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");

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
                }
            }
        }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }


        };
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

    }

    private void goToLeadFragment() {
        Intent intent = new Intent(getApplicationContext(), LeadActivity.class);
//        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void goToInteractionDateFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        Bundle bundle = new Bundle();
        bundle.putParcelable("lead_data", lead);
        InteractionDateFragment interactionDateFragment = new InteractionDateFragment();
        interactionDateFragment.setArguments(bundle);
        ft.replace(R.id.main_acitivity_container, interactionDateFragment);
        ft.commit();
    }

    private void goToLeadActivities() {
        Intent intent = new Intent(getApplicationContext(), LeadInteractionActivitiesActivity.class);
        intent.putExtra("leadIdbyLeadAdapter", id);
        startActivity(intent);
    }

    private void goToLeadFeedbackActivity() {
        Intent intent = new Intent(getApplicationContext(), LeadFeedbackActivity.class);
        intent.putExtra("leadIdbyLeadAdapter", id);
        intent.putExtra("dealIdbyLeadAdapter", dealId);
        startActivity(intent);
        finish();
    }

    private void goToCreateDealFragment() {
        Intent intent = new Intent(getApplicationContext(), CreateDealsActivity.class);
        intent.putExtra("leadIdbyLeadAdapter", id);
        startActivity(intent);
        finish();
    }

    private void goToDealDetailFragment() {
        Intent intent = new Intent(getApplicationContext(), DealDetailActivity.class);
        intent.putExtra("dealIdbyLeadAdapter", dealId);
        startActivity(intent);
//        finish();
    }


    private void getLeadDetail() {

        loader.setVisibility(View.VISIBLE);
        if (id != null) {
            URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.leads_list_path) + "/" + id;
        } else
            URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.leads_list_path) + "/" + getIdByCreateLead;
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_PROJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject allData = new JSONObject(response);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Lead>() {
                    }.getType();
                    lead = gson.fromJson(allData.toString(), type);

                    SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
                    Date date = null;
                    try {
                        date = inputFormatter.parse(lead.getCreatedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String leadidView = lead.getId();
                    String name = lead.getName();
                    String email = lead.getEmail();
                    String mobile = lead.getPhoneNumber();
                    String statuses = lead.getLeadStatus().getName();

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

                    statusSpinnerView.setAdapter(statusAdapter);
                    for(int i=0; i< statusSpinnerNameArray.size(); i++){
                        if( statusSpinnerNameArray.get(i).equals(statuses)){
                            statusSpinnerView.setSelection(i);
                        }
                    }

                    statusSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            slectedStatusId = statusSpinnerIdArray.get(position);
                            if (position > 0) {

                                updateLeadStatus();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    String budget = lead.getBudget();
                    String locationInfo = lead.getLocation().getName() + ", " + lead.getCity().getName();

                    leadId.setText(leadidView);
                    leadDate.setText(outputFormatter.format(date));
                    leadname.setText(name);
                    emailView.setText(email);
                    phoneNumberView.setText(mobile);
                    leadBudget.setText(budget);
                    leadLocation.setText(locationInfo);

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
                    } else  if (error.networkResponse.statusCode == 403) {
                    try {
                        errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeTokenFromSharedPreferences();//will be go SplashActivity
                }
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


    public void getLeadData() {
        final Intent myintent = getIntent();
        if (null != myintent.getExtras()) {

            try {
                id = myintent.getExtras().getString("leadIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                dealId = myintent.getExtras().getInt("dealIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                getIdByCreateLead = myintent.getExtras().getString("leadid");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LeadActivity.class);
        finish();
        super.onBackPressed();
    }


}
