package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.csestateconnect.www.csconnect.ProjectDetailActivity;
import com.csestateconnect.www.csconnect.ProjectImageActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.lead.GetActivity;
import com.csestateconnect.www.csconnect.models.lead_activitied.ActivitiesList;
import com.csestateconnect.www.csconnect.models.project.ProjectImage;

import java.util.ArrayList;
import java.util.List;

public class ProjectImageSliderAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProjectImage> mImages;


    public ProjectImageSliderAdapter(Context context, List<ProjectImage> images) {
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
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = this.mInflater.inflate(R.layout.project_image, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.project_images_slider_image);
        Glide.with(this.mContext).load(this.mImages.get(position).getImageUrl()).into(myImage);

        final ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < mImages.size(); i++) {
            images.add(mImages.get(i).getImageUrl());
        }
        final String imageValues = images.toString();
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectImageActivity.class);
                intent.putExtra("project_value", imageValues);
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
