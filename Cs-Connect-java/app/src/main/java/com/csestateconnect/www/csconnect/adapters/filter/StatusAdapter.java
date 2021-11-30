package com.csestateconnect.www.csconnect.adapters.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.csestateconnect.www.csconnect.FilterAdapterCallback;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.search.Bucket_____;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private final List<Bucket_____> mStatusList;
    private LayoutInflater mInflater;
    private Context mContext;
    private FilterAdapterCallback mFilterAdapterCallback;

    public StatusAdapter(Context context, List<Bucket_____> statusList) {
        mInflater = LayoutInflater.from(context);
        this.mStatusList = statusList;
        this.mContext = context;

        try {
            this.mFilterAdapterCallback = ((FilterAdapterCallback)context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    class StatusViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox status_checkbox;

        final StatusAdapter mAdapter;

        public StatusViewHolder(View itemView, StatusAdapter adapter) {
            super(itemView);
            status_checkbox = (CheckBox) itemView.findViewById(R.id.filter_status_check_item);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.status_item, parent, false);
        return new StatusViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, final int position) {
        Bucket_____ mCurrentStatus = mStatusList.get(position);

        statusViewHolder.status_checkbox.setText(mCurrentStatus.getKey());

        if (mCurrentStatus.getChecked() != null){
            statusViewHolder.status_checkbox.setChecked(mCurrentStatus.getChecked());
        }else{
            statusViewHolder.status_checkbox.setChecked(false);
        }

        statusViewHolder.status_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mFilterAdapterCallback.onStatuscheckedCallback(position, isChecked);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mStatusList.size();
    }

}
