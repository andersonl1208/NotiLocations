package com.notilocations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.notilocations.database.FullLocationTask
import com.notilocations.databinding.FragmentMapsBinding


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var currentView: View

    private lateinit var binding: FragmentMapsBinding

    //    private lateinit var locations: LiveData<List<FullLocationTask>>
    private lateinit var locations: List<FullLocationTask>

    //private val args: NotiLocationTask? =

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        createLocationObserver()

        map.setOnMapLongClickListener { latLng: LatLng? ->
            if (latLng != null) {

                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val input = EditText(requireContext())
                input.hint = "Location name"
                alertDialogBuilder.setView(input)
                alertDialogBuilder.setMessage(R.string.confirmLocation)

                alertDialogBuilder.setCancelable(true)

                alertDialogBuilder.setPositiveButton(
                    getString(android.R.string.ok)
                ) { dialog, _ ->
                    handleNewLocation(
                        NotiLocation(
                            null,
                            input.text.toString(),
                            latLng.latitude,
                            latLng.longitude
                        )
                    )
                    dialog.dismiss()
                }
                alertDialogBuilder.setNegativeButton(
                    getString(android.R.string.cancel)
                ) { dialog, _ ->
                    dialog.cancel()
                }

                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }


        // Set all the settings of the map to match the current state of the checkboxes
        with(map.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = false
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
            isRotateGesturesEnabled = true
        }


        // Moving camera to location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationToZoomTo(), 12F))


    }

    private fun handleNewLocation(notiLocation: NotiLocation?) {

        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        var notiLocationTask: NotiLocationTask;

        try {

            notiLocationTask = MapsFragmentArgs.fromBundle(requireArguments()).notiLocationTask
                ?: NotiLocationTask()
        } catch (e: Exception) {
            notiLocationTask = NotiLocationTask()
        }

        notiLocationTask.location = notiLocation

        if (notiLocationTask.hasTask()) {
            viewModel.syncNotiLocationTask(notiLocationTask)
            currentView.findNavController()
                .navigate(R.id.action_mapsFragment_to_swipeView)
        } else {

            val action =
                SwipeViewFragmentDirections.actionSwipeViewToCreateTaskFragment(
                    notiLocationTask
                )
            currentView.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_maps, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentView = view
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback);

    }

    // This function does nothing and is never called but has to be here for the interface to work.
    override fun onMapReady(googleMap: GoogleMap?) {}


    /**
     * Reloads the locations on maps so it updates with the database
     *
     */
    private fun createLocationObserver() {

        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        viewModel.getActiveFullLocationTasks().observe(viewLifecycleOwner,
            Observer<List<FullLocationTask>> { locationTasks ->
                println("========= Reloading Map")

                map.clear()

                if (locationTasks.isNotEmpty()) {

                    locationTasks.forEach { locationTask ->

                        var name = locationTask.location.name;
                        if (name.equals(""))
                            name = locationTask.task.title


                        // Distance is in meters
                        var distance: Double = 0.0

                        if (locationTask.locationTask.distance != null) {
                            println("here: " + locationTask.locationTask.distance)
                            distance = locationTask.locationTask.distance * 1609.34
                        }

                        var locationCoords =
                            LatLng(locationTask.location.lat, locationTask.location.lng)

                        if (!locationTask.locationTask.triggerOnExit) {

                            this.map.addCircle(
                                CircleOptions()
                                    .center(locationCoords)
                                    // Radius is in meters
                                    .radius(distance)
                                    .strokeWidth(4F)
                                    .strokeColor(Color.argb((.5 * 255).toInt(), 0, 128, 255))
                                    .fillColor(Color.argb((.2 * 255).toInt(), 56, 255, 255))
                            )

                        } else {
                            this.map.addCircle(
                                CircleOptions()
                                    .center(locationCoords)
                                    // Radius is in meters
                                    .radius(distance)
                                    .strokeWidth(4F)
                                    .strokeColor(Color.argb((.3 * 255).toInt(), 255, 0, 0))
                                    .fillColor(Color.argb((.1 * 255).toInt(), 255, 0, 0))
                            )
                        }

                        this.map.addMarker(
                            MarkerOptions()
                                .position(locationCoords)
                                .title(name)
                        ).tag = locationTask.locationTask.id

                    }
                }
            })
    }

    private fun locationToZoomTo(): LatLng {
        var locationToZoom = LatLng(40.7, -73.99);


        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        viewModel.getActiveFullLocationTasks().observe(viewLifecycleOwner,
            Observer<List<FullLocationTask>> { locationTasks ->

                // Right now it just gets the first element in the database
                if (locationTasks.isNotEmpty()) {
                    locationToZoom =
                        LatLng(
                            locationTasks.get(locationTasks.size - 1).location.lat,
                            locationTasks.get(locationTasks.size - 1).location.lng
                        );
                }

            })


        return locationToZoom
    }

}