package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AppinfoFragment extends Fragment {

    NetworkInformation networkInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkInformation = new NetworkInformation(getContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        }
        final Button generate_otp_button = getView().findViewById(R.id.appinfo_cs_connect_button);
            generate_otp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToGenerateOtpFragment();
                }
            });
        }

    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press Retry to Connect.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                getFragmentManager().beginTransaction().detach(AppinfoFragment.this).attach(AppinfoFragment.this).commit();
            }
        });
        builder.show();
    }


    public void goToGenerateOtpFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        ft.replace(R.id.splash_fragment_container, new GenerateOtpFragment());
        ft.commit();
    }

}