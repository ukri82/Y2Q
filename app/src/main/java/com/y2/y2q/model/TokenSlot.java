package com.y2.y2q.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by u on 21.01.2017.
 */

public class TokenSlot implements Serializable
{
    public static String TOKEN_SLOT_OBJ = "TokenSlot";
    public static final int CREATE_TOKEN_SLOT_REQUEST = 1;

    public String mName;
    public String mId;
    public int mCurrentTokenNumber;
    public String mQState;

    public Date mRegDateTime;
    public Date mQStartDateTime;
    public Date mEndDateTime;

    public String mPhotoURL;
    public Bitmap mBitmap;
}
