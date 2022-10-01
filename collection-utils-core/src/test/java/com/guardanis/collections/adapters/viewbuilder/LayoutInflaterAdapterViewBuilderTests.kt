package com.guardanis.collections.adapters.viewbuilder

import android.app.Application
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LayoutInflaterAdapterViewBuilderTests {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun testInflatesTheSpecifiedLayout() {
        val inflater = LayoutInflaterAdapterViewBuilder(android.R.layout.activity_list_item)

        assertEquals(android.R.layout.activity_list_item, inflater.layoutResId)

        val view = inflater.createInstance(context, null)

        assertTrue(view is LinearLayout)
    }
}