package com.guardanis.collections.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.guardanis.collections.tools.PullToRefreshHelper;

public class PTRImageView extends ImageView {

    private PullToRefreshHelper.PulledImageDelegate imageDelegate;

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

    protected void init() {
        setWillNotDraw(false);
    }

    public PTRImageView setPulledImageDelagate(PullToRefreshHelper.PulledImageDelegate imageDelegate){
        this.imageDelegate = imageDelegate;
        return this;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();

        if(imageDelegate != null)
            imageDelegate.adjust(canvas);

        super.onDraw(canvas);

        canvas.restore();
    }

    public void onRefreshViewPulled(float percentageOfThresholdPulled) {
        if(imageDelegate != null)
            imageDelegate.onRefreshViewPulled(percentageOfThresholdPulled);

        postInvalidate();
    }
}
