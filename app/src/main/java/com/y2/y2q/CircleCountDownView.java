package com.y2.y2q;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.y2.y2q.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by u on 01.02.2017.
 */

public class CircleCountDownView extends FrameLayout {
    private ProgressBar progressBarView;
    private TextView progressTextView;

    public CircleCountDownView(Context context) {
        super(context);
        init(context);
    }

    public CircleCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleCountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context ctx) {
        View rootView = inflate(ctx, R.layout.layout_count_down_view, this);
        progressBarView = (ProgressBar) rootView.findViewById(R.id.view_progress_bar);
        progressTextView = (TextView) rootView.findViewById(R.id.view_progress_text);
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
    }

    /*public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);
        int elapsedTime = endTime - startTime;
        progressTextView.setText(String.valueOf(elapsedTime));
    }*/

    public void setProgress(int startTime) {
        progressBarView.setProgress(100);
        progressTextView.setText(String.valueOf(startTime));
    }
}
