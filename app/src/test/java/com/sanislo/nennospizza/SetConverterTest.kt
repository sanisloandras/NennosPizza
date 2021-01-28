package com.sanislo.nennospizza

import com.sanislo.nennospizza.db.SetConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class SetConverterTest {

    @Test
    fun test() {
        val setConverter = SetConverter()
        assertEquals("1,2,7", setConverter.fromSet(setOf(1, 2, 7)))
        assertEquals(setOf(1,2,7), setConverter.toSet("1,2,7"))
    }
}