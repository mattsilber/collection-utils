package com.guardanis.collections.adapters.actions;

import android.util.Log;

import com.guardanis.collections.adapters.Callback;

import java.util.HashMap;
import java.util.Map;

public class SimpleAdapterActionsManager implements AdapterActionsManager {

    protected Map<String, Callback> actionCallbacks = new HashMap<String, Callback>();

    @Override
    public void registerCallback(String key, Callback callback) {
        actionCallbacks.put(key, callback);
    }

    @Override
    public <V> void triggerCallback(String key, V value){
        try{
            actionCallbacks.get(key)
                    .onTriggered(value);
        }
        catch(ClassCastException e) { e.printStackTrace(); }
        catch(NullPointerException e) { Log.d("collections", key + " callback is null. Ignoring.");  }
    }
}
