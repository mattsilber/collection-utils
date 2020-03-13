package com.guardanis.collections.pager.adapters;

import android.os.Bundle;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

import androidx.fragment.app.Fragment;

abstract public class PagerViewModule<T> extends AdapterViewModule<Fragment> {

    public static final String BUNDLE_LAYOUT_RES_ID_KEY = "layout_res_id";

    public PagerViewModule(int layoutResId) {
        super(layoutResId);
    }

    protected abstract Bundle createArguments(ModularAdapter adapter, T item, int position);
}