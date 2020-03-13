package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewModule<T, H extends RecyclerView.ViewHolder> extends AdapterViewModule<View> {

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
    public abstract void updateView(ModularAdapter adapter, T item, int position);

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