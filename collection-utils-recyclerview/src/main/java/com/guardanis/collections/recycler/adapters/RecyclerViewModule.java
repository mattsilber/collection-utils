package com.guardanis.collections.recycler.adapters;

import androidx.recyclerview.widget.RecyclerView;

@Deprecated
public abstract class RecyclerViewModule<T, H extends RecyclerView.ViewHolder> extends RecyclerViewAdapterViewModule<T, H> {

    public RecyclerViewModule(int layoutResId) {
        super(layoutResId);
    }
}
