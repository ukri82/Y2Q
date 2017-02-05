package com.y2.y2q;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.y2.utils.AnimationUtils;
import com.y2.y2q.model.TokenSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class TokenSlotListAdapter extends RecyclerView.Adapter<TokenSlotListAdapter.ViewHolder>
{
    private ArrayList<TokenSlot> mDataset = new ArrayList<>();

    public interface TokenSlotClickListener
    {
        public void onClick(TokenSlot slot);
    }

    private int myPreviousPosition = 0;
    int mPrevSelection = -1;

    TokenSlotClickListener mListener;

    public TokenSlotListAdapter()
    {

    }

    public void registerTokenSlotClickListener(TokenSlotClickListener listener)
    {
        mListener = listener;
    }

    public void appendTokenSlotList(List<TokenSlot> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }

    public void add(TokenSlot dataset)
    {
        mDataset.add(0, dataset);
        notifyItemRangeInserted(0, 1);
    }

    public void clear()
    {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void remove(TokenSlot dataset)
    {
        for(int i = 0; i < mDataset.size(); i++)
        {
            if(mDataset.get(i).mId.compareTo(dataset.mId) == 0)
            {
                mDataset.remove(i);
                notifyItemRangeRemoved(i, 1);
                break;
            }
        }
    }

    public TokenSlot getFirst()
    {
        TokenSlot slot = null;
        if(mDataset.size() > 0)
            slot = mDataset.get(0);

        return slot;
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

    @Override
    public void onBindViewHolder(final TokenSlotListAdapter.ViewHolder holder, final int position)
    {
        holder.mTextView.setText(mDataset.get(position).mName);
        holder.mTokenView.setText(mDataset.get(position).mCurrentTokenNumber + "");

        holder.mParentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //view.setBackgroundColor(view.getContext().getResources().getColor(R.color.primary_light));
                if(mListener != null)
                {
                    mListener.onClick(mDataset.get(holder.getAdapterPosition()));
                }
            }
        });
        //setOrgImage(mDataset.get(position), holder.mPictureView);

        if (position > myPreviousPosition)
        {
            AnimationUtils.animateSunblind(holder, true);
        }
        else
        {
            AnimationUtils.animateSunblind(holder, false);
        }
    }

    /*void selectItem(int currentPos)
    {
        GridLayoutManager layoutManager = ((GridLayoutManager)getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();

    }*/

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
