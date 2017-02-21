package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.list.adapters.ModularArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModularAdapter<RecyclerViewModule> {

    private Context context;

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders =
            new HashMap<Class, ModuleBuilderResolver>();

    private Map<String, ModularArrayAdapter.Callback> actionCallbacks =
            new HashMap<String, ModularArrayAdapter.Callback>();

    private Map<String, Object> properties =
            new HashMap<String, Object>();

    private List<Object> data = new ArrayList<Object>();

    public ModularRecyclerAdapter(Context context){
        this.context = context;
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilder(Class itemType, final ModuleBuilder<RecyclerViewModule> builder) {
        return registerModuleBuilderResolver(itemType,
                new ModuleBuilderResolver(builder) {
                    public ModuleBuilder resolve(ModularAdapter adapter, Object item, int position) {
                        return builder;
                    }
                });
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilderResolver(Class itemType, final ModuleBuilderResolver layoutResolver) {
        viewModuleBuilders.put(itemType, layoutResolver);

        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterViewModule module = getBuilder(viewType)
                .create(parent);

        if(!(module instanceof RecyclerViewModule))
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());

        return ((RecyclerViewModule) module)
                .getViewHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);

        for(Class c : viewModuleBuilders.keySet()){
            if(c == item.getClass()){
                RecyclerViewModule module = (RecyclerViewModule) viewModuleBuilders.get(c)
                        .resolve(this, item, position)
                        .createViewModule();

                module.overrideViewHolder(holder);

                module.updateView(this,
                        item,
                        position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
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

    protected ModuleBuilder getBuilder(int viewType){
        int pos = 0;

        for(Class c : viewModuleBuilders.keySet()){
            int count = viewModuleBuilders.get(c)
                    .getBuilderTypeCount();

            if(pos <= viewType && viewType < pos + count)
                return viewModuleBuilders.get(c)
                        .getBuilder(viewType - pos);

            pos += count;
        }

        throw new RuntimeException("Unsupported Module for view type: " + viewType);
    }

    @Override
    public ModularRecyclerAdapter registerCallback(String key, ModularArrayAdapter.Callback callback){
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
    public ModularRecyclerAdapter setProperty(String key, Object value) {
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

    @Override
    public Context getContext() {
        return context;
    }

    public void add(Object obj){
        this.data.add(obj);
        notifyDataSetChanged();
    }

    public void addAll(List<Object> objects){
        for(Object obj : objects)
            this.data.add(obj);

        notifyDataSetChanged();
    }

    public void remove(Object obj){
        this.data.remove(obj);
        notifyDataSetChanged();
    }

    public void remove(int position){
        this.data.remove(position);
        notifyDataSetChanged();
    }

    public Object getItem(int position){
        return data.get(position);
    }

}
