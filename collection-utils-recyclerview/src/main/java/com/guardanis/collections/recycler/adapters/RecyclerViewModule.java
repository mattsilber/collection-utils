package com.guardanis.collections.recycler.adapters;

import com.guardanis.collections.adapters.viewbuilder.AdapterViewModule;

@Deprecated
public abstract class RecyclerViewModule<T> extends AdapterViewModule<T> {

    public RecyclerViewModule(int layoutResId) {
        super(layoutResId);
    }
}
