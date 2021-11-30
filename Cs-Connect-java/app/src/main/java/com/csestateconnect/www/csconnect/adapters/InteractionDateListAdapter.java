package com.csestateconnect.www.csconnect.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.interaction_dates.DateList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


    public class InteractionDateListAdapter extends RecyclerView.Adapter<InteractionDateListAdapter.InteractionDateViewHolder> {

        private List<DateList> dateList;

        public class InteractionDateViewHolder extends RecyclerView.ViewHolder {
            public TextView dateView;

            public InteractionDateViewHolder(View view) {
                super(view);
                dateView = (TextView) view.findViewById(R.id.interaction_dateView);

            }
        }


        public InteractionDateListAdapter(List<DateList> dateList) {
            this.dateList = dateList;
        }

        @Override
        public InteractionDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interaction_dates_list_item, parent, false);

            return new InteractionDateViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InteractionDateViewHolder holder, int position) {
            DateList listDate = dateList.get(position);
            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm");
            Date date = null;
            try {
                date = inputFormatter.parse(listDate.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.dateView.setText(outputFormatter.format(date));

        }

        @Override
        public int getItemCount() {
            return dateList.size();
        }
    }
