package com.guardanis.collections.sample.modules

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewAdapterViewModule
import com.guardanis.collections.sample.R
import com.guardanis.collections.sample.glide.GlideApp

class SampleImageModule(private val imageUrl: String) {

    class ViewModule(layoutResId: Int): RecyclerViewAdapterViewModule<SampleImageModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleImageModule, position: Int) {
            viewHolder.detail?.text = item.imageUrl
            viewHolder.imageView?.run({
                ViewCompat.setBackground(this, null)

                GlideApp.with(this)
                    .load(item.imageUrl)
                    .apply(noCacheRequestOptions())
                    .into(this)
            })
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var imageView: AppCompatImageView? = view.findViewById(R.id.image_module_logo)
        var detail: TextView? = view.findViewById(R.id.image_module_detail)
    }

    companion object {

        fun createRandomInstance(): SampleImageModule {
            return SampleImageModule("https://source.unsplash.com/random/600x400")
        }
    }
}

fun noCacheRequestOptions(): RequestOptions {
    return RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
}