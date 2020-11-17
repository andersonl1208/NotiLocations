package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.notilocations.database.Task
import com.notilocations.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTaskListBinding>(inflater, R.layout.fragment_task_list, container, false)

        var recyclerLayout = LinearLayoutManager(this.context)
        var recyclerAdapter = TaskListAdapter()

        binding.taskRecycler.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }
        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)
        viewModel.getTasks().observe(this.viewLifecycleOwner, object: Observer<List<Task>>{
            private val adapter = recyclerAdapter

            override fun onChanged(t: List<Task>?) {
                if(t != null){
                    adapter.setTasks(t)
                }
            }
        })
        binding.addTask.setOnClickListener{v: View ->
            val action = SwipeViewFragmentDirections.actionSwipeViewToCreateTaskFragment(recyclerAdapter.itemCount.toLong())
            v.findNavController().navigate(action)
        }
        return binding.root
    }
}