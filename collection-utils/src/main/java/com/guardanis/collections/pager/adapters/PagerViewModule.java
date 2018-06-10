package com.guardanis.collections.pager.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

abstract public class PagerViewModule<T> extends AdapterViewModule<Fragment> {

    public static final String BUNDLE_LAYOUT_RES_ID_KEY = "layout_res_id";

    public PagerViewModule(int layoutResId) {
        super(layoutResId);
    }

    protected abstract Bundle createArguments(ModularAdapter adapter, T item, int position);
}