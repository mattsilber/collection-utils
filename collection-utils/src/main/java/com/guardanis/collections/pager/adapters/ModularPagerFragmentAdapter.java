package com.guardanis.collections.pager.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularPagerFragmentAdapter extends FragmentStatePagerAdapter implements ModularAdapter {

    private WeakReference<Context> context;
    private List<Object> items = new ArrayList<Object>();

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new HashMap<Class, ModuleBuilderResolver>();

    private Map<String, Callback> actionCallbacks = new HashMap<String, Callback>();

    private Map<String, Object> properties = new HashMap<String, Object>();

    public ModularPagerFragmentAdapter(Context context, FragmentManager manager) {
        super(manager);

        this.context = new WeakReference(context);
    }

    public ModularPagerFragmentAdapter addAll(Collection<Object> item) {
        this.items.addAll(item);
        return this;
    }

    public ModularPagerFragmentAdapter add(Object item) {
        this.items.add(item);
        return this;
    }

    public ModularPagerFragmentAdapter remove(Object item) {
        this.items.remove(item);
        return this;
    }

    public ModularPagerFragmentAdapter removeAt(int position) {
        this.items.remove(position);
        return this;
    }

    public ModularPagerFragmentAdapter clear() {
        this.items.clear();
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        Object item = items.get(position);

        ModuleBuilder builder = viewModuleBuilders.get(item.getClass())
                .resolve(this, item, position);

        AdapterViewModule module = builder.createViewModule();

        if (!(module instanceof PagerViewModule))
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());

        PagerViewModule pagerModule = (PagerViewModule) module;

        Fragment fragment = (Fragment) pagerModule.build(context.get(), null);
        fragment.setArguments(pagerModule.createArguments(this, item, position));

        return fragment;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Context getContext() {
        return context.get();
    }

    @Override
    public ModularPagerFragmentAdapter registerModuleBuilder(Class itemType, final ModuleBuilder builder) {
        return registerModuleBuilderResolver(itemType, ModuleBuilderResolver.createSimpleResolverInstance(builder));
    }

    @Override
    public ModularPagerFragmentAdapter registerModuleBuilderResolver(Class itemType, final ModuleBuilderResolver layoutResolver) {
        viewModuleBuilders.put(itemType, layoutResolver);

        return this;
    }

    @Override
    public ModularPagerFragmentAdapter registerCallback(String key, Callback callback){
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
    public ModularPagerFragmentAdapter setProperty(String key, Object value) {
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
