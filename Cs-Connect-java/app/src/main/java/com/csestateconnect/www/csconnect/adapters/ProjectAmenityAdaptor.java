package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project_detail.Amenity;
import java.util.List;

public class ProjectAmenityAdaptor extends RecyclerView.Adapter<ProjectAmenityAdaptor.ProjectAmenityViewHolder> {

    private final List<Amenity> mAmenityList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ProjectAmenityAdaptor(Context context, List<Amenity> amenityList) {
        mInflater = LayoutInflater.from(context);
        this.mAmenityList = amenityList;
        this.mContext = context;
    }

    class ProjectAmenityViewHolder extends RecyclerView.ViewHolder {
        public final ImageView amenityImageView;
        public final TextView amenityNameView;


        final ProjectAmenityAdaptor mAdapter;

        public ProjectAmenityViewHolder(View itemView, ProjectAmenityAdaptor adapter) {
            super(itemView);
            amenityImageView = itemView.findViewById(R.id.project_amenity_item_image);
            amenityNameView = itemView.findViewById(R.id.project_amenity_item_name);

            mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectAmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.project_amenity_item, parent, false);
        return new ProjectAmenityViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAmenityViewHolder amenityViewHolder, int position) {
        Amenity mCurrentAmenity = mAmenityList.get(position);

        amenityViewHolder.amenityNameView.setText(mCurrentAmenity.getName());
        Glide.with(mContext).load(mCurrentAmenity.getImage()).into(amenityViewHolder.amenityImageView);

    }

    @Override
    public int getItemCount() {
        return mAmenityList.size();
    }

}
