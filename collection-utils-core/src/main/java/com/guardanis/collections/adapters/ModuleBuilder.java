package com.guardanis.collections.adapters;

import com.guardanis.collections.adapters.builderdelegates.CompatModuleBuilderDelegate;
import com.guardanis.collections.adapters.builderdelegates.LayoutResourceModuleBuilderDelegate;
import com.guardanis.collections.adapters.builderdelegates.ModuleBuilderDelegate;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class ModuleBuilder<T extends AdapterViewModule> {

    protected ModuleBuilderDelegate<T> builderDelegate;

    public ModuleBuilder(
            @LayoutRes int layoutResId,
            @NonNull final CompatModuleBuilderDelegate<T> builderDelegate) {

        this.builderDelegate = new LayoutResourceModuleBuilderDelegate<T>(layoutResId) {
            @Override
            public T create(int layoutResId) {
                return builderDelegate.create(layoutResId);
            }
        };
    }

    public ModuleBuilder(
            @NonNull final ModuleBuilderDelegate<T> builderDelegate) {

        this.builderDelegate = builderDelegate;
    }

    public T createViewModule() {
        return builderDelegate.create();
    }
}
