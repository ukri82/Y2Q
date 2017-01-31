package com.y2.y2q.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskLoadTokenSlots extends AsyncTask<Void, Void, ArrayList<TokenSlot>>
{
    public interface TaskSlotsLoadedListener
    {
        public void onTaskSlotsLoaded(ArrayList<TokenSlot> listTokenSlots);
    }


    private TaskSlotsLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private int myStart = 0;
    private int myCount = 10;
    private String mPhoneId;

    public TaskLoadTokenSlots(TaskSlotsLoadedListener aListener_in, int aStart_in, int limit, String phoneId)
    {
        mPhoneId = phoneId;
        myStart = aStart_in;
        myCount = limit;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected ArrayList<TokenSlot> doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlNextTokenSlotChunk(myStart, myCount, mPhoneId));
        return TokenSlotResultParser.parse(response);
    }

    @Override
    protected void onPostExecute(ArrayList<TokenSlot> tokenSlots)
    {
        if (myListener != null)
        {
            myListener.onTaskSlotsLoaded(tokenSlots);
        }
    }
}