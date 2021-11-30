package org.gstsuvidhakendra.mygsk.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gstsuvidhakendra.mygsk.R;

public class AccoutingFragment extends Fragment {

    private AccoutingViewModel mViewModel;

    public static AccoutingFragment newInstance() {
        return new AccoutingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.accouting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccoutingViewModel.class);
        // TODO: Use the ViewModel
    }

}
