package com.notilocations

import android.os.Bundle
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


class TaskListAdapter(val fragment: TaskListFragment) :
    RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {
    private var locationTasks: List<FullLocationTask>? = null
    private val bundle = Bundle()

    class TaskHolder(val taskView: View) : RecyclerView.ViewHolder(taskView)

    fun setLocationTasks(locationTaskList: List<FullLocationTask>) {
        locationTasks = locationTaskList
        notifyDataSetChanged()
    }

    private fun getLocationTaskAtPosition(position: Int): FullLocationTask? {
        if (position >= 0 && position < locationTasks?.size ?: 0) {
            return locationTasks?.elementAt(position)
        }

        return null
    }

    fun removeLocationTask(position: Int) {
        val locationTaskToRemove = getLocationTaskAtPosition(position)?.locationTask

        if (locationTaskToRemove != null) {
            ViewModelProvider(fragment).get(NotiLocationsViewModel::class.java)
                .deleteLocationTask(locationTaskToRemove)
            showUndoRemovedSnackbar(locationTaskToRemove)
        }
    }

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

    override fun getItemCount(): Int {
        return locationTasks?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = (LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_task_list_adapter, parent, false))


        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskTitle = holder.taskView.findViewById<TextView>(R.id.titleText)
        val taskDescription = holder.taskView.findViewById<TextView>(R.id.descriptionText)
        val taskLat = holder.taskView.findViewById<TextView>(R.id.latText)
        val taskLng = holder.taskView.findViewById<TextView>(R.id.lngText)

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
            taskLat.text = locationTask?.location?.lat.toString()
            taskLng.text = locationTask?.location?.lng.toString()

        } else {
            taskDescription.text = ""
            taskTitle.text = ""
        }
    }
}