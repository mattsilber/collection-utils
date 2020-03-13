package com.guardanis.collections.recycler.adapters.callbacks;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SimpleLifeCycleViewHolder extends RecyclerView.ViewHolder implements ViewHolderLifeCycleCallbacks {

    public SimpleLifeCycleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewAttachedToWindow() { }

    @Override
    public void onViewDetachedFromWindow() { }

    @Override
    public boolean onFailedToRecycleView() {
        return false;
    }

    @Override
    public void onViewRecycled() { }
}
