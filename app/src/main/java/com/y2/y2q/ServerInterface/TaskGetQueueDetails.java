package com.y2.y2q.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2q.model.QueueDetails;
import com.y2.y2q.misc.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by u on 10.06.2015.
 */
public class TaskGetQueueDetails extends AsyncTask<Void, Void, QueueDetails>
{
    public interface QueueDetailsListener
    {
        public void onQueueDetails(QueueDetails queueDetails);
    }


    private QueueDetailsListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String mQueueId;

    public TaskGetQueueDetails(QueueDetailsListener aListener_in, String qId)
    {
        mQueueId = qId;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected QueueDetails doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlGetQueueDetails(mQueueId));

        QueueDetails queueDetails = null;
        try
        {
            if(response != null && response.getJSONArray("QueueData").length() > 0)
            {
                JSONObject queueData = response.getJSONArray("QueueData").getJSONObject(0);

                queueDetails = QueueDetailsResultParser.parse(queueData);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return queueDetails;
    }

    @Override
    protected void onPostExecute(QueueDetails queueDetails)
    {
        if (myListener != null)
        {
            myListener.onQueueDetails(queueDetails);
        }
    }
}