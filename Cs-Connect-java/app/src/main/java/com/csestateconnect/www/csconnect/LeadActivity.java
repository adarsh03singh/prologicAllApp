package com.csestateconnect.www.csconnect;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.service.autofill.FieldClassification;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.csestateconnect.www.csconnect.adapters.LeadListAdapter;
import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LeadActivity extends AppCompatActivity {

    String token;
    TextView noLeadsTxt;
    ConstraintLayout loader;
    BottomSheetDialog bottomSheetDialog;

    ProgressBar loadProgressBar;
    private LinkedList<Lead> tempLeadList;

    private String nextPage;
    String URL_FOR_GET_PROFILE;
    private boolean isScrolling = false;


    private LinkedList<Lead> mLeadList;

    private RecyclerView mRecyclerView;
    private LeadListAdapter mAdapter;

    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;

    private NavigationDrawerToggle drawerInterface;
    private Button back_button;
    private Button filter_button;
    private Button cancel_button;
    private Button apply_button;
    private EditText date_text_filter1;
    private boolean isLoading;
    private EditText date_text_filter2;
    private Integer position = 0;
    private int currentItem, totalItem, pastVisibleItems;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String date_time = "";

    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        getBttomSheet();


        loadProgressBar = (ProgressBar) findViewById(R.id.lead_progress_bar);
        loader = findViewById(R.id.progress_bar_layout);

        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }else

        noLeadsTxt = findViewById(R.id.noLeads_text);
        scrollView();

        filter_button = findViewById(R.id.lead_filter_button);
        back_button = findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });
        date_text_filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(1);

            }
        });
        date_text_filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(2);

            }
        });

        getValuesFromSharedPreferences();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getLeads(null);
    }

    private void datePicker(final Integer position) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        //*************Call Time Picker Here ********************

                        if(position == 1) {
                            date_text_filter1.setText(date_time);
                        }else if(position == 2) {
                            date_text_filter2.setText(date_time);
                        }
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    @Override
    public void onStop() {
        super.onStop();
        SingletonQueue.getInstance(getApplicationContext()).getRequestQueue().cancelAll(LeadActivity.class);
    }

    public void getBttomSheet() {
        final View parentView = getLayoutInflater().inflate(R.layout.lead_filter_item, null);
        bottomSheetDialog = new BottomSheetDialog(LeadActivity.this);
        bottomSheetDialog.setContentView(parentView);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        cancel_button = parentView.findViewById(R.id.bottomsheet_cancel_button);
        date_text_filter1 = parentView.findViewById(R.id.bottsheet_created_date1);
        date_text_filter2 = parentView.findViewById(R.id.bottsheet_created_date2);
        apply_button = parentView.findViewById(R.id.bottomsheet_apply_button);
    }

    private void getLeads(final String filter) {

        if(filter != null) {
            loader.setVisibility(View.VISIBLE);
                URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.filterleads_bycreated_date_list_path)+ filter;

            }
            if (filter == null) {
                if (nextPage != null) {
                    loadProgressBar.setVisibility(View.VISIBLE);
                    URL_FOR_GET_PROFILE = nextPage;
                } else {
                    loader.setVisibility(View.VISIBLE);
                    URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.leads_list_path);
                }
            }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Gson gson = new Gson();

                    Type type = new TypeToken<LinkedList<Lead>>() {
                    }.getType();

                    nextPage = jObj.getString("next");

                    if(filter != null) {
                        mLeadList = gson.fromJson(jObj.getString("results"), type);
                        mAdapter = new LeadListAdapter(LeadActivity.this, mLeadList);
                        mRecyclerView.setAdapter(mAdapter);
                        loader.setVisibility(View.INVISIBLE);
                    }
                       else if (mLeadList == null) {

                            mLeadList = gson.fromJson(jObj.getString("results"), type);


                            mAdapter = new LeadListAdapter(LeadActivity.this, mLeadList);

                            mRecyclerView.setAdapter(mAdapter);

                            if (mAdapter.getItemCount() == 0) {
                                noLeadsTxt.setVisibility(View.VISIBLE);
                            } else
                                noLeadsTxt.setVisibility(View.GONE);

                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                            loader.setVisibility(View.INVISIBLE);

                        } else {
                            tempLeadList = gson.fromJson(jObj.getString("results"), type);
                            mLeadList.addAll(tempLeadList);
                            mAdapter.notifyDataSetChanged();
                            loadProgressBar.setVisibility(View.INVISIBLE);
                        }

                        isLoading = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", "Login Error: " + error.getMessage());
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
                    }
                   else if(nextPage == "null") {
            loadProgressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(getApplicationContext(), "No more leads.", Toast.LENGTH_SHORT).show();
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
        strReq.setTag(LeadActivity.class);
        SingletonQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);

    }
    public void fieldValidation() {

        if (date_text_filter1.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter previous date", Toast.LENGTH_SHORT).show();

        } else if (date_text_filter2.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your current date", Toast.LENGTH_SHORT).show();
        }else {
            getLeads(date_text_filter1.getText().toString() + "," + date_text_filter2.getText().toString() );
            bottomSheetDialog.cancel();
        }
    }
    private void scrollView(){
        mRecyclerView = findViewById(R.id.lead_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final LinearLayoutManager mLinearlayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLinearlayoutManager.getItemCount();
                lastVisibleItem = mLinearlayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    getLeads(null);
                    isLoading = true;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
