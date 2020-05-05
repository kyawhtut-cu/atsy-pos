package com.kyawhtut.pos

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.ceil

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kyawhtut.pos", appContext.packageName)
    }

    @Test
    fun limitationTest() {
        val array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val page = 4 - 1
        val size = 21
        val limit = 5.0
        val pageCount = ceil(size / limit).toInt()
        val start = (page * limit).toInt()
        var end = (page * limit).toInt() + limit.toInt()
        if (end > array.size) end = array.size
//        assertEquals(start, end)
        if (page < pageCount)
            assertEquals(
                arrayOf(1, 2, 3, 4, 5, 6), array.toMutableList().subList(
                    start,
                    end
                )
            )
        else
            assertEquals(page, pageCount)
    }
}
