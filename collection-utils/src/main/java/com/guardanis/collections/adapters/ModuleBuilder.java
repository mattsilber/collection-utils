package com.guardanis.collections.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

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

    /**
     * Create the AdapterViewModule and inflate it
     */
    public T create(ViewGroup parent){
        T item = createViewModule();

        item.build(parent.getContext(),
                parent);

        return item;
    }

    public T createViewModule(){
        return builderDelegate.create(layoutResId);
    }

    public Class getViewModuleClass(){
        return viewModuleClass;
    }

}
