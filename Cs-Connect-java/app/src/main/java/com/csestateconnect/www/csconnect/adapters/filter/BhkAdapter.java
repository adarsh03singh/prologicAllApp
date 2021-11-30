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
import com.csestateconnect.www.csconnect.models.search.Bucket__;

import java.util.List;

public class BhkAdapter extends RecyclerView.Adapter<BhkAdapter.BhkViewHolder> {
    private final List<Bucket__> mBhkList;
    private LayoutInflater mInflater;
    private Context mContext;

    private FilterAdapterCallback mFilterAdapterCallback;

    public BhkAdapter(Context context, List<Bucket__> bhkList) {
        mInflater = LayoutInflater.from(context);
        this.mBhkList = bhkList;
        this.mContext = context;

        try {
            this.mFilterAdapterCallback = ((FilterAdapterCallback)context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }

    class BhkViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox bhk_checkbox;

        final BhkAdapter mAdapter;

        public BhkViewHolder(View itemView, BhkAdapter adapter) {
            super(itemView);
            bhk_checkbox = (CheckBox) itemView.findViewById(R.id.filter_bhk_check_item);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public BhkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.bhk_item, parent, false);
        return new BhkViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull BhkViewHolder bhkViewHolder, final int position) {
        Bucket__ mCurrentBhk = mBhkList.get(position);

        bhkViewHolder.bhk_checkbox.setText(mCurrentBhk.getKey().toString() + " BHK");

        if (mCurrentBhk.getChecked() != null){
            bhkViewHolder.bhk_checkbox.setChecked(mCurrentBhk.getChecked());
        }else{
            bhkViewHolder.bhk_checkbox.setChecked(false);
        }

        bhkViewHolder.bhk_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mFilterAdapterCallback.onBhkcheckedCallback(position, isChecked);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mBhkList.size();
    }

}
