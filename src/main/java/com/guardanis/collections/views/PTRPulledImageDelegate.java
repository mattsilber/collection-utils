package com.guardanis.collections.views;

import android.graphics.Canvas;

import com.guardanis.collections.tools.PullToRefreshHelper;

public class PTRPulledImageDelegate implements PullToRefreshHelper.PulledImageDelegate {

    private static final float MIN_SCALE = .6f;
    private static final float MAX_SCALE = 1f;

    private float currentScale = MIN_SCALE;

    @Override
    public void adjust(Canvas canvas) {
        canvas.scale(currentScale,
                currentScale,
                canvas.getWidth() / 2,
                canvas.getHeight() / 2);
    }

    @Override
    public void onRefreshViewPulled(float percentageOfThresholdPulled) {
        currentScale = MIN_SCALE + ((MAX_SCALE - MIN_SCALE) * percentageOfThresholdPulled);

        if(currentScale < MIN_SCALE)
            currentScale = MIN_SCALE;
        else if(currentScale > MAX_SCALE) currentScale = MAX_SCALE;
    }

}
