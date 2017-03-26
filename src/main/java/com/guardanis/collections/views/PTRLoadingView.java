package com.guardanis.collections.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.guardanis.collections.tools.PullToRefreshHelper;
import com.guardanis.collections.tools.UpdateLoaderTask;
import com.guardanis.collections.tools.ViewHelper;

public class PTRLoadingView extends ImageView implements Runnable {

    private PullToRefreshHelper.LoadingImageDelegate loadingImageDelegate;

    private UpdateLoaderTask updateLoaderAsync;

    public PTRLoadingView(Context context) {
        super(context);
        init();
    }

    public PTRLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PTRLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        ViewHelper.disableHardwareAcceleration(this);
    }

    public PTRLoadingView setLoadingImageDelegate(PullToRefreshHelper.LoadingImageDelegate loadingImageDelegate){
        this.loadingImageDelegate = loadingImageDelegate;
        return this;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();

        if(loadingImageDelegate != null)
            loadingImageDelegate.adjust(canvas);

        super.onDraw(canvas);

        canvas.restore();
    }

    public void stop() {
        if(updateLoaderAsync != null)
            updateLoaderAsync.cancel();

        updateLoaderAsync = null;
    }

    public void start() {
        stop();

        if(loadingImageDelegate != null)
            loadingImageDelegate.reset();

        updateLoaderAsync = new UpdateLoaderTask(this, this);

        new Thread(updateLoaderAsync)
                .start();
    }

    @Override
    public void run() {
        if(loadingImageDelegate != null)
            loadingImageDelegate.update();

        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();

        super.onDetachedFromWindow();
    }

}
