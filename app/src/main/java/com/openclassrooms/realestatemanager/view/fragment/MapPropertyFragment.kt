package com.openclassrooms.realestatemanager.view.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyWithMainPicture
import com.openclassrooms.realestatemanager.databinding.FragmentMapRealEstatePropertyBinding
import com.openclassrooms.realestatemanager.view.activity.DetailActivity
import com.openclassrooms.realestatemanager.viewmodel.MapPropertyViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapPropertyFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapPropertyViewModel by viewModels()
    private var _binding: FragmentMapRealEstatePropertyBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isLocationUpdatesStarted = false
    private var lastUpdatedLocation: Location? = null
    private val MIN_DISTANCE_FOR_UPDATE = 10


    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProperties()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        val mapFragment =
            childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupLocationRequest()
        setupLocationCallback()
    }

    private fun setupLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                @Suppress("UNUSED_EXPRESSION")

                locationResult
                for (location in locationResult.locations) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                    updateMapLocation(location)
                }
            }
        }
    }

    private fun updateMapLocation(location: Location) {
        if (lastUpdatedLocation == null || location.distanceTo(lastUpdatedLocation!!) > MIN_DISTANCE_FOR_UPDATE) {
            val currentLatLng = LatLng(location.latitude, location.longitude)
            // Change the marker icon here
            map.addMarker(MarkerOptions()
                .position(currentLatLng)
                .title("My Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
            lastUpdatedLocation = location
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener { marker ->
            val property = marker.tag as? PropertyModels
            property?.let { openPropertyDetailActivity(it) }
            true
        }

        val uiSettings = map.uiSettings
        uiSettings.isZoomControlsEnabled = true

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            isLocationUpdatesStarted = true
        } else {
            requestLocationPermission()
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        isLocationUpdatesStarted = false
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
                startLocationUpdates()
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

    private fun observeProperties() {
        viewModel.properties.observe(viewLifecycleOwner) { properties ->
            // Mettre à jour la carte avec les propriétés
            updateMapWithProperties(properties)
        }
    }

    private fun updateMapWithProperties(properties: List<PropertyWithMainPicture>) {
        properties.forEach { propertyWithPicture ->
            val property = propertyWithPicture.property
            if (property.latitude != 0.0 && property.longitude != 0.0) {
                val location = LatLng(property.latitude, property.longitude)
                val markerOptions = MarkerOptions().position(location)
                    .title(property.type + " - $" + property.price)
                val marker = map.addMarker(markerOptions)
                if (marker != null) {
                    marker.tag = property
                }
            }
        }
    }

    private fun openPropertyDetailActivity(property: PropertyModels) {
        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra("EXTRA_ID", property.id)
            putExtra("EXTRA_AGENT_ID", property.agentId)
            putExtra("EXTRA_PRICE", property.price)
            putExtra("EXTRA_TYPE", property.type)
            putExtra("EXTRA_AREA", property.area)
            putExtra("EXTRA_LOCATION", property.address)
            putStringArrayListExtra("EXTRA_NEARBY", ArrayList(property.nearbyPointsOfInterest))
            putExtra("EXTRA_ROOMS", property.numberOfRooms)
            putExtra("EXTRA_DESCRIPTION", property.fullDescription)
            putExtra("EXTRA_IS_SOLD", property.status)
            putExtra("EXTRA_IS_LATITUDE", property.latitude)
            putExtra("EXTRA_IS_LONGITUDE", property.longitude)
            putExtra("EXTRA_ENTRY_DATE", property.marketEntryDate.time)
            property.saleDate?.let { date -> putExtra("EXTRA_SOLD_DATE", date.time) }
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
