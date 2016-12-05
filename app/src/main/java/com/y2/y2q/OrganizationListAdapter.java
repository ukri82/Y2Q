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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class OrganizationListAdapter extends RecyclerView.Adapter<OrganizationListAdapter.ViewHolder>
{
    private ArrayList<Organization> mDataset = new ArrayList<>();


    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;
    private int myPreviousPosition = 0;

    public OrganizationListAdapter()
    {
        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();
    }

    public void appendOrgList(List<Organization> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }

    public void add(ArrayList<Organization> dataset)
    {
        mDataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View mParentView;
        public TextView mTextView;
        public TextView mAddressView;
        public ImageView mPictureView;
        public ViewHolder(View v)
        {
            super(v);
            mParentView = v;
            mTextView = (TextView)v.findViewById(R.id.title);
            mAddressView = (TextView)v.findViewById(R.id.address);
            mPictureView = (ImageView)v.findViewById(R.id.picture);
        }
    }



    @Override
    public OrganizationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.organization_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private void setOrgImage(final Organization organization, final ImageView imageView)
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
    }

    @Override
    public void onBindViewHolder(OrganizationListAdapter.ViewHolder holder, int position)
    {
        holder.mTextView.setText(mDataset.get(position).mName);
        holder.mAddressView.setText(mDataset.get(position).mAddress);

        setOrgImage(mDataset.get(position), holder.mPictureView);

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
