package com.guardanis.collections.generic;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.CollectionModule;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutModule<V extends ViewGroup> extends CollectionModule<V> {

    protected final WeakReference<SwipeRefreshLayout> layout;

    public SwipeRefreshLayoutModule(SwipeRefreshLayout layout, @NonNull SwipeRefreshLayout.OnRefreshListener refreshListener){
        this.layout = new WeakReference<SwipeRefreshLayout>(layout);

        layout.setOnRefreshListener(refreshListener);
    }

    @Override
    public void onDrawDispatched(Canvas canvas) { }

    @Override
    public void onScrollStateChanged(int scrollState) { }

    @Override
    public void onScroll(int... values) { }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Call this method when asynchronous refresh has completed to reset the state of the Pull to Refresh view
     */
    public void onRefreshCompleted() {
        setRefreshing(false);
    }

    public void setRefreshing(boolean refreshing) {
        SwipeRefreshLayout view = layout.get();

        if (layout == null)
            return;

        view.setRefreshing(refreshing);
    }
}