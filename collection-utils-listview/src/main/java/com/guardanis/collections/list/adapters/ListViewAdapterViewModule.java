package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewBuilder;
import com.guardanis.collections.adapters.viewbuilder.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

import java.lang.ref.WeakReference;

public abstract class ListViewAdapterViewModule<T> extends AdapterViewModule<View> {

    protected WeakReference<View> convertView;

    public ListViewAdapterViewModule(int layoutResId) {
        super(layoutResId);
    }

    public ListViewAdapterViewModule(AdapterViewBuilder builder) {
        super(builder);
    }

    @Override
    public View build(Context context, ViewGroup parent) {
        View generatedView = viewBuilder.createInstance(context, parent);

        this.convertView = new WeakReference<View>(generatedView);
        this.locateViewComponents(generatedView);

        return generatedView;
    }

    public void overrideTargetView(View convertView) {
        this.convertView = new WeakReference<View>(convertView);
        this.locateViewComponents(convertView);
    }

    protected abstract void locateViewComponents(View convertView);

    public abstract void updateView(ModularAdapter adapter, T item, int position);

    public View getConvertView(){
        return convertView.get();
    }
}
