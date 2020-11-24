package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.notilocations.database.Task


class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskHolder>(){
    private var tasks: List<Task>?= null
    private val bundle = Bundle()

    class TaskHolder(val taskView: View) : RecyclerView.ViewHolder(taskView)

    fun setTasks(taskList: List<Task>){
        tasks = taskList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tasks?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.fragment_task_list_adapter, parent, false))


        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskTitle = holder.taskView.findViewById<TextView>(R.id.titleText)
        val taskDescription = holder.taskView.findViewById<TextView>(R.id.descriptionText)

        holder.taskView.setOnClickListener { v: View ->
            val action = SwipeViewFragmentDirections.actionSwipeViewToUpdateTaskFragment(
                taskTitle.text.toString(), taskDescription.text.toString(),
                tasks?.get(position.toInt())?.id!!
            )
            v.findNavController().navigate(action)
        }


        if(tasks != null){
            val task = tasks?.get(position)
                ?: Task(null,"Pickup milk", "1/2 gallon 1%")
            taskTitle.text = task.title.toString()
            taskDescription.text = task.description.toString()
        }
        else{
            taskDescription.text = ""
            taskTitle.text = ""
        }
    }
}