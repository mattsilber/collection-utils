package com.guardanis.collections.adapters.viewbuilder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface AdapterViewBuilder {

    public View createInstance(Context context, ViewGroup parent);
}
