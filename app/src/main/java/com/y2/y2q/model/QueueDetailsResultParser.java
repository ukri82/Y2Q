package com.y2.y2q.model;

import com.y2.y2q.ServerInterface.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 02-03-2015.
 */
public class QueueDetailsResultParser
{

    public static QueueDetails parse(JSONObject response)
    {
        QueueDetails queueDetails = new QueueDetails();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONObject currentQItem = response.getJSONObject("y2q_queue");
                JSONObject currentQSlotItem = response.getJSONObject("y2q_queue_slot");

                queueDetails.mId = Utils.get(currentQItem, "id");
                queueDetails.mName = Utils.get(currentQItem, "m_name");

                String imgURL = Utils.get(currentQItem, "m_photo_url");
                queueDetails.mPhotoURL = "";
                if (!imgURL.isEmpty())
                {
                    queueDetails.mPhotoURL = Endpoints.getImageDownloadURL(imgURL);
                }

                if(currentQSlotItem != null)
                {
                    queueDetails.mActiveQueueSlotId = Utils.get(currentQSlotItem, "id");

                    queueDetails.mQSlotState = Utils.get(currentQSlotItem, "m_state");
                    queueDetails.mStartTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_start_time"));
                    queueDetails.mEndTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_end_time"));
                }

                queueDetails.mCurrentTokenNumber = Utils.parseInt(currentQSlotItem, "m_token_number");
                queueDetails.mExpectedTokens = Utils.parseInt(currentQSlotItem, "m_expected_tokens");

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

        return queueDetails;
    }


}
