package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.adapters.actions.AdapterActionsManager;
import com.guardanis.collections.adapters.actions.SimpleAdapterActionsManager;
import com.guardanis.collections.adapters.properties.AdapterPropertiesManager;
import com.guardanis.collections.adapters.properties.SimpleAdapterPropertiesManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModularArrayAdapter extends ArrayAdapter implements ModularAdapter {

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new LinkedHashMap<Class, ModuleBuilderResolver>();
    protected AdapterActionsManager actionsManager = new SimpleAdapterActionsManager();
    protected AdapterPropertiesManager propertiesManager = new SimpleAdapterPropertiesManager();

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
        return ModuleBuilderResolver.getUniqueItemViewTypeCount(viewModuleBuilders);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);

        return ModuleBuilderResolver.resolveUniqueItemViewType(this, item, viewModuleBuilders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Object item = getItem(position);

        ModuleBuilderResolver resolver = viewModuleBuilders.get(item.getClass());

        if (resolver == null)
            throw new RuntimeException("ModuleBuilderResolver for item type [" + item.getClass() + "] not found.");

        ModuleBuilder builder = resolver.resolve(this, item, position);
        ListViewAdapterViewModule module;

        if(convertView == null
                || convertView.getTag() == null
                || convertView.getTag().getClass() != item.getClass()) {
            module = (ListViewAdapterViewModule) builder.createViewModule();
            module.build(getContext(), parent);

            convertView = module.getConvertView();
            convertView.setTag(module);
        }
        else
            module = (ListViewAdapterViewModule) convertView.getTag();

        module.updateView(this, item, position);

        return convertView;
    }

    @Override
    public ModularArrayAdapter registerCallback(String key, Callback callback) {
        actionsManager.registerCallback(key, callback);

        return this;
    }

    @Override
    public <V> void triggerCallback(String key, V value) {
        actionsManager.triggerCallback(key, value);
    }

    @Override
    public ModularArrayAdapter setProperty(String key, Object value) {
        propertiesManager.setProperty(key, value);

        return this;
    }

    @Override
    public <V> V getProperty(String key) {
        return propertiesManager.getProperty(key);
    }
}