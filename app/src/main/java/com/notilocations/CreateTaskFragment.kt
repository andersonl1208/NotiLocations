package com.notilocations

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.notilocations.databinding.FragmentCreateTaskBinding


class CreateTaskFragment : Fragment() {
    private val args: CreateTaskFragmentArgs by navArgs()
    private lateinit var binding: FragmentCreateTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentCreateTaskBinding>(
            inflater,
            R.layout.fragment_create_task,
            container,
            false
        )

        initializeViewsAndClickListeners(CreateTaskFragmentArgs.fromBundle(requireArguments()).notiLocationTask)

        return binding.root
    }

    private fun initializeViewsAndClickListeners(notiLocationTask: NotiLocationTask?) {

        createOnClickListeners(notiLocationTask)
        initializeViews(notiLocationTask)
    }

    private fun createOnClickListeners(notiLocationTask: NotiLocationTask?) {

        binding.addLocation.setOnClickListener { v: View ->
            navigateListener(v, notiLocationTask, true)
        }

        binding.submitTask.setOnClickListener { v: View ->
            navigateListener(v, notiLocationTask, false)
        }
    }

    private fun initializeViews(notiLocationTask: NotiLocationTask?) {
        binding.titleInput.setText(notiLocationTask?.task?.title)
        binding.descriptionInput.setText(notiLocationTask?.task?.description)
        binding.titleInput.hint = notiLocationTask?.task?.title
        binding.descriptionInput.hint = notiLocationTask?.task?.description

        binding.triggerOnExitInput.isChecked = notiLocationTask?.triggerOnExit ?: false

        if (notiLocationTask?.hasLocation() == true) {
            binding.addLocation.text = getString(R.string.updateLocation)
        }

        initializeDeleteButton(notiLocationTask)
        initializeMaxSpeed(notiLocationTask?.maxSpeed)
        initializeDistance(notiLocationTask?.distance)
    }


    private fun initializeDeleteButton(notiLocationTask: NotiLocationTask?) {
        if (notiLocationTask?.hasLocationTaskId() == true) {
            binding.deleteButton.setOnClickListener { v: View ->
                //delete the task
                val viewModel: NotiLocationsViewModel by viewModels()
                if (notiLocationTask.getDatabaseLocationTask() != null) {
                    viewModel.deleteLocationTask(notiLocationTask.getDatabaseLocationTask()!!)
                }

                binding.titleInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
                binding.descriptionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)

                v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)
            }
        } else {
            binding.deleteButton.visibility = View.INVISIBLE
        }
    }

    private fun initializeMaxSpeed(maxSpeed: Int?) {

        binding.maxSpeedInput.max =
            resources.getInteger(R.integer.speed_max) - resources.getInteger(
                R.integer.speed_min
            )

        if (maxSpeed != null) {
            binding.maxSpeedEnabledInput.isChecked = true
            binding.maxSpeedInput.isEnabled = true
            binding.maxSpeedInput.progress = maxSpeed - resources.getInteger(R.integer.speed_min)
        } else {
            binding.maxSpeedEnabledInput.isChecked = false
            binding.maxSpeedInput.isEnabled = false
            binding.maxSpeedInput.progress = 0
        }

        binding.maxSpeedValue.text = getMaxSpeedValue().toString()

        binding.maxSpeedEnabledInput.setOnClickListener {
            binding.maxSpeedInput.isEnabled = binding.maxSpeedEnabledInput.isChecked
        }

        binding.maxSpeedInput.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.maxSpeedValue.text = getMaxSpeedValue().toString()
            }
        })
    }

    private fun initializeDistance(distance: Float?) {
        if (distance != null) {
            binding.distanceInput.setText(distance.toString())
            binding.distanceInput.hint = distance.toString()
        }
    }

    private fun getMaxSpeedValue(): Int {
        return binding.maxSpeedInput.progress + resources.getInteger(R.integer.speed_min)
    }

    private fun navigateListener(
        v: View,
        notiLocationTask: NotiLocationTask?,
        defaultToMap: Boolean
    ) {
        if (TextUtils.isEmpty(binding.titleInput.text.toString())) {
            Toast.makeText(context, "Please enter a title for your task", Toast.LENGTH_SHORT).show()
        } else {
            val viewModel: NotiLocationsViewModel by viewModels()
            binding.titleInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.descriptionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)

            if (notiLocationTask != null) {

                if (notiLocationTask.hasTask()) {
                    notiLocationTask.task?.title = binding.titleInput.text.toString()
                    notiLocationTask.task?.description = binding.descriptionInput.text.toString()
                } else {
                    notiLocationTask.task = NotiTask(
                        null,
                        binding.titleInput.text.toString(),
                        binding.descriptionInput.text.toString()
                    )
                }


                notiLocationTask.maxSpeed = if (binding.maxSpeedEnabledInput.isChecked) {
                    getMaxSpeedValue()
                } else {
                    null
                }
                notiLocationTask.distance = binding.distanceInput.text.toString().toFloatOrNull()
                notiLocationTask.triggerOnExit = binding.triggerOnExitInput.isChecked

                if (notiLocationTask.hasLocation() && !defaultToMap) {
                    viewModel.syncNotiLocationTask(notiLocationTask)
                    v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)
                } else {

                    val action =
                        CreateTaskFragmentDirections.actionCreateTaskFragmentToMapsFragment(
                            notiLocationTask
                        )
                    v.findNavController().navigate(action)
                }
            } else {
                val newTask = NotiTask(
                    null,
                    binding.titleInput.text.toString(),
                    binding.descriptionInput.text.toString()

                )
                val newNotiLocationTask = NotiLocationTask(
                    maxSpeed = if (binding.maxSpeedEnabledInput.isChecked) {
                        getMaxSpeedValue()
                    } else {
                        null
                    },
                    distance = binding.distanceInput.text.toString().toFloatOrNull(),
                    triggerOnExit = binding.triggerOnExitInput.isChecked,
                    task = newTask
                )

                val action = CreateTaskFragmentDirections.actionCreateTaskFragmentToMapsFragment(
                    newNotiLocationTask
                )
                v.findNavController().navigate(action)
            }
        }
    }
}