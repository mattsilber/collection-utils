package com.guardanis.collections.sample.modules

import android.support.v7.widget.RecyclerView
import android.view.View
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R

class SampleDividerModule(private val height: Int) {

    class ViewModule(): RecyclerViewModule<SampleDividerModule, ViewHolder>(R.layout.divider_module) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleDividerModule, position: Int) {
            val params = viewHolder.itemView.layoutParams
            params.height = item.height

            viewHolder.itemView.layoutParams = params
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object {

        fun createInstance(): SampleDividerModule {
            return SampleDividerModule(1)
        }
    }
}