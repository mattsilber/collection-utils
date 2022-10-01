package com.guardanis.collections.adapters

import android.app.Application
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guardanis.collections.adapters.viewbuilder.LayoutInflaterAdapterViewBuilder
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdapterViewModuleTests {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun testDeprecatedInflateCallsViewBuilder() {
        val module = MockAdapterViewModule(android.R.layout.activity_list_item)
        val view = module.inflate(context, null)

        assertTrue(view is LinearLayout)
    }

    @Test
    fun testConstructorWithLayoutResourceIdCreatesLayoutInflater() {
        val module = MockAdapterViewModule(android.R.layout.activity_list_item)

        assertTrue(module.viewBuilder is LayoutInflaterAdapterViewBuilder)
    }

    private class MockAdapterViewModule(val layoutResId: Int): AdapterViewModule<View>(layoutResId) {

        override fun build(context: Context?, parent: ViewGroup?): View {
            return viewBuilder.createInstance(context, parent)
        }
    }
}