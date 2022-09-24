package com.guardanis.collections.adapters

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guardanis.collections.adapters.properties.SimpleAdapterPropertiesManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O])
class AdapterPropertiesManagerTests {

    @Test
    fun testAdapterPropertiesManageReturnsNullForUnsetProperty() {
        val key = "test_key"

        val manager = SimpleAdapterPropertiesManager()

        assertNull(manager.getProperty(key))
    }

    @Test
    fun testAdapterPropertiesManageThrowsClassCastExceptionForBadPropertyTypeAccess() {
        val key = "test_key"

        val manager = SimpleAdapterPropertiesManager()
        manager.setProperty(key, true)

        val rule = ExpectedException.none()
        rule.expect(ClassCastException::class.java)

        manager.getProperty<Int>(key)
    }

    @Test
    fun testAdapterPropertiesManageReturnsStoredProperty() {
        val key = "test_key"
        val expected = "some_value"

        val manager = SimpleAdapterPropertiesManager()
        manager.setProperty(key, expected)

        assertEquals(expected, manager.getProperty(key))
    }
}