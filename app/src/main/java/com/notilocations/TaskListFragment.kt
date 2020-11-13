package com.notilocations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.notilocations.databinding.FragmentCreateTaskBinding
import com.notilocations.databinding.FragmentTaskListBinding
import kotlinx.android.synthetic.main.fragment_task_list.view.*

class TaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTaskListBinding>(inflater, R.layout.fragment_task_list, container, false)
        binding.addTask.setOnClickListener{v: View ->
            v.findNavController().navigate(R.id.action_swipeView_to_createTaskFragment)
        }
        return binding.root
    }
}