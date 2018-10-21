package com.guardanis.collections.sample.modules

import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.adapters.ModuleBuilder
import com.guardanis.collections.pager.adapters.ModularPagerFragmentAdapter
import com.guardanis.collections.recycler.adapters.RecyclerViewModule
import com.guardanis.collections.sample.R

class SampleViewPagerModule(private val fragmentManager: FragmentManager) {

    class ViewModule(layoutResId: Int): RecyclerViewModule<SampleViewPagerModule, ViewHolder>(layoutResId) {

        override fun buildViewHolder(view: View): ViewHolder {
            return ViewHolder(view)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleViewPagerModule, position: Int) {
            val adapter = ModularPagerFragmentAdapter(adapter.context, item.fragmentManager)
            adapter.registerModuleBuilder(
                    SamplePagerFragmentModule::class.java,
                    ModuleBuilder(
                            R.layout.pager_fragment,
                            SamplePagerFragmentModule.ViewModule::class.java,
                            { SamplePagerFragmentModule.ViewModule(it) }))
            adapter.add(SamplePagerFragmentModule.createInstance())
            adapter.add(SamplePagerFragmentModule.createInstance())
            adapter.add(SamplePagerFragmentModule.createInstance())

            viewHolder.pager?.id = position + 1
            viewHolder.pager?.adapter = adapter
            viewHolder.pager?.currentItem = 0

            adapter.notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var pager: ViewPager? = view.findViewById(R.id.pager_module_container)
        var adapter: ModularPagerFragmentAdapter? = null
    }

    companion object {

        fun createInstance(fragmentManager: FragmentManager): SampleViewPagerModule {
            return SampleViewPagerModule(fragmentManager)
        }
    }
}