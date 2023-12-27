package com.openclassrooms.realestatemanager.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.view.adapter.PropertyImagesAdapter
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: PropertyDetailViewModel by viewModels()
    private lateinit var imagesAdapter: PropertyImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        observePicturesViewModel()
        observeAgentViewModel()

        val apiKey = BuildConfig.API_KEY


        // Retrieve data passed in the intent
        val id = intent.getStringExtra("EXTRA_ID") ?: ""
        val agentId = intent.getStringExtra("EXTRA_AGENT_ID") ?: ""
        val price = intent.getIntExtra("EXTRA_PRICE", 0)
        val type = intent.getStringExtra("EXTRA_TYPE") ?: ""
        val area = intent.getIntExtra("EXTRA_AREA", 0)
        val location = intent.getStringExtra("EXTRA_LOCATION") ?: ""
        val nearbyList = intent.getStringArrayListExtra("EXTRA_NEARBY") ?: arrayListOf()
        val nearbyText = nearbyList.joinToString(separator = ", ")
        val rooms = intent.getIntExtra("EXTRA_ROOMS", 0)
        val description = intent.getStringExtra("EXTRA_DESCRIPTION") ?: ""
        val isSold = intent.getBooleanExtra("EXTRA_IS_SOLD", false)
        val longitude = intent.getDoubleExtra("EXTRA_IS_LONGITUDE", 0.0)
        val latitude = intent.getDoubleExtra("EXTRA_IS_LATITUDE", 0.0)
        val entryTimestamp = intent.getLongExtra("EXTRA_ENTRY_DATE", -1L)
        val soldTimestamp = intent.getLongExtra("EXTRA_SOLD_DATE", -1L)

        // Use the data to configure TextViews
        findViewById<TextView>(R.id.priceTextView).text = getString(R.string.price_format, price)
        findViewById<TextView>(R.id.typeDetailPhoneTextView).text = type
        findViewById<TextView>(R.id.surfaceDetailPhoneTextView).text = area.toString()
        findViewById<TextView>(R.id.locationDetailPhoneTextView).text = location
        findViewById<TextView>(R.id.nearbyDetailPhoneTextView).text = nearbyText
        findViewById<TextView>(R.id.roomsDetailPhoneTextView).text = rooms.toString()
        findViewById<TextView>(R.id.descriptionDetailPhoneTextView).text = description
        val stateButton = findViewById<Button>(R.id.button_state)
        val dateEntryTextView = findViewById<TextView>(R.id.dateOfEntryDetailPhoneTextView)
        val dateSoldTextView = findViewById<TextView>(R.id.dateOfSoldDetailPhoneTextView)
        val mapImageView = findViewById<ImageView>(R.id.mapContainer)

        // Build URL  Google Maps Static API
        val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" +
                "$latitude,$longitude&zoom=18&size=600x600&markers=color:red%7C$latitude,$longitude&key=$apiKey"

        Glide.with(this)
            .load(staticMapUrl)
            .into(mapImageView)

        // Get Gallery picture
        viewModel.getPicturesForProperty(id)

        // Get Agent
        viewModel.getOneAgentWithId(agentId)

        // Format the date of entry and display it
        val dateEntryFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        if (entryTimestamp > 0) {
            val dateEntry = Date(entryTimestamp)
            val formattedEntryDate = dateEntryFormat.format(dateEntry)
            dateEntryTextView.text = formattedEntryDate
        } else {
            dateEntryTextView.text = "Entry date not available"
        }

        if (!isSold) {
            // Set state button as "SOLD" and update its background
            stateButton.apply {
                text = "SOLD"
                background = ContextCompat.getDrawable(this@DetailActivity, R.drawable.button_sold)
            }

            // Format and display the sold date
            if (soldTimestamp > 0) { // Ensure the timestamp is valid
                val date = Date(soldTimestamp)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(date)
                dateSoldTextView.text = formattedDate
            } else {
                dateSoldTextView.text = "Sold date not available"
            }
        } else {
            // Set state button as "FOR SALE" and update its background
            stateButton.apply {
                text = "FOR SALE"
                background =
                    ContextCompat.getDrawable(this@DetailActivity, R.drawable.button_for_sale)
            }
            dateSoldTextView.text = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_add -> {
                val intent = Intent(this, PropertyFormActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_edit -> {
                Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)


        val itemToHide = menu?.findItem(R.id.action_switch_fragment)



        itemToHide?.isVisible = false

        return true
    }


    private fun setupRecyclerView() {
        imagesAdapter = PropertyImagesAdapter(this)
        binding.photosRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagesAdapter
        }
    }

    private fun observePicturesViewModel() {
        viewModel.picturesLiveData.observe(this) { pictures ->
            imagesAdapter.submitList(pictures)
        }
    }

    private fun observeAgentViewModel() {
        viewModel.agentLiveData.observe(this) { agent ->
            if (agent != null) {
                findViewById<TextView>(R.id.agentNameTextView).text = agent.agentName
            }

        }
    }


}
