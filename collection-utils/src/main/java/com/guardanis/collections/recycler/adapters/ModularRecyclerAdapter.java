package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.list.adapters.ListViewModule;
import com.guardanis.collections.recycler.adapters.callbacks.ViewHolderLifeCycleCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModularRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModularAdapter {

    private Context context;

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new HashMap<Class, ModuleBuilderResolver>();

    private Map<String, Callback> actionCallbacks = new HashMap<String, Callback>();

    private Map<String, Object> properties = new HashMap<String, Object>();

    private List<Object> data = new ArrayList<Object>();

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

        if (module instanceof RecyclerViewModule) {
            RecyclerViewModule recyclerViewModule = ((RecyclerViewModule) module);
            recyclerViewModule.build(getContext(), parent);

            return recyclerViewModule.getViewHolder();
        }
        else if (module instanceof ListViewModule) {
            ListViewModule listViewModule = ((ListViewModule) module);
            listViewModule.build(getContext(), parent);

            return new RecyclerListViewHolderCompat(listViewModule.getConvertView());
        }

        throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);

        ModuleBuilderResolver resolver = viewModuleBuilders.get(item.getClass());

        if (resolver == null)
            throw new RuntimeException("ModuleBuilderResolver for item type [" + item.getClass() + "] not found.");

        ModuleBuilder builder = resolver.resolve(this, item, position);
        AdapterViewModule module = builder.createViewModule();

        if (module instanceof RecyclerViewModule)
            onBindRecyclerViewModule((RecyclerViewModule) module, item, holder, position);
        else if (module instanceof ListViewModule)
            onBindCompatibilityViewModule((ListViewModule) module, item, holder, position);
        else
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());
    }

    protected void onBindRecyclerViewModule(RecyclerViewModule module, Object item, RecyclerView.ViewHolder holder, int position) {
        module.overrideViewHolder(holder);
        module.updateView(this, item, position);
    }

    protected void onBindCompatibilityViewModule(ListViewModule module, Object item, RecyclerView.ViewHolder holder, int position) {
        module.overrideTargetView(holder.itemView);
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

    public void insert(int position, Object obj){
        this.data.add(position, obj);
        notifyDataSetChanged();
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

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public Object getItem(int position){
        return data.get(position);
    }
}
