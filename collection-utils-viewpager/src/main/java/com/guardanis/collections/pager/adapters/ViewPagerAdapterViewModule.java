package com.guardanis.collections.pager.adapters;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

abstract public class ViewPagerAdapterViewModule<T> extends AdapterViewModule<Fragment> {

    public static final String BUNDLE_LAYOUT_RES_ID_KEY = "layout_res_id";

    public ViewPagerAdapterViewModule(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    protected abstract Bundle createArguments(ModularAdapter adapter, T item, int position);
}