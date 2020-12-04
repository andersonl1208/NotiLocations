package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.notilocations.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var currentView: View

    private lateinit var binding: FragmentMapsBinding

    //private val args: NotiLocationTask? =

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map.setOnMapLongClickListener { latLng: LatLng? ->
            println("long click")
            if (latLng != null) {
                println("NotiLocation: " + latLng.latitude + "\t" + latLng.longitude)


                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setMessage(R.string.confirmLocation)
                alertDialogBuilder.setCancelable(true)

                alertDialogBuilder.setPositiveButton(
                        getString(android.R.string.ok)
                ) { dialog, _ ->
                    dialog.dismiss()


                    val viewModel = ViewModelProvider(this).get(NotiLocationsViewModel::class.java)

                    val tempMarker = latLng
                    googleMap.addMarker(MarkerOptions().position(tempMarker).title("Luke's House"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempMarker))

                    val notiLocationTask = if (arguments == null) {
                        null
                    } else {
                        MapsFragmentArgs.fromBundle(requireArguments()).notiLocationTask
                    }

                    if (notiLocationTask != null) {
                        if (notiLocationTask.hasLocation()) {
                            notiLocationTask.location?.lat = latLng.latitude
                            notiLocationTask.location?.lng = latLng.longitude
                        } else {
                            notiLocationTask.location =
                                    NotiLocation(null, "", latLng.latitude, latLng.longitude)
                        }

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

                    } else {
                        val newLocation = NotiLocation(null, null, latLng.latitude, latLng.longitude)
                        val newNotiLocationTask = NotiLocationTask(location = newLocation)
                        val action = SwipeViewFragmentDirections.actionSwipeViewToCreateTaskFragment(
                                newNotiLocationTask
                        )
                        currentView.findNavController().navigate(action)
                    }


                }
                alertDialogBuilder.setNegativeButton(
                        getString(android.R.string.cancel)
                ) { dialog, _ ->
                    println("===============Canceled")
                    dialog.cancel()
                }

                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()





            }
        }



        val tempLocation = LatLng(44.87335854645772, -91.9216525554657)
        googleMap.addMarker(MarkerOptions().position(tempLocation).title("Luke's House"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempLocation))
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

        // Example Marker
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

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
        super.onViewCreated(view, savedInstanceState)
        currentView = view
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    override fun onMapReady(googleMap: GoogleMap?) {



    }



//    /** Override the onRequestPermissionResult to use EasyPermissions */
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }

    /**
     * enableMyLocation() will enable the location of the map if the user has given permission
     * for the app to access their device location.
     * Android Studio requires an explicit check before setting map.isMyLocationEnabled to true
     * but we are using EasyPermissions to handle it so we can suppress the "MissingPermission"
     * check.
     */
//    @SuppressLint("MissingPermission")
//    @AfterPermissionGranted(R.string.location)
//    private fun enableMyLocation() {
//        if (hasLocationPermission()) {
//            map.isMyLocationEnabled = true
//        } else {
//            EasyPermissions.requestPermissions(this, getString(R.string.location), 0,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//    private fun hasLocationPermission(): Boolean {
//        return EasyPermissions.hasPermissions(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//    }
}