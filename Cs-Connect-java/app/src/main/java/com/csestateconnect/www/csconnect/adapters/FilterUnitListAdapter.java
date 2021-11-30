package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.project.ProjectRoad;

import java.util.LinkedList;

public class FilterUnitListAdapter extends RecyclerView.Adapter<FilterUnitListAdapter.ProjectViewHolder> {

    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilterUnitListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        RadioButton bhkView;

        final FilterUnitListAdapter mAdapter;

        public ProjectViewHolder(View itemView, FilterUnitListAdapter adapter) {
            super(itemView);
            bhkView = itemView.findViewById(R.id.units_bhk_radioButton);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.units_list, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        Project mCurrentProject = mProjectList.get(position);

/*        Object[] get_roads_list = mCurrentProject.getBhks();
        for (Object obj: get_roads_list) {
            String get_roads = ((ProjectRoad) obj).getName();

            projectViewHolder.bhkView.setText(get_roads);
        }*/
    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
