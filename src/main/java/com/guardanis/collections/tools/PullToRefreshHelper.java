package com.guardanis.collections.tools;

import android.view.ViewGroup;

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

    public interface PullToRefreshListener {
        public boolean meetsPullingRequirements();

        public ViewGroup getRefreshViewsParent();

        public void onRefreshViewPulled(float distance);

        public void onRefreshViewReleased(boolean thresholdReached);

        public void onRefreshViewClosed(boolean thresholdReached);

        public void onRefreshCompleted();
    }

    public interface LayoutEventListener {
        public void onOpenedDistanceChanged(float distance);
    }

}
