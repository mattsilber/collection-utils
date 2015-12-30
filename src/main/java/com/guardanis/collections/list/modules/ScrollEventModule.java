package com.guardanis.collections.list.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.list.ModularListView;

public class ScrollEventModule extends CollectionModule<ModularListView> {

    public interface ScrollEventListener {
        public void onScrolled(int distance);
    }

    protected ScrollEventListener eventListener;

    public ScrollEventModule(ScrollEventListener eventListener){
        this.eventListener = eventListener;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if(parent != null)
            eventListener.onScrolled(parent.computeVerticalScrollOffset());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
