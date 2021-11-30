package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project_detail.Amenity;
import com.csestateconnect.www.csconnect.models.project_detail.AmenityCategory;
import java.util.List;

public class ProjectAmenityCategoryAdaptor extends PagerAdapter  {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<AmenityCategory> mAmenityCategories;


    public ProjectAmenityCategoryAdaptor(Context context, List<AmenityCategory> amenityCategories) {
        mContext = context;
        mAmenityCategories = amenityCategories;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return mAmenityCategories.get(position).getName();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.mAmenityCategories.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View amenityCategoryLayout = this.mInflater.inflate(R.layout.project_amenities_fragment, view, false);

        RecyclerView amenitiesRecyclerview = amenityCategoryLayout.findViewById(R.id.project_amenities_recyclerview);
        amenitiesRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        List<Amenity> rvAmenities=  mAmenityCategories.get(position).getAmenities();
        ProjectAmenityAdaptor projectAmenityAdaptor = new ProjectAmenityAdaptor(view.getContext(), rvAmenities);
        amenitiesRecyclerview.setAdapter(projectAmenityAdaptor);
        view.addView(amenityCategoryLayout, 0);

        return amenityCategoryLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


}
