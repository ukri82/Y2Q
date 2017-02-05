package com.y2.y2q;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.y2.serverinterface.DeviceIdentity;
import com.y2.serverinterface.ServerQuery;
import com.y2.serverinterface.ServerQueryChunkTask;
import com.y2.y2q.ServerInterface.QueueResultParser;
import com.y2.y2q.model.Queue;
import com.y2.y2q.model.TokenSlot;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteQueuesFragment extends Fragment implements QueueListAdapter.QueueClickListener
{

    private RecyclerView mTokenListView;
    private QueueListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    QueueDetailsView mQHandler;


    public interface EmptyFavouritesListener
    {
        public void onEmpty();
    }

    EmptyFavouritesListener mEmptyFavListener;

    public FavouriteQueuesFragment()
    {

    }

    public void registerEmptyFavListener(EmptyFavouritesListener listener)
    {
        mEmptyFavListener = listener;
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);
        init();
    }

    void init()
    {
        mTokenListView = (RecyclerView) getView().findViewById(R.id.queue_list_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QueueListAdapter(this.getActivity(), this, mTokenListView);

        mTokenListView.setAdapter(mAdapter);

        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                populatePreviousQueues();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });
        populatePreviousQueues();
    }

    void populatePreviousQueues()
    {
        ServerQuery query = new ServerQuery("get_previous_queues", "y2q/default", mAdapter.getItemCount(), 10);
        query.addParam("PhoneId", DeviceIdentity.get());
        ServerQueryChunkTask<TokenSlot> task = new ServerQueryChunkTask(new ServerQueryChunkTask.ServerQueryTaskListener<Queue>()
        {
            @Override
            public void onResults(ArrayList<Queue> dataList)
            {
                if(mAdapter.getItemCount() == 0 && dataList.size() == 0)
                {
                    showList(false);

                    if(mEmptyFavListener != null)
                        mEmptyFavListener.onEmpty();

                }
                else
                {
                    showList(true);
                }
                mAdapter.appendQueueList(dataList);
            }
        }, new ServerQueryChunkTask.ServerQueryChunkTaskResultParser<Queue>()
        {
            @Override
            public ArrayList<Queue> parse(JSONObject response)
            {
                return QueueResultParser.parse(response);
            }
        },
                query);
        task.execute();
    }

    private void showList(boolean show)
    {
        TextView emptyView = (TextView) getView().findViewById(R.id.empty_view);

        if(show)
        {
            emptyView.setVisibility(View.GONE);
            mTokenListView.setVisibility(View.VISIBLE);
        }
        else
        {
            mTokenListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_queues, container, false);
    }

    @Override
    public void onClick(Queue queue)
    {
        if(mQHandler == null)
        {
            mQHandler = new QueueDetailsView(this.getActivity(), getView().findViewById(R.id.new_queue_card));
        }
        mQHandler.getQueueDetails(queue.mId, true);
    }


}
