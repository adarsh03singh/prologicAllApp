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

import java.util.LinkedList;

public class FilterStatusListAdapter extends RecyclerView.Adapter<FilterStatusListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilterStatusListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        CheckBox statusListView;

        final FilterStatusListAdapter mAdapter;

        public ProjectViewHolder(View itemView, FilterStatusListAdapter adapter) {
            super(itemView);
            statusListView = itemView.findViewById(R.id.status_list_radio_button);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.status_list, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        Project mCurrentProject = mProjectList.get(position);

            String get_status = mCurrentProject.getProjectCompletionStatus().getName();

            projectViewHolder.statusListView.setText(get_status);

    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
