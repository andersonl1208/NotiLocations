package com.notilocations

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.notilocations.database.NotiLocationsRepository
import com.notilocations.database.Task
import com.notilocations.databinding.FragmentCreateTaskBinding

class CreateTaskFragment : Fragment() {
    val args: CreateTaskFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentCreateTaskBinding>(inflater, R.layout.fragment_create_task, container, false)
        Log.i("positon", args.position.toString())
        binding.submitTask.setOnClickListener{v: View ->
            if(TextUtils.isEmpty(binding.titleInput.text.toString())){
                Toast.makeText(context , "Please enter a title for your task", Toast.LENGTH_SHORT).show()
            }
            else{
                val taskDetails : NotiLocationsViewModel by viewModels()
                taskDetails.createTask(Task( null , binding.titleInput.text.toString(), binding.descriptionInput.text.toString()))

                v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)

            }
        }
        binding.addLocation.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_createTaskFragment_to_mapsFragment)
        }
        return binding.root
    }
}