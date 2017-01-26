package com.y2.y2q;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.y2.y2q.misc.AnimationUtils;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.Organization;
import com.y2.y2q.model.TokenSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class TokenSlotListAdapter extends RecyclerView.Adapter<TokenSlotListAdapter.ViewHolder>
{
    private ArrayList<TokenSlot> mDataset = new ArrayList<>();


    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;
    private int myPreviousPosition = 0;

    public TokenSlotListAdapter()
    {
        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();
    }

    public void appendTokenSlotList(List<TokenSlot> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }

    public void add(ArrayList<TokenSlot> dataset)
    {
        mDataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View mParentView;
        public TextView mTextView;
        public TextView mTokenView;
        public ImageView mPictureView;
        public ViewHolder(View v)
        {
            super(v);
            mParentView = v;
            mTextView = (TextView)v.findViewById(R.id.title);
            mTokenView = (TextView)v.findViewById(R.id.token);
            mPictureView = (ImageView)v.findViewById(R.id.picture);
        }
    }



    @Override
    public TokenSlotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_slot_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /*private void setOrgImage(final Organization organization, final ImageView imageView)
    {
        if (!organization.mPhotoURL.equals("N.A") && !organization.mPhotoURL.equals(""))
        {
            if (organization.mBitmap == null)
            {
                myImageLoader.get(organization.mPhotoURL, new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        organization.mBitmap = response.getBitmap();
                        imageView.setImageBitmap(organization.mBitmap);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        imageView.setImageResource(R.drawable.organization);
                    }
                });
            }
            else
            {
                imageView.setImageBitmap(organization.mBitmap);
            }
        }
        else
        {
            imageView.setImageResource(R.drawable.organization);
        }
    }*/

    @Override
    public void onBindViewHolder(TokenSlotListAdapter.ViewHolder holder, int position)
    {
        holder.mTextView.setText(mDataset.get(position).mName);
        holder.mTokenView.setText(mDataset.get(position).mCurrentTokenNumber + "");

        //setOrgImage(mDataset.get(position), holder.mPictureView);

        if (position > myPreviousPosition)
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, true);
            }
        }
        else
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, false);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
