package com.csestateconnect.www.csconnect;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.csestateconnect.www.csconnect.models.interaction_dates.DateList;
import com.csestateconnect.www.csconnect.models.lead.GetInteractionDate;
import com.csestateconnect.www.csconnect.models.lead.Lead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateLeadInteractionDatesFragment extends Fragment {

    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    String token;
    ConstraintLayout loader;
    EditText dateView;
    Lead lead;
    String leadId;

    Button submitButton;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String date_time = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            drawerInterface = (NavigationDrawerToggle) context;
            getValuesFromSharedPreferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerInterface.lockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false);
        return inflater.inflate(R.layout.fragment_create_lead_interaction_dates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getIdforTextAndButton();
        getdata();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInteractionDatesFragment();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInteractionDates();
                submitButton.setClickable(false);
            }
        });
        dateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
    }

    private void datePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth ;
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        dateView.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void getIdforTextAndButton() {
        loader = getView().findViewById(R.id.progress_bar_layout);
        back_button = (Button) getView().findViewById(R.id.left_side_bar_button);
        submitButton = (Button) getView().findViewById(R.id.createDates_save_button);
        dateView = (EditText) getView().findViewById(R.id.create_activity_date_txt);
    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);

    }


    public void createInteractionDates() {
        loader.setVisibility(View.VISIBLE);
        String URL_FOR_CREATE_INTERACTION_DATES = getString(R.string.api_domain) + getString(R.string.create_interaction_dates_api_path);
        JSONObject json1 = new JSONObject();

        try {
            json1.put("lead", leadId);
            json1.put("date", dateView.getText().toString());

//            goToLeadsFragment();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_FOR_CREATE_INTERACTION_DATES, json1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loader.setVisibility(View.INVISIBLE);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.INVISIBLE);
                submitButton.setClickable(true);
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", token);
                return headers;
            }

        };
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

    }


    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

//    private void goToLeadsFragment(){
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
//        ft.replace(R.id.main_acitivity_container, new LeadFragment());
//        ft.commit();
//    }

    private void goToInteractionDatesFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        Bundle bundle = new Bundle();
        bundle.putParcelable("lead_data", lead);
        InteractionDateFragment interactionDateFragment = new InteractionDateFragment();
        interactionDateFragment.setArguments(bundle);
        ft.replace(R.id.main_acitivity_container, interactionDateFragment);
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
    }

    public  void getdata() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lead = bundle.getParcelable("lead_data");

          leadId =(lead.getId().toString());
        }
    }

}