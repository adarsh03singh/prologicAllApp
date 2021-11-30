package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.adapters.ProjectAmenityCategoryAdaptor;
import com.csestateconnect.www.csconnect.adapters.ProjectConnectingRoadsAdapter;
import com.csestateconnect.www.csconnect.adapters.ProjectImageSliderAdapter;
import com.csestateconnect.www.csconnect.adapters.ProjectUnitPlanAdaptor;
import com.csestateconnect.www.csconnect.adapters.ProjectWowFactorAdapter;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.project.ProjectAmenity;
import com.csestateconnect.www.csconnect.models.project.ProjectRoad;
import com.csestateconnect.www.csconnect.models.project.ProjectTower;
import com.csestateconnect.www.csconnect.models.project.ProjectWowFactor;
import com.csestateconnect.www.csconnect.models.project.UnitPlan;
import com.csestateconnect.www.csconnect.models.project_detail.Amenity;
import com.csestateconnect.www.csconnect.models.project_detail.AmenityCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class ProjectDetailActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static ViewPager mAmenitiesPager;
    private static ViewPager mUnitPlansPager;
    Timer swipeTimer;

    private Project mProject;

    private Integer projectId;
    private ConstraintLayout loader;
    private String commissionData;
    private String commission_slab;
    private CollapsingToolbarLayout colapsetoolbar;

    private String token;
    private static int currentPage = 0;
    private String errorMessage;
    private Toolbar toolbar;

    private TextView priceRangeView;
    private TextView priceSqfeetView;
    private TextView sizeRangeView;
    private TextView unitOptionsView;
    private ProgressBar statusProgressBarView;
    private TextView statusComplitionView;
    private TextView projectNameView;
    private ReadMoreTextView projectInfoView;
    private ImageView developerImageView;
    private TextView developerName_head_view;
    private TextView DeveloperName_view;
    private ReadMoreTextView developer_descriptionView;
    private TextView developer_total_project_view;
    private TextView reraNumberView;
    private TextView commisionView;
    private TextView commision_slabView;
    private CardView commision_ButtoncardView;
    private CardView commision_SlabcardView;
    private Button commision_button_view;
    private String rmNumber;
    private String rmEmail;

    private List<AmenityCategory> amanity_categories_list = new ArrayList<>();
    private Hashtable<String, Integer> unique_categories = new Hashtable<String, Integer>();

    private List<ProjectWowFactor> mWowList;
    private ProjectWowFactorAdapter wowfactorAdapter;
    private RecyclerView mWowFactorRecyclerView;

    private List<ProjectRoad> mRoadList;
    private ProjectConnectingRoadsAdapter mProjectRoadAdapter;
    private RecyclerView mRoadRecyclerView;
    NetworkInformation networkInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_detail);

        projectId = getIntent().getExtras().getInt("project_id");
        mWowFactorRecyclerView = findViewById(R.id.wow_factors_recyclerviewTxt);
        mWowFactorRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mRoadRecyclerView = findViewById(R.id.connecting_roads_recyclerViewtxt);
        mRoadRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        getIds();
        getValuesFromSharedPreferences();
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else


        getProjectData();


        commision_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+rmNumber));
                        startActivity(intent);
                    }
                });

    }

      @Override
    public boolean onCreateOptionsMenu(Menu menu) {

          getMenuInflater().inflate(R.menu.create_lead, menu);
          menu.findItem(R.id.create_lead_button).setVisible(false);
         getCommissionData(menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.create_lead_button) {
                Intent intent = new Intent(getApplicationContext(), CreateLeadActivity.class);
                startActivity(intent);
            }
        return super.onOptionsItemSelected(item);
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
        rmNumber = sharedPref.getString(getString(R.string.rm_number_data), null);

    }
    private void getProjectData() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.projects_search_path) + "/?project_id=" + projectId;
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_PROJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject allData = new JSONObject(response);
                    JSONArray projectName = allData.getJSONArray("results");
                    JSONObject projectData = projectName.getJSONObject(0);

                    Gson gson = new Gson();
                    Type type = new TypeToken<Project>() {
                    }.getType();
                    mProject = gson.fromJson(projectData.toString(), type);

                    toolbar = findViewById(R.id.projectDetailtoolbar);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(mProject.getName());

                    mPager = findViewById(R.id.project_images_slider);
                    ProjectImageSliderAdapter mSliderAdapter = new ProjectImageSliderAdapter(getApplicationContext(), mProject.getProjectImages());
                    mPager.setAdapter(mSliderAdapter);

                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        public void run() {
                            if (currentPage == mProject.getProjectImages().size()) {
                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage++, true);
                        }

                    };

                    mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            currentPage = i;
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });

                     swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(runnable);
                        }
                    }, 250, 2500);

                    String costView = mProject.getLowCostView() + " - " + mProject.getHighCostView();
                    String priceSqfeet = mProject.getProjectBspCostView();
                    String sizeRange = mProject.getProjectOpenAreaView() + " - " + mProject.getProjectTotalAreaView();
                    String untOption = mProject.getBhks();
                    Integer statusProgress = mProject.getProjectCompletionStatus().getCompletionPercentage();
                    String statusComplition = mProject.getProjectCompletionStatus().getName();
                    String projecteName = mProject.getName();
                    String projectInfo = mProject.getProjectInfo();
                    String developerNameHead = mProject.getProjectDeveloper().getName();
                    String developerName = mProject.getProjectDeveloper().getName();
                    String developerInfo = mProject.getProjectDeveloper().getDeveloperInfo();
                    String noOfProject = mProject.getProjectDeveloper().getNoOfProjects();
                    String reraNumber = mProject.getProjectReraNumber();
                    Glide.with(getApplicationContext()).load(mProject.getProjectDeveloper().getIconImage()).into(developerImageView);


                    List<ProjectAmenity> amenities = mProject.getProjectAmenities();
                    Integer unique_categories_index_counter = 0;

                    for(Integer i = 0; i < amenities.size(); i++){
                        ProjectAmenity amenity = amenities.get(i);

                        Amenity this_amanity;
                        AmenityCategory this_amenity_category;

                        if(unique_categories.containsKey(amenity.getAmenityCategory().getName())){
                            this_amanity = new Amenity(amenity.getName(), amenity.getIconImage());
                            amanity_categories_list.get(unique_categories.get(amenity.getAmenityCategory().getName())).addAmenity(this_amanity);
                        }else{
                            unique_categories.put(amenity.getAmenityCategory().getName(), unique_categories_index_counter);
                            unique_categories_index_counter++;
                            this_amanity = new Amenity(amenity.getName(), amenity.getIconImage());
                            this_amenity_category = new AmenityCategory(amenity.getAmenityCategory().getName(), new ArrayList<Amenity>(Arrays.asList(this_amanity)));
                            amanity_categories_list.add(this_amenity_category);

                        }
                    }


                    mAmenitiesPager = findViewById(R.id.amenities_view_pager);
                    PagerTabStrip amenitiesPagerTitleStrip = findViewById(R.id.amenities_pager_title_strip);
                    amenitiesPagerTitleStrip.setTextSpacing(20);

                    ProjectAmenityCategoryAdaptor mProjectAmenityCategoryAdaptor = new ProjectAmenityCategoryAdaptor(getApplicationContext(), amanity_categories_list);
                    mAmenitiesPager.setAdapter(mProjectAmenityCategoryAdaptor);


                    List<UnitPlan> allUnitPlans = new ArrayList<UnitPlan>();

                    List<ProjectTower> allProjectTowers = mProject.getProjectTowers();

                    for(Integer i = 0; i < allProjectTowers.size(); i++){
                        allUnitPlans.addAll(allProjectTowers.get(i).getUnitPlans());
                    }


                    if(allUnitPlans.size() > 0) {

                        mUnitPlansPager = findViewById(R.id.unit_plans_view_pager);
                        PagerTabStrip unitPlansPagerTitleStrip = findViewById(R.id.unit_plans_pager_title_strip);
                        unitPlansPagerTitleStrip.setTextSpacing(20);

                        ProjectUnitPlanAdaptor mProjectUnitPlanAdaptor = new ProjectUnitPlanAdaptor(getApplicationContext(), allUnitPlans);
                        mUnitPlansPager.setAdapter(mProjectUnitPlanAdaptor);

                    }else{
                        findViewById(R.id.unit_plans_cardView).setVisibility(View.GONE);
                    }

                    mWowList =  mProject.getProjectWowFactors();
                    wowfactorAdapter = new ProjectWowFactorAdapter(getApplicationContext(), mWowList);
                    mWowFactorRecyclerView.setAdapter(wowfactorAdapter);

                    mRoadList =  mProject.getProjectRoads();
                    mProjectRoadAdapter = new ProjectConnectingRoadsAdapter(getApplicationContext(), mRoadList);
                    mRoadRecyclerView.setAdapter(mProjectRoadAdapter);

                    priceRangeView.setText(costView);
                    priceSqfeetView.setText(priceSqfeet);
                    sizeRangeView.setText(sizeRange);
                    unitOptionsView.setText(untOption);
                    statusProgressBarView.setProgress(statusProgress);
                    statusComplitionView.setText(statusComplition);
                    projectNameView.setText("About "+projecteName);
                    projectInfoView.setText(projectInfo);

                    developerName_head_view.setText("About " +developerNameHead);
                    DeveloperName_view.setText(developerName);
                    developer_descriptionView.setText(developerInfo);
                    developer_total_project_view.setText(noOfProject);

                    reraNumberView.setText(reraNumber);
