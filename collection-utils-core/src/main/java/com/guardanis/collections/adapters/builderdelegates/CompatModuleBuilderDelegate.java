package com.guardanis.collections.adapters.builderdelegates;

public interface CompatModuleBuilderDelegate<T> {

    public T create(int layoutResId);
}
