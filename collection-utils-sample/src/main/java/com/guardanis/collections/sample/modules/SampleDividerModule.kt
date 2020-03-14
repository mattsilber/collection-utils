package com.guardanis.collections.sample.modules

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewAdapterViewModule
import com.guardanis.collections.recycler.adapters.callbacks.ViewHolderLifeCycleCallbacks
import com.guardanis.collections.sample.R

class SampleDividerModule(private val height: Int) {

    class ViewModule(): RecyclerViewAdapterViewModule<SampleDividerModule, ViewHolder>(R.layout.divider_module) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleDividerModule, position: Int) {
            val params = viewHolder.itemView.layoutParams
            params.height = item.height

            viewHolder.itemView.layoutParams = params
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view), ViewHolderLifeCycleCallbacks {

        override fun onViewAttachedToWindow() {
            Log.d(callbackTag, "onViewAttachedToWindow")
        }

        override fun onViewDetachedFromWindow() {
            Log.d(callbackTag, "onViewDetachedFromWindow")
        }

        override fun onFailedToRecycleView(): Boolean {
            Log.d(callbackTag, "onFailedToRecycleView")

            return false
        }

        override fun onViewRecycled() {
            Log.d(callbackTag, "onViewRecycled")
        }
    }

    companion object {

        val callbackTag = "VHLifeCycleCallbacks"

        fun createInstance(): SampleDividerModule {
            return SampleDividerModule(1)
        }
    }
}