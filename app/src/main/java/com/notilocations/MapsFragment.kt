package com.notilocations

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
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
        zoomToLocation()
    }

    private fun handleNewLocation(notiLocation: NotiLocation?) {

        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        var notiLocationTask: NotiLocationTask

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
        mapFragment?.getMapAsync(callback)

    }

    // This function does nothing and is never called
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

                        var name = locationTask.location.name
                        if (name.equals(""))
                            name = locationTask.task.title

                        val sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context)

                        val distance =
                            (locationTask.locationTask.distance
                                ?: sharedPreferences.getString("distance", "")
                                    ?.toFloatOrNull() ?: 1.0F) * 1609.34F

                        var locationCoords =
                            LatLng(locationTask.location.lat, locationTask.location.lng)

                        if (!locationTask.locationTask.triggerOnExit) {

                            this.map.addCircle(
                                CircleOptions()
                                    .center(locationCoords)
                                    // Radius is in meters
                                    .radius(distance.toDouble())
                                    .strokeWidth(4F)
                                    .strokeColor(Color.argb((.5 * 255).toInt(), 0, 128, 255))
                                    .fillColor(Color.argb((.2 * 255).toInt(), 56, 255, 255))
                            )

                        } else {
                            this.map.addCircle(
                                CircleOptions()
                                    .center(locationCoords)
                                    // Radius is in meters
                                    .radius(distance.toDouble())
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

    private fun zoomToLocation() {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 12F
                    )
                )
            }
        }
    }
}