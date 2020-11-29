package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.notilocations.database.FullLocationTask


class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {
    private var locationTasks: List<FullLocationTask>? = null
    private val bundle = Bundle()

    class TaskHolder(val taskView: View) : RecyclerView.ViewHolder(taskView)

    fun setLocationTasks(locationTaskList: List<FullLocationTask>) {
        locationTasks = locationTaskList
        notifyDataSetChanged()
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