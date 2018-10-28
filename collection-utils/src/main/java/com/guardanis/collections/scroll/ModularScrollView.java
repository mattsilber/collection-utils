package com.guardanis.collections.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.guardanis.collections.CollectionController;
import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.adapters.Callback;

public class ModularScrollView extends ScrollView {

    protected CollectionController<ModularScrollView> controller;
    protected boolean flinging = false;

    public ModularScrollView(Context context) {
        super(context);
        init();
    }

    public ModularScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new CollectionController<ModularScrollView>(this);

        setOnTouchListener(controller);

        if(Build.VERSION.SDK_INT >= 9)
            setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        controller.onScroll(l, t, oldl, oldt);
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

    public boolean isFlinging(){
        return flinging;
    }

    public CollectionController<ModularScrollView> getCollectionController(){
        return controller;
    }

    public ModularScrollView registerModule(CollectionModule<ModularScrollView> module){
        controller.registerModule(module);

        return this;
    }

    public void unregisterModule(CollectionModule<ModularScrollView> module){
        controller.unregisterModule(module);
    }

    public <V extends CollectionModule<ModularScrollView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

    public <V extends CollectionModule<ModularScrollView>> ModularScrollView bindModule(Class<V> moduleClass, Callback<V> action){
        if(controller.getModule(moduleClass) != null)
            action.onTriggered(controller.getModule(moduleClass));

        return this;
    }

    @Override
    public int computeVerticalScrollOffset(){
        return super.computeVerticalScrollOffset();
    }
}
