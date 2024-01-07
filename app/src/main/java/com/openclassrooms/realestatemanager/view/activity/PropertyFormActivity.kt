package com.openclassrooms.realestatemanager.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PhotoDescription
import com.openclassrooms.realestatemanager.data.models.modelFirebase.PropertyModels
import com.openclassrooms.realestatemanager.utils.ImageHelper
import com.openclassrooms.realestatemanager.utils.PhotoDetailsHelper
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter
import com.openclassrooms.realestatemanager.viewmodel.PropertyFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class PropertyFormActivity : AppCompatActivity() {
    private var selectedLatLng: LatLng? = null
    private var selectedType: String? = null
    private val selectedNearbyOptions = mutableListOf<String>()
    private var saleDate: Date? = null
    private lateinit var imageHelper: ImageHelper
    private lateinit var photoDetailsHelper: PhotoDetailsHelper
    private lateinit var photoAdapter: PhotoAdapter
    private val photos: MutableList<PhotoDescription> = mutableListOf()
    private var existingPhotos: List<PhotoDescription> = listOf()
    private val viewModel: PropertyFormViewModel by viewModels()
    private var mode:String? =""
    private var propertyId:String =""
    private var originalLatitude: Double? = null
    private var originalLongitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_form)

         mode = intent.getStringExtra("mode")
         propertyId = intent.getStringExtra("property_id").toString()

        if (mode == "edit") {
            viewModel.loadProperty(propertyId)


        }



        photoAdapter = PhotoAdapter(photos)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.isNestedScrollingEnabled = false;
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = photoAdapter

        observeViewModel()
        observeImageUploadViewModel()
        observePropertyDetails()


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
        setupImageHelper()

        val spinnerNearby = findViewById<EditText>(R.id.spinnerNearby)
        spinnerNearby.setOnClickListener {
            showNearbyOptionsDialog()
        }

        photoDetailsHelper = PhotoDetailsHelper(this)

        val buttonAddPhotos: Button = findViewById(R.id.buttonAddPhotos)
        buttonAddPhotos.setOnClickListener {
            tryToAddNewPhoto()
        }


        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            submitForm()
        }
    }

    private fun observePropertyDetails() {
        viewModel.propertyDetails.observe(this) { details ->
            details?.let {
                fillFormWithData(it.property)
                loadImages(it.pictures)
                existingPhotos = it.pictures
            }
        }
    }


    private fun fillFormWithData(property: PropertyModels) {
        findViewById<EditText>(R.id.editTextPrice).setText(property.price.toString())
        findViewById<EditText>(R.id.editTextRooms).setText(property.numberOfRooms.toString())
        findViewById<EditText>(R.id.editTextBathRooms).setText(property.numberOfBathrooms.toString())
        findViewById<EditText>(R.id.editTextSurface).setText(property.area.toString())
        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewAddress).setText(property.address)
        findViewById<EditText>(R.id.editTextDescription).setText(property.fullDescription)
        findViewById<SwitchMaterial>(R.id.switchSold).isChecked = property.status
        // Gérer la date de vente si la propriété est vendue
        if (property.status && property.saleDate != null) {
            saleDate = property.saleDate
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE)
            findViewById<EditText>(R.id.editTextSaleDate).setText(dateFormat.format(property.saleDate))
        }

        findViewById<EditText>(R.id.editTextBathRooms).setText(property.numberOfBathrooms.toString())
        findViewById<EditText>(R.id.editTextNeighborhood).setText(property.neighborhood)

        originalLatitude = property.latitude
        originalLongitude = property.longitude


    }

    private fun loadImages(pictures: List<PhotoDescription>) {
        photoAdapter.updatePhotos(pictures)
        photoAdapter.notifyDataSetChanged()

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ImageHelper.IMAGE_PICK_CODE -> {
                    Log.d("Activity", "onActivityResult: Image pick success")
                    imageHelper.handleGalleryResult(data)
                }


                AUTOCOMPLETE_REQUEST_CODE -> {
                    // Quand un lieu est sélectionné à partir des résultats de l'autocomplete
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    // Mettre à jour le champ d'adresse avec le nom du lieu sélectionné
                    findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewAddress).setText(
                        place.name
                    )
                    // Stocker la latitude et la longitude pour une utilisation ultérieure
                    selectedLatLng = place.latLng!!
                }
                // Vous pouvez ajouter d'autres cas pour d'autres `requestCode` si nécessaire
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
            // Gérer les erreurs pour l'activité d'autocomplete
            val status = Autocomplete.getStatusFromIntent(data)
            Log.i("PropertyFormActivity", status.statusMessage ?: "Error occurred")
        }
    }

    private fun setupImageHelper() {
        photoDetailsHelper = PhotoDetailsHelper(this)
        imageHelper = ImageHelper(this) { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                viewModel.uploadImage(inputStream)
            }
        }
    }

    private fun observeImageUploadViewModel() {
        viewModel.imageUploadLiveData.observe(this) { downloadUrl ->
            // Utiliser l'URL pour mettre à jour l'interface utilisateur ou pour la stocker dans PhotoDescription
            photoDetailsHelper.promptForPhotoCaption(this) { caption, isMainPhoto ->
                val photoDescription = PhotoDescription(
                    id = UUID.randomUUID().toString(),
                    propertyId = "", // Vous devez définir cela après avoir ajouté la propriété
                    uri = downloadUrl, // Ici, utilisez l'URL de téléchargement
                    description = caption,
                    isMain = isMainPhoto
                )
                photos.add(photoDescription)
                // Mettre à jour RecyclerView ou toute autre vue que vous utilisez pour afficher les images
                updateRecyclerViewWithNewPhoto(photoDescription)
            }
        }
    }


    private fun updateRecyclerViewWithNewPhoto(photoDescription: PhotoDescription) {

        photos.add(photoDescription)
        Log.d("PropertyFormActivity", "Before adding, photos.size=${photos.size}")
        photoAdapter.updatePhotos(photos)
        Log.d("PropertyFormActivity", "After adding, photos.size=${photos.size}")
    }


    private fun tryToAddNewPhoto() {
        if (photos.size >= 9) {
            Toast.makeText(this, "Maximum of 9 photos allowed", Toast.LENGTH_SHORT).show()
        } else {

            imageHelper.openGalleryForImage()
        }
    }

    private fun submitForm() {
        if (photos.size < 1) {
            Toast.makeText(this, "At least one photo is required", Toast.LENGTH_SHORT).show()
            return
        }
        submitProperty()

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
            latitude = selectedLatLng?.latitude ?: originalLatitude ?: 0.0,
            longitude = selectedLatLng?.longitude ?: originalLongitude ?: 0.0,
            agentId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
        )

        val currentDate = Calendar.getInstance().time // Obtenez la date actuelle
        val propertyWithDate = property.copy(marketEntryDate = currentDate)



        if (mode == "edit") {

            val newPhotos = photos.filter { newPhoto ->
                existingPhotos.none { existingPhoto -> existingPhoto.id == newPhoto.id }
            }
            viewModel.updateProperty(propertyId, property, newPhotos)
        } else {
            viewModel.submitPropertyAndPhotos(propertyWithDate, photos)
        }

    }

    private fun observeViewModel() {
        viewModel.operationStatus.observe(this) { status ->
            when (status) {
                PropertyFormViewModel.OperationStatus.SUCCESS -> {
                    Toast.makeText(
                        this,
                        "Property and photos added successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController(R.id.nav_host_fragment).popBackStack(
                        R.id.listRealEstatePropertyFragment2,
                        false
                    )

                }

                PropertyFormViewModel.OperationStatus.PROPERTY_FAILURE -> {
                    Toast.makeText(this, "Failed to add property", Toast.LENGTH_LONG).show()
                }

                PropertyFormViewModel.OperationStatus.PHOTOS_FAILURE -> {
                    Toast.makeText(this, "Failed to add photos", Toast.LENGTH_LONG).show()
                }

                null -> {
                    // Gérer le cas où status est null, si nécessaire
                    Toast.makeText(this, "Operation status is undefined", Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Cela couvrira tout autre cas non spécifié, y compris null
                    Toast.makeText(this, "Unexpected status", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }


}
