package com.csestateconnect.www.csconnect;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NavigationDrawerToggle, BottomNavigationToggle {

    ImageLoader imageLoader = SingletonQueue.getInstance(getBaseContext()).getImageLoader();

    String verification_bank_Ac;
    String verification_kyc_Ac;
    String token;
    String brokerNumber;
    View headerView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    TextView header_firstName_text;
    TextView header_lastName_text;
    Button searchButton;
    ImageView header_profileImage;

    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String user_city_name = getIntent().getStringExtra("user_city_name");
//        networkInformation = new NetworkInformation(getApplicationContext());
//        if (networkInformation.isConnectingToInternet() == false) {
////no connection
//            networkErrorPopup();
//        } else

            getValuesFromSharedPreferences();

        if (user_city_name != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("user_city_name", user_city_name);
            ProjectFragment fragment = new ProjectFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.main_acitivity_container, fragment, "ProjectFragment");
            ft.commit();
        } else {
            inflateHomeFragment();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.profile_icon);
        searchButton = (Button) findViewById(R.id.search_toolbar);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);


        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateProfileActivity();
            }
        });

        header_firstName_text = (TextView) headerView.findViewById(R.id.headerFirstName);
        header_lastName_text = (TextView) headerView.findViewById(R.id.headerLastname);
        header_profileImage = (ImageView) headerView.findViewById(R.id.headerImageView);

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getProfiles();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int seletedItemId = bottomNavigationView.getSelectedItemId();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (R.id.navigation_home != seletedItemId) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            super.onBackPressed();
        }

    }

    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

//                finish();
                if (networkInformation.isConnectingToInternet() == false) {
//no connection
                    networkErrorPopup();
                } else
//                 finish();
//                overridePendingTransition(0,0);
//                startActivity(getIntent());
//                overridePendingTransition(0,0);
                recreate();
            }
        });
        builder.show();
    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_ac_verification:

                if (verification_bank_Ac != "null") {
                    Intent detailIntent = new Intent(getApplicationContext(), Ac_Detail_ProfileActivity.class);
                    startActivity(detailIntent);
                } else {

                    Intent acIntent = new Intent(getApplicationContext(), Ac_DetailActivity.class);
                    startActivity(acIntent);
                }
                break;

            case R.id.nav_verification:
                if (verification_kyc_Ac != "null") {
                    Intent verifyIntent = new Intent(getApplicationContext(), VerificationDetailActivity.class);
                    startActivity(verifyIntent);

                } else {
                    Intent verifyDetailIntent = new Intent(getApplicationContext(), VerificationActivity.class);
                    startActivity(verifyDetailIntent);
                }
                break;


            case R.id.nav_assigned_leads:
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.main_acitivity_container, new AssignLeadsFragment(), "AssignLeadsFragment");
                ft2.addToBackStack(null);
                ft2.commit();
                break;

            case R.id.nav_submit_your_leads:
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.main_acitivity_container, new SubmittedLeadsFragment(), "SubmittedLeadsFragment");
                ft3.addToBackStack(null);
                ft3.commit();
                break;

            case R.id.nav_create_lead:
                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                ft4.replace(R.id.main_acitivity_container, new CreateLeadsFragment(), "CreateLeadFragment");
                ft4.addToBackStack(null);
                ft4.commit();
                break;

            case R.id.nav_faqs:
                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                ft5.replace(R.id.main_acitivity_container, new FaqFragment(), "FaqFragment");
                ft5.addToBackStack(null);
                ft5.commit();
                break;

            case R.id.nav_support:
                FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                ft6.replace(R.id.main_acitivity_container, new SupportFragment(), "SupportFragment");
                ft6.addToBackStack(null);
                ft6.commit();
                break;

            case R.id.nav_about_us:
                FragmentTransaction ft7 = getSupportFragmentManager().beginTransaction();
                ft7.replace(R.id.main_acitivity_container, new AboutUsFragment(), "AboutUsFragment");
                ft7.addToBackStack(null);
                ft7.commit();
                break;

            case R.id.nav_Terms_conditions:
                Intent tcIntent = new Intent(getApplicationContext(), TermsConditionsActivity.class);
                startActivity(tcIntent);
                break;

            case R.id.privacy_policy:
                Intent paIntent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(paIntent);
                break;

            case R.id.log_out:
                SharedPreferences pref = getBaseContext().getSharedPreferences("shared_preference_key", 0);
                pref.edit().remove("token").commit();
                inflateGenerateOtpFragment();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Activity activity;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.main_acitivity_container, new HomeFragment(), "HomeFragment");
                    ft1.commit();
                    return true;

                case R.id.navigation_projects:
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_acitivity_container, new ProjectFragment(), "ProjectFragment");
                    ft.commit();
                    return true;

                case R.id.navigation_assigned_leads:
                    Intent leadIntent = new Intent(getApplicationContext(), LeadActivity.class);
                    startActivity(leadIntent);
                    finish();
                    return false;

                case R.id.navigation_comission:

                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.main_acitivity_container, new CommissionFragment(), "CommissionFragment");
                    ft2.commit();
                    return true;


            }
            return false;
        }
    };


    private void inflateGenerateOtpFragment() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(intent);
        finish();
    }

    private void inflateProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
        finish();
    }


    public void setBottomNavigationVisibility(boolean visible) {
        if (bottomNavigationView.isShown() && !visible) {
            bottomNavigationView.setVisibility(View.GONE);
        } else if (!bottomNavigationView.isShown() && visible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void inflateHomeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_acitivity_container, new HomeFragment(), "HomeFragment");
        ft.commit();
    }

    @Override
    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSelectToCurrentFragment(Integer id) {
        bottomNavigationView.setSelectedItemId(id);
    }

    private void getProfiles() {

        String URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.get_profile_api_path);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);


                    String first_name = jObj.getString("first_name");
                    String last_name = jObj.getString("last_name");
                    header_firstName_text.setText(first_name);
                    header_lastName_text.setText(last_name);

                    if (first_name.isEmpty() && last_name.isEmpty() )
                        header_firstName_text.setText(brokerNumber);

                    Glide.with(getApplicationContext()).load(jObj.getString("profile_image"))
                            .apply(RequestOptions.circleCropTransform()).into(header_profileImage);
                    if (!jObj.getString("profile_image").isEmpty()) {
                        header_profileImage.setBackground(null);
                    }

                    JSONObject verification = jObj.getJSONObject("user");

                    verification_bank_Ac = verification.getString("bankaccount");
                    verification_kyc_Ac = verification.getString("kyc");
                    try {
                        JSONObject country = jObj.getJSONObject("country");

                        JSONObject city = jObj.getJSONObject("city");
                        String cityName = city.getString("name");
                        SharedPreferences pref = getSharedPreferences("shared_preference_key", 0);
                        SharedPreferences.Editor editors = pref.edit();
                        editors.putString("userCity", cityName);
                        editors.apply();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject Rm = jObj.getJSONObject("rm");

                        String RmEmail = Rm.getString("email");
                        String RmNumber = Rm.getString("phone_number");

                        setRmNumberInSharedPrefrences(RmNumber, RmEmail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());


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

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
        brokerNumber = sharedPref.getString(getString(R.string.phonenumber_data), null);

    }

    public void setRmNumberInSharedPrefrences(String RmNumber, String RmEmail) {
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.rm_number_data), RmNumber);
        editor.putString(getString(R.string.rm_email_data), RmEmail);

        editor.commit();
    }
}
