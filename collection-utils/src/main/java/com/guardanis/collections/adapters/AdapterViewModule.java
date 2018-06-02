package com.guardanis.collections.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AdapterViewModule<V> {

    protected int layoutResId;

    public AdapterViewModule(int layoutResId){
        this.layoutResId = layoutResId;
    }

    public abstract V build(Context context, @Nullable ViewGroup parent);

    protected View inflate(Context context, ViewGroup parent){
        return LayoutInflater.from(context)
                .inflate(layoutResId, parent, false);
    }
}
