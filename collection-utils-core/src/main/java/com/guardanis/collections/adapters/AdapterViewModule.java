package com.guardanis.collections.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.viewbuilder.AdapterViewBuilder;
import com.guardanis.collections.adapters.viewbuilder.LayoutInflaterAdapterViewBuilder;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public abstract class AdapterViewModule<V> {

    protected AdapterViewBuilder viewBuilder;

    public AdapterViewModule(@LayoutRes int layoutResId) {
        this(new LayoutInflaterAdapterViewBuilder(layoutResId));
    }

    public AdapterViewModule(AdapterViewBuilder viewBuilder) {
        this.viewBuilder = viewBuilder;
    }

    public abstract V build(Context context, @Nullable ViewGroup parent);

    @Deprecated
    protected View inflate(Context context, ViewGroup parent) {
        return viewBuilder.createInstance(context, parent);
    }
}
