package com.y2.y2q.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.OrgResultParser;
import com.y2.y2q.model.Organization;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskLoadOrganiations extends AsyncTask<Void, Void, ArrayList<Organization>>
{
    public interface OrganizationsLoadedListener
    {
        public void onVideoItemsLoaded(ArrayList<Organization> listOrgs);
    }


    private OrganizationsLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private int myStart = 0;
    private int myCount = 10;
    private String mLocationId;
    private double mLat = 0.0;
    private double mLong = 0.0;



    public TaskLoadOrganiations(OrganizationsLoadedListener aListener_in, int aStart_in, int limit, String locationId, double lattidue, double longitudet)
    {
        mLocationId = locationId;
        mLat = lattidue;
        mLong = longitudet;
        myStart = aStart_in;
        myCount = limit;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<Organization> doInBackground(Void... params)
    {
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlNextOrgChunk(myStart, myCount, mLocationId, mLat, mLong));
        return OrgResultParser.parse(response);
    }

    @Override
    protected void onPostExecute(ArrayList<Organization> listVideo)
    {
        if (myListener != null)
        {
            myListener.onVideoItemsLoaded(listVideo);
        }
    }
}