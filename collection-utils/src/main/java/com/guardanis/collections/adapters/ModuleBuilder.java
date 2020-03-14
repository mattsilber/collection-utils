package com.guardanis.collections.adapters;

import com.guardanis.collections.adapters.builderdelegates.CompatModuleBuilderDelegate;
import com.guardanis.collections.adapters.builderdelegates.LayoutResourceModuleBuilderDelegate;
import com.guardanis.collections.adapters.builderdelegates.ModuleBuilderDelegate;

import androidx.annotation.NonNull;

public class ModuleBuilder<T extends AdapterViewModule> {

    protected Class viewModuleClass;
    protected ModuleBuilderDelegate<T> builderDelegate;

    public ModuleBuilder(
            int layoutResId,
            Class viewModuleClass,
            @NonNull final CompatModuleBuilderDelegate<T> builderDelegate) {

        this.viewModuleClass = viewModuleClass;
        this.builderDelegate = new LayoutResourceModuleBuilderDelegate<T>(layoutResId) {
            @Override
            public T create(int layoutResId) {
                return builderDelegate.create(layoutResId);
            }
        };
    }

    public ModuleBuilder(
            Class viewModuleClass,
            @NonNull final ModuleBuilderDelegate<T> builderDelegate) {

        this.viewModuleClass = viewModuleClass;
        this.builderDelegate = builderDelegate;
    }

    public T createViewModule() {
        return builderDelegate.create();
    }

    public Class getViewModuleClass(){
        return viewModuleClass;
    }
}
