package com.y2.y2q.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by u on 10.06.2015.
 */
public class TaskGetTokenSlot extends AsyncTask<Void, Void, TokenSlot>
{
    public interface GetTokenSlotListener
    {
        public void onGet(TokenSlot slot);
    }


    private GetTokenSlotListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String mTokenSlotId;

    public TaskGetTokenSlot(GetTokenSlotListener aListener_in, String tokenSlotId)
    {
        mTokenSlotId = tokenSlotId;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected TokenSlot doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlGetTokenSlot(mTokenSlotId));

        TokenSlot slot = null;
        try
        {
            if(response != null && response.getJSONArray("TokenSlotData").length() > 0)
            {
                JSONObject queueData = response.getJSONArray("TokenSlotData").getJSONObject(0);

                slot = TokenSlotResultParser.parseOne(queueData);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return slot;
    }

    @Override
    protected void onPostExecute(TokenSlot slot)
    {
        if (myListener != null)
        {
            myListener.onGet(slot);
        }
    }
}