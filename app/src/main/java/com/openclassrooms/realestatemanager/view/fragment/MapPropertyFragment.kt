package com.openclassrooms.realestatemanager.view.fragment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapRealEstatePropertyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapPropertyFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapRealEstatePropertyBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MapDebug", "onCreateView called")
        _binding = FragmentMapRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MapDebug", "onViewCreated called")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Ici, nous utilisons le View Binding pour trouver le fragment
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MapDebug", "onMapReady called")
        map = googleMap
        getLastKnownLocation()

        val uiSettings = map.uiSettings
        uiSettings.isZoomControlsEnabled = true


    }

    private fun getLastKnownLocation() {
        Log.d("MapDebug", "getLastKnownLocation called")
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("MapDebug", "Location retrieved: ${location?.latitude}, ${location?.longitude}")
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    map.addMarker(MarkerOptions().position(currentLatLng).title("Ma Position"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                }
            }.addOnFailureListener { exception ->
                Log.e("MapDebug", "Location retrieval failed", exception)
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {

            showLocationExplanationDialog()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showLocationExplanationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Location Permission Needed")
            .setMessage(
                "This app requires access to your location to show your current position on the map and to display nearby properties." +
                        " We respect your privacy and will not use this information for any other purpose."
            )
            .setPositiveButton("OK") { dialog, which ->
                // After explanation, request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation()
            } else {

                // Show a toast message if permission is denied
                Toast.makeText(
                    context,
                    "Permission required to show location on map",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
