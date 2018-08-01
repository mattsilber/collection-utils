package com.guardanis.collections.recycler.adapters.callbacks;

import android.support.v7.widget.RecyclerView;
import android.view.View;

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
