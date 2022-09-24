package com.guardanis.collections.recycler.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.recycler.ModularRecyclerView;

public class EndlessModule extends CollectionModule<ModularRecyclerView> {

    public interface EndlessEventListener {

        public void onNextPage();
    }

    public static final int NEXT_PAGE_ITEM_THRESHOLD = 7;

    protected EndlessEventListener eventListener;
    protected boolean loading = false;
    protected boolean endingReached = false;
    protected int nextPageThresholdThreshold = EndlessModule.NEXT_PAGE_ITEM_THRESHOLD;

    public EndlessModule(EndlessEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public void reset() {
        loading = false;
        endingReached = false;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {
    }

    @Override
    public void onScrollStateChanged(int i) {
    }

    @Override
    public void onScroll(int... values) {
        final ModularRecyclerView parent = getParent();

        if (isScrollEventProcessable()) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) parent.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();

                handleNextPage(lastVisibleItem);
            }
            else if (parent.getLayoutManager() instanceof GridLayoutManager) {
                int lastVisibleItem = ((GridLayoutManager) parent.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();

                handleNextPage(lastVisibleItem);
            }
            else {
                throw new RuntimeException(
                    String.format(
                        "Unsupported LayoutManager of type: %s",
                        parent
                            .getLayoutManager()
                            .getClass()
                            .getName()
                    )
                );
            }
        }
    }

    private void handleNextPage(int lastVisibleItem) {
        if (getParent().getAdapter().getItemCount() - NEXT_PAGE_ITEM_THRESHOLD < lastVisibleItem) {
            loading = true;
            eventListener.onNextPage();
        }
    }

    private boolean isScrollEventProcessable() {
        final ModularRecyclerView parent = getParent();

        return !(
            parent == null
                || parent.getAdapter() == null
                || parent.getAdapter().getItemCount() < 1
                || eventListener == null
                || loading
                || endingReached
        );
    }

    public void onEndingReached() {
        endingReached = true;
    }

    public void setEndingReached(boolean endingReached) {
        this.endingReached = endingReached;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setNextPageThreshold(int nextPageThresholdThreshold) {
        if (nextPageThresholdThreshold < 0) {
            throw new RuntimeException("Next page threshold can't be less than 0");
        }

        this.nextPageThresholdThreshold = nextPageThresholdThreshold;
    }
}
