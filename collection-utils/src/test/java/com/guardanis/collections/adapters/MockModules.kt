package com.guardanis.collections.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import org.robolectric.annotation.Config

object TestLayoutId {
    const val stringModule = 0
    const val intModule = 1
    const val booleanModule = 2
}

class MockViewModule(val layoutResId: Int): AdapterViewModule<View>(layoutResId) {

    @Config(shadows=[View::class])
    override fun build(context: Context, parent: ViewGroup?): View {
        return View(context)
    }
}

class MockModuleBuilderDelegate: ModuleBuilder.BuilderDelegate<MockViewModule> {

    override fun create(layoutResId: Int): MockViewModule {
        return MockViewModule(layoutResId)
    }
}