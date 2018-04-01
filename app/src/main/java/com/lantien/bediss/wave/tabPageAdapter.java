package com.lantien.bediss.wave;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by bediss on 25/03/2018.
 */

public class tabPageAdapter extends FragmentStatePagerAdapter{

    public tabPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "tab0";
        } else if(position==1) {
            return "tab1";
        } else  {
            return "tab2";
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return MainActivity.PlaceholderFragment.newInstance(position + 1);

        if(position==0) {
            return new tab1();
        } else if(position==1) {
            return new tab2();
        } else if (position==2){
            return new tab3();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

}
