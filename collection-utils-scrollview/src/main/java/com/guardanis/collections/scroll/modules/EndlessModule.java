package com.guardanis.collections.scroll.modules;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.scroll.ModularScrollView;

public class EndlessModule extends CollectionModule<ModularScrollView> {

    public interface EndlessEventListener {
        public void onNextPage();
    }

    protected EndlessEventListener eventListener;
    protected boolean loading = false;
    protected boolean endingReached = false;

    public EndlessModule(EndlessEventListener eventListener){
        this.eventListener = eventListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public void reset(){
        loading = false;
        endingReached = false;
    }

    @Override
    public void onDrawDispatched(Canvas canvas) { }

    @Override
    public void onScrollStateChanged(int i) { }

    @Override
    public void onScroll(int... values) {
        final ModularScrollView parent = getParent();

        if(isScrollEventProcessable()){
            ViewGroup container = (ViewGroup) parent.getChildAt(0);
            View view = container.getChildAt(container.getChildCount() - 1);

            int diff = view == null
                    ? 0
                    : ((view.getBottom() - view.getHeight() / 2) - (parent.getHeight() + parent.getScrollY()));

            if(diff < 1){
                loading = true;

                eventListener.onNextPage();
            }
        }
    }

    private boolean isScrollEventProcessable() {
        return !(getParent() == null
                || eventListener == null
                || loading
                || endingReached);
    }

    public void onEndingReached(){
        endingReached = true;
    }

    public void setEndingReached(boolean endingReached){
        this.endingReached = endingReached;
    }

    public boolean isLoading(){
        return loading;
    }

    public void setLoading(boolean loading){
        this.loading = loading;
    }
}
