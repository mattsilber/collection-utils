package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.recycler.adapters.callbacks.ViewHolderLifeCycleCallbacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModularRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModularAdapter {

    protected Context context;

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new HashMap<Class, ModuleBuilderResolver>();
    protected Map<String, Callback> actionCallbacks = new HashMap<String, Callback>();
    protected Map<String, Object> properties = new HashMap<String, Object>();

    protected List<Object> data = new ArrayList<Object>();

    public ModularRecyclerAdapter(Context context){
        this.context = context;
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilder(Class itemType, final ModuleBuilder builder) {
        return registerModuleBuilderResolver(itemType, ModuleBuilderResolver.createSimpleResolverInstance(builder));
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilderResolver(Class itemType, final ModuleBuilderResolver layoutResolver) {
        viewModuleBuilders.put(itemType, layoutResolver);

        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterViewModule module = getBuilder(viewType)
                .createViewModule();

        return createViewHolder(module, parent, viewType);
    }

    protected RecyclerView.ViewHolder createViewHolder(
            AdapterViewModule module,
            ViewGroup parent,
            int viewType) {

        RecyclerViewModule recyclerViewModule = ((RecyclerViewModule) module);
        recyclerViewModule.build(getContext(), parent);

        return recyclerViewModule.getViewHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);
        ModuleBuilderResolver resolver = findResolverOf(item.getClass());
        ModuleBuilder builder = resolver.resolve(this, item, position);
        AdapterViewModule module = builder.createViewModule();

        bind(item, module, holder, position);
    }

    protected <T> ModuleBuilderResolver findResolverOf(Class<T> type) {
        ModuleBuilderResolver resolver = viewModuleBuilders.get(type);

        if (resolver == null)
            throw new RuntimeException("ModuleBuilderResolver for item type [" + type + "] not found.");

        return resolver;
    }

    protected <T> void bind(
            Object item,
            AdapterViewModule module,
            RecyclerView.ViewHolder holder,
            int position) {

        if (module instanceof RecyclerViewModule)
            onBindRecyclerViewModule((RecyclerViewModule) module, item, holder, position);
        else
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());
    }

    protected void onBindRecyclerViewModule(
            RecyclerViewModule module,
            Object item,
            RecyclerView.ViewHolder holder,
            int position) {

        module.overrideViewHolder(holder);
        module.updateView(this, item, position);
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
    public ModularRecyclerAdapter registerCallback(String key, Callback callback){
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
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks)
            ((ViewHolderLifeCycleCallbacks) holder).onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks)
            ((ViewHolderLifeCycleCallbacks) holder).onViewDetachedFromWindow();
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderLifeCycleCallbacks
                && ((ViewHolderLifeCycleCallbacks) holder).onFailedToRecycleView())
            return true;

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks)
            ((ViewHolderLifeCycleCallbacks) holder).onViewRecycled();
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

    public void insert(final int position, final Object obj) {
        data.add(position, obj);

        notifyItemInserted(position);
    }

    public void add(final Object obj) {
        data.add(obj);

        notifyItemInserted(data.size() - 1);
    }

    public void addAll(final Collection<Object> objects) {
        data.addAll(objects);

        notifyItemRangeInserted(data.size() - objects.size(), objects.size());
    }

    public void remove(final Object obj) {
        for (int index = data.size() - 1; 0 <= index; index--) {
            if (obj == data.get(index))
                remove(index);
        }
    }

    public void remove(final int position) {
        data.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void clear() {
        data.clear();

        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public List<Object> getItems() {
        return data;
    }
}
