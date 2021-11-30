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
import com.csestateconnect.www.csconnect.models.project.ProjectWowFactor;

import java.util.LinkedList;
import java.util.List;

public class ProjectWowFactorAdapter extends RecyclerView.Adapter<ProjectWowFactorAdapter.WowViewHolder> {

    private final List<ProjectWowFactor> mWowFactorList;
    private LayoutInflater mInflater;
    private Context mContext;
   int placeholder = -1;

    public ProjectWowFactorAdapter(Context context, List<ProjectWowFactor> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mWowFactorList = mProjectList;
        this.mContext = context;
    }

    class WowViewHolder extends RecyclerView.ViewHolder {
        public final TextView wowFactorsNameView;

        final ProjectWowFactorAdapter mAdapter;

        public WowViewHolder(View itemView, ProjectWowFactorAdapter adapter) {
            super(itemView);
            wowFactorsNameView = itemView.findViewById(R.id.wow_factors_txt);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public WowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wow_factor_list, parent, false);
        return new WowViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WowViewHolder projectViewHolder, final int position) {
        ProjectWowFactor mCurrentProject = mWowFactorList.get(position);

        projectViewHolder.wowFactorsNameView.setText(mCurrentProject.getName());

    }

    @Override
    public int getItemCount() {
        return mWowFactorList.size();
    }
}
