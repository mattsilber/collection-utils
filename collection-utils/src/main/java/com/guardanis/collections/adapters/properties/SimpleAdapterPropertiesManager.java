package com.guardanis.collections.adapters.properties;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SimpleAdapterPropertiesManager implements AdapterPropertiesManager {

    protected Map<String, Object> properties = new HashMap<String, Object>();

    @Override
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    @Override
    public <V> V getProperty(String key){
        try{
            return (V) properties.get(key);
        }
        catch(ClassCastException e){ e.printStackTrace(); }
        catch(NullPointerException e){ Log.d("collections", key + " property is null. Ignoring.");  }

        return null;
    }
}
