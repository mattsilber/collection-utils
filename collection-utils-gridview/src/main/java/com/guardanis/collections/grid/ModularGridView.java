package com.guardanis.collections.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

import com.guardanis.collections.CollectionController;
import com.guardanis.collections.CollectionModule;

public class ModularGridView extends GridView implements AbsListView.OnScrollListener {

    protected CollectionController<ModularGridView> controller;

    protected boolean flinging = false;

    public ModularGridView(Context context) {
        super(context);
        init();
    }

    public ModularGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new CollectionController<ModularGridView>(this);

        setOnTouchListener(controller);
        setOnScrollListener(this);

        if(Build.VERSION.SDK_INT >= 9)
            setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onScrollStateChanged(AbsListView v, int scrollState) {
        flinging = scrollState == OnScrollListener.SCROLL_STATE_FLING;

        controller.onScrollStateChanged(scrollState);
    }

    @Override
    public void onScroll(AbsListView v, int i, int i1, int i2) {
        controller.onScroll(i, i1, i2);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        controller.onDrawDispatched(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(controller != null)
            controller.onDetachedFromWindow();
    }

    public boolean isFlinging() {
        return flinging;
    }

    public CollectionController<ModularGridView> getCollectionController(){
        return controller;
    }

    public ModularGridView registerModule(CollectionModule<ModularGridView> module){
        controller.registerModule(module);

        return this;
    }

    public void unregisterModule(CollectionModule<ModularGridView> module){
        controller.unregisterModule(module);
    }

    public <V extends CollectionModule<ModularGridView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

    @Override
    public int computeVerticalScrollOffset(){
        return super.computeVerticalScrollOffset();
    }
}
