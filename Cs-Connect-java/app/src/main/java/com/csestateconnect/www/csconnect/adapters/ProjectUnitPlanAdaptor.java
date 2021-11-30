package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.UnitPlan;
import java.util.List;

public class ProjectUnitPlanAdaptor extends PagerAdapter  {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<UnitPlan> mUnitPlans;


    public ProjectUnitPlanAdaptor(Context context, List<UnitPlan> unitPlans) {
        mContext = context;
        mUnitPlans = unitPlans;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return mUnitPlans.get(position).getBhkType().getName();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.mUnitPlans.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View unitPlanLayout = this.mInflater.inflate(R.layout.project_unit_plan_fragment, view, false);

        TextView builtUpArea = unitPlanLayout.findViewById(R.id.built_up_area);
        TextView carpetArea = unitPlanLayout.findViewById(R.id.carpet_area);

        ImageView unitPlanImage = unitPlanLayout.findViewById(R.id.unit_plan_image);


        UnitPlan unitPlan=  mUnitPlans.get(position);

        builtUpArea.setText(unitPlan.getBuiltUpAreaView());
        carpetArea.setText(unitPlan.getCarpetAreaView());

        Glide.with(mContext).load(unitPlan.getIconImage()).into(unitPlanImage);

        view.addView(unitPlanLayout, 0);

        return unitPlanLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


}
