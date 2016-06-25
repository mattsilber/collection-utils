package com.guardanis.collections.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.guardanis.collections.tools.Loader;
import com.guardanis.collections.tools.PullToRefreshHelper;
import com.guardanis.collections.tools.UpdateLoaderTask;
import com.guardanis.collections.tools.ViewHelper;

public class PTRLoadingView extends ImageView implements Loader {

    private final int UPDATE_RATE = 50;

    private PullToRefreshHelper.LoadingImageDelegate loadingImageDelegate;

    private UpdateLoaderTask updateLoaderAsync;
    private boolean enabled = false;

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

    @Override
    public void update() {
        if(loadingImageDelegate != null)
            loadingImageDelegate.update();

        invalidate();
    }

    @Override
    public void stop() {
        enabled = false;

        if(updateLoaderAsync != null)
            updateLoaderAsync.cancel();
    }

    @Override
    public void start() {
        stop();

        enabled = true;

        if(loadingImageDelegate != null)
            loadingImageDelegate.reset();

        updateLoaderAsync = new UpdateLoaderTask(this);

        new Thread(updateLoaderAsync)
                .start();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getUpdateRate() {
        return UPDATE_RATE;
    }

    @Override
    public boolean canUpdate() {
        return enabled;
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();

        super.onDetachedFromWindow();
    }
}
