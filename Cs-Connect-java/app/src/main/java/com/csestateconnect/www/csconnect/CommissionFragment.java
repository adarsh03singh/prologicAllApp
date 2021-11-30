package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.csestateconnect.www.csconnect.adapters.DealListAdapter;
import com.csestateconnect.www.csconnect.adapters.LeadListAdapter;
import com.csestateconnect.www.csconnect.models.deal.Deal;
import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CommissionFragment extends Fragment  {


    private String token;

    private ConstraintLayout loader;
    private TextView noDealsTxt;
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private String nextPage;
    ProgressBar loadProgressBar;

    private LinkedList<Deal> mDealList;
    private LinkedList<Deal> tempLeadList;

    private RecyclerView mRecyclerView;
    private DealListAdapter mAdapter;

    private NavigationDrawerToggle drawerInterface;
    private Button back_button;

    private NetworkInformation networkInformation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            drawerInterface = (NavigationDrawerToggle) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerInterface.lockDrawer();
        View rootView = inflater.inflate(R.layout.fragment_commission, container, false);

        mRecyclerView = rootView.findViewById(R.id.deal_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final LinearLayoutManager mLinearlayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLinearlayoutManager.getItemCount();
                lastVisibleItem = mLinearlayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    getdeals();
                    isLoading = true;
                }

            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProgressBar = (ProgressBar) getView().findViewById(R.id.deal_progress_bar);
        loader = getView().findViewById(R.id.progress_bar_layout);
        noDealsTxt = getView().findViewById(R.id.noDeals_text);
        back_button=getView().findViewById(R.id.left_side_bar_button);
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        getValuesFromSharedPreferences();

    }

    @Override
    public void onResume() {
        super.onResume();
        getdeals();
    }


    @Override
    public void onStop () {
        super.onStop();
        SingletonQueue.getInstance(getActivity()).getRequestQueue().cancelAll(CommissionFragment.class);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
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
                    getFragmentManager().beginTransaction().detach(CommissionFragment.this)
                            .attach(CommissionFragment.this).commit();
                }

            }
        });
        builder.show();
    }

    private void getdeals() {


        String URL_FOR_GET_Deal_PROFILE;

        if(nextPage == "null"){
            loadProgressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(getContext(), "No more leads.", Toast.LENGTH_SHORT).show();
            return;
        }else if(nextPage != null){
            loadProgressBar.setVisibility(View.VISIBLE);
            URL_FOR_GET_Deal_PROFILE = nextPage;
        }else{
            loader.setVisibility(View.VISIBLE);
            URL_FOR_GET_Deal_PROFILE = getString(R.string.api_domain) + getString(R.string.deals_list_path);
        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_Deal_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :",response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Gson gson = new Gson();


                    Type type = new TypeToken<LinkedList<Deal>>(){}.getType();
                    nextPage = jObj.getString("next");

                    if(mDealList == null){
                    mDealList = gson.fromJson(jObj.getString("results"), type);

                    mAdapter = new DealListAdapter(getContext(), mDealList);

                    mRecyclerView.setAdapter(mAdapter);

                    if (mAdapter.getItemCount() == 0) {
                        noDealsTxt.setVisibility(View.VISIBLE);
                    } else
                        noDealsTxt.setVisibility(View.GONE);

                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                    loader.setVisibility(View.INVISIBLE);

                    }else {
                        tempLeadList = gson.fromJson(jObj.getString("results"), type);
                        mDealList.addAll(tempLeadList);
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

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);

                return params;
            }
        };
        strReq.setTag(CommissionFragment.class);
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(strReq);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
    }

}