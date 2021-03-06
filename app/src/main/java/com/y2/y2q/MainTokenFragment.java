package com.y2.y2q;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryTask;
import com.y2.serverinterface.VolleySingleton;
import com.y2.utils.Utils;
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
    private ImageLoader myImageLoader;

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

        if(mUpdateTokenRunnable != null)
            mHandler.removeCallbacks(mUpdateTokenRunnable);

        if(mTokenSlot != null)
        {
            mUpdateTokenRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    getTokenDetails(mTokenSlot.mId);
                    mHandler.postDelayed(this, 10000);
                }
            };
            mHandler.postDelayed(mUpdateTokenRunnable, 10000);
        }


    }

    public interface QueueSlotUnsubscribeListener
    {
        public void onUnsubscribe(TokenSlot slot );
    }

    QueueSlotUnsubscribeListener mUnsubListener;
    public void registerUnsubscribeListener(QueueSlotUnsubscribeListener listener)
    {
        mUnsubListener = listener;
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


    void updateUI()
    {
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        if(mTokenSlot == null)
        {
            findViewById(R.id.main_token_layout).setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            emptyView.setVisibility(View.GONE);
            findViewById(R.id.main_token_layout).setVisibility(View.VISIBLE);
        }

        String name = "";
        if(mTokenSlot != null)
            name = mTokenSlot.mName;
        ((TextView)findViewById(R.id.main_title)).setText(name);


        String url = "";
        if(mTokenSlot != null)
            url = mTokenSlot.mPhotoURL;

        if(myImageLoader == null)
            myImageLoader = VolleySingleton.getInstance(null).getImageLoader();

        ImageView imView = (ImageView)findViewById(R.id.main_picture);
        Utils.setImage(myImageLoader, url, imView, R.drawable.ic_people_black_48dp);

        int progress = 0;
        if(mTokenSlot != null)
            progress = mTokenSlot.mCurrentTokenNumber;
        CircleCountDownView countDownView = (CircleCountDownView)findViewById(R.id.circle_count_down_view);
        countDownView.setProgress(progress);

        OnClickListener listener = null;
        if(mTokenSlot != null)
        {
            listener = new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(mUnsubListener != null)
                    {
                        mUnsubListener.onUnsubscribe(mTokenSlot);
                    }
                }
            };
        }
        findViewById(R.id.unsubscribe_button).setOnClickListener(listener);
    }



    public void close()
    {
        mHandler.removeCallbacks(mUpdateTokenRunnable);
    }
}
