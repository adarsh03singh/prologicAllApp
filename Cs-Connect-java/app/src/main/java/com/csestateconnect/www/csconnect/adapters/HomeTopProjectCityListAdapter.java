package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.top_project_city.TopProjectCity;

import java.util.LinkedList;

public class HomeTopProjectCityListAdapter extends RecyclerView.Adapter<HomeTopProjectCityListAdapter.ProjectViewHolder> {

    private final LinkedList<TopProjectCity> mProjectCityList;
    private LayoutInflater mInflater;
    private Context mContext;

    public HomeTopProjectCityListAdapter(Context context, LinkedList<TopProjectCity> mProjectCityList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectCityList = mProjectCityList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView projectCityNameView;
        public final ImageView projectCityIconImageView;



        final HomeTopProjectCityListAdapter mAdapter;

        public ProjectViewHolder(View itemView, HomeTopProjectCityListAdapter adapter) {
            super(itemView);
            projectCityNameView =(TextView) itemView.findViewById(R.id.project_name);
            projectCityIconImageView = itemView.findViewById(R.id.project_image);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.home_project_list_item, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        TopProjectCity mCurrentProject = mProjectCityList.get(position);
        projectViewHolder.projectCityNameView.setText(mCurrentProject.getCities());

        Glide.with(mContext).load(mCurrentProject.getProjectsDetails().getCity()).into(projectViewHolder.projectCityIconImageView);
    }

    @Override
    public int getItemCount() {
        return mProjectCityList.size();
    }
}
