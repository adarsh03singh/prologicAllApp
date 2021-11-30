package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.project.Project;

import java.util.LinkedList;

public class FilterPriceListAdapter extends RecyclerView.Adapter<FilterPriceListAdapter.ProjectViewHolder> {


    private final LinkedList<Project> mProjectList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilterPriceListAdapter(Context context, LinkedList<Project> mProjectList) {
        mInflater = LayoutInflater.from(context);
        this.mProjectList = mProjectList;
        this.mContext = context;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
 //       SeekBar priceProgressView;
//        SeekBar areaProgressView;
        TextView priceLowCost;
        TextView priceHighCost;
        TextView areaLowRange;
        TextView areaHighRange;

        final FilterPriceListAdapter mAdapter;

        public ProjectViewHolder(View itemView, FilterPriceListAdapter adapter) {
            super(itemView);
 //           priceProgressView = itemView.findViewById(R.id.priceSeekBar);
            priceLowCost = itemView.findViewById(R.id.lowCostPrice_txt);
            priceHighCost = itemView.findViewById(R.id.highCostPrice_txt);

//            areaProgressView = itemView.findViewById(R.id.areaSeekBar);
            areaLowRange = itemView.findViewById(R.id.lowAreaRange_txt);
            areaHighRange = itemView.findViewById(R.id.highAreaRange_txt);

            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.price_list, parent, false);
        return new ProjectViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int position) {
        Project mCurrentProject = mProjectList.get(position);

//            String priceBar = mCurrentProject.getC;
        String lowPrice = mCurrentProject.getLowCostView();
        String highPrice = mCurrentProject.getHighCostView();
//        Integer areaBar = mCurrentProject.getProjectOpenAreaPercentage();
        String lowAreaRang = mCurrentProject.getProjectOpenAreaView();
        String highAreaRang = mCurrentProject.getProjectTotalAreaView();

            projectViewHolder.priceLowCost.setText(lowPrice);
        projectViewHolder.priceHighCost.setText(highPrice);
        projectViewHolder.areaHighRange.setText(highAreaRang);
        projectViewHolder.areaLowRange.setText(lowAreaRang);

     /*   projectViewHolder.areaProgressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lowValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lowValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                areaLowRange.setText( seekBar.getMax());
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mProjectList.size();
    }
}
