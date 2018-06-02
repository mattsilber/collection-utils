package com.guardanis.collections.sample.modules

import android.content.Context
import android.support.v4.view.ViewCompat
import android.view.View
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import com.guardanis.collections.recycler.adapters.ModularRecyclerAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R
import com.guardanis.imageloader.ImageRequest
import java.util.*

class SampleDividerModule(private val height: Int) {

    class ViewModule(): RecyclerViewModule<SampleDividerModule, ViewHolder>(R.layout.divider_module) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularRecyclerAdapter, item: SampleDividerModule, position: Int) {
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