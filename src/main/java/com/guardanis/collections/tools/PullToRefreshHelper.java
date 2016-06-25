package com.guardanis.collections.tools;

import android.graphics.Canvas;

public class PullToRefreshHelper {

    public enum PullToRefreshState {
        NONE,
        PULLING_BELOW_THRESHOLD,
        PULLING_PASSED_THRESHOLD,
        RELEASING_BELOW_THRESHOLD,
        RELEASING_PASSED_THRESHOLD,
        REFRESHING;
    }

    public interface RefreshEventListener {
        public void onRefresh();
    }

    public interface LayoutEventListener {
        public void onOpenedDistanceChanged(float distance);
    }

    public interface PulledImageDelegate {
        public void adjust(Canvas canvas);
        public void onRefreshViewPulled(float percentageOfThresholdPulled);
    }

    public interface LoadingImageDelegate {
        public void reset();
        public void adjust(Canvas canvas);
        public void update();
    }

}
