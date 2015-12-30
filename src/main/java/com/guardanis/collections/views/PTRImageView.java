package com.guardanis.collections.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PTRImageView extends ImageView {

    private static final float MIN_SCALE = .6f;
    private static final float MAX_SCALE = 1f;

    private float currentScale = MIN_SCALE;

    public PTRImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PTRImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PTRImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(currentScale, currentScale, canvas.getWidth() / 2, canvas.getHeight() / 2);
        super.onDraw(canvas);
        canvas.restore();
    }

    public void onRefreshViewPulled(float percentageOfThresholdPulled) {
        currentScale = MIN_SCALE + ((MAX_SCALE - MIN_SCALE) * percentageOfThresholdPulled);

        if(currentScale < MIN_SCALE)
            currentScale = MIN_SCALE;
        else if(currentScale > MAX_SCALE) currentScale = MAX_SCALE;

        postInvalidate();
    }
}
