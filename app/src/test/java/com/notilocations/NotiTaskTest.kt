package com.notilocations

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotiTaskTest {

    @Test
    fun hasIdTest() {
        val task = NotiTask()
        assertFalse(task.hasId())
        task.taskId = 0
        assertTrue(task.hasId())
    }
}