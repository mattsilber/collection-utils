package com.guardanis.collections.recycler.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.recycler.ModularRecyclerView;

public class ScrollEventModule extends CollectionModule<ModularRecyclerView> {

    public interface ScrollEventListener {
        public void onScrolled(int dX, int dY);
    }

    protected ScrollEventListener eventListener;

    public ScrollEventModule(ScrollEventListener eventListener){
        this.eventListener = eventListener;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) { }

    @Override
    public void onScrollStateChanged(int i) { }

    @Override
    public void onScroll(int... values) {
        if(getParent() != null)
            eventListener.onScrolled(values[0], values[1]);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
