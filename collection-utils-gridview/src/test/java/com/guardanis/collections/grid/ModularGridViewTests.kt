package com.guardanis.collections.grid

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ModularGridViewTests {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun testCreatesControllerOnInit() {
        val view = ModularGridView(context)

        assertNotNull(view.controller)
    }
}