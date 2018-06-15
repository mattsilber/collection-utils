package com.guardanis.collections.sample

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.guardanis.collections.adapters.ModuleBuilder
import com.guardanis.collections.generic.SwipeRefreshLayoutModule
import com.guardanis.collections.recycler.ModularRecyclerView
import com.guardanis.collections.recycler.adapters.ModularRecyclerAdapter
import com.guardanis.collections.recycler.modules.EndlessModule
import com.guardanis.collections.sample.modules.*
import java.lang.ref.WeakReference
import java.util.*

class MainActivity: AppCompatActivity(), EndlessModule.EndlessEventListener {

    private var recycler: WeakReference<ModularRecyclerView> = WeakReference<ModularRecyclerView>(null)
    private lateinit var adapter: ModularRecyclerAdapter

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(R.layout.activity_main)
        setupRecyclerView()
        refresh()
    }

    private fun setupRecyclerView() {
        val recycler = findViewById<ModularRecyclerView>(R.id.main_list)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.registerModule(EndlessModule(this))
        recycler.registerModule(KSwipeRefreshLayoutModule(findViewById(R.id.main_swipe_refresh_layout), { refresh() }))

        this.adapter = ModularRecyclerAdapter(this)

        adapter.registerModuleBuilder(
                SampleTextModule::class.java,
                ModuleBuilder(
                        R.layout.text_module,
                        SampleTextModule.ViewModule::class.java,
                        { SampleTextModule.ViewModule(it) }))

        adapter.registerModuleBuilder(
                SampleImageModule::class.java,
                ModuleBuilder(
                        R.layout.image_module,
                        SampleImageModule.ViewModule::class.java,
                        { SampleImageModule.ViewModule(it) }))

        adapter.registerModuleBuilder(
                SampleViewPagerModule::class.java,
                ModuleBuilder(
                        R.layout.pager_module,
                        SampleViewPagerModule.ViewModule::class.java,
                        { SampleViewPagerModule.ViewModule(it) }))

        adapter.registerModuleBuilder(
                SampleDividerModule::class.java,
                ModuleBuilder(
                        R.layout.divider_module,
                        SampleDividerModule.ViewModule::class.java,
                        { SampleDividerModule.ViewModule() }))

        // A ListViewModule is backwards compatible with the ModularRecyclerAdapter
        adapter.registerModuleBuilder(
                SampleTextListModule::class.java,
                ModuleBuilder(
                        R.layout.text_list_module,
                        SampleTextListModule.ViewModule::class.java,
                        { SampleTextListModule.ViewModule() }))

        recycler.adapter = this.adapter

        this.recycler = WeakReference(recycler)
    }

    private fun refresh() {
        recycler.get()
                ?.getModule(EndlessModule::class.java)
                ?.isLoading = true

        recycler.get()
                ?.getModule(EndlessModule::class.java)
                ?.setEndingReached(false)

        adapter.clear()

        appendRandomContent()
    }

    private fun appendRandomContent() {
        0.until(30)
                .map({
                    when (Random().nextInt(5)) {
                        0 -> SampleImageModule.createRandomInstance()
                        1 -> SampleViewPagerModule.createInstance(supportFragmentManager)
                        2 -> SampleTextListModule.createInstance()
                        else -> SampleTextModule.createRandomInstance()
                    }
                })
                .forEach({
                    adapter.add(it)
                    adapter.add(SampleDividerModule.createInstance())
                })

        adapter.notifyDataSetChanged()

        recycler.get()
                ?.getModule(EndlessModule::class.java)
                ?.isLoading = false

        recycler.get()
                ?.getModule(KSwipeRefreshLayoutModule::class.java)
                ?.onRefreshCompleted()
    }

    override fun onNextPage() {
        recycler.get()
                ?.getModule(EndlessModule::class.java)
                ?.isLoading = true

        appendRandomContent()

        if (1000 < adapter.itemCount)
            recycler.get()
                    ?.getModule(EndlessModule::class.java)
                    ?.onEndingReached()
    }

    class KSwipeRefreshLayoutModule(layout: SwipeRefreshLayout, refreshListener: (() -> Unit)):
            SwipeRefreshLayoutModule<ModularRecyclerView>(layout, refreshListener)
}
