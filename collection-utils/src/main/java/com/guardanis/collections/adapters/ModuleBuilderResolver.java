package com.guardanis.collections.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public abstract class ModuleBuilderResolver<V> {

    protected List<ModuleBuilder> builders = new ArrayList<ModuleBuilder>();

    public ModuleBuilderResolver(@NonNull ModuleBuilder... builders){
        if(builders.length < 1)
            throw new RuntimeException("The builders must be supplied, else delegation will not work efficiently");

        this.builders.addAll(Arrays.asList(builders));
    }

    public abstract ModuleBuilder resolve(ModularAdapter adapter, V item, int position);

    public int getBuilderTypeCount(){
        return builders.size();
    }

    public int getViewTypeIndex(ModularAdapter adapter, V item, int position){
        ModuleBuilder builder = resolve(adapter, item, position);

        for(int i = 0; i < builders.size(); i++) {
            if (builders.get(i) == builder)
                return i;
        }

        throw new RuntimeException("No ModuleBuilder registered for " + item.getClass().getSimpleName() + " at position " + position);
    }

    public ModuleBuilder getBuilder(int localIndex) {
        return builders.get(localIndex);
    }

    public static int resolveUniqueItemViewType(
            ModularAdapter adapter,
            Object item,
            Map<Class, ModuleBuilderResolver> viewModuleBuilders) {

        int groupOffsetPosition = 0;

        for(Class type : viewModuleBuilders.keySet()) {
            ModuleBuilderResolver resolver = viewModuleBuilders.get(type);

            if(type == item.getClass()) {
                int subgroupIndex = resolver.getViewTypeIndex(adapter, item, groupOffsetPosition);

                return groupOffsetPosition + subgroupIndex;
            };

            groupOffsetPosition += resolver.getBuilderTypeCount();
        }

        throw new RuntimeException("No Registered Module for " + item.getClass());
    }

    public static ModuleBuilder resolveModuleBuilderFromUniqueItemViewType(
            int uniqueViewTypeIndex,
            Map<Class, ModuleBuilderResolver> viewModuleBuilders) {

        int groupOffsetPosition = 0;

        for(Class type : viewModuleBuilders.keySet()){
            ModuleBuilderResolver resolver = viewModuleBuilders.get(type);
            int count = resolver.getBuilderTypeCount();

            if(groupOffsetPosition <= uniqueViewTypeIndex && uniqueViewTypeIndex < groupOffsetPosition + count) {
                return resolver.getBuilder(uniqueViewTypeIndex - groupOffsetPosition);
            }

            groupOffsetPosition += count;
        }

        throw new RuntimeException("No Registered Module for unique view type index: " + uniqueViewTypeIndex);
    }

    public static int getUniqueItemViewTypeCount(Map<Class, ModuleBuilderResolver> viewModuleBuilders) {
        int count = 0;

        for(ModuleBuilderResolver resolver : viewModuleBuilders.values())
            count += resolver.getBuilderTypeCount();

        return count;
    }

    @SuppressWarnings("unchecked")
    public static <V> ModuleBuilderResolver<V> createSimpleResolverInstance(final ModuleBuilder builder) {
        return new ModuleBuilderResolver(builder) {
            public ModuleBuilder resolve(ModularAdapter adapter, Object item, int position) {
                return builder;
            }
        };
    }
}