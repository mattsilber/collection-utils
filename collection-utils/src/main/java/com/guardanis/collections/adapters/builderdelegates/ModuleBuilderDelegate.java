package com.guardanis.collections.adapters.builderdelegates;

import com.guardanis.collections.adapters.AdapterViewModule;

public interface ModuleBuilderDelegate<T extends AdapterViewModule> {

    public T create();
}
