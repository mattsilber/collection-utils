package com.guardanis.collections.recycler.adapters.callbacks;

/**
 * A simple interface that can be applied exclusively to RecyclerView.ViewHolder instances.
 * This interfaces with the ModularRecyclerAdapter's callbacks to give ViewHolder instances
 * the ability to respond to certain attachment events. (e.g. injecting a Fragment once its attached to the window)
 */
public interface ViewHolderLifeCycleCallbacks {

    public void onViewAttachedToWindow();
    public void onViewDetachedFromWindow();

    /**
     * See: RecyclerView.Adapter.onFailedToRecycleView
     * @return true to force the recycle, otherwise allow parent adapter to decide
     */
    public boolean onFailedToRecycleView();

    public void onViewRecycled();
}
