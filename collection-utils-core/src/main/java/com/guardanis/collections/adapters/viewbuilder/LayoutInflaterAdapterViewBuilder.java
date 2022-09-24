package com.guardanis.collections.adapters.viewbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public class LayoutInflaterAdapterViewBuilder implements AdapterViewBuilder {

    @LayoutRes
    protected int layoutResId;

    public LayoutInflaterAdapterViewBuilder(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    @Override
    public View createInstance(Context context, ViewGroup parent) {
        return LayoutInflater.from(context)
            .inflate(layoutResId, parent, false);
    }
}
