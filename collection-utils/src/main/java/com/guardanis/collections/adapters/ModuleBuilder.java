package com.guardanis.collections.adapters;

import android.support.annotation.NonNull;

public class ModuleBuilder<T extends AdapterViewModule> {

    public interface BuilderDelegate<T extends AdapterViewModule> {
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

    public T createViewModule() {
        return builderDelegate.create(layoutResId);
    }

    public Class getViewModuleClass(){
        return viewModuleClass;
    }

}
