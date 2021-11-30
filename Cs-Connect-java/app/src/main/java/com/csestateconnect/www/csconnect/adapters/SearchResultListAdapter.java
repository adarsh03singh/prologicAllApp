package com.csestateconnect.www.csconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csestateconnect.www.csconnect.MainActivity;
import com.csestateconnect.www.csconnect.ProjectDetailActivity;
import com.csestateconnect.www.csconnect.ProjectFragment;
import com.csestateconnect.www.csconnect.R;
import com.csestateconnect.www.csconnect.models.search.SearchResult;

import java.util.LinkedList;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.SearchResultViewHolder> {

    private final LinkedList<SearchResult> mSearchResultList;
    private LayoutInflater mInflater;
    private Context mContext;

    public SearchResultListAdapter(Context context, LinkedList<SearchResult> searchResultList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mSearchResultList = searchResultList;
    }


    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public final ImageView logoItemView;
        public final TextView projectName;
        public final TextView projectLocation;
        public final TextView cityName;
        public final LinearLayout searchResultParent;

        final SearchResultListAdapter mAdapter;

        public SearchResultViewHolder(View itemView, SearchResultListAdapter adapter) {
            super(itemView);

            logoItemView = itemView.findViewById(R.id.search_result_image);

            projectName =  itemView.findViewById(R.id.search_result_project_name);
            projectLocation =  itemView.findViewById(R.id.search_result_project_location_name);

            cityName =  itemView.findViewById(R.id.search_result_city_name);

            searchResultParent = itemView.findViewById(R.id.search_result_parent);

            this.mAdapter = adapter;
        }
    }

    @Override
    public int getItemCount() {
        return mSearchResultList.size();
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.search_result, parent, false);
        return new SearchResultViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        SearchResult mCurrentSearchResult = mSearchResultList.get(position);

        if (mCurrentSearchResult.getType().equals("CITY")){
            holder.projectName.setVisibility(View.GONE);
            holder.projectLocation.setVisibility(View.GONE);

            holder.logoItemView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_location_on_black_24dp));

            holder.cityName.setVisibility(View.VISIBLE);
            holder.cityName.setText(mCurrentSearchResult.getName());

            final String city_name = mCurrentSearchResult.getName();

            holder.searchResultParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("user_city_name", city_name);
                    mContext.startActivity(intent);
                }
            });



        } else {
            holder.projectName.setVisibility(View.VISIBLE);
            holder.projectLocation.setVisibility(View.VISIBLE);

            holder.projectName.setText(mCurrentSearchResult.getName());
            holder.projectLocation.setText(mCurrentSearchResult.getLocation());

            holder.logoItemView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_project));

            holder.cityName.setVisibility(View.GONE);

            final Integer project_id = mCurrentSearchResult.getId();

            holder.searchResultParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProjectDetailActivity.class);
                    intent.putExtra("project_id", project_id);
                    mContext.startActivity(intent);
                }
            });

        }

    }
}
