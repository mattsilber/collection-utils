package com.guardanis.collections.adapters.actions;

import com.guardanis.collections.adapters.Callback;

public interface AdapterActionsManager {

    /**
     * Register an ActionCallback to be triggered later by submodules.
     * @param key
     * @param callback
     */
    public void registerCallback(String key, Callback callback);

    /**
     * Trigger a previously added ActionCallback or do nothing if it doesn't exist or
     * the supplied value is not of the same type as the ActionCallback
     * @param key callback key
     * @param value value to be passed
     */
    public <V> void triggerCallback(String key, V value);
}
