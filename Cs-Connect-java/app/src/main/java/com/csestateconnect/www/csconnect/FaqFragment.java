package com.csestateconnect.www.csconnect;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import com.csestateconnect.www.csconnect.adapters.FaqExpandableListViewAdapter;
import com.csestateconnect.www.csconnect.models.faq.Faq;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;


public class FaqFragment extends Fragment {


    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    Button support_issue_button;

    ExpandableListView expandableListView;

    ConstraintLayout loader;

    private LinkedList<Faq> mFaqList;
    private FaqExpandableListViewAdapter mAdapter;
NetworkInformation networkInformation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            drawerInterface = (NavigationDrawerToggle) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerInterface.lockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false);
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        loader = getView().findViewById(R.id.progress_bar_layout);
        back_button = getView().findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        expandableListView = getActivity().findViewById(R.id.faqExpandableListView);
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getFaqs();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
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
                    getFragmentManager().beginTransaction().detach(FaqFragment.this).attach(FaqFragment.this).commit();
                }

            }
        });
        builder.show();
    }

    private void getFaqs() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.faqs_path);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    Gson gson = new Gson();

                    Type type = new TypeToken<LinkedList<Faq>>(){}.getType();

                    mFaqList = gson.fromJson(jObj.getString("results"), type);

                    mAdapter = new FaqExpandableListViewAdapter(getContext(), mFaqList);

                    expandableListView.setAdapter(mAdapter);

                    loader.setVisibility(View.INVISIBLE);

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

                    String errorMessage = null;
                    if (error instanceof NetworkError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        errorMessage = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "Cannot connect to Internet...Please check your connection!";
//                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            }        });
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(strReq);

    }

}
