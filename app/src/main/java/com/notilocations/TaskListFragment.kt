package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.notilocations.database.FullLocationTask
import com.notilocations.databinding.FragmentTaskListBinding
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTaskListBinding>(
            inflater,
            R.layout.fragment_task_list,
            container,
            false
        )

        val recyclerLayout = LinearLayoutManager(this.context)
        val recyclerAdapter = TaskListAdapter(this)

        binding.taskRecycler.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)
        viewModel.getActiveFullLocationTasks()
            .observe(this.viewLifecycleOwner, object : Observer<List<FullLocationTask>> {
                private val adapter = recyclerAdapter
                override fun onChanged(t: List<FullLocationTask>?) {
                    if (t != null) {
                        adapter.setLocationTasks(t)
                        if(adapter.itemCount == 0){
                            binding.addTaskText.visibility = View.VISIBLE
                            binding.taskRecycler.visibility = View.GONE
                        }
                        else{
                            binding.addTaskText.visibility = View.GONE
                            binding.taskRecycler.visibility = View.VISIBLE
                        }
                    }
                }
            })

        val swipeHandler = SwipeToDeleteCallback(requireContext(), recyclerAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.taskRecycler)

        binding.addTask.setOnClickListener { v: View ->
            val action = SwipeViewFragmentDirections.actionSwipeViewToCreateTaskFragment()
            v.findNavController().navigate(action)
        }

        binding.settingButton.setOnClickListener { v: View ->
            val action = SwipeViewFragmentDirections.actionSwipeViewToSettingsFragment()
            v.findNavController().navigate(action)
        }

        return binding.root
    }
}