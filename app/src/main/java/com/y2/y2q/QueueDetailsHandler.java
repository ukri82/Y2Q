package com.y2.y2q;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.y2.y2q.ServerInterface.TaskCreateTokenSlot;
import com.y2.y2q.model.DeviceIdentity;
import com.y2.y2q.model.QueueDetails;
import com.y2.y2q.model.TokenSlot;

/**
 * Created by u on 31.01.2017.
 */

public class QueueDetailsHandler
{
    QueueDetails mQueueDetails;
    Activity mActivity;
    View mQueueView;

    public QueueDetailsHandler(Activity activity, View queueView, QueueDetails queueDetails)
    {
        mActivity = activity;
        mQueueView = queueView;
        mQueueDetails = queueDetails;
    }

    public void initializeQueueHandler()
    {
        if(mQueueDetails != null)
        {
            TextView qView = (TextView) mQueueView.findViewById(R.id.queue_name);
            qView.setText(mQueueDetails.mName);

            if(mQueueDetails.mActiveQueueSlotId.compareTo("-1") != -1)
            {
                mQueueView.findViewById(R.id.subscribe_to_queue).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        new TaskCreateTokenSlot(new TaskCreateTokenSlot.CreateTokenSlotListener()
                        {
                            @Override
                            public void onCreate(TokenSlot slot)
                            {
                                Intent intent = new Intent(mActivity, TokenSlotActivity.class);
                                intent.putExtra(TokenSlot.TOKEN_SLOT_OBJ, slot);
                                mActivity.startActivity(intent);
                            }
                        }, DeviceIdentity.get(), mQueueDetails.mActiveQueueSlotId).execute();
                    }
                });
            }
        }
    }
}
