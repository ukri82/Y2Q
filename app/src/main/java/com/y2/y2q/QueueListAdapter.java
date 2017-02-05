package com.y2.y2q;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.y2.serverinterface.VolleySingleton;
import com.y2.utils.AnimationUtils;
import com.y2.utils.Utils;
import com.y2.y2q.model.Queue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder>
{
    public interface QueueClickListener
    {
        public void onClick(Queue queue);
    }

    QueueClickListener mListener;

    private ArrayList<Queue> mDataset = new ArrayList<>();


    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;
    private int myPreviousPosition = 0;
    Activity mActivity;
    RecyclerView mParentView;

    public QueueListAdapter(Activity activity, QueueClickListener listener, RecyclerView parentView)
    {
        mActivity = activity;
        mParentView = parentView;
        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();
        mListener = listener;
    }


    public void appendQueueList(List<Queue> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }


    public void clear()
    {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View mParentView;
        public TextView mTextView;
        public TextView mAddressView;
        public ImageView mPictureView;

        public ImageButton mArrow;
        public View mSubsPart;
        public View mSubsButton;

        private int mOriginalHeight = 0;
        private boolean mIsViewExpanded = false;


        public ViewHolder(View v)
        {
            super(v);
            mParentView = v;
            mTextView = (TextView)v.findViewById(R.id.queue_name);
            mAddressView = (TextView)v.findViewById(R.id.queue_address);
            mPictureView = (ImageView)v.findViewById(R.id.queue_picture);
            mArrow = (ImageButton)v.findViewById(R.id.down_button);
            mSubsPart = v.findViewById(R.id.queue_subscribe_view);
            mSubsButton = v.findViewById(R.id.queue_subscribe_button);
        }

        public void showSubsPart(boolean show)
        {
            if(show)
            {
                //mSubsPart.setVisibility(View.VISIBLE);
                mArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                mSubsButton.setVisibility(View.VISIBLE);
                mIsViewExpanded = true;


            }
            else
            {
                mSubsButton.setOnClickListener(null);
                //mSubsPart.setVisibility(View.INVISIBLE);
                mArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                mSubsButton.setVisibility(View.GONE);
                mIsViewExpanded = false;
                mOriginalHeight = 0;
            }
        }
    }



    @Override
    public QueueListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final QueueListAdapter.ViewHolder holder, final int position)
    {
        /*holder.mParentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onClick(mDataset.get(position));
            }
        });*/


        holder.mTextView.setText(mDataset.get(position).mQName);
        if(mDataset.get(position).mAddress != null)
            holder.mAddressView.setText(mDataset.get(position).mAddress);

        holder.mArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                animateSubsView(view, holder);
            }
        });

        holder.mSubsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onClick(mDataset.get(position));
            }
        });

        //loadImages(mDataset.get(position).mPhotoURL, holder.mPictureView);
        Utils.setImage(myImageLoader, mDataset.get(position).mPhotoURL, holder.mPictureView, R.drawable.ic_people_black_48dp);

        if (position > myPreviousPosition)
        {
            AnimationUtils.animateSunblind(holder, true);
        }
        else
        {
            AnimationUtils.animateSunblind(holder, false);
        }
    }


    void animateSubsView(final View view, final QueueListAdapter.ViewHolder holder)
    {
        // If the originalHeight is 0 then find the height of the View being used
        // This would be the height of the cardview
        if (holder.mOriginalHeight == 0)
        {
            holder.mOriginalHeight = view.getHeight();
        }

        // Declare a ValueAnimator object
        ValueAnimator valueAnimator;
        if (!holder.mIsViewExpanded)
        {

            valueAnimator = ValueAnimator.ofInt(holder.mOriginalHeight, holder.mOriginalHeight + (int) (holder.mOriginalHeight * 2.0)); // These values in this method can be changed to expand however much you like
        }
        else
        {
            holder.mIsViewExpanded = false;
            valueAnimator = ValueAnimator.ofInt(holder.mOriginalHeight + (int) (holder.mOriginalHeight * 2.0), holder.mOriginalHeight);

            Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

            a.setDuration(200);
            // Set a listener to the animation and configure onAnimationEnd
            a.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    holder.showSubsPart(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            // Set the animation on the custom view
            holder.mSubsPart.startAnimation(a);
        }
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Integer value = (Integer) animation.getAnimatedValue();
                view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                holder.showSubsPart(true);
            }
        });
        valueAnimator.start();
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }


}
