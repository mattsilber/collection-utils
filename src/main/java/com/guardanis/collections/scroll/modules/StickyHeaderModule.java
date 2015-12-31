package com.guardanis.collections.scroll.modules;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.R;
import com.guardanis.collections.list.ModularListView;
import com.guardanis.collections.scroll.ModularScrollView;

public class StickyHeaderModule extends CollectionModule<ModularScrollView> {

    private int stickyHeaderId = 0;
    private ViewGroup currentStickyViewParent;
    private View currentStickyView;

    public StickyHeaderModule(int stickyHeaderId){
        this.stickyHeaderId = stickyHeaderId;
    }

    public void reset() {
        currentStickyView = null;
        parent.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onScrollStateChanged(int scrollState) { }

    @Override
    public void onScroll(int... values) {
        adjustStickyHeaders();
    }

    private void adjustStickyHeaders() {
        LinearLayout contentsParent = (LinearLayout) parent.getChildAt(0);
        int firstPosition = getFirstVisibleChildPosition(contentsParent);

        if(firstPosition > -1){
            currentStickyViewParent = (ViewGroup) contentsParent.getChildAt(firstPosition);
            if(currentStickyViewParent != null)
                currentStickyView = currentStickyViewParent.findViewById(stickyHeaderId);
        }
        else currentStickyView = null;
    }

    private int getFirstVisibleChildPosition(LinearLayout contentsParent) {
        Rect scrollBounds = new Rect();
        parent.getHitRect(scrollBounds);

        for(int i = 0; i < contentsParent.getChildCount(); i++)
            if(contentsParent.getChildAt(i).getLocalVisibleRect(scrollBounds))
                return i;

        return -1;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) {
        if(currentStickyView != null){
            canvas.save();
            canvas.translate(0, getAdjustedTopPosition());
            currentStickyView.draw(canvas);
            canvas.restore();
        }
    }

    private int getAdjustedTopPosition() {
        if(parent.getScrollY() > (currentStickyViewParent.getTop() + currentStickyViewParent.getHeight() - currentStickyView.getHeight()))
            return currentStickyViewParent.getTop() + currentStickyViewParent.getHeight() - currentStickyView.getHeight();
        else return parent.getScrollY();
    }

}
