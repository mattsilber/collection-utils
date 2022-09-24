package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.adapters.actions.AdapterActionsManager;
import com.guardanis.collections.adapters.actions.SimpleAdapterActionsManager;
import com.guardanis.collections.adapters.properties.AdapterPropertiesManager;
import com.guardanis.collections.adapters.properties.SimpleAdapterPropertiesManager;
import com.guardanis.collections.recycler.adapters.callbacks.ViewHolderLifeCycleCallbacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModularRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModularAdapter {

    protected Context context;

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new LinkedHashMap<Class, ModuleBuilderResolver>();
    protected AdapterActionsManager actionsManager = new SimpleAdapterActionsManager();
    protected AdapterPropertiesManager propertiesManager = new SimpleAdapterPropertiesManager();

    protected List<Object> data = new ArrayList<Object>();

    public ModularRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilder(Class itemType, final ModuleBuilder builder) {
        return registerModuleBuilderResolver(itemType, ModuleBuilderResolver.createSimpleResolverInstance(builder));
    }

    @Override
    public ModularRecyclerAdapter registerModuleBuilderResolver(
        Class itemType,
        final ModuleBuilderResolver layoutResolver
    ) {
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
        int viewType
    ) {

        RecyclerViewAdapterViewModule recyclerViewModule = ((RecyclerViewAdapterViewModule) module);
        recyclerViewModule.build(getContext(), parent);

        return recyclerViewModule.getViewHolder();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);

        ModuleBuilder builder = ModuleBuilderResolver.resolveModuleBuilder(this, item, position, viewModuleBuilders);
        AdapterViewModule module = builder.createViewModule();

        bind(item, module, holder, position);
    }

    protected <T> void bind(
        Object item,
        AdapterViewModule module,
        RecyclerView.ViewHolder holder,
        int position
    ) {

        if (!(module instanceof RecyclerViewAdapterViewModule)) {
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());
        }

        onBindRecyclerViewModule((RecyclerViewAdapterViewModule) module, item, holder, position);
    }

    @SuppressWarnings("unchecked")
    protected void onBindRecyclerViewModule(
        RecyclerViewAdapterViewModule module,
        Object item,
        RecyclerView.ViewHolder holder,
        int position
    ) {

        module.overrideViewHolder(holder);
        module.updateView(this, item, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);

        return ModuleBuilderResolver.resolveUniqueItemViewType(this, item, viewModuleBuilders);
    }

    protected ModuleBuilder getBuilder(int viewType) {
        return ModuleBuilderResolver.resolveModuleBuilderFromUniqueItemViewType(viewType, viewModuleBuilders);
    }

    @Override
    public ModularRecyclerAdapter registerCallback(String key, Callback callback) {
        actionsManager.registerCallback(key, callback);

        return this;
    }

    @Override
    public <V> void triggerCallback(String key, V value) {
        actionsManager.triggerCallback(key, value);
    }

    @Override
    public ModularRecyclerAdapter setProperty(String key, Object value) {
        propertiesManager.setProperty(key, value);

        return this;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks) {
            ((ViewHolderLifeCycleCallbacks) holder).onViewAttachedToWindow();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks) {
            ((ViewHolderLifeCycleCallbacks) holder).onViewDetachedFromWindow();
        }
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderLifeCycleCallbacks
            && ((ViewHolderLifeCycleCallbacks) holder).onFailedToRecycleView()) {
            return true;
        }

        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof ViewHolderLifeCycleCallbacks) {
            ((ViewHolderLifeCycleCallbacks) holder).onViewRecycled();
        }
    }

    @Override
    public <V> V getProperty(String key) {
        return propertiesManager.getProperty(key);
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
            if (obj == data.get(index)) {
                remove(index);
            }
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

    @Override
    @Nullable
    public Object getItem(int position) {
        return data.get(position);
    }

    public List<Object> getItems() {
        return data;
    }
}
