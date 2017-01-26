package com.y2.y2q.model;

import java.util.Date;

/**
 * Created by u on 26.01.2017.
 */

public class QueueDetails
{
    public String mId;
    public String mName;

    public String mPhotoURL;

    public String mActiveQueueSlotId;
    public String mQSlotState;
    public Date mStartTime;
    public Date mEndTime;

    public int mCurrentTokenNumber;
    public int mExpectedTokens;
}
