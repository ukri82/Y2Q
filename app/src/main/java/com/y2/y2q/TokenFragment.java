package com.y2.y2q;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.y2.y2q.ServerInterface.TaskLoadOrganiations;
import com.y2.y2q.ServerInterface.TaskLoadTokenSlots;
import com.y2.y2q.model.DeviceIdentity;
import com.y2.y2q.model.TokenSlot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TokenFragment extends Fragment implements TaskLoadTokenSlots.TaskSlotsLoadedListener, TokenSlotListAdapter.TokenSlotClickListener
{

    private RecyclerView mTokenListView;
    private TokenSlotListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public TokenFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        initFab();

        init();
    }

    void init()
    {
        mTokenListView = (RecyclerView) getActivity().findViewById(R.id.token_list_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TokenSlotListAdapter();
        mAdapter.registerTokenSlotClickListener(this);

        mTokenListView.setAdapter(mAdapter);

        final TaskLoadTokenSlots.TaskSlotsLoadedListener listener = this;
        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadTokenSlots(listener, mAdapter.getItemCount(), 10, DeviceIdentity.get()).execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });

        new TaskLoadTokenSlots(this, 0, 10, DeviceIdentity.get()).execute();
    }
    void initFab()
    {
        final Activity activity = this.getActivity();
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
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

                Intent intent = new Intent(activity, CreateTokenSlotActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_token, container, false);
    }

    @Override
    public void onTaskSlotsLoaded(ArrayList<TokenSlot> listTokenSlots)
    {
        mAdapter.appendTokenSlotList(listTokenSlots);
    }

    @Override
    public void onClick(TokenSlot slot)
    {
        Intent intent = new Intent(this.getActivity(), TokenSlotActivity.class);
        intent.putExtra(TokenSlot.TOKEN_SLOT_OBJ, slot);
        startActivity(intent);
    }
}
