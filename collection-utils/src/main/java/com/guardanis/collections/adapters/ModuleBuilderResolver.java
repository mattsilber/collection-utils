package com.guardanis.collections.adapters;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ModuleBuilderResolver<V> {

    protected List<ModuleBuilder> builders = new ArrayList<ModuleBuilder>();

    public ModuleBuilderResolver(@NonNull ModuleBuilder... builders){
        if(builders == null || builders.length < 1)
            throw new RuntimeException("The builders must be supplied, else delegation will not work efficiently");

        for(ModuleBuilder builder : builders)
            this.builders.add(builder);
    }

    public abstract ModuleBuilder resolve(ModularAdapter adapter, V item, int position);

    public int getBuilderTypeCount(){
        return builders.size();
    }

    public int getViewTypeIndex(ModularAdapter adapter, V item, int position){
        ModuleBuilder builder = resolve(adapter, item, position);

        for(int i = 0; i < builders.size(); i++)
            if(builders.get(i) == builder)
                return i;

        throw new RuntimeException("No ModuleBuilder registered for " + item.getClass().getSimpleName() + " at position " + position);
    }

    public ModuleBuilder getBuilder(int localIndex){
        return builders.get(localIndex);
    }

    public static <V> ModuleBuilderResolver<V> createSimpleResolverInstance(final ModuleBuilder builder) {
        return new ModuleBuilderResolver(builder) {
            public ModuleBuilder resolve(ModularAdapter adapter, Object item, int position) {
                return builder;
            }
        };
    }
}