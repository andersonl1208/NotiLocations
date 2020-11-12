package com.notilocations

import android.Manifest
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap;

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map.setOnMapLongClickListener { latLng: LatLng? ->
            println("long click")
            if (latLng != null) {
                println("NotiLocation: " + latLng.latitude + latLng.longitude)

                // TODO Luke find a way to put this in the database


                val tempMarker = latLng
                googleMap.addMarker(MarkerOptions().position(tempMarker).title("Luke's House"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempMarker))


            }
         }


//        enableMyLocation()


        // Set all the settings of the map to match the current state of the checkboxes
        with(map.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
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
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
//    private fun enableMyLocation() {
//        if (hasLocationPermission()) {
//            map.isMyLocationEnabled = true
//        } else {
//            EasyPermissions.requestPermissions(this, getString(R.string.location),
//                REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//    private fun hasLocationPermission(): Boolean {
//        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
//    }
}