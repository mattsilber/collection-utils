package com.guardanis.collections;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class CollectionController<T extends ViewGroup> implements View.OnTouchListener {

    private Map<String, CollectionModule<T>> modules = new HashMap<String, CollectionModule<T>>();

    private T parent;

    private View.OnTouchListener secondaryTouchListener;

    public CollectionController(T parent){
        this.parent = parent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for(String key : modules.keySet())
            if(modules.get(key).onTouch(v, event))
                return true;

        if(secondaryTouchListener != null)
            return secondaryTouchListener.onTouch(v, event);

        return false;
    }

    public CollectionController<T> registerModule(CollectionModule<T> module){
        modules.put(module.getClass().getName(), module);
        module.setParent(parent);

        return this;
    }

    public void unregisterModule(CollectionModule<T> module){
        modules.remove(module.getClass().getName());
    }

    public <V extends CollectionModule<T>> V getModule(Class<V> moduleClass){
        CollectionModule module = modules.get(moduleClass.getName());
        return module == null ? null
            : (V) modules.get(moduleClass.getName());
    }

    public void onDrawDispatched(Canvas canvas){
        for(String key : modules.keySet())
            modules.get(key).onDrawDispatched(canvas);
    }

    public void onDetachedFromWindow(){
        for(String key : modules.keySet())
            modules.get(key).onDetachedFromWindow();
    }

    public void onScrollStateChanged(int scrollState) {
        for(String key : modules.keySet())
            modules.get(key).onScrollStateChanged(scrollState);
    }

    /**
     * @param values the values, in order of declaration, of the appropriate scroll event listener (AbsListview.OnScollListener or ScrollView's override onScrollChanged)
     */
    public void onScroll(int... values) {
        for(String key : modules.keySet())
            modules.get(key).onScroll(values);
    }

    public CollectionController<T> registerSecondaryTouchListener(View.OnTouchListener secondaryTouchListener){
        this.secondaryTouchListener = secondaryTouchListener;

        return this;
    }
}
