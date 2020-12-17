package com.notilocations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.notilocations.database.FullLocationTask
import com.notilocations.database.LocationTask


/**
 * Adapter class for location tasks. Creates each of the location task views.
 * @property locationTasks A list of location tasks to display.
 */
class TaskListAdapter(val fragment: TaskListFragment) :
    RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {
    private var locationTasks: List<FullLocationTask>? = null

    class TaskHolder(val taskView: View) : RecyclerView.ViewHolder(taskView)

    fun setLocationTasks(locationTaskList: List<FullLocationTask>) {
        locationTasks = locationTaskList
        notifyDataSetChanged()
    }

    /**
     * Gets the location task at the given position in the adapter.
     * @param position The position in the adapter of the location task to get.
     * @return The location task at the given position, or null if the position was invalid.
     */
    private fun getLocationTaskAtPosition(position: Int): FullLocationTask? {
        if (position >= 0 && position < locationTasks?.size ?: 0) {
            return locationTasks?.elementAt(position)
        }

        return null
    }

    /**
     * Removes the location task from the database at the given position in the adapter, and shows an undo snackbar.
     * @param position The position in the adapter of the location task to remove.
     */
    fun removeLocationTask(position: Int) {
        val locationTaskToRemove = getLocationTaskAtPosition(position)?.locationTask

        if (locationTaskToRemove != null) {
            ViewModelProvider(fragment).get(NotiLocationsViewModel::class.java)
                .deleteLocationTask(locationTaskToRemove)
            showUndoRemovedSnackbar(locationTaskToRemove)
        }
    }

    /**
     * Sets the location task to complete in the database at the given position in the adapter, and shows an undo snackbar.
     * @param position The position in the adapter of the location task to complete.
     */
    fun completeLocationTask(position: Int) {
        val locationTaskToComplete = getLocationTaskAtPosition(position)?.locationTask

        if (locationTaskToComplete != null) {
            val completedLocationTask = LocationTask(
                locationTaskToComplete.id,
                locationTaskToComplete.locationId,
                locationTaskToComplete.taskId,
                locationTaskToComplete.distance,
                locationTaskToComplete.maxSpeed,
                locationTaskToComplete.creationDate,
                true,
                locationTaskToComplete.triggerOnExit
            )
            ViewModelProvider(fragment).get(NotiLocationsViewModel::class.java)
                .updateLocationTask(completedLocationTask)
            showUndoCompletedSnackbar(completedLocationTask)
        }
    }

    /**
     * Shows an undo snackbar for when a location task is completed, and sets the action to uncomplete it if undo is clicked.
     * @param completedLocationTask The location task that was completed.
     */
    private fun showUndoCompletedSnackbar(completedLocationTask: LocationTask) {
        val snackbar = Snackbar.make(
            fragment.requireView(),
            R.string.snackbarItemUpdated,
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(R.string.snackbarUndo, fun(_) {
            val viewModel: NotiLocationsViewModel =
                ViewModelProvider(fragment).get(NotiLocationsViewModel::class.java)

            val updatedLocationTask = LocationTask(
                completedLocationTask.id,
                completedLocationTask.locationId,
                completedLocationTask.taskId,
                completedLocationTask.distance,
                completedLocationTask.maxSpeed,
                completedLocationTask.creationDate,
                false,
                completedLocationTask.triggerOnExit
            )
            viewModel.updateLocationTask(updatedLocationTask)
        })

        snackbar.show()
    }

    /**
     * Shows an undo snackbar for when a location task is removed, and sets the action to re-add it if undo is clicked.
     * @param removedLocationTask The location task that was removed.
     */
    private fun showUndoRemovedSnackbar(removedLocationTask: LocationTask) {

        val snackbar = Snackbar.make(
            fragment.requireView(),
            R.string.snackbarItemRemoved,
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(R.string.snackbarUndo, fun(_) {
            val viewModel: NotiLocationsViewModel =
                ViewModelProvider(fragment).get(NotiLocationsViewModel::class.java)
            viewModel.createLocationTask(removedLocationTask)
        })

        snackbar.show()
    }

    /**
     * Gets the number of location tasks.
     * @return The count of location tasks.
     */
    override fun getItemCount(): Int {
        return locationTasks?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = (LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task_list_adapter, parent, false))


        return TaskHolder(view)
    }

    /**
     * override onBindViewHOlder sets the value of items from binding
     * @param holder the TaskHolder being passed through
     * @param position position of the item
     */
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskTitle = holder.taskView.findViewById<TextView>(R.id.titleText)
        val taskDescription = holder.taskView.findViewById<TextView>(R.id.descriptionText)

        holder.taskView.setOnClickListener { v: View ->
            val action = SwipeViewFragmentDirections.actionSwipeViewToCreateTaskFragment(
                NotiLocationTask.createFromFullLocationTask(locationTasks?.get(position.toInt()))
            )
            v.findNavController().navigate(action)
        }


        if (locationTasks != null) {
            val locationTask = locationTasks?.get(position)
            taskTitle.text = locationTask?.task?.title.toString()
            taskDescription.text = locationTask?.task?.description.toString()

        } else {
            taskDescription.text = ""
            taskTitle.text = ""
        }
    }
}