package com.y2.y2q.model;

import com.y2.y2q.ServerInterface.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Windows on 02-03-2015.
 */
public class OrgResultParser
{

    public static ArrayList<Organization> parse(JSONObject response)
    {
        ArrayList<Organization> listOrgs = new ArrayList<>();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONArray arrayItems = response.getJSONArray("OrgData");
                for (int i = 0; i < arrayItems.length(); i++)
                {
                    JSONObject currentItem = arrayItems.getJSONObject(i);

                    Organization org = new Organization();
                    org.mId = Utils.get(currentItem, "id");
                    org.mName = Utils.get(currentItem, "m_name");
                    String imgURL = Utils.get(currentItem, "m_photo_url");
                    org.mPhotoURL = "";
                    if(!imgURL.isEmpty())
                    {
                        org.mPhotoURL = Endpoints.getImageDownloadURL(imgURL);
                    }
                    org.mAddress = Utils.get(currentItem, "m_address");
                    listOrgs.add(org);

                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return listOrgs;
    }


}
