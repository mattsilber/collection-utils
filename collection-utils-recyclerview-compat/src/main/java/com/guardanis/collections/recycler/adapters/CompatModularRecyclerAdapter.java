package com.guardanis.collections.recycler.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.guardanis.collections.adapters.AdapterViewModule;
import com.guardanis.collections.list.adapters.ListViewModule;

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

        if (module instanceof ListViewModule) {
            ListViewModule listViewModule = ((ListViewModule) module);
            listViewModule.build(getContext(), parent);

            return new RecyclerListViewHolderCompat(listViewModule.getConvertView());
        }

        return super.createViewHolder(module, parent, viewType);
    }

    @Override
    protected <T> void bind(
            Object item,
            com.guardanis.collections.adapters.AdapterViewModule module,
            RecyclerView.ViewHolder holder,
            int position) {

        if (module instanceof ListViewModule)
            onBindCompatibilityViewModule((ListViewModule) module, item, holder, position);
        else
            super.bind(item, module, holder, position);
    }

    protected void onBindCompatibilityViewModule(ListViewModule module, Object item, RecyclerView.ViewHolder holder, int position) {
        module.overrideTargetView(holder.itemView);
        module.updateView(this, item, position);
    }
}
