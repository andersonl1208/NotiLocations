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

    /**
     * Function that calls on first initialization of the map
     */
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
            isMyLocationButtonEnabled = true
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = true
        }

        val notiLocationTask = try {
            MapsFragmentArgs.fromBundle(requireArguments()).notiLocationTask
        } catch (e: Exception) {
            null
        }

        // Moving camera to location
        if (notiLocationTask?.hasLocation() == true) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        notiLocationTask.location?.lat ?: 0.0,
                        notiLocationTask.location?.lng ?: 0.0
                    ), 12F
                )
            )
        } else {
            zoomToUsersLocation()
        }
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

    /**
     * Inflates the fragment and sets the binding variable.
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
            inflater, R.layout.fragment_maps, container, false
        )

        return binding.root
    }

    /**
     * Called when the view has been created. Sets up the map fragment and gets the map.
     * @param view The view that was created.
     * @param savedInstanceState The previously saved state of the fragment, if it exists.A
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentView = view
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    /**
     *  This function does nothing and is never called
     */
    override fun onMapReady(googleMap: GoogleMap?) {}


    /**
     * Reloads the locations on maps so it updates with the database
     */
    private fun createLocationObserver() {

        val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

        viewModel.getActiveFullLocationTasks().observe(viewLifecycleOwner,
            Observer<List<FullLocationTask>> { locationTasks ->

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

                        drawLocationCircle(
                            locationCoords,
                            distance.toDouble(),
                            locationTask.locationTask.triggerOnExit
                        )

                        this.map.addMarker(
                            MarkerOptions()
                                .position(locationCoords)
                                .title(name)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gear_icon))
                        ).tag = locationTask.locationTask.id

                    }
                }
            })
    }

    /**
     * Draws a circle on the map around a location.
     * @param locationCoords The coordinates of the center of the circle.
     * @param distance The radius of the circle.
     * @param triggerOnExit Whether or not the associated location task is triggered on exit.
     */
    private fun drawLocationCircle(
        locationCoords: LatLng,
        distance: Double,
        triggerOnExit: Boolean
    ) {
        var strokeColor = Color.argb((.5 * 255).toInt(), 0, 128, 255)
        var fillColor = Color.argb((.2 * 255).toInt(), 56, 255, 255)

        if (triggerOnExit) {
            strokeColor = Color.argb((.3 * 255).toInt(), 255, 0, 0)
            fillColor = Color.argb((.1 * 255).toInt(), 255, 0, 0)
        }

        this.map.addCircle(
            CircleOptions()
                .center(locationCoords)
                .radius(distance)
                .strokeWidth(4F)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
        )
    }

    /**
     * Zooms the map to the users location if it is available.
     */
    private fun zoomToUsersLocation() {

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