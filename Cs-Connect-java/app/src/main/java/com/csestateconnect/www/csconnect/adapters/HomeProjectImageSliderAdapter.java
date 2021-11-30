package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.ProjectDetailActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.home.HomeProjectImageSliderList;

import java.util.List;

public class HomeProjectImageSliderAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<HomeProjectImageSliderList> mImages;
//    private int custom_position = 0;

    public HomeProjectImageSliderAdapter(Context context, List<HomeProjectImageSliderList> images) {
        this.mContext = context;
        this.mImages = images;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.mImages.size();
//        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = this.mInflater.inflate(R.layout.home_project_list_item, view, false);

//        if(custom_position > 6)
//            custom_position = 0;
        HomeProjectImageSliderList sliderList = mImages.get(position);
//        custom_position++;

        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.project_icon_image);
        TextView projectNameView = (TextView) myImageLayout.findViewById(R.id.project_name_slider);
        TextView projectlocationView = (TextView) myImageLayout.findViewById(R.id.project_address_slider);
        Glide.with(this.mContext).load(sliderList.getSliderIconImage()).into(myImage);


        String projectName = sliderList.getProjectName();
        String projectlocation = sliderList.getProjectLocation();
        final Integer projectIdView = sliderList.getProjectId();

        projectNameView.setText(projectName);
        projectlocationView.setText(projectlocation);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectDetailActivity.class);
                intent.putExtra("project_id", projectIdView);
                mContext.startActivity(intent);
            }
        });


        view.addView(myImageLayout, 0);
        return myImageLayout;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
