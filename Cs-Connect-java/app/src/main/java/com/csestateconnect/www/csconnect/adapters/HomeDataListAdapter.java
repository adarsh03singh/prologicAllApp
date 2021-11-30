package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.home.BrokerDetail;
import com.csestateconnect.www.csconnect.models.home.HomeAllData;

import java.util.LinkedList;
import java.util.List;

public class HomeDataListAdapter extends RecyclerView.Adapter<HomeDataListAdapter.HomeDataViewHolder> {

    private final List<BrokerDetail> mhomeDataList;
    private LayoutInflater mInflater;
    private Context mContext;

    public HomeDataListAdapter(Context context, List<BrokerDetail> mhomeDataList) {
        mInflater = LayoutInflater.from(context);
        this.mhomeDataList = mhomeDataList;
        this.mContext = context;
    }

    class HomeDataViewHolder extends RecyclerView.ViewHolder {
        private TextView channelPartnerSView;

        final HomeDataListAdapter mAdapter;

        public HomeDataViewHolder(View itemView, HomeDataListAdapter adapter) {
            super(itemView);
            channelPartnerSView=(TextView) itemView.findViewById(R.id.channel_partner_first);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public HomeDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.chennel_partner_items, parent, false);
        return new HomeDataViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDataViewHolder projectViewHolder, final int position) {
        BrokerDetail mCurrentHomeData = mhomeDataList.get(position);

        projectViewHolder.channelPartnerSView.setText(mCurrentHomeData.getName());
    }

    @Override
    public int getItemCount() {
        return mhomeDataList.size();
    }
}
