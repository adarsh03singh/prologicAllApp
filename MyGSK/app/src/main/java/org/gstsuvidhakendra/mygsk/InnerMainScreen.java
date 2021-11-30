package org.gstsuvidhakendra.mygsk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONObject;

public class InnerMainScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public String gskDataGlob;
    public NavController navController;
    Long backPressedTime = Long.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences getShared = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        final String userGskCompany = getShared.getString("GskCompany","gsk_head");
        final String userGskPhone = getShared.getString("GskPhone","gsk_head");
        final String userGskAddress = getShared.getString("GSkAddress","gsk_head");
        gskDataGlob = userGskCompany+"<!>"+userGskPhone+"<!>"+userGskAddress;

        final SharedPreferences.Editor editor = getShared.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_main_screan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_service,R.id.nav_slideshow,
                R.id.nav_tools,R.id.nav_share,R.id.nav_send,R.id.nav_gst,R.id.nav_tax,R.id.nav_accouting,
                R.id.nav_itr,R.id.nav_dsc,R.id.nav_msme,R.id.nav_company_registration,R.id.nav_money_transfer,R.id.nav_travel,R.id.nav_pan,R.id.nav_loan,R.id.nav_insurance,R.id.nav_recharge,R.id.nav_bill_payment,R.id.nav_other)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            getApplicationContext().getSharedPreferences("gsk_lead", 0).edit().clear().apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
//            moveTaskToBack(true);
            finish();
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inner_main_screan, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        // this code use for app closed every navigation fragment back pressed
//        moveTaskToBack(true);

        Toast backToast;
        // using back press go to home by all navigation fragment
        int destId = navController.getCurrentDestination().getId();

        if (destId == R.id.nav_gst || destId == R.id.nav_tax || destId == R.id.nav_accouting ||
                destId == R.id.nav_itr || destId == R.id.nav_dsc || destId == R.id.nav_msme ||
                destId == R.id.nav_company_registration || destId == R.id.nav_money_transfer || destId == R.id.nav_travel ||
                destId == R.id.nav_pan || destId == R.id.nav_loan|| destId == R.id.nav_insurance
                || destId == R.id.nav_recharge || destId == R.id.nav_bill_payment || destId == R.id.nav_other){

            navController.popBackStack(R.id.nav_home, false);
        }
//        else if(destId == R.id.nav_home){
//            backToast = Toast.makeText(getApplicationContext(), "Press back again to leave the app.", Toast.LENGTH_SHORT);
//            if (backPressedTime + 2000 > System.currentTimeMillis()) {
//                backToast.cancel();
//                moveTaskToBack(true);
//                return;
//            } else {
//                backToast.show();
//            }
//            backPressedTime =  System.currentTimeMillis();
//        }
        else {
            super.onBackPressed();
        }

    }
    public String getGskDataGlob(){
        return gskDataGlob;
    }


    public void setLeadTypeHelper(String gskData,String userChoice){
        SharedPreferences getShared = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = getShared.edit();
        final String leadName = getShared.getString("leadName","nO nAME");
        final String leadMobileNumber = getShared.getString("mobileNumber","No mobile number");
        final String leadPincode = getShared.getString("pincode","No pincode");


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://gstsuvidhakendra.org.in/api/mygsk_lead.php?lead_mobile="+leadMobileNumber+"&lead_type="+userChoice+"&lead_action=lead_type";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*try {

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //Intent intent = new Intent(GskList.this, InnerMainScreen.class);
                //startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");

            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public static void setLeadType(Context context,String gskData,String userChoice){
        Toast.makeText(context,gskData,Toast.LENGTH_SHORT).show();
    }

}
