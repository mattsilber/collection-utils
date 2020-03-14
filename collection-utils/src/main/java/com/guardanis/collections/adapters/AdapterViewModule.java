package com.guardanis.collections.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.viewbuilder.LayoutInflaterAdapterViewBuilder;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public abstract class AdapterViewModule<V> {

    public interface ViewBuilder {
        public View createInstance(Context context, ViewGroup parent);
    }

    protected ViewBuilder viewBuilder;

    public AdapterViewModule(@LayoutRes int layoutResId) {
        this(new LayoutInflaterAdapterViewBuilder(layoutResId));
    }

    public AdapterViewModule(ViewBuilder viewBuilder) {
        this.viewBuilder = viewBuilder;
    }

    public abstract V build(Context context, @Nullable ViewGroup parent);

    @Deprecated
    protected View inflate(Context context, ViewGroup parent) {
        return viewBuilder.createInstance(context, parent);
    }
}
