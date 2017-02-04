package com.y2.y2q;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryTask;
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

public class QueueDetailsHandler
{
    QueueDetails mQueueDetails;
    Activity mActivity;
    View mQueueView;

    public QueueDetailsHandler(Activity activity, View queueView)
    {
        mActivity = activity;
        mQueueView = queueView;
    }

    public void initializeQueueHandler()
    {
        if (mQueueDetails != null)
        {
            TextView qView = (TextView) mQueueView.findViewById(R.id.queue_name);
            qView.setText(mQueueDetails.mName);

            mQueueView.findViewById(R.id.subscribe_to_queue).setOnClickListener(new View.OnClickListener()
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

    public void getQueueDetails(String queueId)
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
                    initializeQueueHandler();
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
