package com.openclassrooms.realestatemanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyBinding


class ListRealEstatePropertyFragment : Fragment() {
    private lateinit var binding: FragmentListRealEstatePropertyBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListRealEstatePropertyBinding.inflate(inflater, container, false)

      /*  // Création de données fictives
        val properties = listOf(
            PropertyModels("Le Manoir"),
            PropertyModels("La Villa"),
            PropertyModels("Le Cottage"),
            PropertyModels("L'Appartement"),
            PropertyModels("Le Studio")
        )
*/
        // Configuration du RecyclerView
        binding.propertiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
       //     adapter = PropertyAdapter(properties,requireContext())
        }


        return binding.root
    }
}

