package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.csestateconnect.www.csconnect.adapters.HomeDataListAdapter;
import com.csestateconnect.www.csconnect.adapters.HomeProjectImageSliderAdapter;
import com.csestateconnect.www.csconnect.models.home.BrokerDetail;
import com.csestateconnect.www.csconnect.models.home.HomeAllData;
import com.csestateconnect.www.csconnect.models.home.HomeProjectImageSliderList;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private static ViewPager mPager;
    PageIndicatorView pageIndicatorView;
    private Project mProject;
    private Timer timer;
    private int current_position = 0;
    private int custom_position = 0;

    android.support.v7.widget.Toolbar toolbar;

    private HomeProjectImageSliderAdapter mAdapter;
    private List<Project> mProjectList;

    private HomeAllData homeAllData;
    private List<BrokerDetail> mBrokerDetailList;
    private RecyclerView mHomeDataRecyclerView1;
    private HomeDataListAdapter mHomeDataAdapter;

    ConstraintLayout loader;

    String errorMessage;
    String token;
    String RmEmail;
    String RmNumber;

    Button callRm_button;
    TextView totalCommissionText;
    TextView commissionPaidText;
    TextView CommissionUnPaidText;
    CardView cardViewButton;
    LinearLayout dotsLayout;
    Timer swipeTimer;
