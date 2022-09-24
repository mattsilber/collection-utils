package com.guardanis.collections.grid.modules;

import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.grid.ModularGridView;

public abstract class OuterViewModule extends CollectionModule<ModularGridView> {

    protected View targetView;
    protected float translationY;

    protected int originalPadding = -1;

    /**
     * To use this properly, the GridView must be wrapped in a RelativeLayout
     */
    public OuterViewModule(View targetView) {
        this.targetView = targetView;
    }

    @Override
    public CollectionModule<ModularGridView> setParent(final ModularGridView parent) {
        super.setParent(parent);

        if (parent != null) {
            updateParentPadding();

            parent.setClipToPadding(false);

            parent.post(new Runnable() {
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        parent.scrollListBy(-targetView.getHeight());
                    }
                    else {
                        parent.setSelection(0);
                    }
                }
            });
        }

        return this;
    }

    protected abstract void updateParentPadding();

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {
    }

    @Override
    public void onScrollStateChanged(int i) {
    }

    protected boolean isScrollEventProcessable() {
        return getParent() != null;
    }

    public void invalidate() {
        if (getParent() != null) {
            updateParentPadding();
        }
    }

}
