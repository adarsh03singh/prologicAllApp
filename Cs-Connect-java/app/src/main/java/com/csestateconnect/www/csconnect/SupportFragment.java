package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SupportFragment extends Fragment {

    private NavigationDrawerToggle drawerInterface;
    Button back_button;
    Button calling_button;
    Button commission_issue_button;
    Button support_issue_button;
    String rmNumber;
    String rmEmail;
    NetworkInformation networkInformation;

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
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getIdforTextAndButton();
        networkInformation = new NetworkInformation(getActivity());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        getrRmDataFromSharedPreferences();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        calling_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+rmNumber));
                startActivity(intent);
            }
        });

        commission_issue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + rmEmail);
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });

        support_issue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent1 = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + rmEmail);
                mailIntent1.setData(data);
                startActivity(Intent.createChooser(mailIntent1, "Send mail..."));
            }
        });

    }

    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.Press Retry to Connect.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (networkInformation.isConnectingToInternet() == false) {
//no connection
                    networkErrorPopup();
                } else{
                    getFragmentManager().beginTransaction().detach(SupportFragment.this)
                            .attach(SupportFragment.this).commit();
                }

            }
        });
        builder.show();
    }
    public void getIdforTextAndButton() {
        back_button = getView().findViewById(R.id.left_side_bar_button);
        calling_button = (Button) getView().findViewById(R.id.support_calling_button);
        support_issue_button = (Button) getView().findViewById(R.id.support_issue_button);
        commission_issue_button = (Button) getView().findViewById(R.id.support_ommission_issue_button);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerInterface.unlockDrawer();
        ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
    }

    public void getrRmDataFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("shared_preference_key", 0);
         rmNumber = sharedPref.getString(getString(R.string.rm_number_data), null);
         rmEmail = sharedPref.getString(getString(R.string.rm_email_data), null);
    }
}