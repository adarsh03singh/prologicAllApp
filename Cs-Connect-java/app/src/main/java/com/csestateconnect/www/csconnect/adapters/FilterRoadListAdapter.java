package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.project.ProjectRoad;

import java.util.LinkedList;

public class FilterRoadListAdapter extends RecyclerView.Adapter<FilterRoadListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilterRoadListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        CheckBox roadListView;

        final FilterRoadListAdapter mAdapter;

        public ProjectViewHolder(View itemView, FilterRoadListAdapter adapter) {
            super(itemView);
            roadListView = itemView.findViewById(R.id.roads_list_radioButton);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.road_list, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        Project mCurrentProject = mProjectList.get(position);
        Object[] get_roads_list = mCurrentProject.getProjectRoads().toArray();
        for (Object obj: get_roads_list) {
            String get_roads = ((ProjectRoad) obj).getName();

            projectViewHolder.roadListView.setText(get_roads);
        }
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
