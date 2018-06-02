package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;

public abstract class RecyclerViewModule<T, H extends ViewHolder> extends AdapterViewModule<T, ModularRecyclerAdapter> {

    private H viewHolder;

    public RecyclerViewModule(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public View build(Context context, ViewGroup parent){
        View convertView = inflate(context, parent);
        this.viewHolder = buildViewHolder(convertView);

        return convertView;
    }

    public abstract H buildViewHolder(View view);

    /**
     * Override this RecyclerViewModule's target viewHolder for calls to update(ModularAdapter, T, int)
     * for use with handling onCreateViewHolder / onBindViewHolder so no re-inflation occurs
     */
    public RecyclerViewModule overrideViewHolder(H viewHolder){
        this.viewHolder = viewHolder;
        return this;
    }

    public H getViewHolder(){
        return viewHolder;
    }

}
