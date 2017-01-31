package com.y2.y2q;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.y2.y2q.ServerInterface.ServerIPProvider;
import com.y2.y2q.ServerInterface.TaskCreateTokenSlot;
import com.y2.y2q.ServerInterface.TaskGetQueueDetails;
import com.y2.y2q.misc.PermissionChecker;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.DeviceIdentity;
import com.y2.y2q.model.QueueDetails;
import com.y2.y2q.model.TokenSlot;

public class CreateTokenSlotActivity extends AppCompatActivity
{

    CreateTokenSlotTabsAdapter mTopLevelTabsAdapter;
    ViewPager mViewPager;

    ServerIPProvider myServerIPProvider = new ServerIPProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_token_slot);

        VolleySingleton.getInstance(this);  //  Just initialize the singleton
        DeviceIdentity.initialize(this);
        myServerIPProvider.create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTopLevelTabsAdapter = new CreateTokenSlotTabsAdapter( getSupportFragmentManager() );
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTopLevelTabsAdapter);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PermissionChecker.RequestCode:
                mTopLevelTabsAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            mTopLevelTabsAdapter.onActivityResult(result);
        }
        else
        {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)
        {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
