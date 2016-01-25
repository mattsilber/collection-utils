package com.guardanis.collections.list.modules;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.R;
import com.guardanis.collections.list.ModularListView;
import com.guardanis.collections.tools.PullToRefreshHelper.*;
import com.guardanis.collections.views.PTRImageView;
import com.guardanis.collections.views.PTRLoadingView;

public class PullToRefreshModule extends CollectionModule<ModularListView> {

    public static final int PULL_CLOSING_ANIMATION_CYCLE_COUNT = 25;
    public static final int PULL_CLOSING_ANIMATION_SLEEP_TIME = 10;

    protected ViewGroup refreshViewParent;
    protected PTRImageView refreshImageView;

    protected ViewGroup refreshLoadingViewParent;
    protected PTRLoadingView refreshLoadingView;

    private PullToRefreshState pullToRefreshState = PullToRefreshState.NONE;
    private int refreshThreshold = 0;
    private int maxPullThreshold = 0;
    private float pullTouchStartY = -1;
    private float pullTouchLastY = -1;

    private Thread refreshViewScaleAnimation;
    private boolean closingAnimationActive = false;

    private RefreshEventListener refreshEventListener;
    private boolean refreshable = true;

    private LayoutEventListener layoutEventListener;

    public PullToRefreshModule(Context context, RefreshEventListener refreshEventListener) {
        this.refreshEventListener = refreshEventListener;

        refreshThreshold = (int) (context.getResources().getDisplayMetrics().heightPixels * .20);
        maxPullThreshold = (int) (context.getResources().getDisplayMetrics().heightPixels * .7);
    }

    @Override
    public void onDrawDispatched(Canvas canvas) { }

    @Override
    public void onScrollStateChanged(int i) { }

    @Override
    public void onScroll(int... values) { }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(refreshable && meetsPullingRequirements()){
            if(onRefreshableTouch(v, event))
                return true;
        }
        else if(isTouchBelowStart()) return true;

