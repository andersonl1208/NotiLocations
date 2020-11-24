package com.notilocations

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.notilocations.database.Task
import com.notilocations.databinding.FragmentCreateTaskBinding
import com.notilocations.databinding.FragmentUpdateTaskBinding

class UpdateTaskFragment : Fragment() {
    val args: UpdateTaskFragmentArgs by navArgs()
    val taskDetails : NotiLocationsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentUpdateTaskBinding>(inflater, R.layout.fragment_update_task, container, false)

        binding.titleInput.setText(args.title)
        binding.descriptionInput.setText(args.description)
        binding.titleInput.hint = args.title
        binding.descriptionInput.hint = args.title

        Log.i("position of item", args.position.toString())

        binding.submitUpdate.setOnClickListener{v: View ->
            if(TextUtils.isEmpty(binding.titleInput.text.toString())){
                Toast.makeText(context , "Please enter a title for your task", Toast.LENGTH_SHORT).show()
            }
            else{
                //update the task
                taskDetails.updateTask(Task( args.position, binding.titleInput.text.toString(), binding.descriptionInput.text.toString()))
                binding.titleInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
                binding.descriptionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
                v.findNavController().navigate(R.id.action_updateTaskFragment_to_swipeView)

            }
        }


        binding.deleteButton.setOnClickListener{v: View ->
            //delete the task
            taskDetails.deleteTask(Task(args.position, binding.titleInput.text.toString(), binding.descriptionInput.text.toString()))
            binding.titleInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.descriptionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
            v.findNavController().navigate(R.id.action_updateTaskFragment_to_swipeView)
        }
        binding.updateLocation.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_updateTaskFragment_to_mapsFragment)
        }
        return binding.root
    }
}