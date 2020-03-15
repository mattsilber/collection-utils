package com.guardanis.collections.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.guardanis.collections.adapters.Callback
import com.guardanis.collections.adapters.ModuleBuilder
import com.guardanis.collections.recycler.ModularRecyclerView
import com.guardanis.collections.recycler.adapters.CompatModularRecyclerAdapter
import com.guardanis.collections.recycler.adapters.ModularRecyclerAdapter
import com.guardanis.collections.recycler.modules.EndlessModule
import com.guardanis.collections.recycler.modules.SwipeRefreshLayoutModule
import com.guardanis.collections.sample.glide.GlideApp
import com.guardanis.collections.sample.modules.*
import java.lang.ref.WeakReference
import java.util.*

class MainActivity: AppCompatActivity(), EndlessModule.EndlessEventListener {

    private var recycler: WeakReference<ModularRecyclerView> = WeakReference<ModularRecyclerView>(null)
    private lateinit var adapter: ModularRecyclerAdapter

    private val sampleTextModuleLongClickedCallback = Callback<Int> {
        adapter.remove(it)
    }

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
        recycler.registerModule(KSwipeRefreshLayoutModule(findViewById(R.id.main_swipe_refresh_layout), ::refresh))
        recycler.itemAnimator = DefaultItemAnimator()

        this.adapter = CompatModularRecyclerAdapter(this)

        adapter.registerModuleBuilder(
                SampleTextModule::class.java,
                ModuleBuilder(
                        R.layout.text_module,
                        { SampleTextModule.ViewModule(it) }))

        adapter.registerModuleBuilder(
                SampleImageModule::class.java,
                ModuleBuilder(
                        { SampleImageModule.ViewModule(R.layout.image_module) }))

        adapter.registerModuleBuilder(
                SampleViewPagerModule::class.java,
                ModuleBuilder(
                        R.layout.pager_module,
                        { SampleViewPagerModule.ViewModule(it) }))

        adapter.registerModuleBuilder(
                SampleDividerModule::class.java,
                ModuleBuilder({ SampleDividerModule.ViewModule() }))

        // A ListViewAdapterViewModule is backwards compatible with the ModularRecyclerAdapter
        adapter.registerModuleBuilder(
                SampleTextListModule::class.java,
                ModuleBuilder(SampleTextListModule::ViewModule))

        // Callbacks are registered with the ModularAdapter to be triggered later by items
        adapter.registerCallback(SampleTextModule.itemLongClicked, sampleTextModuleLongClickedCallback)

        recycler.adapter = this.adapter

        this.recycler = WeakReference(recycler)
    }

    private fun refresh() {
        recycler.get()
                ?.let({ it.getModule(EndlessModule::class.java) })
                ?.apply({
                    this.isLoading = true
                    this.setEndingReached(false)
                })

        adapter.clear()

        appendRandomContent()

        recycler.get()
                ?.getModule(KSwipeRefreshLayoutModule::class.java)
                ?.onRefreshCompleted()
    }

    private fun appendRandomContent() {
        0.until(10)
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

        recycler.get()
                ?.getModule(EndlessModule::class.java)
                ?.isLoading = false
    }

    override fun onNextPage() {
        recycler.get()
                ?.apply({
                    this.getModule(EndlessModule::class.java)?.isLoading = true
                    this.post({
                        appendRandomContent()

                        if (1000 < this@MainActivity.adapter.itemCount)
                            this.getModule(EndlessModule::class.java)?.onEndingReached()
                    })
                })
    }

    class KSwipeRefreshLayoutModule(layout: SwipeRefreshLayout, refreshListener: (() -> Unit)):
            SwipeRefreshLayoutModule<ModularRecyclerView>(layout, refreshListener)
}
