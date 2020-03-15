package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.list.adapters.ListViewAdapterViewModule;

import androidx.recyclerview.widget.RecyclerView;

public class CompatModularRecyclerAdapter extends ModularRecyclerAdapter {

    public CompatModularRecyclerAdapter(Context context){
        super(context);
    }

    @Override
    protected RecyclerView.ViewHolder createViewHolder(
            AdapterViewModule module,
            ViewGroup parent,
            int viewType) {

        if (module instanceof ListViewAdapterViewModule) {
            ListViewAdapterViewModule listViewModule = ((ListViewAdapterViewModule) module);
            listViewModule.build(getContext(), parent);

            return new RecyclerListViewHolderCompat(listViewModule.getConvertView());
        }

        return super.createViewHolder(module, parent, viewType);
    }

    @Override
    protected <T> void bind(
            Object item,
            AdapterViewModule module,
            RecyclerView.ViewHolder holder,
            int position) {

        if (module instanceof ListViewAdapterViewModule)
            onBindCompatibilityViewModule((ListViewAdapterViewModule) module, item, holder, position);
        else
            super.bind(item, module, holder, position);
    }

    @SuppressWarnings("unchecked")
    protected void onBindCompatibilityViewModule(
            ListViewAdapterViewModule module,
            Object item,
            RecyclerView.ViewHolder holder,
            int position) {

        module.overrideTargetView(holder.itemView);
        module.updateView(this, item, position);
    }
}
