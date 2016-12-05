package com.y2.y2q;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.y2.y2q.ServerInterface.TaskLoadOrganiations;
import com.y2.y2q.misc.VolleySingleton;
import com.y2.y2q.model.Organization;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrganizationFragment extends Fragment implements TaskLoadOrganiations.OrganizationsLoadedListener
{

    private RecyclerView mOrgListView;
    private OrganizationListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;



    public OrganizationFragment()
    {

    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        mOrgListView = (RecyclerView) getActivity().findViewById(R.id.org_list_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mOrgListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new OrganizationListAdapter();

        mOrgListView.setAdapter(mAdapter);

        final TaskLoadOrganiations.OrganizationsLoadedListener listener = this;
        mOrgListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadOrganiations(listener, mAdapter.getItemCount(), 10, "1", 0.0, 0.0).execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });

        new TaskLoadOrganiations(this, 0, 10, "1", 0.0, 0.0).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization, container, false);
    }



    @Override
    public void onVideoItemsLoaded(ArrayList<Organization> listOrgs)
    {
        mAdapter.appendOrgList(listOrgs);
    }
}