//                    getCommissionData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse == null) {

                    String errorMessage  = null;
                    if (error instanceof ServerError) {
                        errorMessage = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                    } else if (error instanceof ParseError) {
                        errorMessage = "Parsing error! Please try again after some time!!";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else if (error.networkResponse.statusCode == 403) {
                    errorMessage = null;
                    try {
                        errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeTokenFromSharedPreferences();//will be go SplashActivity
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = null;
        }
    }


    private void getCommissionData(final Menu menu) {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROJECT = getString(R.string.api_domain) + getString(R.string.projects_commission_path) + "/" + projectId;
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_FOR_GET_PROJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loader.setVisibility(View.INVISIBLE);
                try {
                    JSONObject commissionObj = new JSONObject(response);
                     commissionData = commissionObj.getString("commission");
                     commission_slab = commissionObj.getString("commission_slabs");

                    if(commissionData == null && commission_slab == null) {
          menu.findItem(R.id.create_lead_button).setVisible(false);
          }else
              menu.findItem(R.id.create_lead_button).setVisible(true);

                    if(commissionData != null && commission_slab != null) {
                        commision_ButtoncardView.setVisibility(View.GONE);
                        commisionView.setText(commissionData);
                        commision_slabView.setText(commission_slab);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                loader.setVisibility(View.INVISIBLE);
                Integer statusCode = error.networkResponse.statusCode;

                if(statusCode == 404){
                    commision_SlabcardView.setVisibility(View.GONE);
                    commision_ButtoncardView.setVisibility(View.VISIBLE);

                }
                if (statusCode == 403) {
                    errorMessage = null;
                    try {
                        errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    removeTokenFromSharedPreferences();//will be go SplashActivity
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
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        startActivity(intent);
        finish();
    }



    public void getIds() {
        loader = (ConstraintLayout) findViewById(R.id.progress_bar_layout);
        priceRangeView = (TextView) findViewById(R.id.price_range_txt);
        priceSqfeetView = (TextView) findViewById(R.id.perSquareFeetPrice_txt);
        sizeRangeView = (TextView) findViewById(R.id.size_range_starting_txt);
        unitOptionsView = (TextView) findViewById(R.id.unit_option_txt);
        statusProgressBarView = (ProgressBar) findViewById(R.id.statusProgressBar);
        statusComplitionView = (TextView) findViewById(R.id.status_complition_txt);
        projectNameView = (TextView) findViewById(R.id.project_name_txt);
        projectInfoView = (ReadMoreTextView) findViewById(R.id.about_project_text);
        developerImageView = (ImageView) findViewById(R.id.developer_icon_image);
        developerName_head_view = (TextView) findViewById(R.id.developer_name_txtHead);
        DeveloperName_view = (TextView) findViewById(R.id.developer_name_txt);
        developer_descriptionView = (ReadMoreTextView) findViewById(R.id.developer_description_txt);
        developer_total_project_view = (TextView) findViewById(R.id.developer_totalNoOfProject);
        reraNumberView = (TextView) findViewById(R.id.rera_no_txt);
        commisionView = (TextView) findViewById(R.id.commission_txt);
        commision_slabView = (TextView) findViewById(R.id.commission_slab_txt);
        commision_ButtoncardView = (CardView) findViewById(R.id.commision_cardView);
        commision_SlabcardView = (CardView) findViewById(R.id.commision_slab_cardView);
        commision_button_view = (Button) findViewById(R.id.project_detail_call_rm_button);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
