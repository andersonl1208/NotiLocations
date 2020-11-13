package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.notilocations.databinding.FragmentCreateTaskBinding

class CreateTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateTaskBinding>(inflater, R.layout.fragment_create_task, container, false)
        binding.submitTask.setOnClickListener{v: View ->
            v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)
        }
        binding.addLocation.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_createTaskFragment_to_mapsFragment)
        }
        return binding.root
    }
}