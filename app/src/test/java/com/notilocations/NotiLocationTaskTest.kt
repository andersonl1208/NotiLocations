package com.notilocations

import com.notilocations.database.FullLocationTask
import com.notilocations.database.Location
import com.notilocations.database.LocationTask
import com.notilocations.database.Task
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class NotiLocationTaskTest {

    @Test
    fun hasLocation() {
        val locationTask = NotiLocationTask()
        assertFalse(locationTask.hasLocation())
        locationTask.location = NotiLocation()
        assertTrue(locationTask.hasLocation())
    }

    @Test
    fun hasTask() {
        val locationTask = NotiLocationTask()
        assertFalse(locationTask.hasTask())
        locationTask.task = NotiTask()
        assertTrue(locationTask.hasTask())
    }

    @Test
    fun hasLocationId() {
        val locationTask = NotiLocationTask()
        assertFalse(locationTask.hasLocationId())
        locationTask.location = NotiLocation()
        assertFalse(locationTask.hasLocationId())
        locationTask.location?.locationId = 126
        assertTrue(locationTask.hasLocationId())
    }

    @Test
    fun hasTaskId() {
        val locationTask = NotiLocationTask()
        assertFalse(locationTask.hasTaskId())
        locationTask.task = NotiTask()
        assertFalse(locationTask.hasTaskId())
        locationTask.task?.taskId = 16
        assertTrue(locationTask.hasTaskId())
    }

    @Test
    fun hasLocationTaskId() {
        val locationTask = NotiLocationTask()
        assertFalse(locationTask.hasLocationTaskId())
        locationTask.locationTaskId = 14
        assertTrue(locationTask.hasLocationTaskId())
    }

    @Test
    fun getDatabaseLocation() {

        val locationTask = NotiLocationTask()
        assertNull(locationTask.getDatabaseLocation())
        locationTask.location = NotiLocation(20, "Test", 0.0978, -3.2)
        val databaseLocation = locationTask.getDatabaseLocation()
        assertEquals(20L, databaseLocation?.id)
        assertEquals("Test", databaseLocation?.name)
        assertEquals(0.0978, databaseLocation?.lat ?: 0.0, 0.0001)
        assertEquals(-3.2, databaseLocation?.lng ?: 0.0, 0.0001)
    }

    @Test
    fun getDatabaseTask() {

        val locationTask = NotiLocationTask()
        assertNull(locationTask.getDatabaseTask())
        locationTask.task = NotiTask(21, "MyTitle", "Slightly longer description")
        val databaseTask = locationTask.getDatabaseTask()
        assertEquals(21L, databaseTask?.id)
        assertEquals("MyTitle", databaseTask?.title)
        assertEquals("Slightly longer description", databaseTask?.description)
    }

    @Test
    fun getDatabaseLocationTask() {

        val locationTask = NotiLocationTask(
            42,
            10.0f,
            40,
            Date(2020, 12, 10),
            isCompleted = true,
            triggerOnExit = true
        )
        assertNull(locationTask.getDatabaseLocationTask())

        locationTask.task = NotiTask(23)
        assertNull(locationTask.getDatabaseLocationTask())
        locationTask.location = NotiLocation(24)
        locationTask.task?.taskId = null
        assertNull(locationTask.getDatabaseLocationTask())
        locationTask.task?.taskId = 25

        val databaseLocationTask = locationTask.getDatabaseLocationTask()
        assertEquals(42L, databaseLocationTask?.id)
        assertEquals(10.0, databaseLocationTask?.distance?.toDouble() ?: 0.0, 0.0001)
        assertEquals(40, databaseLocationTask?.maxSpeed)
        assertEquals(Date(2020, 12, 10), databaseLocationTask?.creationDate)
        assert(databaseLocationTask?.isCompleted == true) // Note that == true to handle nulls
        assert(databaseLocationTask?.triggerOnExit == true)
        assertEquals(24L, databaseLocationTask?.locationId)
        assertEquals(25L, databaseLocationTask?.taskId)
    }

    @Test
    fun createFromFullLocationTask() {

        assertNull(NotiLocationTask.createFromFullLocationTask(null))

        val locationTask = LocationTask(
            50,
            51,
            52,
            15.5f,
            60,
            Date(2020, 12, 17),
            isCompleted = false,
            triggerOnExit = true
        )
        val location = Location(51, "Location Name", 5.6, -40.2)
        val task = Task(52, "Task Title", "Task Description")

        val fullLocationTask = FullLocationTask(locationTask, task, location)
        val notiLocationTask = NotiLocationTask.createFromFullLocationTask(fullLocationTask)

        assertEquals(50L, notiLocationTask?.locationTaskId)
        assertEquals(15.5f, notiLocationTask?.distance ?: 0.0f, 0.0001f)
        assertEquals(60, notiLocationTask?.maxSpeed)
        assertEquals(Date(2020, 12, 17), notiLocationTask?.creationDate)
        assertFalse(notiLocationTask?.isCompleted == true)
        assertTrue(notiLocationTask?.triggerOnExit == true)

        assertEquals(51L, notiLocationTask?.location?.locationId)
        assertEquals("Location Name", notiLocationTask?.location?.name)
        assertEquals(5.6, notiLocationTask?.location?.lat ?: 0.0, 0.0001)
        assertEquals(-40.2, notiLocationTask?.location?.lng ?: 0.0, 0.0001)

        assertEquals(52L, notiLocationTask?.task?.taskId)
        assertEquals("Task Title", notiLocationTask?.task?.title)
        assertEquals("Task Description", notiLocationTask?.task?.description)
    }
}