package com.guardanis.collections.sample.modules

import android.graphics.Color
import androidx.annotation.ColorInt
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R
import com.guardanis.fontutils.TextView
import java.util.*

class SampleTextModule(
        private val title: String,
        private val details: String,
        private @ColorInt val backgroundColor: Int) {

    class ViewModule(layoutResId: Int): RecyclerViewModule<SampleTextModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleTextModule, position: Int) {
            viewHolder.titleView?.text = item.title
            viewHolder.detailsView?.text = item.details
            viewHolder.itemView.setBackgroundColor(item.backgroundColor)
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

        private val randomColor: Int
            get() {
                return when (Random().nextInt(4)) {
                    0 -> Color.parseColor("#F44336")
                    1 -> Color.parseColor("#2980B9")
                    2 -> Color.TRANSPARENT
                    else -> Color.parseColor("#7F8C8D")
                }
            }

        fun createRandomInstance(): SampleTextModule {
            return SampleTextModule(randomTitle, randomDetails, randomColor)
        }
    }
}