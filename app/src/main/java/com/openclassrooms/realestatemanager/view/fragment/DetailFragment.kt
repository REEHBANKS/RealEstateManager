package com.openclassrooms.realestatemanager.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.adapter.PropertyImagesAdapter
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        fun newInstance(
            propertyId: String,
            agentId: String,
            price: Int,
            type: String,
            area: Int,
            location: String,
            nearbyList: ArrayList<String>,
            rooms: Int,
            description: String,
            isSold: Boolean,
            latitude: Double,
            longitude: Double,
            entryDate: Long,
            soldDate: Long?
        ): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle().apply {
                putString("EXTRA_ID", propertyId)
                putString("EXTRA_AGENT_ID", agentId)
                putInt("EXTRA_PRICE", price)
                putString("EXTRA_TYPE", type)
                putInt("EXTRA_AREA", area)
                putString("EXTRA_LOCATION", location)
                putStringArrayList("EXTRA_NEARBY", nearbyList)
                putInt("EXTRA_ROOMS", rooms)
                putString("EXTRA_DESCRIPTION", description)
                putBoolean("EXTRA_IS_SOLD", isSold)
                putDouble("EXTRA_IS_LATITUDE", latitude)
                putDouble("EXTRA_IS_LONGITUDE", longitude)
                putLong("EXTRA_ENTRY_DATE", entryDate)
                soldDate?.let { putLong("EXTRA_SOLD_DATE", it) }
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: PropertyDetailViewModel
    private lateinit var imagesAdapter: PropertyImagesAdapter
    private var propertyId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initial du ViewModel  onCreateView et onViewCreated
        viewModel = ViewModelProvider(requireActivity())[PropertyDetailViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_detail, container, false)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePicturesViewModel()
        observeAgentViewModel()

        val apiKey = BuildConfig.API_KEY

        // Extraction
        arguments?.let { bundle ->
            propertyId = bundle.getString("EXTRA_ID") ?: ""
            val type = bundle.getString("EXTRA_TYPE", "")
            val agentId = bundle.getString("EXTRA_AGENT_ID") ?: ""
            val price = bundle.getInt("EXTRA_PRICE", 0)
            val area = bundle.getInt("EXTRA_AREA", 0)
            val location = bundle.getString("EXTRA_LOCATION", "")
            val nearbyList = bundle.getStringArrayList("EXTRA_NEARBY")?.joinToString(separator = ", ") ?: ""
            val rooms = bundle.getInt("EXTRA_ROOMS", 0)
            val description = bundle.getString("EXTRA_DESCRIPTION", "")
            val isSold = bundle.getBoolean("EXTRA_IS_SOLD", false)
            val latitude = bundle.getDouble("EXTRA_IS_LATITUDE", 0.0)
            val longitude = bundle.getDouble("EXTRA_IS_LONGITUDE", 0.0)
            val entryDate = bundle.getLong("EXTRA_ENTRY_DATE", -1L)
            val soldDate = bundle.getLong("EXTRA_SOLD_DATE", -1L)
            val isEuro = bundle.getBoolean("isEuro", true) // Assure-toi de passer cette donnée aussi si nécessaire

            val format = if (isEuro) {
                NumberFormat.getCurrencyInstance(Locale.FRANCE)
            } else {
                NumberFormat.getCurrencyInstance(Locale.US)
            }
            val formattedPrice = format.format(price)

            // Config view
            view.findViewById<TextView>(R.id.priceTextView).text = getString(R.string.price_format, formattedPrice)
            view.findViewById<TextView>(R.id.typeDetailPhoneTextView).text = type
            view.findViewById<TextView>(R.id.surfaceDetailPhoneTextView).text = area.toString()
            view.findViewById<TextView>(R.id.locationDetailPhoneTextView).text = location
            view.findViewById<TextView>(R.id.nearbyDetailPhoneTextView).text = nearbyList
            view.findViewById<TextView>(R.id.roomsDetailPhoneTextView).text = rooms.toString()
            view.findViewById<TextView>(R.id.descriptionDetailPhoneTextView).text = description

            val stateButton = view.findViewById<Button>(R.id.button_state)
            if (isSold) {
                stateButton.apply {
                    text = getString(R.string.sold)
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.button_sold)
                }
            } else {
                stateButton.apply {
                    text = getString(R.string.for_sale)
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.button_for_sale)
                }
            }

            val dateEntryTextView = view.findViewById<TextView>(R.id.dateOfEntryDetailPhoneTextView)
            val dateSoldTextView = view.findViewById<TextView>(R.id.dateOfSoldDetailPhoneTextView)
            dateEntryTextView.text = if (entryDate > 0) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                Date(entryDate)
            ) else getString(R.string.entry_date_not_available)
            dateSoldTextView.text = if (soldDate > 0) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(soldDate)) else "----"

            val mapImageView = view.findViewById<ImageView>(R.id.mapContainer)
            // Assure-toi d'avoir l'API Key pour charger l'image de la carte
            val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=18&size=600x600&markers=color:red%7C$latitude,$longitude&key=$apiKey"
            Glide.with(this).load(staticMapUrl).into(mapImageView)


            // Get Gallery picture
            viewModel.getPicturesForProperty(propertyId)

            // Get Agent
            viewModel.getOneAgentWithId(agentId)
        }
    }

    private fun setupRecyclerView() {
        imagesAdapter = PropertyImagesAdapter(requireContext())
        //  Be sure to use the fragment layout to find the RecyclerView
        val photosRecyclerView: RecyclerView? = view?.findViewById(R.id.photosRecyclerView)
        if (photosRecyclerView != null) {
            photosRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        if (photosRecyclerView != null) {
            photosRecyclerView.adapter = imagesAdapter
        }
    }

    private fun observePicturesViewModel() {
        viewModel.picturesLiveData.observe(viewLifecycleOwner) { pictures ->
            imagesAdapter.submitList(pictures)
        }
    }

    private fun observeAgentViewModel() {
        viewModel.agentLiveData.observe(viewLifecycleOwner) { agent ->
            if (agent != null) {
                view?.findViewById<TextView>(R.id.agentNameTextView)?.text = agent.agentName
            }
        }
    }




}