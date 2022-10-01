package com.guardanis.collections

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.mockito.Mockito.`when` as WHEN

@RunWith(AndroidJUnit4::class)
class CollectionControllerTests {

    private val parent = mock<ListView>()
    private val controller = CollectionController<ListView>(parent)
    private val module = spy<CollectionModule<ListView>>()

    @Test
    fun testRegisterModuleUpdatesParent() {
        controller.registerModule(module)

        assertEquals(parent, module.parent)
        assertEquals(module, controller.getModule(module::class.java))
    }

    @Test
    fun testModuleCanBeUnregistered() {
        controller.registerModule(module)
        controller.unregisterModule(module)

        assertNull(controller.getModule(module::class.java))
    }

    @Test
    fun testDispatchDrawDelegatedToModules() {
        val canvas = mock<Canvas>()

        controller.registerModule(module)
        controller.onDrawDispatched(canvas)

        verify(module, times(1))
            .onDrawDispatched(eq(canvas))
    }

    @Test
    fun testOnScrollStateChangedDelegatedToModules() {
        controller.registerModule(module)
        controller.onScrollStateChanged(10)

        verify(module, times(1))
            .onScrollStateChanged(eq(10))
    }

    @Test
    fun testOnScrollDelegatedToModules() {
        controller.registerModule(module)
        controller.onScroll(10)

        verify(module, times(1))
            .onScroll(eq(10))
    }

    @Test
    fun testModuleReturningTrueInOnTouchBreaksOutWithTrue() {
        abstract class SecondModule: CollectionModule<ListView>()

        val secondModule = spy<SecondModule>()

        WHEN(module.onTouch(any(), any()))
            .thenReturn(true)

        WHEN(secondModule.onTouch(any(), any()))
            .thenReturn(false)

        controller.registerModule(module)
        controller.registerModule(secondModule)

        assertTrue(
            controller.onTouch(parent, mock<MotionEvent>())
        )

        verify(module, times(1))
            .onTouch(eq(parent), any())

        verify(secondModule, never())
            .onTouch(eq(parent), any())
    }

    @Test
    fun testOnTouchReturnsFalseWhenNoModuleReturnsTrue() {
        WHEN(module.onTouch(any(), any()))
            .thenReturn(false)

        controller.registerModule(module)

        assertFalse(
            controller.onTouch(parent, mock<MotionEvent>())
        )

        verify(module, times(1))
            .onTouch(eq(parent), any())
    }

    @Test
    fun testOnTouchReturnsSecondaryOnTouchListenerResultWhenSetAndNoModuleHandles() {
        val listener = mock<View.OnTouchListener>()

        WHEN(listener.onTouch(any(), any()))
            .thenReturn(true)

        val controller = CollectionController<ListView>(parent)
        controller.registerSecondaryTouchListener(listener)

        assertTrue(
            controller.onTouch(parent, mock<MotionEvent>())
        )

        verify(listener, times(1))
            .onTouch(eq(parent), any())
    }
}