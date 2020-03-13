package com.guardanis.collections.sample.modules

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R
import com.guardanis.collections.sample.glide.GlideApp
import java.util.*

class SampleImageModule(private val imageUrl: String) {

    class ViewModule(layoutResId: Int): RecyclerViewModule<SampleImageModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleImageModule, position: Int) {
            viewHolder.imageView?.run({
                ViewCompat.setBackground(this, null)

                GlideApp.with(this)
                        .load(item.imageUrl)
                        .into(this)
            })
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var imageView: AppCompatImageView? = view.findViewById(R.id.image_module_logo)
    }

    companion object {

        private val randomImageUrl: String
            get() {
                return when (Random().nextInt(3)) {
                    0 -> "https://guardanis.com/image/android_dreamer.png"
                    1 -> "https://guardanis.com/image/android_quitter.png"
                    else -> "https://guardanis.com/image/android_corners.png"
                }
            }

        fun createRandomInstance(): SampleImageModule {
            return SampleImageModule(randomImageUrl)
        }
    }
}