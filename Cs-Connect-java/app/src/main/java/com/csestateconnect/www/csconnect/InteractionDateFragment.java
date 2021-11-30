package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.csestateconnect.www.csconnect.adapters.InteractionDateListAdapter;
import com.csestateconnect.www.csconnect.adapters.ProjectListAdapter;
import com.csestateconnect.www.csconnect.models.interaction_dates.DateList;
import com.csestateconnect.www.csconnect.models.lead.GetInteractionDate;
import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.csestateconnect.www.csconnect.models.project.Project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InteractionDateFragment extends Fragment implements Toolbar.OnMenuItemClickListener, android.support.v7.widget.Toolbar.OnMenuItemClickListener {

    Lead lead;
    String interact_date;
    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    Button support_issue_button;
    android.support.v7.widget.Toolbar toolbar;

    private List<DateList> mDateList = new ArrayList<>();;
    private RecyclerView mRecyclerView;
    private InteractionDateListAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            drawerInterface = (NavigationDrawerToggle) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerInterface.lockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(false);
        setHasOptionsMenu(true);
        View rootView =  inflater.inflate(R.layout.fragment_interaction_dates, container, false);
         toolbar=  rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.interaction_date_button);
        toolbar.setOnMenuItemClickListener (this);

        mRecyclerView = rootView.findViewById(R.id.interactionDate_recyclerView);
         mAdapter = new InteractionDateListAdapter(mDateList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getdata();
        back_button = getView().findViewById(R.id.left_side_bar_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goToLeadFragment();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.interact_date_create:
                goToCreateInteractionDatesFragment();
                break;
        }
        return false;
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
  /*  private void goToLeadFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        Bundle bundle = new Bundle();
        bundle.putParcelable("lead_data", lead);
        LeadDetailFragment leadDetailFragment = new LeadDetailFragment();
        leadDetailFragment.setArguments(bundle);
        ft.replace(R.id.main_acitivity_container, leadDetailFragment);
        ft.commit();
    }*/

    private void goToCreateInteractionDatesFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        Bundle bundle = new Bundle();
        bundle.putParcelable("lead_data", lead);
        CreateLeadInteractionDatesFragment createLeadInteractionDatesFragment = new CreateLeadInteractionDatesFragment();
        createLeadInteractionDatesFragment.setArguments(bundle);
        ft.replace(R.id.main_acitivity_container, createLeadInteractionDatesFragment);
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
    }

    public  void getdata() {
         Bundle bundle = this.getArguments();
        if (bundle != null) {
             lead = bundle.getParcelable("lead_data");
            Object[] get_interactionDate = lead.getGetInteractionDates().toArray();
            for (Object obj: get_interactionDate){

                mDateList.add(new DateList(((GetInteractionDate)obj).getDate()));

            }
            mAdapter.notifyDataSetChanged();
        }
    }

}