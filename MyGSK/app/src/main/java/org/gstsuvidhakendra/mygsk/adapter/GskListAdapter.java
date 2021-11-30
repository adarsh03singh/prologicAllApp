package org.gstsuvidhakendra.mygsk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import org.gstsuvidhakendra.mygsk.R;
import org.gstsuvidhakendra.mygsk.data.GetGskAddressListdata;

import java.util.ArrayList;

public class GskListAdapter extends RecyclerView.Adapter<GskListAdapter.LeadViewHolder> {



    private ArrayList<GetGskAddressListdata> gskList;
    private LayoutInflater mInflater;
    private Context mContext;

    public GskListAdapter(Context context, ArrayList<GetGskAddressListdata> gskList) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.gskList = gskList;
    }


    class LeadViewHolder extends RecyclerView.ViewHolder {

        public final TextView gskId;
        public final ConstraintLayout gskListParentLayout;




        final GskListAdapter mAdapter;

        public LeadViewHolder(View itemView, GskListAdapter adapter) {
            super(itemView);

            gskId = itemView.findViewById(R.id.tvGskItems);
            gskListParentLayout = itemView.findViewById(R.id.gskListParentLayout);

            this.mAdapter = adapter;
        }

    }


    @Override
    public LeadViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.gsk_list_item, parent, false);
        return new LeadViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder( final LeadViewHolder leadViewHolder, final int position) {
        GetGskAddressListdata modal = gskList.get(position);
            String address1 = modal.getBilling_address_1();
            String address2 =  modal.getBilling_address_2();
            String state =  modal.getBilling_state();
            String city =  modal.getBilling_city();
            String postcode =  modal.getBilling_postcode();

            leadViewHolder.gskId.setText(address1+","+address2+","+state+","+city+","+postcode);

    }

    @Override
    public int getItemCount() {
        return  gskList == null ? 0 : gskList.size();

    }

}
