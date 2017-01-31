package com.y2.y2q;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.y2.y2q.ServerInterface.TaskGetQueueDetails;
import com.y2.y2q.ServerInterface.TaskLoadPreviousQueues;
import com.y2.y2q.model.DeviceIdentity;
import com.y2.y2q.model.Queue;
import com.y2.y2q.model.QueueDetails;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllPrevQueuesFragment extends Fragment implements QueueListAdapter.QueueClickListener, TaskLoadPreviousQueues.QueuesLoadedListener, TaskGetQueueDetails.QueueDetailsListener
{

    private RecyclerView mTokenListView;
    private QueueListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    QueueDetailsHandler mQHandler;


    public AllPrevQueuesFragment()
    {

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

        final TaskLoadPreviousQueues.QueuesLoadedListener listener = this;
        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadPreviousQueues(listener, mAdapter.getItemCount(), 10, DeviceIdentity.get()).execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });
        new TaskLoadPreviousQueues(listener, mAdapter.getItemCount(), 10, DeviceIdentity.get()).execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_prev_queues, container, false);
    }

    @Override
    public void onClick(Queue queue)
    {
        new TaskGetQueueDetails(this, queue.mId).execute();
    }

    @Override
    public void onQueuesLoaded(ArrayList<Queue> listQueues)
    {
        mAdapter.appendQueueList(listQueues);
    }

    @Override
    public void onQueueDetails(QueueDetails queueDetails)
    {
        if(queueDetails != null)
        {
            mQHandler = new QueueDetailsHandler(this.getActivity(), getView().findViewById(R.id.new_queue_card), queueDetails);
            mQHandler.initializeQueueHandler();
        }
    }
}
