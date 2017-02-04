package com.y2.y2q;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryTask;
import com.y2.y2q.ServerInterface.TokenSlotResultParser;
import com.y2.y2q.model.QueueDetails;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainTokenFragment extends LinearLayout
{

    TokenSlot mTokenSlot;

    private Handler mHandler = new Handler();

    private Runnable mUpdateTokenRunnable;

    public MainTokenFragment(Context context)
    {
        super(context);
        inflate(context, R.layout.layout_main_token_slot, this);
    }

    public MainTokenFragment(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.layout_main_token_slot, this);
    }

    public MainTokenFragment(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        inflate(context, R.layout.layout_main_token_slot, this);
    }

    /*public MainTokenFragment()
    {
        super();
        // Required empty public constructor
    }*/


    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_token, container, false);
    }*/

    public void selectTokenSlot(TokenSlot slot)
    {
        mTokenSlot = slot;
        updateUI();

        mUpdateTokenRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                getTokenDetails(mTokenSlot.mId);
                mHandler.postDelayed(this,5000);
            }
        };
        mHandler.postDelayed(mUpdateTokenRunnable,5000);


    }

    void getTokenDetails(String tokenSlotId)
    {
        ServerQuery query = new ServerQuery("get_token_slot", "y2q/default");
        query.addParam("TokenSlotId", tokenSlotId);
        ServerQueryTask<TokenSlot> task = new ServerQueryTask(new ServerQueryTask.ServerQueryTaskListener<TokenSlot>()
        {
            @Override
            public void onResults(TokenSlot tokenSlot)
            {
                if(tokenSlot != null)
                {
                    mTokenSlot = tokenSlot;
                    updateUI();
                }
            }
        }, new ServerQueryTask.ServerQueryTaskResultParser<TokenSlot>()
        {
            @Override
            public TokenSlot parse(JSONObject response)
            {
                return TokenSlotResultParser.parseOne(response);
            }
        },
                query);
        task.execute();
    }

    /*@Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mHandler.removeCallbacks(mUpdateTokenRunnable);
    }*/

    void updateUI()
    {
        ((TextView)findViewById(R.id.main_title)).setText(mTokenSlot.mName);
        //((TextView)findViewById(R.id.token)).setText(mTokenSlot.mCurrentTokenNumber + "");

        CircleCountDownView countDownView = (CircleCountDownView)findViewById(R.id.circle_count_down_view);
        countDownView.setProgress(mTokenSlot.mCurrentTokenNumber);
    }

    public void close()
    {
        mHandler.removeCallbacks(mUpdateTokenRunnable);
    }
}
