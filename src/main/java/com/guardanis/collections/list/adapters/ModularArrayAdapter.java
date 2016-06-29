package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularArrayAdapter extends ArrayAdapter {

    protected Map<Class, ModuleBuilder> viewModuleBuilders = new HashMap<Class, ModuleBuilder>();

    public ModularArrayAdapter(Context context, int resource, @NonNull List data) {
        super(context, resource, data);
    }

    public ModularArrayAdapter registerViewModule(Class itemType, ModuleBuilder builder) {
        viewModuleBuilders.put(itemType, builder);

        return this;
    }

    @Override
    public int getViewTypeCount() {
        return viewModuleBuilders.keySet()
                .size();
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
                ModuleBuilder builder = viewModuleBuilders.get(c);

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

}