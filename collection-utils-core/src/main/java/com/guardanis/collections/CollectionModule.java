package com.guardanis.collections;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class CollectionModule<T extends ViewGroup> implements View.OnTouchListener {

    @NonNull protected WeakReference<T> parentRef = new WeakReference<T>(null);

    public CollectionModule<T> setParent(T parent){
        this.parentRef = new WeakReference<T>(parent);

        return this;
    }

    public abstract void onDrawDispatched(Canvas canvas);

    public void onDetachedFromWindow(){
        this.parentRef = new WeakReference<T>(null);
    }

    public abstract void onScrollStateChanged(int scrollState);

    public abstract void onScroll(int... values);

    @Nullable
    public T getParent() {
        return parentRef.get();
    }
}
