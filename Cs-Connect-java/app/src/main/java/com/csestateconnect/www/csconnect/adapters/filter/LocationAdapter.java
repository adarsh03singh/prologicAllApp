package com.csestateconnect.www.csconnect.adapters.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.csestateconnect.www.csconnect.FilterAdapterCallback;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.search.Bucket______;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private final List<Bucket______> mLocationList;
    private LayoutInflater mInflater;
    private Context mContext;
    private FilterAdapterCallback mFilterAdapterCallback;

    public LocationAdapter(Context context, List<Bucket______> locationList) {
        mInflater = LayoutInflater.from(context);
        this.mLocationList = locationList;
        this.mContext = context;

        try {
            this.mFilterAdapterCallback = ((FilterAdapterCallback)context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox location_checkbox;

        final LocationAdapter mAdapter;

        public LocationViewHolder(View itemView, LocationAdapter adapter) {
            super(itemView);
            location_checkbox = (CheckBox) itemView.findViewById(R.id.filter_location_check_item);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, final int position) {
        Bucket______ mCurrentLocation = mLocationList.get(position);

        locationViewHolder.location_checkbox.setText(mCurrentLocation.getKey());

        if (mCurrentLocation.getChecked() != null){
            locationViewHolder.location_checkbox.setChecked(mCurrentLocation.getChecked());
        }else{
            locationViewHolder.location_checkbox.setChecked(false);
        }

        locationViewHolder.location_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mFilterAdapterCallback.onLocationcheckedCallback(position, isChecked);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

}
