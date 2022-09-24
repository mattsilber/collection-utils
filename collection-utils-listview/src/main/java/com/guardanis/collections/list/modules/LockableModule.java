package com.guardanis.collections.list.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.list.ModularListView;

public class LockableModule extends CollectionModule<ModularListView> {

    private boolean locked = false;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return locked;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {
    }

    @Override
    public void onScrollStateChanged(int i) {
    }

    @Override
    public void onScroll(int... values) {
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
