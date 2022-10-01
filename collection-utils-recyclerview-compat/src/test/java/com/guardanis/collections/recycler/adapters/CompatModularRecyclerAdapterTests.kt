package com.guardanis.collections.recycler.adapters

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompatModularRecyclerAdapterTests {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun testCanBindACompatListAdapterItem() {
        val adapter = CompatModularRecyclerAdapter(context)

        // TODO: Test the adapter
    }
}