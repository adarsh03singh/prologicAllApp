package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animateSplashFragmentImages();
        scheduleGoToAppinfoFragment();
    }

    private void scheduleGoToAppinfoFragment(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getTokenFromSharedPrefrences();
            }
        }, 3000);
    }

    private void goToAppinfoFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        ft.replace(R.id.splash_fragment_container, new AppinfoFragment());
        ft.commit();
    }

    private void goToGenerateOtpFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        ft.replace(R.id.splash_fragment_container, new GenerateOtpFragment());
        ft.commit();
    }

    private void animateSplashFragmentImages() {
        animateSplashCsLogo();
        animateSplashBuilding();
    }

    private void animateSplashCsLogo() {
        final ImageView splash_cslogo = getView().findViewById(R.id.cslogo);
        Animation splash_cslogo_animation = AnimationUtils.loadAnimation(getContext(), R.anim.splash_logo_anim);
        splash_cslogo_animation.setInterpolator(new LinearInterpolator());
        splash_cslogo_animation.setRepeatCount(Animation.INFINITE);
        splash_cslogo_animation.setDuration(1200);
        splash_cslogo.startAnimation(splash_cslogo_animation);
    }

    private void animateSplashBuilding() {
        final ImageView splash_building = getView().findViewById(R.id.splash_building);
        Animation splash_building_animation = AnimationUtils.loadAnimation(getContext(), R.anim.splash_building_animation);
        splash_building.startAnimation(splash_building_animation);
    }
    public void getTokenFromSharedPrefrences() {
        SharedPreferences sharedPref =getActivity().getSharedPreferences("shared_preference_key",0);
        String token = sharedPref.getString(getString(R.string.token_data_key),null);
        String phone_number = sharedPref.getString(getString(R.string.phonenumber_data),null);
        if (token != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        } else if(phone_number != null) {
            goToGenerateOtpFragment();

        }else{
            goToAppinfoFragment();
        }
    }
}