package com.guardanis.collections.list.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewModule<T> {

    protected int layoutResId;
    protected View convertView;

    public ViewModule(int layoutResId){
        this.layoutResId = layoutResId;
    }

    public View build(Context context, ViewGroup parent){
        this.convertView = inflate(context, parent);

        locateViewComponents(convertView);

        return convertView;
    }

    protected View inflate(Context context, ViewGroup parent){
        return LayoutInflater.from(context)
                .inflate(layoutResId, parent, false);
    }

    protected abstract void locateViewComponents(View convertView);

    public abstract void updateView(ModularArrayAdapter adapter, T item, int position);

    public View getConvertView(){
        return convertView;
    }
}
