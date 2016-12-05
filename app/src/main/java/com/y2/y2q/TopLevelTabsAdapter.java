package com.y2.y2q;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by u on 05.12.2016.
 */

public class TopLevelTabsAdapter extends FragmentStatePagerAdapter
{
    public TopLevelTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        OrganizationFragment fragment = new OrganizationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = "Organizations";
        if(position == 1)
            title = "Tokens";

        return title;
    }
}
