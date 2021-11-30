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
import com.csestateconnect.www.csconnect.models.project.ProjectAmenity;
import com.csestateconnect.www.csconnect.models.project.ProjectRoad;

import java.util.LinkedList;

public class FilterAttributesListAdapter extends RecyclerView.Adapter<FilterAttributesListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilterAttributesListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        CheckBox attributesListView;

        final FilterAttributesListAdapter mAdapter;

        public ProjectViewHolder(View itemView, FilterAttributesListAdapter adapter) {
            super(itemView);
            attributesListView = itemView.findViewById(R.id.attributes_list_radioButton);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.attributes_list, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        Project mCurrentProject = mProjectList.get(position);
        Object[] get_amenities_list = mCurrentProject.getProjectAmenities().toArray();
        for (Object obj: get_amenities_list) {
            String get_amenities = ((ProjectAmenity) obj).getName();

            projectViewHolder.attributesListView.setText(get_amenities);
        }
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
