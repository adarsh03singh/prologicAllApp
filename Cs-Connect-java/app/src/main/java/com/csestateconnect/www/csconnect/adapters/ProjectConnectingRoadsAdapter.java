package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.ProjectRoad;
import com.csestateconnect.www.csconnect.models.project.ProjectWowFactor;

import java.util.List;

public class ProjectConnectingRoadsAdapter extends RecyclerView.Adapter<ProjectConnectingRoadsAdapter.WowViewHolder> {

    private final List<ProjectRoad> mRoadsList;
    private LayoutInflater mInflater;
    private Context mContext;
   int placeholder = -1;

    public ProjectConnectingRoadsAdapter(Context context, List<ProjectRoad> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mRoadsList = mProjectList;
        this.mContext = context;
    }

    class WowViewHolder extends RecyclerView.ViewHolder {
        public final TextView wowFactorsNameView;

        final ProjectConnectingRoadsAdapter mAdapter;

        public WowViewHolder(View itemView, ProjectConnectingRoadsAdapter adapter) {
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
        ProjectRoad mCurrentProject = mRoadsList.get(position);

        projectViewHolder.wowFactorsNameView.setText(mCurrentProject.getName());

    }

    @Override
    public int getItemCount() {
        return mRoadsList.size();
    }
}
