package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularArrayAdapter extends ArrayAdapter {

    public interface ModuleBuilderResolver<V, T extends ModuleBuilder> {
        public T resolve(ModularArrayAdapter adapter, V item, int position);
        public int getBuilderTypeCount();
    }

    public interface ActionCallback<T> {
        public void onTriggered(T value);
    }

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders =
            new HashMap<Class, ModuleBuilderResolver>();

    private Map<String, ActionCallback> actionCallbacks =
            new HashMap<String, ActionCallback>();

    public ModularArrayAdapter(Context context, int resource, @NonNull List data) {
        super(context, resource, data);
    }

    /**
     * Register a single ModuleBuilder and link it to the specified class type
     */
    public ModularArrayAdapter registerModuleBuilder(Class itemType, final ModuleBuilder builder) {
        return registerModuleBuilderResolver(itemType,
                new ModuleBuilderResolver() {
                    public ModuleBuilder resolve(ModularArrayAdapter adapter, Object item, int position) {
                        return builder;
                    }
                    public int getBuilderTypeCount(){
                        return 1;
                    }
                });
    }

    /**
     * Register a ModuleBuilderResolver that can delegate between multiple ModuleBuilders
     * linked to a single class type.
     */
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
                return pos;

            pos++;
        }

        throw new RuntimeException("No Registered Module for " + getItem(position).getClass());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Object item = getItem(position);

        for(Class c : viewModuleBuilders.keySet()){
            if(c == item.getClass()){
                ModuleBuilder builder = viewModuleBuilders.get(c)
                        .resolve(this, item, position);

                final ViewModule module;

                if(convertView == null
                        || convertView.getTag() == null
                        || convertView.getTag().getClass() != c){
                    module = builder.create(parent);

                    convertView = module.getConvertView();

                    convertView.setTag(module);
                }
                else module = (ViewModule) convertView.getTag();

                module.updateView(this, item, position);

                return convertView;
            }
        }

        throw new RuntimeException("ViewModule for item type [" + item.getClass() + "] not found."
                + " You must call registerViewModule(Class, ModuleBuilder) for all item types.");
    }

    /**
     * Register an ActionCallback to be triggered later by submodules.
     * @param key
     * @param callback
     */
    public ModularArrayAdapter registerCallback(String key, ActionCallback callback){
        actionCallbacks.put(key, callback);

        return this;
    }

    /**
     * Trigger a previously added ActionCallback or do nothing if it doesn't exist or
     * the supplied value is not of the same type as the ActionCallback
     * @param key callback key
     * @param value value to be passed
     */
    public <V> void triggerActionCallback(String key, V value){
        try{
            actionCallbacks.get(key)
                    .onTriggered(value);
        }
        catch(ClassCastException e){ e.printStackTrace(); }
        catch(NullPointerException e){ Log.d("collections", key + " is null. Ignoring.");  }
    }

}