package com.openclassrooms.realestatemanager.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyBinding
import com.openclassrooms.realestatemanager.view.activity.DetailActivity
import com.openclassrooms.realestatemanager.view.adapter.PropertyAdapter
import com.openclassrooms.realestatemanager.viewmodel.ListPropertyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListPropertyFragment : Fragment() {
    private lateinit var binding: FragmentListRealEstatePropertyBinding
    private val viewModel: ListPropertyViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.maybeAddFakeProperties()


        val adaptor = PropertyAdapter(requireContext()) { property ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_ID", property.property.id)
            intent.putExtra("EXTRA_PRICE", property.property.price)
            intent.putExtra("EXTRA_TYPE", property.property.type)
            intent.putExtra("EXTRA_AREA", property.property.area)
            intent.putExtra("EXTRA_LOCATION", property.property.address)
            intent.putExtra("EXTRA_NEARBY", property.property.neighborhood)
            intent.putExtra("EXTRA_ROOMS", property.property.numberOfRooms)
            intent.putExtra("EXTRA_DESCRIPTION", property.property.fullDescription)
            intent.putExtra("EXTRA_IS_SOLD", property.property.status)
            intent.putExtra("EXTRA_IS_LATITUDE", property.property.latitude)
            intent.putExtra("EXTRA_IS_LONGITUDE", property.property.longitude)
            intent.putExtra("EXTRA_ENTRY_DATE", property.property.marketEntryDate.time)
            property.property.saleDate?.let { intent.putExtra("EXTRA_SOLD_DATE", it.time) }
            startActivity(intent)
        }
        binding.propertiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adaptor
        }


        viewModel.propertiesWithMainPicture.observe(viewLifecycleOwner) { properties ->
            adaptor.submitList(properties)
        }
    }
}


