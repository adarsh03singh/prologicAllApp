package com.csestateconnect.www.csconnect;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.csestateconnect.www.csconnect.adapters.ProjectListAdapter;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.project_detail.Amenity;
import com.csestateconnect.www.csconnect.models.search.Bucket;
import com.csestateconnect.www.csconnect.models.search.Bucket__;
import com.csestateconnect.www.csconnect.models.search.Bucket____;
import com.csestateconnect.www.csconnect.models.search.Bucket_____;
import com.csestateconnect.www.csconnect.models.search.Bucket______;
import com.csestateconnect.www.csconnect.models.search.SearchFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProjectFragment extends Fragment   {

    Toolbar toolbar;
    BottomNavigationView navigation;

    ConstraintLayout loader;

    private LinkedList<Project> mProjectList;

    private ArrayList<String> allCities = new ArrayList<>();
    private String userCityName;

    private RecyclerView mRecyclerView;

    private SearchFilter mSearchFilter;
    private ProjectListAdapter mAdapter;

    Button filterButton;
    Spinner spinnerView;
    NetworkInformation networkInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_project, container, false);

        mRecyclerView = rootView.findViewById(R.id.project_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loader = getView().findViewById(R.id.progress_bar_layout);

        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else
        getAllCities();

        Bundle bundle = getArguments();
        if(bundle != null){
            userCityName = getArguments().getString("user_city_name");
        }else{
            userCityName = userCity();
        }
        if(userCityName == null){
            userCityName = "Noida";
        }


        spinnerView = (Spinner) getView().findViewById(R.id.spinnerView);

        final ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                allCities
        );

        spinnerView.setAdapter(citiesAdapter);
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userCityName = allCities.get(position);
                clearFilterData();
                mSearchFilter = null;
                getProjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerView.setSelection(allCities.indexOf(userCityName));

        filterButton = (Button) getView().findViewById(R.id.filter_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity();
            }
        });

    }

    @Override
    public void onStop () {
        super.onStop();
        SingletonQueue.getInstance(getActivity()).getRequestQueue().cancelAll(ProjectFragment.class);
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
                    getFragmentManager().beginTransaction().detach(ProjectFragment.this).attach(ProjectFragment.this).commit();
                }

            }
        });
        builder.show();
    }

    private void getProjects() {


        String url_for_projects = getString(R.string.api_domain) + getString(R.string.projects_search_path) + "/?";

        if(userCityName != null){
            url_for_projects = url_for_projects + "city_name__term=" + userCityName;
            getFilterData();

            if(mSearchFilter != null){
                url_for_projects = buildQuerFromFilter(url_for_projects);
            }

        }

        loader.setVisibility(View.VISIBLE);
        SingletonQueue.getInstance(getActivity()).getRequestQueue().cancelAll(ProjectFragment.class);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url_for_projects, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :",response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Gson gson = new Gson();

                    Type type = new TypeToken<LinkedList<Project>>(){}.getType();

                    mProjectList = gson.fromJson(jObj.getString("results"), type);

                    mAdapter = new ProjectListAdapter(getContext(), mProjectList);

                    mRecyclerView.setAdapter(mAdapter);

                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                    if(mSearchFilter == null){
                        setFilterData(jObj.getString("facets"));
                        getFilterData();
                    }

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

                    String errorMessage  = null;
                  if (error instanceof ServerError) {
                        errorMessage = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

            }

        });
        strReq.setTag(ProjectFragment.class);
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(strReq);

    }

    private void goToFilterActivity() {
        Intent intent = new Intent(getContext(), FilterActivity.class);
        getContext().startActivity(intent);
    }

    private void getAllCities(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        try {
            JSONArray countries = new JSONArray(sharedPref.getString("LOCATIONS_DATA", null));

            for(Integer i=0; i<countries.length();i++){
                JSONObject country = countries.getJSONObject(i);
                JSONArray cities = country.getJSONArray("cities");

                for(Integer j=0; i<cities.length();j++){
                    JSONObject city = cities.getJSONObject(j);
                    allCities.add(city.getString("name"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String userCity(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        return sharedPref.getString("userCity", null);
    }

    private void getFilterData(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        String facets = sharedPref.getString("searchFilters", null);
        Gson filterGson = new Gson();
        Type filterType = new TypeToken<SearchFilter>(){}.getType();
        mSearchFilter = filterGson.fromJson(facets, filterType);
    }

    private void setFilterData(String facets){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("searchFilters", facets);
        editor.apply();
    }

    private void clearFilterData(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("searchFilters");
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearFilterData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFilterData();
        getProjects();
    }

    private String buildQuerFromFilter(String url_for_projects){

        String locationsFilter = "&location_name__in=";
        String roomsFilter = "&rooms__in=";

        String statusFilter = "&status__in=";

        String bspFilter = "&project_bsp_cost__range=";
        String priceFilter = "&low_cost__range=";
        String areaFilter = "&unit_super_area__range=";


        // Amenities will be in and condition
        List<Bucket> amenitiesBuckets = mSearchFilter.getFilterAmenities().getAmenities().getBuckets();
        if (amenitiesBuckets.size() > 0) {
            for(int i=0; i < amenitiesBuckets.size(); i++){
                if(amenitiesBuckets.get(i).getChecked() != null && amenitiesBuckets.get(i).getChecked()){
                    url_for_projects = url_for_projects + "&amenities__contains=" + amenitiesBuckets.get(i).getKey();
                }
            }
        }

        // Locations will be in or condition
        List<Bucket______> locationsBuckets = mSearchFilter.getFilterLocations().getLocations().getBuckets();
        if (locationsBuckets.size() > 0) {
            Integer counter = 0;
            for(int i=0; i < locationsBuckets.size(); i++){
                if(locationsBuckets.get(i).getChecked() != null && locationsBuckets.get(i).getChecked()){

                    if(counter == 0){
                        url_for_projects = url_for_projects + locationsFilter;
                    }

                    if(counter != 0){
                        url_for_projects = url_for_projects + "__";
                    }

                    url_for_projects = url_for_projects + locationsBuckets.get(i).getKey();

                    counter++;
                }
            }
        }


        // BHK will be in or condition
        List<Bucket__> roomsBuckets = mSearchFilter.getFilterRooms().getRooms().getBuckets();
        if (roomsBuckets.size() > 0) {
            Integer counter = 0;
            for(int i=0; i < roomsBuckets.size(); i++){
                if(roomsBuckets.get(i).getChecked() != null && roomsBuckets.get(i).getChecked() ){

                    if(counter == 0){
                        url_for_projects = url_for_projects + roomsFilter;
                    }

                    if(counter != 0){
                        url_for_projects = url_for_projects + "__";
                    }

                    url_for_projects = url_for_projects + roomsBuckets.get(i).getKey();

                    counter++;
                }
            }

        }

        // Status will be in or condition
        List<Bucket_____> statusBuckets = mSearchFilter.getFilterStatus().getStatus().getBuckets();
        if (statusBuckets.size() > 0) {
            Integer counter = 0;
            for(int i=0; i < statusBuckets.size(); i++){
                if(statusBuckets.get(i).getChecked() != null && statusBuckets.get(i).getChecked()){

                    if(counter == 0){
                        url_for_projects = url_for_projects + statusFilter;
                    }

                    if(counter != 0){
                        url_for_projects = url_for_projects + "__";
                    }

                    url_for_projects = url_for_projects + statusBuckets.get(i).getKey();
                }
            }
        }


        if (mSearchFilter.getFilterBsp().getSelected() != null){
            bspFilter = bspFilter + mSearchFilter.getFilterBsp().getSelected().getFrom().intValue() + "__" + mSearchFilter.getFilterBsp().getSelected().getTo().intValue();
            url_for_projects = url_for_projects + bspFilter;
        }

        if (mSearchFilter.getFilterArea().getSelected() != null){
            areaFilter = areaFilter + mSearchFilter.getFilterArea().getSelected().getFrom().intValue() + "__" + mSearchFilter.getFilterArea().getSelected().getTo().intValue();
            url_for_projects = url_for_projects + areaFilter;
        }

        if (mSearchFilter.getFilterPrice().getSelected() != null){
            priceFilter = priceFilter + mSearchFilter.getFilterPrice().getSelected().getFrom().intValue() + "__" + mSearchFilter.getFilterPrice().getSelected().getTo().intValue();
            url_for_projects = url_for_projects + priceFilter;
        }

        return url_for_projects;

    }

}