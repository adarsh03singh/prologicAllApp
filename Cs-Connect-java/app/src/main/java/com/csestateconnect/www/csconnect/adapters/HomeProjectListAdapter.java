package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csestateconnect.www.csconnect.ProjectDetailActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.Project;

import java.util.LinkedList;

public class HomeProjectListAdapter extends RecyclerView.Adapter<HomeProjectListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public HomeProjectListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView projectNameView;
        public final ImageView projectIconImageView;
        public final TextView projectLocationInfo;
        public final RelativeLayout LayoutView;


        final HomeProjectListAdapter mAdapter;

        public ProjectViewHolder(View itemView, HomeProjectListAdapter adapter) {
            super(itemView);
            projectNameView =(TextView) itemView.findViewById(R.id.project_name);
            projectIconImageView = itemView.findViewById(R.id.project_image);
            projectLocationInfo =(TextView) itemView.findViewById(R.id.project_address);
            LayoutView = itemView.findViewById(R.id.home_project_parent_layout);
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
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, final int position) {
        Project mCurrentProject = mProjectList.get(position);

        projectViewHolder.projectNameView.setText(mCurrentProject.getName());
        String locationInfo = mCurrentProject.getLocation().getName() + ", " + mCurrentProject.getCity().getName();
        projectViewHolder.projectLocationInfo.setText(locationInfo);

        String priceRange = mCurrentProject.getLowCostView() + " - " + mCurrentProject.getHighCostView();

        Glide.with(mContext).load(mCurrentProject.getIconImage()).into(projectViewHolder.projectIconImageView);

        projectViewHolder.LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectDetailActivity.class);
                intent.putExtra("project_id", mProjectList.get(position).getId());
                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
