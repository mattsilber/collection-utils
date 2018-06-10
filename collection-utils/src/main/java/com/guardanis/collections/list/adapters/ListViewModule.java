package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.adapters.ModularAdapter;

public abstract class ListViewModule<T> extends AdapterViewModule<View> {

    protected View convertView;

    public ListViewModule(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public View build(Context context, ViewGroup parent){
        this.convertView = inflate(context, parent);

        locateViewComponents(convertView);

        return convertView;
    }

    protected abstract void locateViewComponents(View convertView);

    public abstract void updateView(ModularAdapter adapter, T item, int position);

    public View getConvertView(){
        return convertView;
    }
}
