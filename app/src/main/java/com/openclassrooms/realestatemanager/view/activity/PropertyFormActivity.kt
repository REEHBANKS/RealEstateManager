package com.openclassrooms.realestatemanager.view.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import io.grpc.android.BuildConfig
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PropertyFormActivity : AppCompatActivity() {
    private var selectedLatLng: LatLng? = null
    private var selectedType: String? = null
    private val selectedNearbyOptions = mutableListOf<String>()
    private var saleDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_form)

        // Initialize Places before creating the client
        if (!Places.isInitialized()) {
            Places.initialize(
                applicationContext,
                com.openclassrooms.realestatemanager.BuildConfig.API_KEY
            )
        }

        // Create a new Places client instance
        val placesClient = Places.createClient(this)

        // Setup autocomplete for address field
        setupAutoCompleteAddress(placesClient)

        val editTextSaleDate = findViewById<EditText>(R.id.editTextSaleDate)
        val switchSold = findViewById<SwitchMaterial>(R.id.switchSold)
        editTextSaleDate.visibility = if (switchSold.isChecked) View.VISIBLE else View.GONE

        setupTypeSpinner()
        setupSaleDate()

        val spinnerNearby = findViewById<EditText>(R.id.spinnerNearby)
        spinnerNearby.setOnClickListener {
            showNearbyOptionsDialog()
        }


        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            submitProperty()
        }
    }

    private fun setupAutoCompleteAddress(placesClient: PlacesClient) {
        // Implementation of the Place Autocomplete functionality
        val autocompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewAddress)
        autocompleteTextView.setOnClickListener {
            // Launch the autocomplete activity on address field click
            startAutocompleteActivity()
        }
    }

    private fun startAutocompleteActivity() {
        // Define the fields to request
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Start the autocomplete intent with the specified fields
        val intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    // Handle the results of the autocomplete activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                // When a place is selected from the autocomplete results
                val place = Autocomplete.getPlaceFromIntent(data)
                // Update the address field with the selected place's name
                findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewAddress).setText(place.name)
                // Store the latitude and longitude for later use
                selectedLatLng = place.latLng!!
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
                // Handle error responses
                val status = Autocomplete.getStatusFromIntent(data)
                Log.i("PropertyFormActivity", status.statusMessage ?: "Error occurred")
            }
        }
    }

    private fun setupTypeSpinner() {
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val types = arrayOf("flat", "loft", "duplex", "house")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                selectedType = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedType = null
            }
        }
    }

    private fun showNearbyOptionsDialog() {
        // Les options à afficher dans le dialogue
        val nearbyOptions = arrayOf("schools", "shops", "parks", "station")

        val checkedOptions = BooleanArray(nearbyOptions.size)


        AlertDialog.Builder(this)
            .setMultiChoiceItems(nearbyOptions, checkedOptions) { _, which, isChecked ->

                if (isChecked) {
                    selectedNearbyOptions.add(nearbyOptions[which])
                } else {
                    selectedNearbyOptions.remove(nearbyOptions[which])
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                updateNearbySelections()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun updateNearbySelections() {
        val nearbyEditText = findViewById<EditText>(R.id.spinnerNearby)
        val nearbyOptionsString = selectedNearbyOptions.joinToString(separator = ", ")
        nearbyEditText.text = Editable.Factory.getInstance().newEditable(nearbyOptionsString)
    }


    private fun setupSaleDate() {
        val switchSold = findViewById<SwitchMaterial>(R.id.switchSold)
        val editTextSaleDate = findViewById<EditText>(R.id.editTextSaleDate)

        switchSold.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Afficher le DatePickerDialog quand le bien est marqué comme vendu
                val calendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, year, monthOfYear, dayOfMonth ->
                        // Utilisez le format "yyyy-MM-dd"
                        val dateString = "$year-${monthOfYear + 1}-$dayOfMonth"
                        editTextSaleDate.setText(dateString)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
                editTextSaleDate.visibility = View.VISIBLE
            } else {
                editTextSaleDate.visibility = View.GONE
                editTextSaleDate.text.clear()
            }
        }
    }

    private fun getSaleDate(): Date? {
        val isSold = findViewById<Switch>(R.id.switchSold).isChecked
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE)

        return if (isSold) {
            val dateString = findViewById<EditText>(R.id.editTextSaleDate).text.toString()
            try {
                dateFormat.parse(dateString) // Convertit la chaîne de caractères en Date
            } catch (e: ParseException) {
                null // Retourner null si la chaîne ne peut pas être analysée
            }
        } else {
            null
        }
    }


    private fun submitProperty() {

        if (selectedType == null) {
            Toast.makeText(this, "Please select a property type.", Toast.LENGTH_LONG).show()
            return
        }


        val price = findViewById<EditText>(R.id.editTextPrice).text.toString().toIntOrNull()
        val rooms = findViewById<EditText>(R.id.editTextRooms).text.toString().toIntOrNull()
        val surface = findViewById<EditText>(R.id.editTextSurface).text.toString().toIntOrNull()
        val address =
            findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewAddress).text.toString()
        val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
        val isSold = findViewById<SwitchMaterial>(R.id.switchSold).isChecked
        if (isSold) {
            saleDate = getSaleDate()
        }
        val bathrooms = findViewById<EditText>(R.id.editTextBathRooms).text.toString().toIntOrNull()
        val neighborhood = findViewById<EditText>(R.id.editTextNeighborhood).text.toString()

        // Validation des données saisies
        if (price == null || rooms == null || surface == null || bathrooms == null) {
            Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_LONG)
                .show()
            return
        }


        val property = PropertyModels(
            type = selectedType!!,
            price = price,
            numberOfRooms = rooms,
            numberOfBathrooms = bathrooms,
            area = surface,
            address = address,
            neighborhood = neighborhood,
            fullDescription = description,
            status = isSold,
            nearbyPointsOfInterest = selectedNearbyOptions,
            saleDate = saleDate,
            latitude = selectedLatLng!!.latitude,
            longitude = selectedLatLng!!.longitude,
            agentId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
        )

        // Afficher les détails de l'objet PropertyModels dans Logcat
        Log.d("PropertyFormActivity", "Property Details: $property")

    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }


}
