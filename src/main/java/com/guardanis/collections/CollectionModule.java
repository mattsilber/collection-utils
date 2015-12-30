package com.guardanis.collections;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public abstract class CollectionModule<T extends ViewGroup> implements View.OnTouchListener, AbsListView.OnScrollListener {

    protected T parent;

    public CollectionModule<T> setParent(T parent){
        this.parent = parent;

        return this;
    }

    public abstract void onDrawDispatched(Canvas canvas);

    public void onDetachedFromWindow(){
        this.parent = null;
    }

}
