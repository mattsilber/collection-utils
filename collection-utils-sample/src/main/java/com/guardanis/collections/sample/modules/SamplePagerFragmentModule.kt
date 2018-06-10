package com.guardanis.collections.sample.modules

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.pager.adapters.PagerViewModule

class SamplePagerFragmentModule {

    class ViewModule(layoutResId: Int): PagerViewModule<SamplePagerFragmentModule>(layoutResId) {

        override fun build(context: Context, parent: ViewGroup?): Fragment {
            return SamplePagerFragment()
        }

        override fun createArguments(adapter: ModularAdapter, item: SamplePagerFragmentModule, position: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt(PagerViewModule.BUNDLE_LAYOUT_RES_ID_KEY, layoutResId)

            return bundle
        }
    }

    class SamplePagerFragment: Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            return inflater.inflate(arguments?.getInt(PagerViewModule.BUNDLE_LAYOUT_RES_ID_KEY) ?: 0, container, false)
        }
    }

    companion object {

        fun createInstance(): SamplePagerFragmentModule {
            return SamplePagerFragmentModule()
        }
    }
}