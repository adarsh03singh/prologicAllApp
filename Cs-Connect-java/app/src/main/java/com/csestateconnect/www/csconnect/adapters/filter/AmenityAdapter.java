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
import com.csestateconnect.www.csconnect.models.search.Bucket;

import java.util.List;

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.AmenityViewHolder> {
    private final List<Bucket> mAmenityList;
    private LayoutInflater mInflater;
    private Context mContext;
    private FilterAdapterCallback mFilterAdapterCallback;

    public AmenityAdapter(Context context, List<Bucket> amenityList) {
        mInflater = LayoutInflater.from(context);
        this.mAmenityList = amenityList;
        this.mContext = context;

        try {
            this.mFilterAdapterCallback = ((FilterAdapterCallback)context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }


    }

    class AmenityViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox amenity_checkbox;

        final AmenityAdapter mAdapter;

        public AmenityViewHolder(View itemView, AmenityAdapter adapter) {
            super(itemView);
            amenity_checkbox = (CheckBox) itemView.findViewById(R.id.filter_amenity_check_item);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public AmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.amenity_item, parent, false);
        return new AmenityViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityViewHolder amenityViewHolder, final int position) {
        Bucket mCurrentAmenity = mAmenityList.get(position);

        amenityViewHolder.amenity_checkbox.setText(mCurrentAmenity.getKey());

        if (mCurrentAmenity.getChecked() != null){
            amenityViewHolder.amenity_checkbox.setChecked(mCurrentAmenity.getChecked());
        }else{
            amenityViewHolder.amenity_checkbox.setChecked(false);
        }

        amenityViewHolder.amenity_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mFilterAdapterCallback.onAmenitycheckedCallback(position, isChecked);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mAmenityList.size();
    }

}
