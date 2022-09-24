package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.viewbuilder.AdapterViewBuilder;

public abstract class RecyclerViewAdapterViewModule<T, H extends RecyclerView.ViewHolder> extends AdapterViewModule<View> {

    private H viewHolder;

    public RecyclerViewAdapterViewModule(int layoutResId) {
        super(layoutResId);
    }

    public RecyclerViewAdapterViewModule(AdapterViewBuilder builder) {
        super(builder);
    }

    @Override
    public View build(Context context, ViewGroup parent) {
        View convertView = viewBuilder.createInstance(context, parent);
        this.viewHolder = buildViewHolder(convertView);

        return convertView;
    }

    public abstract H buildViewHolder(View view);

    public abstract void updateView(ModularAdapter adapter, T item, int position);

    /**
     * Override this RecyclerViewAdapterViewModule's target viewHolder for calls to update(ModularAdapter, T, int)
     * for use with handling onCreateViewHolder / onBindViewHolder so no re-inflation occurs
     */
    public RecyclerViewAdapterViewModule overrideViewHolder(H viewHolder) {
        this.viewHolder = viewHolder;

        return this;
    }

    public H getViewHolder() {
        return viewHolder;
    }
}
