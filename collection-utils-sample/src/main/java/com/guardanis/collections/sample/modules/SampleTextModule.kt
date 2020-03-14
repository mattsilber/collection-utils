package com.guardanis.collections.sample.modules

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R
import com.guardanis.fontutils.TextView
import java.util.*

class SampleTextModule(
        private val title: String,
        private val details: String) {

    class ViewModule(layoutResId: Int): RecyclerViewModule<SampleTextModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleTextModule, position: Int) {
            viewHolder.titleView?.text = item.title
            viewHolder.detailsView?.text = 0.until(3).fold("", { last, next -> "$last\n${item.details}".trim() })
            viewHolder.itemView.setOnLongClickListener({
                adapter.triggerCallback(itemLongClicked, position)
                return@setOnLongClickListener true
            })
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var titleView: TextView?
        var detailsView: TextView?

        init {
            this.titleView = view.findViewById(R.id.text_module_title)
            this.detailsView = view.findViewById(R.id.text_module_details)
        }
    }

    companion object {

        const val itemLongClicked = "sample_text_module_clicked" // Int

        private val randomTitle: String
            get() {
                return when (Random().nextInt(3)) {
                    0 -> "This is a random title"
                    1 -> "This is another title"
                    else -> "This title is superior"
                }
            }

        private val randomDetails: String
            get() {
                return when (Random().nextInt(3)) {
                    0 -> "Sometimes random messages can be cool"
                    1 -> "Other times they can be weirdly formatted,\nwith multiple lines and with useless information."
                    else -> "I'm running out of ideas for random text"
                }
            }

        fun createRandomInstance(): SampleTextModule {
            return SampleTextModule(randomTitle, randomDetails)
        }
    }
}