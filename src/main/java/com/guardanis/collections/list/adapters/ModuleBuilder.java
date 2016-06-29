package com.guardanis.collections.list.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

public class ModuleBuilder<T extends ViewModule> {

    public interface BuilderDelegate<T extends ViewModule> {
        public T create(int layoutResId);
    }

    protected int layoutResId;
    protected Class viewModuleClass;

    protected BuilderDelegate<T> builderDelegate;

    public ModuleBuilder(int layoutResId, Class viewModuleClass, @NonNull final BuilderDelegate<T> builderDelegate){
        this.layoutResId = layoutResId;
        this.viewModuleClass = viewModuleClass;
        this.builderDelegate = builderDelegate;
    }

    public T create(ViewGroup parent){
        T item = builderDelegate.create(layoutResId);

        item.build(parent.getContext(), parent);

        return item;
    }

    public Class getViewModuleClass(){
        return viewModuleClass;
    }


}
