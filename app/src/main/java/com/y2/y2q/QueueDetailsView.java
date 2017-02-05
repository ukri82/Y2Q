package com.y2.y2q;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryTask;
import com.y2.serverinterface.VolleySingleton;
import com.y2.utils.Utils;
import com.y2.y2q.ServerInterface.QueueDetailsResultParser;
import com.y2.serverinterface.DeviceIdentity;
import com.y2.y2q.ServerInterface.TokenSlotResultParser;
import com.y2.y2q.model.QueueDetails;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by u on 31.01.2017.
 */

public class QueueDetailsView
{
    QueueDetails mQueueDetails;
    Activity mActivity;
    View mQueueView;
    private ImageLoader myImageLoader;

    public QueueDetailsView(Activity activity, View queueView)
    {
        mActivity = activity;
        myImageLoader = VolleySingleton.getInstance(null).getImageLoader();
        mQueueView = queueView;

    }


    public void initializeQueueHandler()
    {
        if (mQueueDetails != null)
        {
            mQueueView.findViewById(R.id.down_button).setVisibility(View.GONE);
            mQueueView.getLayoutParams().height = mQueueView.getLayoutParams().height * 2;
            mQueueView.requestLayout();
            mQueueView.findViewById(R.id.queue_subscribe_button).setVisibility(View.VISIBLE);

            TextView qView = (TextView) mQueueView.findViewById(R.id.queue_name);
            qView.setText(mQueueDetails.mName);
            Utils.setImage(myImageLoader, mQueueDetails.mPhotoURL, (ImageView)mQueueView.findViewById(R.id.queue_picture), R.drawable.ic_people_black_48dp);

            mQueueView.findViewById(R.id.queue_subscribe_button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    if (mQueueDetails.mActiveQueueSlotId.compareTo("-1") != -1)
                    {
                        createTokenSlot(mQueueDetails.mActiveQueueSlotId);
                    }
                    else
                    {
                        Toast.makeText(mActivity, "Please select a queue first. Make a QR scan or select a previous queue or enter a queue number", Toast.LENGTH_SHORT);
                    }
                }
            });

        }
    }

    void createTokenSlot(String queueSlotId)
    {
        ServerQuery query = new ServerQuery("create_token_slot", "y2q/default");
        query.addParam("QueueSlotId", queueSlotId);
        query.addParam("PhoneId", DeviceIdentity.get());
        ServerQueryTask<TokenSlot> task = new ServerQueryTask(new ServerQueryTask.ServerQueryTaskListener<TokenSlot>()
        {
            @Override
            public void onResults(TokenSlot tokenSlot)
            {
                if (tokenSlot != null)
                {
                    Intent returnIntent = new Intent(mActivity, mActivity.getClass());
                    returnIntent.putExtra(TokenSlot.TOKEN_SLOT_OBJ, tokenSlot);
                    mActivity.setResult(Activity.RESULT_OK, returnIntent);
                    mActivity.finish();
                    //mActivity.startActivity(intent);
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

    public void getQueueDetails(String queueId, final boolean triggerCreationAutomatic)
    {
        ServerQuery query = new ServerQuery("get_queue_details", "y2q/default");
        query.addParam("QueueId", queueId);
        ServerQueryTask<QueueDetails> task = new ServerQueryTask(new ServerQueryTask.ServerQueryTaskListener<QueueDetails>()
        {
            @Override
            public void onResults(QueueDetails queueDetails)
            {
                if (queueDetails != null)
                {
                    mQueueDetails = queueDetails;

                    if(triggerCreationAutomatic)
                    {
                        createTokenSlot(mQueueDetails.mActiveQueueSlotId);
                    }
                    else
                    {
                        initializeQueueHandler();
                    }
                }
            }
        }, new ServerQueryTask.ServerQueryTaskResultParser<QueueDetails>()
        {
            @Override
            public QueueDetails parse(JSONObject response)
            {
                return QueueDetailsResultParser.parse(response);
            }
        },
                query);
        task.execute();
    }
}
