package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModularArrayAdapter extends ArrayAdapter implements ModularAdapter {

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new HashMap<Class, ModuleBuilderResolver>();
    protected Map<String, Callback> actionCallbacks = new HashMap<String, Callback>();
    protected Map<String, Object> properties = new HashMap<String, Object>();

    public ModularArrayAdapter(Context context) {
        super(context, 0, new ArrayList());
    }

    @Override
    public ModularArrayAdapter registerModuleBuilder(Class itemType, final ModuleBuilder builder) {
        return registerModuleBuilderResolver(itemType, ModuleBuilderResolver.createSimpleResolverInstance(builder));
    }

    @Override
    public ModularArrayAdapter registerModuleBuilderResolver(Class itemType, final ModuleBuilderResolver layoutResolver) {
        viewModuleBuilders.put(itemType, layoutResolver);

        return this;
    }

    @Override
    public int getViewTypeCount() {
        int count = 0;

        for(ModuleBuilderResolver resolver : viewModuleBuilders.values())
            count += resolver.getBuilderTypeCount();

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int pos = 0;

        Object item = getItem(position);

        for(Class c : viewModuleBuilders.keySet()){
            if(c == item.getClass())
                return pos + viewModuleBuilders.get(c)
                        .getViewTypeIndex(this, item, position);

            pos += viewModuleBuilders.get(c)
                .getBuilderTypeCount();
        }

        throw new RuntimeException("No Registered Module for " + getItem(position).getClass());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Object item = getItem(position);

        ModuleBuilderResolver resolver = viewModuleBuilders.get(item.getClass());

        if (resolver == null)
            throw new RuntimeException("ModuleBuilderResolver for item type [" + item.getClass() + "] not found.");

        ModuleBuilder builder = resolver.resolve(this, item, position);
        ListViewModule module;

        if(convertView == null
                || convertView.getTag() == null
                || convertView.getTag().getClass() != item.getClass()) {
            module = (ListViewModule) builder.createViewModule();
            module.build(getContext(), parent);

            convertView = module.getConvertView();
            convertView.setTag(module);
        }
        else
            module = (ListViewModule) convertView.getTag();

        module.updateView(this, item, position);

        return convertView;
    }

    @Override
    public ModularArrayAdapter registerCallback(String key, Callback callback){
        actionCallbacks.put(key, callback);

        return this;
    }

    @Override
    public <V> void triggerCallback(String key, V value){
        try{
            actionCallbacks.get(key)
                    .onTriggered(value);
        }
        catch(ClassCastException e){ e.printStackTrace(); }
        catch(NullPointerException e){ Log.d("collections", key + " callback is null. Ignoring.");  }
    }

    @Override
    public ModularArrayAdapter setProperty(String key, Object value) {
        this.properties.put(key, value);
        return this;
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