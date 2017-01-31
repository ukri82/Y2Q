package com.y2.y2q;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.y2.y2q.ServerInterface.TaskGetTokenSlot;
import com.y2.y2q.model.TokenSlot;

public class TokenSlotActivity extends AppCompatActivity implements TaskGetTokenSlot.GetTokenSlotListener
{

    TokenSlot mTokenSlot;

    private Handler mHandler = new Handler();

    private Runnable mUpdateTokenRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_slot);

        Intent intent = getIntent();
        mTokenSlot = (TokenSlot)intent.getSerializableExtra(TokenSlot.TOKEN_SLOT_OBJ);

        updateUI();

        final TaskGetTokenSlot.GetTokenSlotListener listener = this;
        mUpdateTokenRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                // The method you want to call every now and then.
                new TaskGetTokenSlot(listener, mTokenSlot.mId).execute();
                mHandler.postDelayed(this,5000);
            }
        };
        mHandler.postDelayed(mUpdateTokenRunnable,5000);
    }

    @Override
    public void onBackPressed()
    {
        mHandler.removeCallbacks(mUpdateTokenRunnable);
        super.onBackPressed();
        this.finish();
    }

    void updateUI()
    {
        ((TextView)findViewById(R.id.title)).setText(mTokenSlot.mName);
        ((TextView)findViewById(R.id.token)).setText(mTokenSlot.mCurrentTokenNumber + "");
    }

    @Override
    public void onGet(TokenSlot slot)
    {
        mTokenSlot = slot;
        updateUI();
    }
}
