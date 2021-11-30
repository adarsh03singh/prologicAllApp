package com.csestateconnect.adapters;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.csestateconnect.ui.home.ProfileViewPagerFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        ProfileViewPagerFragment demoFragment = new ProfileViewPagerFragment();
        position = position+1;
         Bundle bundle = new Bundle();
         bundle.putString("message","fragment :"+position);
         demoFragment.setArguments(bundle);
         return demoFragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       position = position+1;
       return "fra "+position;
    }

    @Override
    public int getCount() {
        return 4;


    }
}
