package com.guardanis.collections

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class CollectionModuleTests {

    @Test
    fun testDetachedFromWindowClearsParentReference() {
        val parent = mock<ListView>()
        val module = MockCollectionModule()

        module.parent = parent

        assertEquals(parent, module.parent)

        module.onDetachedFromWindow()

        assertNull(module.parent)
    }

    private class MockCollectionModule: CollectionModule<ListView>() {

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            return true
        }

        override fun onDrawDispatched(canvas: Canvas?) {

        }

        override fun onScrollStateChanged(scrollState: Int) {

        }

        override fun onScroll(vararg values: Int) {

        }
    }
}