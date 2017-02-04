package com.y2.y2q;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterQueueDetailsFragment extends Fragment
{
    QueueDetailsHandler mQHandler;

    public EnterQueueDetailsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        initializeQueueNumberEdit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_queue_details, container, false);
    }


    private void initializeQueueNumberEdit()
    {

        EditText editText = (EditText)getView().findViewById(R.id.queue_number);
        editText.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s)
            {
                String queueId = s.toString();
                if(!queueId.isEmpty() )
                {
                    if(mQHandler == null)
                    {
                        mQHandler = new QueueDetailsHandler(getActivity(), getView().findViewById(R.id.new_queue_card));
                    }
                    mQHandler.getQueueDetails(queueId);
                }
            }
        });
    }

}
