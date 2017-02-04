package com.y2.y2q;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryChunkTask;
import com.y2.serverinterface.DeviceIdentity;
import com.y2.serverinterface.ServerQueryTask;
import com.y2.y2q.ServerInterface.TokenSlotResultParser;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TokenSlotFragment extends FrameLayout implements TokenSlotListAdapter.TokenSlotClickListener, MainTokenFragment.QueueSlotUnsubscribeListener
{

    private RecyclerView mTokenListView;
    private TokenSlotListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    MainTokenFragment mMainTokenFragment;

    Activity mActivity;

    public TokenSlotFragment(Context context)
    {
        super(context);
        inflate(context, R.layout.layout_token_slots, this);
    }

    public TokenSlotFragment(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.layout_token_slots, this);
    }

    public TokenSlotFragment(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        inflate(context, R.layout.layout_token_slots, this);
    }


    public void init(Activity activity)
    {
        mActivity = activity;
        initFab();

        initTokenSlots();

    }
    void initTokenSlots()
    {
        //mMainTokenFragment = (MainTokenFragment)mActivity.getFragmentManager().findFragmentById(R.id.fragment_id_main_token);
        mMainTokenFragment = (MainTokenFragment)findViewById(R.id.fragment_id_main_token);
        mMainTokenFragment.registerUnsubscribeListener(this);
        mTokenListView = (RecyclerView) findViewById(R.id.token_list_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TokenSlotListAdapter();
        mAdapter.registerTokenSlotClickListener(this);

        mTokenListView.setAdapter(mAdapter);

        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                populateTokenSlots();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });

        populateTokenSlots();
    }

    void populateTokenSlots()
    {
        ServerQuery query = new ServerQuery("get_token_slots", "y2q/default", mAdapter.getItemCount(), 10);
        query.addParam("PhoneId", DeviceIdentity.get());
        ServerQueryChunkTask<TokenSlot> task = new ServerQueryChunkTask(new ServerQueryChunkTask.ServerQueryTaskListener<TokenSlot>()
        {
            @Override
            public void onResults(ArrayList<TokenSlot> dataList)
            {

                if(mAdapter.getItemCount() == 0 && dataList.size() == 0)
                    mMainTokenFragment.selectTokenSlot(null);
                else if(mAdapter.getItemCount() == 0 && dataList.size() > 0)
                    mMainTokenFragment.selectTokenSlot(dataList.get(0));

                mAdapter.appendTokenSlotList(dataList);

            }
        }, new ServerQueryChunkTask.ServerQueryChunkTaskResultParser<TokenSlot>()
        {
            @Override
            public ArrayList<TokenSlot> parse(JSONObject response)
            {
                return TokenSlotResultParser.parse(response);
            }
        },
                query);
        task.execute();
    }
    void initFab()
    {
        //final Activity activity = this.getActivity();
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                /*IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.initiateScan();*/

                Intent intent = new Intent(mActivity, CreateTokenSlotActivity.class);
                mActivity.startActivityForResult(intent, TokenSlot.CREATE_TOKEN_SLOT_REQUEST);
            }
        });

    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_token, container, false);
    }*/

    @Override
    public void onClick(TokenSlot slot)
    {
        /*Intent intent = new Intent(this.getActivity(), TokenSlotActivity.class);
        intent.putExtra(TokenSlot.TOKEN_SLOT_OBJ, slot);
        startActivity(intent);*/

        mMainTokenFragment.selectTokenSlot(slot);

    }

    public void newTokenSlotCreated(TokenSlot slot)
    {
        mAdapter.add(slot);
        mMainTokenFragment.selectTokenSlot(slot);
    }

    public void close()
    {
        if(mMainTokenFragment != null)
        {
            mMainTokenFragment.close();
        }
    }

    @Override
    public void onUnsubscribe(TokenSlot slot)
    {
        mAdapter.remove(slot);
        unsubscribeTokenSlot(slot.mId);
    }

    void unsubscribeTokenSlot(String tokenSlotId)
    {
        ServerQuery query = new ServerQuery("unsubscribe_token_slot", "y2q/default");
        query.addParam("TokenSlotId", tokenSlotId);
        ServerQueryTask<TokenSlot> task = new ServerQueryTask(new ServerQueryTask.ServerQueryTaskListener<TokenSlot>()
        {
            @Override
            public void onResults(TokenSlot tokenSlot)
            {
                mMainTokenFragment.selectTokenSlot(mAdapter.getFirst());
            }
        }, new ServerQueryTask.ServerQueryTaskResultParser<TokenSlot>()
        {
            @Override
            public TokenSlot parse(JSONObject response)
            {
                return null;
            }
        },
                query);
        task.execute();
    }
}
