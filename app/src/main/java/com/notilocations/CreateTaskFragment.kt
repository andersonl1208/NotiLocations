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
import com.notilocations.databinding.FragmentCreateTaskBinding

/**
 * Lets the user create or edit a task.
 */
class CreateTaskFragment : Fragment() {

    private lateinit var binding: FragmentCreateTaskBinding

    /**
     * Inflates the fragment, initializes the views, and sets up the click listeners.
     *
     * @param inflater The inflater to use to inflate the fragment layout.
     * @param container The container to place the fragment in.
     * @param savedInstanceState The previously saved state of the fragment if it exists.
     * @return The view returned by the inflater.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_task,
            container,
            false
        )

        initializeViewsAndClickListeners(CreateTaskFragmentArgs.fromBundle(requireArguments()).notiLocationTask)

        return binding.root
    }

    /**
     * Initializes the views and click listeners.
     * @param notiLocationTask The NotiLocationTask to use to initialize the views and click listeners.
     */
    private fun initializeViewsAndClickListeners(notiLocationTask: NotiLocationTask?) {

        createOnClickListeners(notiLocationTask)
        initializeViews(notiLocationTask)
    }

    /**
     * Creates the click listeners.
     * @param notiLocationTask The NotiLocationTask to use to create the click listeners.
     */
    private fun createOnClickListeners(notiLocationTask: NotiLocationTask?) {

        binding.addLocation.setOnClickListener { v: View ->
            navigateListener(v, notiLocationTask, true)
        }

        binding.submitTask.setOnClickListener { v: View ->
            navigateListener(v, notiLocationTask, false)
        }
    }

    /**
     * Initializes all the views using the provided notiLocationTask.
     * @param notiLocationTask The NotiLocationTask to use to initialize the views.
     */
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


    /**
     * Initializes the delete button, setting it to invisible if it hasn't been created in the database.
     * @param notiLocationTask The notiLocationTask that will be deleted if the button is clicked.
     */
    private fun initializeDeleteButton(notiLocationTask: NotiLocationTask?) {
        if (notiLocationTask?.hasLocationTaskId() == true) {
            binding.deleteButton.setOnClickListener { v: View ->
                //delete the task
                val viewModel: NotiLocationsViewModel by viewModels()
                if (notiLocationTask.getDatabaseLocationTask() != null) {
                    viewModel.deleteLocationTask(notiLocationTask.getDatabaseLocationTask()!!)
                }

                closeKeyboard()

                v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)
            }
        } else {
            binding.deleteButton.visibility = View.INVISIBLE
        }
    }

    /**
     * Initializes the max speed inputs to use the current max speed and sets up their on click listeners.
     * @param maxSpeed The current max speed if it exists.
     */
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

    /**
     * Initializes the distance inputs to use the current distance.
     * @param The current distance if it exists.
     */
    private fun initializeDistance(distance: Float?) {
        if (distance != null) {
            binding.distanceInput.setText(distance.toString())
            binding.distanceInput.hint = distance.toString()
        }
    }

    /**
     * Gets the current value of the max speed input converted to the proper max speed.
     * @return The value of the max speed input + 10.
     */
    private fun getMaxSpeedValue(): Int {
        return binding.maxSpeedInput.progress + resources.getInteger(R.integer.speed_min)
    }

    /**
     * Closes the keyboard.
     */
    private fun closeKeyboard() {
        binding.titleInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.descriptionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.distanceInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    /**
     * Validates the values of the inputs.
     * @return Returns true if the inputs are valid.
     */
    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(binding.titleInput.text.toString())) {
            Toast.makeText(context, "Please enter a title for your task", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    /**
     * Fills a NotiLocationTask from the inputs.
     * @param notiLocationTask The base NotiLocationTask to use.
     * @return The filled NotiLocationTask.
     */
    private fun fillNotiLocationTaskFromInputs(notiLocationTask: NotiLocationTask): NotiLocationTask {

        if (!notiLocationTask.hasTask()) {
            notiLocationTask.task = NotiTask()
        }

        notiLocationTask.task?.title = binding.titleInput.text.toString()
        notiLocationTask.task?.description = binding.descriptionInput.text.toString()

        notiLocationTask.maxSpeed = if (binding.maxSpeedEnabledInput.isChecked) {
            getMaxSpeedValue()
        } else {
            null
        }

        notiLocationTask.distance = binding.distanceInput.text.toString().toFloatOrNull()
        notiLocationTask.triggerOnExit = binding.triggerOnExitInput.isChecked

        return notiLocationTask
    }

    /**
     * Navigates to the next fragment when submitted, and syncs the NotiLocationTask with the database when necessary.
     * @param v The view that was clicked.
     * @param notiLocationTask The NotiLocationTask to sync with the database or send with the navigation.
     * @param defaultToMap Whether or not to navigate to the map by default.
     */
    private fun handleNavigationOnSubmit(
        v: View,
        notiLocationTask: NotiLocationTask,
        defaultToMap: Boolean
    ) {
        val viewModel: NotiLocationsViewModel by viewModels()

        if (notiLocationTask.hasLocation() && !defaultToMap) {
            viewModel.syncNotiLocationTask(notiLocationTask)
            HandleGeofences.getInstance(requireActivity().application).create()
            v.findNavController().navigate(R.id.action_createTaskFragment_to_swipeView)
        } else {

            val action =
                CreateTaskFragmentDirections.actionCreateTaskFragmentToMapsFragment(
                    notiLocationTask
                )
            v.findNavController().navigate(action)
        }
    }

    /**
     * Listener for when a click where submit navigation should occur happens, and then handles that navigation.
     * @param v The view that was clicked.
     * @param notiLocationTask The NotiLocationTask to fill with the inputs.
     * @param defaultToMap Whether or not to navigate to the map by default.
     */
    private fun navigateListener(
        v: View,
        notiLocationTask: NotiLocationTask?,
        defaultToMap: Boolean
    ) {
        if (validateInputs()) {
            closeKeyboard()
            val filledNotiLocationTask =
                fillNotiLocationTaskFromInputs(notiLocationTask ?: NotiLocationTask())
            handleNavigationOnSubmit(v, filledNotiLocationTask, defaultToMap)
        }
    }
}