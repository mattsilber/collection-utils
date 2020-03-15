package com.guardanis.collections.pager.adapters;

import android.content.Context;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.Callback;
import com.guardanis.collections.adapters.ModularAdapter;
import com.guardanis.collections.adapters.ModuleBuilder;
import com.guardanis.collections.adapters.ModuleBuilderResolver;
import com.guardanis.collections.adapters.actions.AdapterActionsManager;
import com.guardanis.collections.adapters.actions.SimpleAdapterActionsManager;
import com.guardanis.collections.adapters.properties.AdapterPropertiesManager;
import com.guardanis.collections.adapters.properties.SimpleAdapterPropertiesManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ModularPagerFragmentAdapter extends FragmentStatePagerAdapter implements ModularAdapter {

    protected WeakReference<Context> context;
    protected List<Object> items = new ArrayList<Object>();

    protected Map<Class, ModuleBuilderResolver> viewModuleBuilders = new LinkedHashMap<>();
    protected AdapterActionsManager actionsManager = new SimpleAdapterActionsManager();
    protected AdapterPropertiesManager propertiesManager = new SimpleAdapterPropertiesManager();

    public ModularPagerFragmentAdapter(Context context, FragmentManager manager) {
        this(context, manager, BEHAVIOR_SET_USER_VISIBLE_HINT);
    }

    public ModularPagerFragmentAdapter(Context context, FragmentManager manager, int behavior) {
        super(manager, behavior);

        this.context = new WeakReference<Context>(context);
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
        ModuleBuilder builder = ModuleBuilderResolver.resolveModuleBuilder(this, item, position, viewModuleBuilders);
        AdapterViewModule module = builder.createViewModule();

        if (!(module instanceof ViewPagerAdapterViewModule))
            throw new RuntimeException("Unsupported module of type: " + module.getClass().getName());

        ViewPagerAdapterViewModule pagerModule = (ViewPagerAdapterViewModule) module;

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
    public ModularPagerFragmentAdapter registerCallback(String key, Callback callback) {
        actionsManager.registerCallback(key, callback);

        return this;
    }

    @Override
    public <V> void triggerCallback(String key, V value) {
        actionsManager.triggerCallback(key, value);
    }

    @Override
    public ModularPagerFragmentAdapter setProperty(String key, Object value) {
        propertiesManager.setProperty(key, value);

        return this;
    }

    @Override
    public <V> V getProperty(String key) {
        return propertiesManager.getProperty(key);
    }
}
