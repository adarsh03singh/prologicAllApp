package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.csestateconnect.www.csconnect.models.lead.Lead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateDealsActivity extends AppCompatActivity {

    private Button back_button;
    private Button support_issue_button;
    private String token;
    private Integer dealId;
    private String errorMessage;
    private ConstraintLayout loader;
    private Integer slectedStatusId = 0;
    private Integer slectedProjectId = 0;
    private Spinner dealStatusSpinner;
    private Spinner projectSpinner;
    private EditText totalcommisionText;
    private EditText paidCommissiontext;
    private EditText unpaidCommissiontext;
    private ImageView imageView;
    private Button submitButton;
    private String leadId;

    Lead lead;
    JSONArray status;
    JSONArray projectNameLIst;
NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deals);
        getValuesFromSharedPreferences();
        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        getLeadData();
        getStatusValue();
        getProjectData();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
                intent.putExtra("leadIdbyLeadAdapter",leadId);
                startActivity(intent);
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeals();
                submitButton.setClickable(false);
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

    public void getIdforTextAndButton() {
        loader = findViewById(R.id.progress_bar_layout);
        back_button = (Button) findViewById(R.id.left_side_bar_button);
        projectSpinner = (Spinner) findViewById(R.id.deal_project_name_Spinner);
        totalcommisionText = (EditText) findViewById(R.id.deal_total_commision);
        paidCommissiontext = (EditText) findViewById(R.id.deal_paid_commission);
        unpaidCommissiontext = (EditText) findViewById(R.id.deal_unpaid_commission);
        dealStatusSpinner = (Spinner) findViewById(R.id.deal_status_spinner);
        submitButton = (Button) findViewById(R.id.deal_submit_button);
        imageView = (ImageView) findViewById(R.id.deal_cheque_image);
    }

    public void showError(String error_message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fragment_validateotp_root), error_message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.snakbar_error_background));
        snackbar.show();
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        String status_array_string = (sharedPref.getString("dealStatusCode", null));
        String projectList_array_string = (sharedPref.getString("projectDetails", null));

        try {
            status = new JSONArray(status_array_string);
            projectNameLIst = new JSONArray(projectList_array_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createDeals() {
        loader.setVisibility(View.VISIBLE);
        String URL_FOR_CREATE_DEALS = getString(R.string.api_domain) + getString(R.string.create_deal_api_path);
        JSONObject json1 = new JSONObject();

        try {

            json1.put("lead", leadId);
            json1.put("deal_status", slectedStatusId);
            json1.put("project", slectedProjectId);
            json1.put("commission_amount_total_view", totalcommisionText.getText());
            json1.put("commission_amount_paid_view", paidCommissiontext.getText());
            json1.put("commission_amount_unpaid_view", unpaidCommissiontext.getText());



        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_FOR_CREATE_DEALS, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        Toast.makeText(getApplicationContext(), "Successfully Created", Toast.LENGTH_SHORT).show();

                        try {
                           dealId = (Integer) response.get("id");

                            Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
                            intent.putExtra("leadIdbyLeadAdapter",leadId);
                            intent.putExtra("dealIdbyLeadAdapter",dealId);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loader.setVisibility(View.INVISIBLE);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.INVISIBLE);
                submitButton.setClickable(true);

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

    public void getStatusValue() {
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
                android.R.layout.simple_spinner_dropdown_item,
                statusSpinnerNameArray
        );

        dealStatusSpinner.setAdapter(statusAdapter);

        for(int i=0; i< statusSpinnerNameArray.size(); i++){
            if( statusSpinnerNameArray.get(i).equals("Open")){
                dealStatusSpinner.setSelection(i);
            }
        }

        dealStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slectedStatusId = statusSpinnerIdArray.get(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getProjectData() {
        final List<String> projectSpinnerNameArray = new ArrayList<>();
        final List<Integer> projectSpinnerIdArray = new ArrayList<>();

        for (Integer index_of_project = 0; index_of_project < projectNameLIst.length(); index_of_project++) {
            try {
                projectSpinnerNameArray.add(new JSONObject(projectNameLIst.get(index_of_project).toString()).getString("name"));
                projectSpinnerIdArray.add(new JSONObject(projectNameLIst.get(index_of_project).toString()).getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                projectSpinnerNameArray
        );

        projectSpinner.setAdapter(projectAdapter);

        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slectedProjectId = projectSpinnerIdArray.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void getLeadData() {
        final Intent myintent = getIntent();
        if (null != myintent.getExtras()) {

            try {
                leadId = myintent.getExtras().getString("leadIdbyLeadAdapter");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LeadDetailActivity.class);
        intent.putExtra("leadIdbyLeadAdapter",leadId);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}
