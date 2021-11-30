package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.LeadDetailActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.lead.Lead;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class LeadListAdapter extends RecyclerView.Adapter<LeadListAdapter.LeadViewHolder> {


    private final LinkedList<Lead> mLeadList;
    private LayoutInflater mInflater;
    private Context mContext;

    public LeadListAdapter(Context context, LinkedList<Lead> mLeadList) {
        mInflater = LayoutInflater.from(context);
        this.mLeadList = mLeadList;
        this.mContext = context;
    }



    class LeadViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutview;
        public final TextView leadIdView;
        public final TextView leadDateView;
        public final TextView leadStatusView;
        public final TextView leadNameView;
        public final TextView leadEmailView;
        public final TextView leadMobileView;
        public final TextView leadBudgetView;
        public final TextView leadLocationView;


        final LeadListAdapter mAdapter;

        public LeadViewHolder(View itemView, LeadListAdapter adapter) {
            super(itemView);
            linearLayoutview = itemView.findViewById(R.id.leadListParentLayout);
            leadIdView = itemView.findViewById(R.id.lead_id);
            leadDateView = itemView.findViewById(R.id.lead_created_date);
            leadStatusView = itemView.findViewById(R.id.lead_status);
            leadNameView = itemView.findViewById(R.id.lead_name);
            leadEmailView = itemView.findViewById(R.id.lead_email);
            leadMobileView = itemView.findViewById(R.id.lead_phone_number);
            leadBudgetView = itemView.findViewById(R.id.lead_budget);
            leadLocationView = itemView.findViewById(R.id.lead_location);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public LeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.lead_list_item, parent, false);
        return new LeadViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final LeadViewHolder leadViewHolder, final int position) {
        Lead mCurrentLead = mLeadList.get(position);
        leadViewHolder.leadIdView.setText(mCurrentLead.getId());

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Date date = null;
        try {
            date = inputFormatter.parse(mCurrentLead.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        leadViewHolder.linearLayoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,LeadDetailActivity.class);
                intent.putExtra("leadIdbyLeadAdapter", mLeadList.get(position).getId());
                try {
                    intent.putExtra("dealIdbyLeadAdapter", mLeadList.get(position).getDealId().getId());
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                mContext.startActivity(intent);


            }
        });
        leadViewHolder.leadDateView.setText(outputFormatter.format(date));
        leadViewHolder.leadStatusView.setText(mCurrentLead.getLeadStatus().getName());
        leadViewHolder.leadNameView.setText(mCurrentLead.getName());
        leadViewHolder.leadEmailView.setText(mCurrentLead.getEmail());
        leadViewHolder.leadMobileView.setText(mCurrentLead.getPhoneNumber());
        leadViewHolder.leadBudgetView.setText(mCurrentLead.getBudget());
        String locationInfo = mCurrentLead.getLocation().getName() + ", " + mCurrentLead.getCity().getName();
        leadViewHolder.leadLocationView.setText(locationInfo);

    }

    @Override
    public int getItemCount() {
        return mLeadList.size();
    }



    private FragmentManager getSupportFragmentManager() {
        return null;
    }
}
