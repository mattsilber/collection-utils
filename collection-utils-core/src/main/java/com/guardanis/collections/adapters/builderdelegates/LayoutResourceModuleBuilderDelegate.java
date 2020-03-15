package com.guardanis.collections.adapters.builderdelegates;

import com.guardanis.collections.adapters.AdapterViewModule;

import androidx.annotation.LayoutRes;

public abstract class LayoutResourceModuleBuilderDelegate<T extends AdapterViewModule>
        implements ModuleBuilderDelegate<T>, CompatModuleBuilderDelegate<T> {

    @LayoutRes protected int layoutResId;

    public LayoutResourceModuleBuilderDelegate(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    @Override
    public T create() {
        return create(layoutResId);
    }
}
