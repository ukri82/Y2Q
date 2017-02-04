package com.y2.y2q.ServerInterface;

import com.y2.serverinterface.Endpoints;
import com.y2.utils.Utils;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 02-03-2015.
 */
public class TokenSlotResultParser
{

    public static ArrayList<TokenSlot> parse(JSONObject response)
    {
        ArrayList<TokenSlot> listTokenSlots = new ArrayList<>();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONArray arrayItems = response.getJSONArray("TokenSlotData");
                for (int i = 0; i < arrayItems.length(); i++)
                {
                    JSONObject currentItem = arrayItems.getJSONObject(i);

                    TokenSlot slot = parseItem(arrayItems.getJSONObject(i));
                    if(null != slot)
                        listTokenSlots.add(slot);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return listTokenSlots;
    }

    public static TokenSlot parseOne(JSONObject response)
    {
        TokenSlot slot = null;
        try
        {
            if(response != null && response.getJSONArray("TokenSlotData").length() > 0)
            {
                JSONObject queueData = response.getJSONArray("TokenSlotData").getJSONObject(0);

                slot = TokenSlotResultParser.parseItem(queueData);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return slot;
    }

    static TokenSlot parseItem(JSONObject response)
    {
        TokenSlot tokenSlot = new TokenSlot();
        if (response != null)
        {
            try
            {
                JSONObject currentItem = response;

                JSONObject currentQSlotClientItem = currentItem.getJSONObject("y2q_queue_client");
                JSONObject currentQItem = currentItem.getJSONObject("y2q_queue");
                JSONObject currentQSlotItem = currentItem.getJSONObject("y2q_queue_slot");

                tokenSlot.mId = Utils.get(currentQSlotClientItem, "id");
                tokenSlot.mName = Utils.get(currentQItem, "m_name");
                tokenSlot.mQState = Utils.get(currentQSlotItem, "m_state");
                tokenSlot.mRegDateTime = Utils.parseDate(Utils.get(currentQSlotClientItem, "m_registration_time"));
                tokenSlot.mQStartDateTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_start_time"));
                tokenSlot.mEndDateTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_end_time"));
                String imgURL = Utils.get(currentQItem, "m_photo_url");
                tokenSlot.mPhotoURL = "";
                if (!imgURL.isEmpty())
                {
                    tokenSlot.mPhotoURL = Endpoints.getImageDownloadURL(imgURL);
                }

                String tokenNum = Utils.get(currentQSlotItem, "m_token_number");
                if (tokenNum.compareTo("N.A") == 0)
                {
                    tokenNum = "0";
                }
                tokenSlot.mCurrentTokenNumber = Integer.parseInt(tokenNum);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return tokenSlot;
    }


}
