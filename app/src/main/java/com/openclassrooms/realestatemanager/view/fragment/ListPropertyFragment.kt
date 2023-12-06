package com.openclassrooms.realestatemanager.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyBinding
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


        val adaptor = PropertyAdapter(requireContext())
        binding.propertiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adaptor
        }


        viewModel.propertiesWithMainPicture.observe(viewLifecycleOwner) { properties ->
            adaptor.submitList(properties)
        }
    }
}


