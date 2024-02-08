package com.openclassrooms.realestatemanager.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyBinding
import com.openclassrooms.realestatemanager.view.activity.DetailActivity
import com.openclassrooms.realestatemanager.view.activity.MainActivity
import com.openclassrooms.realestatemanager.view.adapter.PropertyAdapter
import com.openclassrooms.realestatemanager.viewmodel.ListPropertyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListPropertyFragment : Fragment() {
    private lateinit var binding: FragmentListRealEstatePropertyBinding
    private val viewModel: ListPropertyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {

        // Adapter initialization
        val adapter = PropertyAdapter(requireContext()) { property ->
            // Check for tablet mode by trying to find the detail container view

            val mainActivity = activity as? MainActivity
            val isTablet = mainActivity?.isTabletMode() ?: false

            Log.d("ListPropertyFragment", "isTabletMode: $isTablet")


            if (isTablet) {
                // Tablet mode: Pass necessary data to DetailFragment and display it
                val detailFragment = DetailFragment.newInstance(
                    propertyId = property.property.id,
                    agentId = property.property.agentId,
                    price = property.property.price,
                    type = property.property.type,
                    area = property.property.area,
                    location = property.property.address,
                    nearbyList = ArrayList(property.property.nearbyPointsOfInterest),
                    rooms = property.property.numberOfRooms,
                    description = property.property.fullDescription,
                    isSold = property.property.status,
                    latitude = property.property.latitude,
                    longitude = property.property.longitude,
                    entryDate = property.property.marketEntryDate.time,
                    soldDate = property.property.saleDate?.time ?: -1L
                )
                // Perform the fragment transaction to replace the detail container content with the DetailFragment
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.detail_container, detailFragment)
                    .commit()
                val detailContainer = view.findViewById<View>(R.id.detail_container)
                if (detailContainer != null) {
                    val visibility = when (detailContainer.visibility) {
                        View.VISIBLE -> "VISIBLE"
                        View.INVISIBLE -> "INVISIBLE"
                        View.GONE -> "GONE"
                        else -> "UNDEFINED"
                    }
                    Log.d("VisibilityCheck", "detail_container is $visibility")
                } else {
                    Log.d("VisibilityCheck", "detail_container not found")
                }


            } else {
                // Phone mode: Start DetailActivity with an Intent and pass all necessary data
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("EXTRA_ID", property.property.id)
                    putExtra("EXTRA_AGENT_ID", property.property.agentId)
                    putExtra("EXTRA_PRICE", property.property.price)
                    putExtra("EXTRA_TYPE", property.property.type)
                    putExtra("EXTRA_AREA", property.property.area)
                    putExtra("EXTRA_LOCATION", property.property.address)
                    putStringArrayListExtra("EXTRA_NEARBY", ArrayList(property.property.nearbyPointsOfInterest))
                    putExtra("EXTRA_ROOMS", property.property.numberOfRooms)
                    putExtra("EXTRA_DESCRIPTION", property.property.fullDescription)
                    putExtra("EXTRA_IS_SOLD", property.property.status)
                    putExtra("EXTRA_IS_LATITUDE", property.property.latitude)
                    putExtra("EXTRA_IS_LONGITUDE", property.property.longitude)
                    putExtra("EXTRA_ENTRY_DATE", property.property.marketEntryDate.time)
                    property.property.saleDate?.let { soldDate ->
                        putExtra("EXTRA_SOLD_DATE", soldDate.time)
                    }

                }
                startActivity(intent)
            }

        }

        binding.propertiesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.propertiesRecyclerView.adapter = adapter



        viewModel.convertedProperties.observe(viewLifecycleOwner) { convertedProperties ->
            adapter.submitList(convertedProperties.toList())
            adapter.notifyDataSetChanged()
        }


        viewModel.propertiesWithPicturesFiltered.observe(viewLifecycleOwner) { propertiesFilters ->

            adapter.submitList(propertiesFilters)
        }


        viewModel.isEuro.observe(viewLifecycleOwner) { isEuro ->
            // Pass the currency preference to the adapter
            adapter.updateCurrency(isEuro)
        }

        } else {

            Log.d("ListPropertyFragment", "Fragment is not added to the activity.")
        }

    }
}


