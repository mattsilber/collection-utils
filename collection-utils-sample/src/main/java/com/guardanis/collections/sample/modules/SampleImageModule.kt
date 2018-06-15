package com.guardanis.collections.sample.modules

import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R
import com.guardanis.imageloader.ImageRequest
import java.util.*

class SampleImageModule(private val imageUrl: String) {

    class ViewModule(layoutResId: Int): RecyclerViewModule<SampleImageModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleImageModule, position: Int) {
            viewHolder.imageView?.let({
                ViewCompat.setBackground(it, null)

                ImageRequest.create(it)
                        .setTargetUrl(item.imageUrl)
                        .setTagRequestPreventionEnabled(false)
                        .execute()
            })
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var imageView: AppCompatImageView?

        init {
            this.imageView = view.findViewById(R.id.image_module_logo)
        }
    }

    companion object {

        private val randomImageUrl: String
            get() {
                return when (Random().nextInt(3)) {
                    0 -> "http://guardanis.com/image/android_dreamer.png"
                    1 -> "http://guardanis.com/image/android_quitter.png"
                    else -> "http://guardanis.com/image/android_corners.png"
                }
            }

        fun createRandomInstance(): SampleImageModule {
            return SampleImageModule(randomImageUrl)
        }
    }
}