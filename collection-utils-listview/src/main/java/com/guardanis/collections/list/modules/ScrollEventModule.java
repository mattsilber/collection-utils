package com.guardanis.collections.list.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.list.ModularListView;

public class ScrollEventModule extends CollectionModule<ModularListView> {

    public interface ScrollEventListener {

        public void onScrolled(int distance);
    }

    protected ScrollEventListener eventListener;

    protected boolean headerEnabled = false;
    protected int headerHeight = 0;

    public ScrollEventModule(ScrollEventListener eventListener) {
        this(eventListener, false);
    }

    public ScrollEventModule(ScrollEventListener eventListener, boolean headerEnabled) {
        this.eventListener = eventListener;
        this.headerEnabled = headerEnabled;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {
    }

    @Override
    public void onScrollStateChanged(int i) {
    }

    @Override
    public void onScroll(int... values) {
        final ModularListView parent = getParent();

        if (parent != null) {
            if (headerEnabled) {
                onHeaderedScroll(values);
            }
            else {
                eventListener.onScrolled(parent.computeVerticalScrollOffset());
            }
        }
    }

    private void onHeaderedScroll(int... values) {
        final ModularListView parent = getParent();

        if (values[1] > 0 && values[0] == 0) {
            headerHeight = parent.getChildAt(0).getHeight();
            eventListener.onScrolled(-parent.getChildAt(0).getTop());
        }
        else {
            eventListener.onScrolled(headerHeight + parent.computeVerticalScrollOffset());
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
