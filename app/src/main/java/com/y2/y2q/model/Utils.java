package com.y2.y2q.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Windows on 02-03-2015.
 */
public class Utils
{
    public static boolean contains(JSONObject jsonObject, String key)
    {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    public static String get(JSONObject jsonObject, String key) throws JSONException
    {
        if (contains(jsonObject, key))
        {
            return jsonObject.getString(key);
        }

        return "N.A";
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId()
    {
        for (;;)
        {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue))
            {
                return result;
            }
        }
    }

    public static Date parseDate(String dateString)
    {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        Date dateOut = null;
        try
        {
            if(!dateString.isEmpty() && dateString.compareTo("N.A") != 0)
                dateOut = df.parse(dateString);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return dateOut;
    }

    public static int parseInt(JSONObject jsonObject, String keyName)
    {
        int returnNum = 0;
        String tokenNum = "0";
        try
        {
            tokenNum = Utils.get(jsonObject, keyName);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (tokenNum.compareTo("N.A") != 0)
        {
            returnNum = Integer.parseInt(tokenNum);
        }

        return returnNum;
    }

}
