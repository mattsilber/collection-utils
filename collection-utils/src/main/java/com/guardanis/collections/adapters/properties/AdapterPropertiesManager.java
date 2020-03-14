package com.guardanis.collections.adapters.properties;

import androidx.annotation.Nullable;

public interface AdapterPropertiesManager {

    /**
     * Set a property for use later
     * @param key the key to reference
     * @param value the value to store
     */
    public void setProperty(String key, Object value);

    /**
     * Get a value stored in the properties [String: Object] map
     * @param key property key
     */
    @Nullable
    public <V> V getProperty(String key);
}
