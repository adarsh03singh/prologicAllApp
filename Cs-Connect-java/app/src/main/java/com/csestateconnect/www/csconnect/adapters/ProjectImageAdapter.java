package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.ProjectImageActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.ProjectImage;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class ProjectImageAdapter extends PagerAdapter  {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mImages;


    public ProjectImageAdapter(Context context, List<String>  images) {
        mContext = context;
        mImages = images;
        mInflater = LayoutInflater.from(context);
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
        View myImageLayout = this.mInflater.inflate(R.layout.project_image_list, view, false);
        final PhotoView myImage = (PhotoView) myImageLayout.findViewById(R.id.project_images);
        Glide.with(mContext).load(this.mImages.get(position)).into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


}
