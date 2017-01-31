package com.y2.y2q;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.zxing.integration.android.IntentResult;

/**
 * Created by u on 05.12.2016.
 */

public class CreateTokenSlotTabsAdapter extends FragmentStatePagerAdapter
{
    public CreateTokenSlotTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    ScanQRFragment mScanQRFragment;

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if(position == 0)
        {
            fragment = new AllPrevQueuesFragment();
        }
        else if( position == 1)
        {
            mScanQRFragment = new ScanQRFragment();
            fragment = mScanQRFragment;
        }
        else if( position == 2)
        {
            fragment = new EnterQueueDetailsFragment();
        }

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = "My queues";
        if(position == 1)
            title = "Scan";

        if(position == 2)
            title = "Enter";

        return title;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        mScanQRFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(IntentResult result)
    {
        mScanQRFragment.onActivityResult(result);
    }
}