NetworkInformation networkInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        inflateRegisterFragment();

        mHomeDataRecyclerView1 = rootView.findViewById(R.id.channel_partners_list_recyclerView);
        mHomeDataRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }

        mPager = getView().findViewById(R.id.home_project_images_slider);
        pageIndicatorView = getView().findViewById(R.id.pageIndicatorView);
        callRm_button = (Button) getView().findViewById(R.id.home_callRm_button);
        loader = (ConstraintLayout) getView().findViewById(R.id.home_progress_bar_layout);
        totalCommissionText = (TextView) getView().findViewById(R.id.totalCommissionView);

        commissionPaidText = (TextView) getView().findViewById(R.id.commissionPaidView);
        CommissionUnPaidText = (TextView) getView().findViewById(R.id.commissionUnPaidView);
        cardViewButton = (CardView) getView().findViewById(R.id.cardViewButton);

        callRm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + RmNumber));
                startActivity(intent);
            }
        });

        cardViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommissionFragment();
            }
        });

        getProfiles();
        getValuesFromSharedPreferences();
        getProjects();
        getHomedata();


    }

    @Override
    public void onStop() {
        super.onStop();
        SingletonQueue.getInstance(getActivity()).getRequestQueue().cancelAll(HomeFragment.class);
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
                    getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();
                }

            }
        });
        builder.show();
    }


    private void getProjects() {

        loader.setVisibility(View.VISIBLE);
        String URL_FOR_GET_PROFILE = getString(R.string.api_domain) + getString(R.string.projects_search_path);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_GET_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response :", response);
                SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Ac_response", response);
                editor.commit();
                try {

                    JSONObject allData = new JSONObject(response);
                    JSONArray projectName = allData.getJSONArray("results");
                    final List<HomeProjectImageSliderList> allProjectIconImages = new ArrayList<HomeProjectImageSliderList>();
                    for (int i = 0; i < projectName.length(); i++) {
                        JSONObject projectData = projectName.getJSONObject(i);

                        Gson gson = new Gson();
                        Type type = new TypeToken<Project>() {
                        }.getType();
                        mProject = gson.fromJson(projectData.toString(), type);

                        HomeProjectImageSliderList image = new HomeProjectImageSliderList(mProject.getIconImage(), mProject.getName(), mProject.getAddress(), mProject.getId());

                        allProjectIconImages.add(image);

                    }


                    HomeProjectImageSliderAdapter mSliderAdapter = new HomeProjectImageSliderAdapter(getActivity(), allProjectIconImages);
                    mPager.setAdapter(mSliderAdapter);


                    pageIndicatorView.setCount(allProjectIconImages.size()); // specify total count of indicators
                    pageIndicatorView.setSelection(0);

                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        public void run() {
                            if (current_position == allProjectIconImages.size()) {
                                current_position = 0;
                            }
                            mPager.setCurrentItem(current_position++, true);
                        }

                    };

                    mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            current_position = i;
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
                    }, 300, 3000);

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

                    String errorMessage = errorMessage = null;
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
        strReq.setTag(HomeFragment.class);
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(strReq);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = null;
        }
    }

    private void getHomedata() {
        String URL_FOR_GET_HOMEDATA = getString(R.string.api_domain) + getString(R.string.get_homeData_path);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_GET_HOMEDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", ">>>>" + response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            Gson gson = new Gson();
                            Type type = new TypeToken<HomeAllData>() {
                            }.getType();
                            homeAllData = gson.fromJson(jsonResponse.toString(), type);

                            mBrokerDetailList = homeAllData.getBrokers().getBrokerDetails();

                            mHomeDataAdapter = new HomeDataListAdapter(getContext(), mBrokerDetailList.subList(0, 5));
                            mHomeDataRecyclerView1.setAdapter(mHomeDataAdapter);

                            JSONObject obj3 = jsonResponse.getJSONObject("commission");
                            String totalAmount = obj3.getString("commission_amount_total__sum");
                            String paidAmount = obj3.getString("commission_amount_paid__sum");
                            String unpaidAmount = obj3.getString("commission_amount_unpaid__sum");

                            totalCommissionText.setText(totalAmount);
                            commissionPaidText.setText(paidAmount);
                            CommissionUnPaidText.setText(unpaidAmount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");

                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse == null) {

                            String errorMessage = errorMessage = null;
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

                }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", token);
                return headers;
            }
        };
        stringRequest.setTag(HomeFragment.class);
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

                    JSONObject Rm = jObj.getJSONObject("rm");

                    String RmId = Rm.getString("id");
                    String RmFirstName = Rm.getString("first_name");
                    String RmLastname = Rm.getString("last_name");
                    RmEmail = Rm.getString("email");
                    RmNumber = Rm.getString("phone_number");
                    setRmNumberInSharedPrefrences(RmNumber, RmEmail);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());

                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse == null) {

                    String errorMessage = errorMessage = null;
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", token);

                return params;
            }
        };
        SingletonQueue.getInstance(getActivity()).addToRequestQueue(strReq);

    }

    public void getValuesFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
        token = sharedPref.getString(getString(R.string.token_data_key), null);
    }

    private void removeTokenFromSharedPreferences() {
        SharedPreferences pref = getActivity().getBaseContext().getSharedPreferences("shared_preference_key", 0);
        pref.edit().remove("token").commit();
        goToSplashActivity();
    }

    public void goToSplashActivity() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.putExtra("errorMessage", errorMessage);
        startActivity(intent);
        getActivity().finish();
    }

    private void goToCommissionFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        ft.replace(R.id.main_acitivity_container, new CommissionFragment());
        ft.addToBackStack(null);
        ft.commit();
    }


//    public void getrRmDataFromSharedPreferences() {
//        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
//        rmnNumber = sharedPref.getString(getString(R.string.rm_number_data),null);
//        rmEmail = sharedPref.getString(getString(R.string.rm_email_data), null);
//
//    }

    public void setRmNumberInSharedPrefrences(String RmNumber, String RmEmail) {
        try {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("shared_preference_key", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.rm_number_data), RmNumber);
            editor.putString(getString(R.string.rm_email_data), RmEmail);
            editor.commit();
        } catch (NullPointerException e) {
        }

    }

    private void inflateRegisterFragment() {
        Intent myintent = getActivity().getIntent();
        if (null != myintent.getExtras()) {
            boolean is_existing_user = myintent.getExtras().getBoolean("is_existing_user");
            // FIXME: 1/27/2019 change == to != in next line
            if (is_existing_user != true) {
                myintent.removeExtra("is_existing_user");
                FragmentTransaction ft3 = getActivity().getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.main_acitivity_container, new UserRegisterFragment(), "ProfileEditFragment");
                ft3.addToBackStack(null);
                ft3.commit();
            }
        }
    }


}