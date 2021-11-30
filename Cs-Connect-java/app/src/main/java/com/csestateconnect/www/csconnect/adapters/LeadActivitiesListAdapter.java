package com.csestateconnect.www.csconnect.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.interaction_dates.DateList;
import com.csestateconnect.www.csconnect.models.lead.GetActivity;
import com.csestateconnect.www.csconnect.models.lead_activitied.ActivitiesList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class LeadActivitiesListAdapter extends RecyclerView.Adapter<LeadActivitiesListAdapter.ActivitiesViewHolder> {

    private List<GetActivity> activitiesList;

    public class ActivitiesViewHolder extends RecyclerView.ViewHolder {
        public TextView dateView;
        public TextView activityView;

        public ActivitiesViewHolder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.activity_date_listView);
            activityView = (TextView) view.findViewById(R.id.activities_list_view);

        }
    }


    public LeadActivitiesListAdapter(List<GetActivity> activitiesList) {
        this.activitiesList = activitiesList;
    }

    @Override
    public ActivitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lead_activity_list_item, parent, false);

        return new ActivitiesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivitiesViewHolder holder, int position) {
        GetActivity activity_list = activitiesList.get(position);

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Date date = null;
        try {
            date = inputFormatter.parse(activity_list.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.dateView.setText(outputFormatter.format(date));
        holder.activityView.setText(activity_list.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }
}
