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
    }

    public static String getServerIPs(String aUserId_in, String aStaticIp_in)
    {
        String aQueryString = "http://" + aStaticIp_in + "/get_servers?";
        aQueryString += "UserId=" + aUserId_in;
        return aQueryString;
    }

    public static String getRequestUrlNextOrgChunk(int aStart_in, int limit, String locationId, double lattidue, double longitude)
    {
        //get_organizations
        String aQueryString = "http://" + myServerIp + "/" + "get_organizations" + "?Start=" + aStart_in + "&Count=" + limit + "&LocationId=" + locationId + "&Lat=" + lattidue + "&Long=" + longitude;

        return aQueryString;
    }

    public static String getImageDownloadURL(String photoURL)
    {
        String imageURL = "http://" + myServerIp + "/download/" + photoURL;
        return imageURL;
    }
}
