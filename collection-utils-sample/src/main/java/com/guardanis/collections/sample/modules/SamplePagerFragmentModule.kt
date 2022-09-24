package com.guardanis.collections.sample.modules

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestOptions
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.pager.adapters.ViewPagerAdapterViewModule
import com.guardanis.collections.sample.R
import com.guardanis.collections.sample.glide.GlideApp
import jp.wasabeef.glide.transformations.BlurTransformation

class SamplePagerFragmentModule {

    class ViewModule(val layoutResId: Int): ViewPagerAdapterViewModule<SamplePagerFragmentModule>(layoutResId) {

        override fun build(context: Context, parent: ViewGroup?): Fragment {
            return SamplePagerFragment()
        }

        override fun createArguments(adapter: ModularAdapter, item: SamplePagerFragmentModule, position: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt(ViewPagerAdapterViewModule.BUNDLE_LAYOUT_RES_ID_KEY, layoutResId)

            return bundle
        }
    }

    class SamplePagerFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            val inflated = inflater.inflate(arguments?.getInt(ViewPagerAdapterViewModule.BUNDLE_LAYOUT_RES_ID_KEY) ?: 0,
                container,
                false)

            val imageView = inflated.findViewById<ImageView>(R.id.pager_fragment_image)

            GlideApp.with(imageView.context)
                .load("https://picsum.photos/400/600")
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .apply(noCacheRequestOptions())
                .into(imageView)

            return inflated
        }
    }

    companion object {

        fun createInstance(): SamplePagerFragmentModule {
            return SamplePagerFragmentModule()
        }
    }
}