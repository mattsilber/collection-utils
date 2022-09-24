package com.guardanis.collections.list.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.list.ModularListView;

public class EndlessModule extends CollectionModule<ModularListView> {

    public interface EndlessEventListener {

        public void onNextPage();
    }

    public final int NEXT_PAGE_ITEM_THRESHOLD = 7;

    protected EndlessEventListener eventListener;
    protected boolean loading = false;
    protected boolean endingReached = false;

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
        if (isScrollEventProcessable() && values[1] + values[0] >= values[2] - NEXT_PAGE_ITEM_THRESHOLD) {
            loading = true;
            eventListener.onNextPage();
        }
    }

    private boolean isScrollEventProcessable() {
        final ModularListView parent = getParent();

        return !(
            parent == null
                || parent.getAdapter() == null
                || parent.getAdapter().getCount() < 1
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
}
