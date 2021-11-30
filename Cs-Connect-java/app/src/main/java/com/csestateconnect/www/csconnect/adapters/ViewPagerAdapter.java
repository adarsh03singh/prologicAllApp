package com.csestateconnect.www.csconnect.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.csestateconnect.www.csconnect.DemoFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DemoFragment demoFragment = new DemoFragment();
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
       return "fragment "+position;
    }

    @Override
    public int getCount() {
        return 4;


    }
}
