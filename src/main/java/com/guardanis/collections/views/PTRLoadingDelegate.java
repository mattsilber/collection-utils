package com.guardanis.collections.views;

import android.graphics.Canvas;

import com.guardanis.collections.tools.PullToRefreshHelper;

public class PTRLoadingDelegate implements PullToRefreshHelper.LoadingImageDelegate {

    private final int MAX_ROTATION_ANGLE_SPEED = 30;

    private int currentLoadingAngle = 0;
    private int currentLoadingAngleSpeed = 1;

    @Override
    public void reset(){
        currentLoadingAngle = 0;
        currentLoadingAngleSpeed = 1;
    }

    @Override
    public void adjust(Canvas canvas) {
        canvas.rotate(currentLoadingAngle,
                canvas.getWidth() / 2,
                canvas.getHeight() / 2);
    }

    @Override
    public void update() {
        currentLoadingAngle += currentLoadingAngleSpeed;

        currentLoadingAngleSpeed++;
        if(currentLoadingAngleSpeed > MAX_ROTATION_ANGLE_SPEED)
            currentLoadingAngleSpeed = MAX_ROTATION_ANGLE_SPEED;
    }

}
