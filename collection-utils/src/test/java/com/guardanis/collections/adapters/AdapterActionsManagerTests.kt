package com.guardanis.collections.adapters

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guardanis.collections.adapters.actions.SimpleAdapterActionsManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk=[Build.VERSION_CODES.O])
class AdapterActionsManagerTests {

    abstract class KStringCallback: Callback<String>

    @Test
    fun testAdapterActionsManagerTriggersNothingWhenIncorrectType() {
        val key = "test_key"
        val callback = mock(KStringCallback::class.java)

        val manager = SimpleAdapterActionsManager()
        manager.registerCallback(key, callback)
        manager.triggerCallback(key, 0)

        verify(callback, never())
                .onTriggered(ArgumentMatchers.any())
    }

    @Test
    fun testAdapterActionsManagerTriggersActionCallback() {
        val key = "test_key"
        val callback = mock(KStringCallback::class.java)
        val expected = "some_value"

        val manager = SimpleAdapterActionsManager()
        manager.registerCallback(key, callback)
        manager.triggerCallback(key, expected)

        verify(callback, times(1))
                .onTriggered(expected)
    }
}