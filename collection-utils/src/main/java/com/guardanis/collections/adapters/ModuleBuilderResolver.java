package com.guardanis.collections.adapters;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ModuleBuilderResolver<V, T extends ModuleBuilder> {

    protected List<T> builders = new ArrayList<T>();

    public ModuleBuilderResolver(@NonNull T... builders){
        if(builders == null || builders.length < 1)
            throw new RuntimeException("The builders must be supplied, else delegation will not work efficiently");

        for(T builder : builders)
            this.builders.add(builder);
    }

    public abstract T resolve(ModularAdapter adapter, V item, int position);

    public int getBuilderTypeCount(){
        return builders.size();
    }

    public int getViewTypeIndex(ModularAdapter adapter, V item, int position){
        ModuleBuilder builder = resolve(adapter, item, position);

        for(int i = 0; i < builders.size(); i++)
            if(builders.get(i) == builder)
                return i;

        throw new RuntimeException("No ModuleBuilder registered for " + item.getClass().getSimpleName()
                + " at position " + position);
    }

    public T getBuilder(int localIndex){
        return builders.get(localIndex);
    }
}