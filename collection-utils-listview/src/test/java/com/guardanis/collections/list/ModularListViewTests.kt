package com.guardanis.collections.list

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModularListViewTests {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun testCreatesControllerOnInit() {
        val view = ModularListView(context)

        Assert.assertNotNull(view.controller)
    }
}