package com.guardanis.collections.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.guardanis.collections.CollectionController;
import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.tools.ListUtils;

public class ModularRecyclerView extends RecyclerView implements AbsListView.OnScrollListener {

    protected CollectionController<ModularRecyclerView> controller;

    protected boolean flinging = false;

    public ModularRecyclerView(Context context) {
        super(context);
        init();
    }

    public ModularRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new CollectionController<ModularRecyclerView>(this);

        setOnTouchListener(controller);
//        setOnScrollListener(this);

        if(Build.VERSION.SDK_INT >= 9)
            setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

    public CollectionController<ModularRecyclerView> getCollectionController(){
        return controller;
    }

    public ModularRecyclerView registerModule(CollectionModule<ModularRecyclerView> module){
        controller.registerModule(module);

        return this;
    }

    public void unregisterModule(CollectionModule<ModularRecyclerView> module){
        controller.unregisterModule(module);
    }

    public <V extends CollectionModule<ModularRecyclerView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

    public <V extends CollectionModule<ModularRecyclerView>> ModularRecyclerView bindModule(Class<V> moduleClass, ListUtils.Action<V> action){
        if(controller.getModule(moduleClass) != null)
            action.executeAction(controller.getModule(moduleClass));

        return this;
    }

    @Override
    public int computeVerticalScrollOffset(){
        return super.computeVerticalScrollOffset();
    }

}