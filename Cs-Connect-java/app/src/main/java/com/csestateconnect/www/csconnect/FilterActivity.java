package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.csestateconnect.www.csconnect.adapters.filter.AmenityAdapter;
import com.csestateconnect.www.csconnect.adapters.filter.BhkAdapter;
import com.csestateconnect.www.csconnect.adapters.filter.LocationAdapter;
import com.csestateconnect.www.csconnect.adapters.filter.StatusAdapter;
import com.csestateconnect.www.csconnect.models.search.Bucket_;
import com.csestateconnect.www.csconnect.models.search.Bucket___;
import com.csestateconnect.www.csconnect.models.search.Bucket____;
import com.csestateconnect.www.csconnect.models.search.SearchFilter;
import com.csestateconnect.www.csconnect.models.search.Selected;
import com.csestateconnect.www.csconnect.models.search.Selected_;
import com.csestateconnect.www.csconnect.models.search.Selected__;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements FilterAdapterCallback{

    private Button back_button;
    private Button clear_button;
    private Button apply_button;

    private SearchFilter mSearchFilter;

    // Menu buttons
    private Button filter_bhk_button;
    private Button filter_price_button;
    private Button filter_area_button;
    private Button filter_status_button;
    private Button filter_amenities_button;
    private Button filter_locations_button;
    private Button filter_bsp_button;

    // Layouts
    private LinearLayout filter_bhk_layout;
    private LinearLayout filter_price_layout;
    private LinearLayout filter_area_layout;
    private LinearLayout filter_status_layout;
    private LinearLayout filter_amenities_layout;
    private LinearLayout filter_locations_layout;
    private LinearLayout filter_bsp_layout;

    // Recycler views
    private RecyclerView filter_bhk_recyclerView;
    private RangeBar filter_price_rangebar;
    private TextView filter_price_selected_min_cost;
    private TextView filter_price_selected_max_cost;

    private RangeBar filter_area_rangebar;
    private TextView filter_area_selected_min;
    private TextView filter_area_selected_max;


    private RecyclerView filter_status_recyclerView;
    private RecyclerView filter_amenities_recyclerView;
    private RecyclerView filter_locations_recyclerView;

    private RangeBar filter_bsp_rangebar;
    private TextView filter_bsp_selected_min_cost;
    private TextView filter_bsp_selected_max_cost;
    NetworkInformation networkInformation;


    private static String[] c = new String[]{"T","L", "Cr", "Ar"};

    /**
     * Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
     * @param n the number to format
     * @param iteration in fact this is the class from the array c
     * @return a String representing the number n formatted in a cool looking way.
     */
    private static String priceFormat(double n, int iteration) {
        double d;
        if(iteration==0){
            d = ((long) n / 100) / 10.0;
        }else{
            d = ((long) n / 10) / 10.0;
        }
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 100? ((d > 99.9 || isRound || (!isRound && d > 9.9)? (int) d * 10 / 10 : d + "" ) + "" + c[iteration]): priceFormat(d, iteration+1));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }
        getFilterData();

        back_button = findViewById(R.id.filter_left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        clear_button = findViewById(R.id.filter_clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilterData();
                onBackPressed();
            }
        });

        apply_button = findViewById(R.id.filter_submit_button);
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilterData();
                onBackPressed();
            }
        });

        // Layouts
        filter_bhk_layout = findViewById(R.id.filter_bhk_layout);
        filter_price_layout = findViewById(R.id.filter_price_layout);
        filter_area_layout = findViewById(R.id.filter_area_layout);
        filter_status_layout = findViewById(R.id.filter_status_layout);
        filter_amenities_layout = findViewById(R.id.filter_amenities_layout);
        filter_locations_layout = findViewById(R.id.filter_locations_layout);
        filter_bsp_layout = findViewById(R.id.filter_bsp_layout);




        // Menu click listeners

        filter_bhk_button = findViewById(R.id.filter_bhk_button);
        filter_bhk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.VISIBLE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.GONE);

                filter_bhk_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);

            }
        });


        filter_price_button = findViewById(R.id.filter_price_button);
        filter_price_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.VISIBLE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.GONE);

                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        filter_area_button = findViewById(R.id.filter_area_button);
        filter_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.VISIBLE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.GONE);


                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);

            }
        });


        filter_status_button = findViewById(R.id.filter_status_button);
        filter_status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.VISIBLE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.GONE);

                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        filter_amenities_button = findViewById(R.id.filter_amenities_button);
        filter_amenities_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.VISIBLE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.GONE);


                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        filter_locations_button = findViewById(R.id.filter_locations_button);
        filter_locations_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.VISIBLE);
                filter_bsp_layout.setVisibility(View.GONE);

                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(getResources().getColor(android.R.color.white));
                filter_bsp_button.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        filter_bsp_button = findViewById(R.id.filter_bsp_button);
        filter_bsp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_bhk_layout.setVisibility(View.GONE);
                filter_price_layout.setVisibility(View.GONE);
                filter_area_layout.setVisibility(View.GONE);
                filter_status_layout.setVisibility(View.GONE);
                filter_amenities_layout.setVisibility(View.GONE);
                filter_locations_layout.setVisibility(View.GONE);
                filter_bsp_layout.setVisibility(View.VISIBLE);

                filter_bhk_button.setBackgroundColor(Color.TRANSPARENT);
                filter_price_button.setBackgroundColor(Color.TRANSPARENT);
                filter_area_button.setBackgroundColor(Color.TRANSPARENT);
                filter_status_button.setBackgroundColor(Color.TRANSPARENT);
                filter_amenities_button.setBackgroundColor(Color.TRANSPARENT);
                filter_locations_button.setBackgroundColor(Color.TRANSPARENT);
                filter_bsp_button.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });


        // Recycler views
        filter_bhk_recyclerView = findViewById(R.id.filter_bhk_recyclerView);
        filter_bhk_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BhkAdapter mBhkAdapter = new BhkAdapter(this, mSearchFilter.getFilterRooms().getRooms().getBuckets());
        filter_bhk_recyclerView.setAdapter(mBhkAdapter);


        // price range filter

        filter_price_rangebar = findViewById(R.id.filter_price_rangebar);
        filter_price_selected_min_cost = findViewById(R.id.filter_price_selected_min_cost);
        filter_price_selected_max_cost = findViewById(R.id.filter_price_selected_max_cost);

        List<Bucket___> priceBuckets = mSearchFilter.getFilterPrice().getPrice().getBuckets();

        Long minPriceFilter =  priceBuckets.get(0).getKey();
        Long maxPriceFilter =  priceBuckets.get(priceBuckets.size()-1).getKey();

        filter_price_rangebar.setTickInterval(100000);

        filter_price_rangebar.setTickEnd(maxPriceFilter);
        filter_price_rangebar.setTickStart(minPriceFilter);


        filter_price_selected_min_cost.setText(priceFormat(minPriceFilter,0));
        filter_price_selected_max_cost.setText(priceFormat(maxPriceFilter,0));

        filter_price_rangebar.setPinTextFormatter(new RangeBar.PinTextFormatter() {
            @Override
            public String getText(String value) {
                return priceFormat(Double.parseDouble(value), 0);
            }
        });

        filter_price_rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                filter_price_selected_min_cost.setText(leftPinValue);
                filter_price_selected_max_cost.setText(rightPinValue);

                if(mSearchFilter.getFilterPrice().getSelected() == null){
                    mSearchFilter.getFilterPrice().setSelected(new Selected_());
                }

                mSearchFilter.getFilterPrice().getSelected().setFrom((long)rangeBar.getTickStart() + (long)(leftPinIndex * rangeBar.getTickInterval()));
                mSearchFilter.getFilterPrice().getSelected().setTo((long)rangeBar.getTickStart() + (long)(rightPinIndex * rangeBar.getTickInterval()));

            }
        });

        if(mSearchFilter.getFilterPrice().getSelected() != null){
            filter_price_rangebar.setRangePinsByValue(
                    mSearchFilter.getFilterPrice().getSelected().getFrom(),
                    mSearchFilter.getFilterPrice().getSelected().getTo()
            );
        }


        // area range filter

        filter_area_rangebar = findViewById(R.id.filter_area_rangebar);
        filter_area_selected_min = findViewById(R.id.filter_area_selected_min);
        filter_area_selected_max = findViewById(R.id.filter_area_selected_max);

        List<Bucket____> areaBuckets = mSearchFilter.getFilterArea().getArea().getBuckets();

        Long minAreaFilter =  areaBuckets.get(0).getKey();
        Long maxAreaFilter =  areaBuckets.get(areaBuckets.size()-1).getKey();

        filter_area_rangebar.setTickInterval(100);

        filter_area_rangebar.setTickEnd(Float.valueOf(maxAreaFilter));
        filter_area_rangebar.setTickStart(Float.valueOf(minAreaFilter));


        filter_area_selected_min.setText(minAreaFilter.toString());
        filter_area_selected_max.setText(maxAreaFilter.toString());


        filter_area_rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                filter_area_selected_min.setText(leftPinValue);
                filter_area_selected_max.setText(rightPinValue);

                if(mSearchFilter.getFilterArea().getSelected() == null){
                    mSearchFilter.getFilterArea().setSelected(new Selected__());
                }

                mSearchFilter.getFilterArea().getSelected().setFrom((long)rangeBar.getTickStart() + (long)(leftPinIndex * rangeBar.getTickInterval()));
                mSearchFilter.getFilterArea().getSelected().setTo((long)rangeBar.getTickStart() + (long)(rightPinIndex * rangeBar.getTickInterval()));

            }
        });

        if(mSearchFilter.getFilterArea().getSelected() != null){
            filter_area_rangebar.setRangePinsByValue(
                    mSearchFilter.getFilterArea().getSelected().getFrom(),
                    mSearchFilter.getFilterArea().getSelected().getTo()
            );
        }



        // Status Filter

        filter_status_recyclerView = findViewById(R.id.filter_status_recyclerView);
        filter_status_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StatusAdapter mStatusAdapter = new StatusAdapter(this, mSearchFilter.getFilterStatus().getStatus().getBuckets());
        filter_status_recyclerView.setAdapter(mStatusAdapter);


        // Filter Amenities

        filter_amenities_recyclerView = findViewById(R.id.filter_amenities_recyclerView);
        filter_amenities_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AmenityAdapter mAmenityAdapter = new AmenityAdapter(this, mSearchFilter.getFilterAmenities().getAmenities().getBuckets());
        filter_amenities_recyclerView.setAdapter(mAmenityAdapter);


        // Filter location

        filter_locations_recyclerView = findViewById(R.id.filter_locations_recyclerView);
        filter_locations_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocationAdapter mLocationAdapter = new LocationAdapter(this, mSearchFilter.getFilterLocations().getLocations().getBuckets());
        filter_locations_recyclerView.setAdapter(mLocationAdapter);


        // bsp filter

        filter_bsp_rangebar = findViewById(R.id.filter_bsp_rangebar);
        filter_bsp_selected_min_cost = findViewById(R.id.filter_bsp_selected_min_cost);
        filter_bsp_selected_max_cost = findViewById(R.id.filter_bsp_selected_max_cost);

        List<Bucket_> bspBuckets = mSearchFilter.getFilterBsp().getBsp().getBuckets();

        Long minBspFilter =  bspBuckets.get(0).getKey();
        Long maxBspFilter =  bspBuckets.get(bspBuckets.size()-1).getKey();

        filter_bsp_rangebar.setTickInterval(100);

        filter_bsp_rangebar.setTickStart(minBspFilter);
        filter_bsp_rangebar.setTickEnd(maxBspFilter);

        filter_bsp_selected_min_cost.setText(minBspFilter.toString());
        filter_bsp_selected_max_cost.setText(maxBspFilter.toString());

        filter_bsp_rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                filter_bsp_selected_min_cost.setText(leftPinValue);
                filter_bsp_selected_max_cost.setText(rightPinValue);

                if(mSearchFilter.getFilterBsp().getSelected() == null){
                    mSearchFilter.getFilterBsp().setSelected(new Selected());
                }

                mSearchFilter.getFilterBsp().getSelected().setFrom((long)rangeBar.getTickStart() + (long)(leftPinIndex * rangeBar.getTickInterval()));
                mSearchFilter.getFilterBsp().getSelected().setTo((long)rangeBar.getTickStart() + (long)(rightPinIndex * rangeBar.getTickInterval()));
            }
        });

        if(mSearchFilter.getFilterBsp().getSelected() != null){
            filter_bsp_rangebar.setRangePinsByValue(
                    mSearchFilter.getFilterBsp().getSelected().getFrom(),
                    mSearchFilter.getFilterBsp().getSelected().getTo()
            );
        }

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
    private void getFilterData(){
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        String facets = sharedPref.getString("searchFilters", null);
        Gson filterGson = new Gson();
        Type filterType = new TypeToken<SearchFilter>(){}.getType();
        mSearchFilter = filterGson.fromJson(facets, filterType);
    }

    private void setFilterData(){
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String filterJson = gson.toJson(mSearchFilter);
        editor.putString("searchFilters", filterJson);
        editor.apply();
    }

    private void clearFilterData(){
        SharedPreferences sharedPref = getSharedPreferences("shared_preference_key", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        mSearchFilter.getFilterPrice().setSelected(null);
        mSearchFilter.getFilterBsp().setSelected(null);
        mSearchFilter.getFilterArea().setSelected(null);

        for(int i = 0; i < mSearchFilter.getFilterRooms().getRooms().getBuckets().size(); i++){
            mSearchFilter.getFilterRooms().getRooms().getBuckets().get(i).setChecked(false);
        }

        for(int i = 0; i < mSearchFilter.getFilterStatus().getStatus().getBuckets().size(); i++){
            mSearchFilter.getFilterStatus().getStatus().getBuckets().get(i).setChecked(false);
        }

        for(int i = 0; i < mSearchFilter.getFilterAmenities().getAmenities().getBuckets().size(); i++){
            mSearchFilter.getFilterAmenities().getAmenities().getBuckets().get(i).setChecked(false);
        }

        for(int i = 0; i < mSearchFilter.getFilterLocations().getLocations().getBuckets().size(); i++){
            mSearchFilter.getFilterLocations().getLocations().getBuckets().get(i).setChecked(false);
        }

        Gson gson = new Gson();
        String filterJson = gson.toJson(mSearchFilter);
        editor.putString("searchFilters", filterJson);
        editor.apply();
    }


    @Override
    public void onBhkcheckedCallback(Integer position, Boolean checkedValue) {
        mSearchFilter.getFilterRooms().getRooms().getBuckets().get(position).setChecked(checkedValue);
    }

    @Override
    public void onStatuscheckedCallback(Integer position, Boolean checkedValue) {
        mSearchFilter.getFilterStatus().getStatus().getBuckets().get(position).setChecked(checkedValue);
    }

    @Override
    public void onAmenitycheckedCallback(Integer position, Boolean checkedValue) {
        mSearchFilter.getFilterAmenities().getAmenities().getBuckets().get(position).setChecked(checkedValue);
    }

    @Override
    public void onLocationcheckedCallback(Integer position, Boolean checkedValue) {
        mSearchFilter.getFilterLocations().getLocations().getBuckets().get(position).setChecked(checkedValue);
    }


}
