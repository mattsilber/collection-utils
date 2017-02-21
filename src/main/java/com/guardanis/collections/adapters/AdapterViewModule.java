package com.guardanis.collections.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AdapterViewModule<T, A extends ModularAdapter> {

    protected int layoutResId;

    public AdapterViewModule(int layoutResId){
        this.layoutResId = layoutResId;
    }

    public abstract View build(Context context, ViewGroup parent);

    protected View inflate(Context context, ViewGroup parent){
        return LayoutInflater.from(context)
                .inflate(layoutResId, parent, false);
    }

    public abstract void updateView(A adapter, T item, int position);

}
