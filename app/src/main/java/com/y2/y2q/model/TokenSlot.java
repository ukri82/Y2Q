package com.y2.y2q.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by u on 21.01.2017.
 */

public class TokenSlot
{
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
