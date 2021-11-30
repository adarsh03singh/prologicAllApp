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

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;
   int placeholder = -1;

    public ProjectListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final TextView projectNameView;
        public final ImageView projectIconImageView;
        public final TextView availableBhks;

        public final ProgressBar projectStatusProgressBar;
        public final TextView projectType;
        public final TextView projectStatus;

        public final TextView projectLocationInfo;
        public final TextView projectPrice;
        public final RelativeLayout LayoutView;

        final ProjectListAdapter mAdapter;

        public ProjectViewHolder(View itemView, ProjectListAdapter adapter) {
            super(itemView);
            projectNameView = itemView.findViewById(R.id.project_name);
            projectIconImageView = itemView.findViewById(R.id.project_image);
            availableBhks = itemView.findViewById(R.id.project_available_bhks);

            projectStatus = itemView.findViewById(R.id.project_status);
            projectStatusProgressBar = itemView.findViewById(R.id.statusProgressBar);
            projectType = itemView.findViewById(R.id.project_type);
            LayoutView = itemView.findViewById(R.id.project_parent_layout);

            projectLocationInfo = itemView.findViewById(R.id.project_address);
            projectPrice = itemView.findViewById(R.id.project_price_range);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.project_list_item, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, final int position) {
        Project mCurrentProject = mProjectList.get(position);
        projectViewHolder.LayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectDetailActivity.class);
                intent.putExtra("project_id", mProjectList.get(position).getId());
                mContext.startActivity(intent);


            }
        });

        projectViewHolder.projectNameView.setText(mCurrentProject.getName());
        projectViewHolder.availableBhks.setText(String.format("%s BHK", mCurrentProject.getBhks()));

        // TODO: - add actual types
        projectViewHolder.projectType.setText("Apartment");
        projectViewHolder.projectStatus.setText(mCurrentProject.getProjectCompletionStatus().getName());
        projectViewHolder.projectStatusProgressBar.setProgress(mCurrentProject.getProjectCompletionStatus().getCompletionPercentage());

        String locationInfo = mCurrentProject.getLocation().getName() + ", " + mCurrentProject.getCity().getName();
        projectViewHolder.projectLocationInfo.setText(locationInfo);

        String priceRange = mCurrentProject.getLowCostView() + " - " + mCurrentProject.getHighCostView();
        projectViewHolder.projectPrice.setText(priceRange);

        Glide.with(mContext).load(mCurrentProject.getIconImage()).into(projectViewHolder.projectIconImageView);
//        if(mCurrentProject.getIconImage().equals(true))
//        projectViewHolder.projectIconImageView.setBackgroundResource(android.R.color.transparent);

   /*     Glide.with(mContext)
                .load(mCurrentProject.getIconImage())(projectViewHolder.projectIconImageView)
                .centerCrop()
                .placeholder(R.drawable.image_background_before_uploader)
                .crossFade()
                .into(projectViewHolder.projectIconImageView);
*/

//        Glide.with(mContext)
//                .load(mCurrentProject.getIconImage())
//                .place(R.drawable.image_background_before_uploader)
//                .into(projectViewHolder.projectIconImageView);
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
