package com.guardanis.collections.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.guardanis.collections.tools.Loader;
import com.guardanis.collections.tools.UpdateLoaderTask;
import com.guardanis.collections.tools.ViewHelper;

import java.util.Date;

public class PTRLoadingView extends ImageView implements Loader {

    private final int UPDATE_RATE = 50;
    private final int MAX_ROTATION_ANGLE_SPEED = 30;

    private int currentLoadingAngle = 0;
    private int currentLoadingAngleSpeed = 1;

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

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(currentLoadingAngle, canvas.getWidth() / 2, canvas.getHeight() / 2);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public void update() {
        currentLoadingAngle += currentLoadingAngleSpeed;

        currentLoadingAngleSpeed++;
        if(currentLoadingAngleSpeed > MAX_ROTATION_ANGLE_SPEED)
            currentLoadingAngleSpeed = MAX_ROTATION_ANGLE_SPEED;

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
        currentLoadingAngle = 0;
        currentLoadingAngleSpeed = 1;

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
