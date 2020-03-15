package com.guardanis.collections.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.guardanis.collections.CollectionController;
import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.adapters.Callback;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ModularRecyclerView extends RecyclerView {

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

        if(Build.VERSION.SDK_INT >= 9)
            setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        controller.onDrawDispatched(canvas);
    }

    @Override
    public void onScrollStateChanged(int newState) {
        super.onScrollStateChanged(newState);

        flinging = newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING;

        controller.onScrollStateChanged(newState);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        controller.onScroll(dx, dy);
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

    public <V extends CollectionModule<ModularRecyclerView>> ModularRecyclerView bindModule(Class<V> moduleClass, Callback<V> action){
        if(controller.getModule(moduleClass) != null)
            action.onTriggered(controller.getModule(moduleClass));

        return this;
    }

    @Override
    public int computeVerticalScrollOffset(){
        return super.computeVerticalScrollOffset();
    }

}
