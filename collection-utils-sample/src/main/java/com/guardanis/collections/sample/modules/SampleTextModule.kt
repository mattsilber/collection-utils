package com.guardanis.collections.sample.modules

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewAdapterViewModule
import com.guardanis.collections.sample.R
import com.guardanis.fontutils.TextView

class SampleTextModule(
    private val title: String,
    private val details: String,
) {

    class ViewModule(layoutResId: Int): RecyclerViewAdapterViewModule<SampleTextModule, ViewHolder>(layoutResId) {

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

        fun createRandomInstance(): SampleTextModule {
            val titles: Array<String> = arrayOf(
                "This is a random title",
                "Some important title",
                "A superior title emerges"
            )

            val details: Array<String> = arrayOf(
                "Sometimes short random messages can be cool",
                "Other times they can be weirdly formatted,\nwith multiple lines and with useless information.",
                "I'm running out of ideas for random text",
                "Pretend this says something really important"
            )

            return SampleTextModule(titles.random(), details.random())
        }
    }
}