        return false;
    }

    public boolean isTouchBelowStart() {
        return pullTouchLastY - pullTouchStartY > 0;
    }

    private boolean onRefreshableTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(requiresViewSetup())
                    onPrepareForPullToRefresh(getRefreshViewsParent(), event);
                return onTouchMove(event);
            case MotionEvent.ACTION_UP:
            default:
                onTouchUp(event);
                break;
        }
        return false;
    }

    public boolean requiresViewSetup() {
        return pullTouchStartY < 0;
    }

    public boolean onTouchMove(MotionEvent event) {
        pullTouchLastY = event.getRawY();
        float distance = pullTouchLastY - pullTouchStartY;

        if(distance >= 0){
            onTouchMovedPositiveDistance(distance);
            return true;
        }

        pullToRefreshState = PullToRefreshState.NONE;
        resetTouchCoordinates();
        return false;
    }

    private void onTouchMovedPositiveDistance(float distance) {
        if(distance > maxPullThreshold)
            distance = maxPullThreshold;

        onRefreshViewPulled(distance);

        if(distance < refreshThreshold){
            if(pullToRefreshState != PullToRefreshState.PULLING_BELOW_THRESHOLD)
                resetViews();

            pullToRefreshState = PullToRefreshState.PULLING_BELOW_THRESHOLD;
        }
        else pullToRefreshState = PullToRefreshState.PULLING_PASSED_THRESHOLD;
    }

    public boolean onTouchUp(MotionEvent event) {
        if(pullTouchLastY - pullTouchStartY >= refreshThreshold){
            pullToRefreshState = PullToRefreshState.RELEASING_PASSED_THRESHOLD;
            onReleasedPullToRefreshViewPassedThreshold();
        }
        else{
            pullToRefreshState = PullToRefreshState.RELEASING_BELOW_THRESHOLD;
            onReleasedPullToRefreshViewBeforeThreshold();
        }

        resetTouchCoordinates();
        return false;
    }

    private void resetTouchCoordinates() {
        pullTouchStartY = -1;
        pullTouchLastY = -1;
    }

    protected void onPrepareForPullToRefresh(ViewGroup parent, MotionEvent event) {
        refreshViewParent = (ViewGroup) parent.findViewById(R.id.cu__ptr_parent);
        if(refreshViewParent != null){
            refreshImageView = (PTRImageView) refreshViewParent.findViewById(R.id.cu__ptr_image);

            refreshLoadingViewParent = (ViewGroup) parent.findViewById(R.id.cu__ptr_loading_view_parent);
            refreshLoadingView = (PTRLoadingView) parent.findViewById(R.id.cu__ptr_loading_view);
        }

        pullTouchStartY = event.getRawY();
    }

    public boolean meetsPullingRequirements() {
        return !(parent == null || parent.getChildAt(0) == null || parent.isFlinging())
                && parent.getFirstVisiblePosition() < 1
                && parent.getChildAt(0).getTop() == 0;
    }

    public ViewGroup getRefreshViewsParent() {
        return (ViewGroup) parent.getChildAt(0);
    }

    public void onRefreshViewPulled(float distance) {
        stopClosingAnimation();

        if(refreshable && refreshViewParent != null){
            refreshViewParent.setVisibility(View.VISIBLE);
            refreshViewParent.getLayoutParams().height = (int) distance;
            refreshViewParent.setLayoutParams(refreshViewParent.getLayoutParams());
            refreshViewParent.postInvalidate();

            refreshImageView.onRefreshViewPulled(distance / refreshThreshold);
        }

        if(layoutEventListener != null)
            layoutEventListener.onOpenedDistanceChanged(distance);
    }

    protected void resetViews() {
        if(refreshImageView != null)
            refreshImageView.setVisibility(View.VISIBLE);

        if(refreshLoadingViewParent != null)
            refreshLoadingViewParent.setVisibility(View.GONE);
    }

    protected void onReleasedPullToRefreshViewBeforeThreshold() {
        closeRefreshView(0, false);
    }

    protected void onReleasedPullToRefreshViewPassedThreshold() {
        disablePreLoadingViews();
        closeRefreshView((int) parent.getResources().getDimension(R.dimen.cu__ptr_loading_image_height), true);
    }

    private void closeRefreshView(final int height, final boolean thresholdReached) {
        if(refreshViewParent != null){
            closingAnimationActive = true;
            refreshViewScaleAnimation = new Thread(new Runnable() {
                public void run() {
                    int preAdjustmentCloseDistance = (refreshViewParent.getLayoutParams().height - height) / PULL_CLOSING_ANIMATION_CYCLE_COUNT;

                    if(preAdjustmentCloseDistance < 1)
                        preAdjustmentCloseDistance = 1;

                    final int closeDistancePerCycle = preAdjustmentCloseDistance;

                    while(closingAnimationActive && refreshViewParent.getLayoutParams().height > height){
                        try{
                            refreshViewParent.post(new Runnable() {
                                public void run() {
                                    if((int) (refreshViewParent.getLayoutParams().height - closeDistancePerCycle) >= height)
                                        refreshViewParent.getLayoutParams().height = (int) (refreshViewParent.getLayoutParams().height - closeDistancePerCycle);
                                    else refreshViewParent.getLayoutParams().height = height;

                                    refreshViewParent.setLayoutParams(refreshViewParent.getLayoutParams());
                                    if(layoutEventListener != null)
                                        layoutEventListener.onOpenedDistanceChanged(refreshViewParent.getLayoutParams().height);
                                }
                            });

                            Thread.sleep(PULL_CLOSING_ANIMATION_SLEEP_TIME);
                        }
                        catch(Exception e){ }
                    }

                    if(closingAnimationActive){
                        refreshViewParent.post(new Runnable() {
                            public void run() {
                                onRefreshViewClosed(thresholdReached);
                            }
                        });
                    }
                }
            });
            refreshViewScaleAnimation.start();
        }
    }

    public void onRefreshViewClosed(boolean thresholdReached) {
        stopClosingAnimation();

        refreshViewParent.getLayoutParams().height = 0;
        refreshViewParent.setLayoutParams(refreshViewParent.getLayoutParams());
        refreshViewParent.setVisibility(View.GONE);
        refreshViewParent.postInvalidate();

        if(thresholdReached && refreshEventListener != null){
            pullToRefreshState = PullToRefreshState.REFRESHING;
            enableLoadingView();
            refreshable = false;
            refreshEventListener.onRefresh();
        }

        if(layoutEventListener != null)
            layoutEventListener.onOpenedDistanceChanged(0);
    }

    private void enableLoadingView() {
        if(!(refreshLoadingViewParent == null || refreshLoadingView == null)){
            refreshLoadingViewParent.setVisibility(View.VISIBLE);
            refreshLoadingView.start();
        }
    }

    private void disableLoadingView() {
        if(!(refreshLoadingViewParent == null || refreshLoadingView == null)){
            refreshLoadingViewParent.setVisibility(View.GONE);
            refreshLoadingView.stop();
        }
    }

    private void disablePreLoadingViews() {
        if(refreshImageView != null)
            refreshImageView.setVisibility(View.GONE);
    }

    protected void stopClosingAnimation() {
        closingAnimationActive = false;
    }

    public void setRefreshEventListener(RefreshEventListener refreshEventListener) {
        this.refreshEventListener = refreshEventListener;
    }

    /**
     * Call this method when asynchronous refresh has completed to reset the state of the Pull to Refresh view
     */
    public void onRefreshCompleted() {
        pullToRefreshState = PullToRefreshState.NONE;
        this.refreshable = true;
        disableLoadingView();

        if(refreshViewParent != null){
            refreshViewParent.getLayoutParams().height = (int) refreshViewParent.getResources().getDimension(R.dimen.cu__ptr_loading_image_height);
            refreshViewParent.setLayoutParams(refreshViewParent.getLayoutParams());
            refreshViewParent.postInvalidate();

            if(layoutEventListener != null)
                layoutEventListener.onOpenedDistanceChanged(refreshViewParent.getLayoutParams().height);

            onReleasedPullToRefreshViewBeforeThreshold();
        }
    }

    public void setLayoutEventListener(LayoutEventListener layoutEventListener) {
        this.layoutEventListener = layoutEventListener;
    }
}
