package com.guardanis.collections.adapters

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk=[Build.VERSION_CODES.O])
class ModuleBuilderResolverTests {

    @Test
    fun testSimpleModuleBuilderResolverResolvesSingleItemBuilder() {
        val stringBuilder = createBuilder(TestLayoutId.stringModule)
        val resolver = ModuleBuilderResolver.createSimpleResolverInstance<String>(stringBuilder)

        assertEquals(1, resolver.builderTypeCount)
        assertEquals(stringBuilder, resolver.builders.single())
        assertEquals(stringBuilder, resolver.resolve(mock(ModularAdapter::class.java), "", 0))
    }

    @Test
    fun testModuleBuilderTypeIndexMatchesSuppliedOrder() {
        val adapter = mock(ModularAdapter::class.java)
        val stringBuilder = createBuilder(TestLayoutId.stringModule)
        val booleanBuilder = createBuilder(TestLayoutId.booleanModule)
        val mockItem = "fake"

        val resolver = object: ModuleBuilderResolver<Any>(stringBuilder, booleanBuilder) {
            override fun resolve(adapter: ModularAdapter, item: Any, position: Int): ModuleBuilder<out AdapterViewModule<*>> {
                return if (position.rem(builders.size) == 0) stringBuilder else booleanBuilder
            }
        }

        assertEquals(2, resolver.builderTypeCount)

        assertEquals(0, resolver.getViewTypeIndex(adapter, mockItem, 0))
        assertEquals(1, resolver.getViewTypeIndex(adapter, mockItem, 1))

        assertEquals(stringBuilder, resolver.resolve(adapter, mockItem, 0))
        assertEquals(booleanBuilder, resolver.resolve(adapter, mockItem, 1))
    }

    @Test
    fun testModuleBuilderAccessByLocalIndex() {
        val stringBuilder = createBuilder(TestLayoutId.stringModule)
        val booleanBuilder = createBuilder(TestLayoutId.booleanModule)

        val resolver = object: ModuleBuilderResolver<Any>(stringBuilder, booleanBuilder) {
            override fun resolve(adapter: ModularAdapter, item: Any, position: Int): ModuleBuilder<out AdapterViewModule<*>> {
                return if (position.rem(builders.size) == 0) stringBuilder else booleanBuilder
            }
        }

        assertEquals(stringBuilder, resolver.getBuilder(0))
        assertEquals(booleanBuilder, resolver.getBuilder(1))
    }

    private fun createBuilder(layoutResId: Int): ModuleBuilder<MockViewModule> {
        return ModuleBuilder(layoutResId, ::MockViewModule)
    }
}