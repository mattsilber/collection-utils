package com.guardanis.collections.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.guardanis.collections.CollectionController;
import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.tools.ListUtils;

public class ModularViewPager extends ViewPager {

    protected CollectionController<ModularViewPager> controller;

    public ModularViewPager(Context context) {
        super(context);
        init();
    }

    public ModularViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init(){
        controller = new CollectionController<ModularViewPager>(this);

        setOnTouchListener(controller);
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

    public CollectionController<ModularViewPager> getCollectionController(){
        return controller;
    }

    public ModularViewPager registerModule(CollectionModule<ModularViewPager> module){
        controller.registerModule(module);

        return this;
    }

    public void unregisterModule(CollectionModule<ModularViewPager> module){
        controller.unregisterModule(module);
    }

    public <V extends CollectionModule<ModularViewPager>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

    public <V extends CollectionModule<ModularViewPager>> ModularViewPager bindModule(Class<V> moduleClass, ListUtils.Action<V> action){
        if(controller.getModule(moduleClass) != null)
            action.executeAction(controller.getModule(moduleClass));

        return this;
    }
}
