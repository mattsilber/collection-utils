package com.guardanis.collections;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

public abstract class CollectionModule<T extends ViewGroup> implements View.OnTouchListener {

    protected T parent;

    public CollectionModule<T> setParent(T parent){
        this.parent = parent;

        return this;
    }

    public abstract void onDrawDispatched(Canvas canvas);

    public void onDetachedFromWindow(){
        this.parent = null;
    }

    public abstract void onScrollStateChanged(int scrollState);

    public abstract void onScroll(int... values);
}
