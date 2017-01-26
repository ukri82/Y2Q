package com.y2.y2q.ServerInterface;


/**
 * Created by Windows on 02-03-2015.
 */
public class Endpoints
{
    static String myServerIp;

    public static void setIP(String anIP_in)
    {
        myServerIp = anIP_in;
        synchronized(syncObject)
        {
            syncObject.notify();
        }
    }

    public static String getServerIPs(String aUserId_in, String aStaticIp_in)
    {
        String aQueryString = "http://" + aStaticIp_in + "/y2q/default/get_servers?";
        aQueryString += "UserId=" + aUserId_in;
        return aQueryString;
    }

    public static String getRequestUrlNextOrgChunk(int aStart_in, int limit, String locationId, double lattidue, double longitude)
    {
        //get_organizations
        String aQueryString = "http://" + myServerIp + "/y2q/default/get_organizations" + "?Start=" + aStart_in + "&Count=" + limit + "&LocationId=" + locationId + "&Lat=" + lattidue + "&Long=" + longitude;

        return aQueryString;
    }

    public static String getRequestUrlNextTokenSlotChunk(int aStart_in, int limit, String phoneId)
    {
        //get_token_slots
        String aQueryString = "http://" + myServerIp + "/y2q/default/get_token_slots" + "?Start=" + aStart_in + "&Count=" + limit + "&PhoneId=" + phoneId;

        return aQueryString;
    }

    public static String getRequestUrlGetQueueDetails(String queueId)
    {
        //get_queue_details
        String aQueryString = "http://" + myServerIp + "/y2q/default/get_queue_details" + "?QueueId=" + queueId;

        return aQueryString;
    }

    public static String getImageDownloadURL(String photoURL)
    {
        String imageURL = "http://" + myServerIp + "/y2q/default/download/" + photoURL;
        return imageURL;
    }

    public static void waitForIP()
    {
        if(myServerIp == null)
        {
            synchronized (syncObject)
            {
                try
                {
                    // Calling wait() will block this thread until another thread
                    // calls notify() on the object.
                    syncObject.wait();
                } catch (InterruptedException e)
                {
                    // Happens if someone interrupts your thread.
                }
            }
        }
    }

    private static Object syncObject = new Object();
}
