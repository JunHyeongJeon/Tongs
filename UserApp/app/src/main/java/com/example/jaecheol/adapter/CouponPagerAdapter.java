package com.example.jaecheol.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jaecheol.tab.UsedCouponTab;
import com.example.jaecheol.tab.ValidCouponTab;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class CouponPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    ValidCouponTab validCouponTab;
    UsedCouponTab usedCouponTab;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public CouponPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    public Object getTab(int idx)
    {
        switch(idx)
        {
            case 0:
                return validCouponTab;
            case 1:
                return usedCouponTab;
        }
        return null;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            if(validCouponTab == null)
                validCouponTab = new ValidCouponTab();
            return validCouponTab;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            if(usedCouponTab == null)
                usedCouponTab = new UsedCouponTab();
            return usedCouponTab;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
