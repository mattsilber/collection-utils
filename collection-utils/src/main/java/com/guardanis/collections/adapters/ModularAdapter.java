package com.guardanis.collections.adapters;

import android.content.Context;

public interface ModularAdapter<M extends AdapterViewModule> {

    public Context getContext();

    /**
     * Register a single ModuleBuilder and link it to the specified class type
     */
    public ModularAdapter<M> registerModuleBuilder(Class itemType, final ModuleBuilder<M> builder);

    /**
     * Register a ModuleBuilderResolver that can delegate between multiple ModuleBuilders
     * linked to a single class type.
     */
    public ModularAdapter<M> registerModuleBuilderResolver(Class itemType, final ModuleBuilderResolver layoutResolver);

    /**
     * Register an ActionCallback to be triggered later by submodules.
     * @param key
     * @param callback
     */
    public ModularAdapter<M> registerCallback(String key, Callback callback);

    /**
     * Trigger a previously added ActionCallback or do nothing if it doesn't exist or
     * the supplied value is not of the same type as the ActionCallback
     * @param key callback key
     * @param value value to be passed
     */
    public <V> void triggerCallback(String key, V value);

    /**
     * Set a property for use later
     * @param key the key to reference
     * @param value the value to store
     */
    public ModularAdapter<M> setProperty(String key, Object value);

    /**
     * Get a value stored in the properties [String: Object] map
     * @param key property key
     */
    public <V> V getProperty(String key);

}
