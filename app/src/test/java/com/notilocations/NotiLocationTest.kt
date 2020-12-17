package com.notilocations

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotiLocationTest {

    @Test
    fun hasIdTest() {
        val loc = NotiLocation()
        assertFalse(loc.hasId())
        loc.locationId = 0
        assertTrue(loc.hasId())
    }
}