package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.DealDetailActivity;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.deal.Deal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.DealViewHolder> {



    private final LinkedList<Deal> mDealList;
    private LayoutInflater mInflater;
    private Context mContext;

    public DealListAdapter(Context context, LinkedList<Deal> mDealList) {
        mInflater = LayoutInflater.from(context);
        this.mDealList = mDealList;
        this.mContext = context;
    }

    class DealViewHolder extends RecyclerView.ViewHolder {
        public final TextView dealIdView;
        public final TextView dealDateView;
        public final TextView dealStatusView;
        public final TextView dealClientNameView;
        public final TextView dealProjectNameView;
        public final TextView dealEmailView;
        public final TextView dealMobileView;
        public final TextView dealTotalAmountView;
        public final TextView dealPaybleAmountView;
        ImageView dealImageViewUpsider;
        ImageView dealImageViewDownSider;
        LinearLayout rmLayout;
        LinearLayout parentLayout;

        final DealListAdapter mAdapter;

        public DealViewHolder(View itemView, DealListAdapter adapter) {
            super(itemView);
            dealIdView = itemView.findViewById(R.id.deal_id);
            dealDateView = itemView.findViewById(R.id.deal_date);
            dealStatusView = itemView.findViewById(R.id.deal_status);
            dealClientNameView = itemView.findViewById(R.id.deal_client_name);
            dealProjectNameView = itemView.findViewById(R.id.deal_project_name);
            dealEmailView = itemView.findViewById(R.id.deal_email);
            dealMobileView = itemView.findViewById(R.id.deal_phone_number);
            dealTotalAmountView = itemView.findViewById(R.id.deal_total_amount);
            dealPaybleAmountView = itemView.findViewById(R.id.deal_payble_amount);
            dealImageViewUpsider = itemView.findViewById(R.id.dealImageViewUp);
            dealImageViewDownSider = itemView.findViewById(R.id.dealImageViewDown);
            rmLayout = itemView.findViewById(R.id.deal_linearLayout);
            parentLayout = itemView.findViewById(R.id.deal_parent_layout);
            this.mAdapter = adapter;
        }

    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.deal_list_item, parent, false);
        return new DealViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final DealViewHolder dealViewHolder, final int position) {
        final Deal mCurrentDeal = mDealList.get(position);
        dealViewHolder.dealIdView.setText(mCurrentDeal.getId().toString());

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Date date = null;
        try {
            date = inputFormatter.parse(mCurrentDeal.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dealViewHolder.dealEmailView.setText(mCurrentDeal.getLead().getEmail());
        dealViewHolder.dealDateView.setText(outputFormatter.format(date));
        dealViewHolder.dealStatusView.setText(mCurrentDeal.getDealStatus().getName());
        dealViewHolder.dealClientNameView.setText(mCurrentDeal.getLead().getName());
        dealViewHolder.dealProjectNameView.setText(mCurrentDeal.getProject().getName());
        dealViewHolder.dealMobileView.setText(mCurrentDeal.getLead().getPhoneNumber());
        dealViewHolder.dealTotalAmountView.setText(mCurrentDeal.getCommissionAmountTotalView());
        dealViewHolder.dealPaybleAmountView.setText(mCurrentDeal.getCommissionAmountPaidView());

        dealViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DealDetailActivity.class);
                intent.putExtra("dealIdbyDealAdapter",mDealList.get(position).getId() );
                mContext.startActivity(intent);

            }
        });

        dealViewHolder.dealImageViewUpsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealViewHolder.rmLayout.getVisibility() == View.GONE) {
                    dealViewHolder.dealImageViewUpsider.setVisibility(View.GONE);
                    dealViewHolder.dealImageViewDownSider.setVisibility(View.VISIBLE);
                    dealViewHolder.rmLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        dealViewHolder.dealImageViewDownSider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealViewHolder.rmLayout.getVisibility() == View.VISIBLE) {
                    dealViewHolder.dealImageViewUpsider.setVisibility(View.VISIBLE);
                    dealViewHolder.dealImageViewDownSider.setVisibility(View.GONE);
                    dealViewHolder.rmLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }
}
