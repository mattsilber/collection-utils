package com.guardanis.collections.list.modules;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.R;
import com.guardanis.collections.list.ModularListView;

public class StickyHeaderModule extends CollectionModule<ModularListView> {

    private Bitmap currentStickyBitmap;
    private View nextStickyView;

    private int lastPosition = -1;
    private int stickyHeaderPosition = 0;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onScrollStateChanged(int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            adjustStickyHeaders();
    }

    @Override
    public void onScroll(int... values) {
        adjustStickyHeaders();
    }

    private void adjustStickyHeaders() {
        final ModularListView parent = getParent();

        if(parent == null)
            return;

        int firstVisiblePosition = parent.getFirstVisiblePosition();

        if(firstVisiblePosition < 1)
            removeSelectedPosition();
        else if(lastPosition < firstVisiblePosition)
            onCurrentPositionIncreased(firstVisiblePosition);
        else updateGeneralStickyPosition(firstVisiblePosition);

        lastPosition = firstVisiblePosition;
    }

    private void removeSelectedPosition() {
        currentStickyBitmap = null;
        nextStickyView = null;
    }

    private void onCurrentPositionIncreased(int firstVisiblePosition){
        View firstVisible = getParent()
                .getChildAt(0);

        if(isViewAHeader(firstVisible)){
            currentStickyBitmap = getCurrentStickyBitmap(firstVisible);
            stickyHeaderPosition = firstVisiblePosition;
        }

        updateNextStickyPosition();
    }

    private void updateGeneralStickyPosition(int firstVisiblePosition){
        if(firstVisiblePosition < stickyHeaderPosition){
            currentStickyBitmap = null;
            nextStickyView = null;
            stickyHeaderPosition = 0;
        }
    }

    private void updateNextStickyPosition(){
        View secondVisible = getParent()
                .getChildAt(1);

        if(isViewAHeader(secondVisible))
            nextStickyView = secondVisible;
        else nextStickyView = null;
    }

    protected boolean isViewAHeader(View view) {
        if(view == null)
            return false;
        else return Boolean.parseBoolean(
                String.valueOf(view.getTag(R.integer.cu__sticky_header_tag_ref)));
    }

    protected Bitmap getCurrentStickyBitmap(View v){
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        return bitmap;
    }

    @Override
    public void onDrawDispatched(Canvas canvas){
        if(currentStickyBitmap != null){
            canvas.save();
            canvas.translate(0, getAdjustedTopPosition());
            canvas.drawBitmap(currentStickyBitmap, 0, 0, null);
            canvas.restore();
        }
    }

    private int getAdjustedTopPosition() {
        if(nextStickyView != null && nextStickyView.getTop() <= currentStickyBitmap.getHeight())
            return Math.max(-1 * (currentStickyBitmap.getHeight() - nextStickyView.getTop()), -currentStickyBitmap.getHeight());

        return 0;
    }

}
