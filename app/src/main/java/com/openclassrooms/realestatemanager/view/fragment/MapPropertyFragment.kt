package com.openclassrooms.realestatemanager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapRealEstatePropertyBinding

class MapPropertyFragment : Fragment() {

    // Déclaration du binding avec un type nullable et sans initialisation immédiate
    private var _binding: FragmentMapRealEstatePropertyBinding? = null

    // Cette propriété est uniquement valide entre onCreateView et onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Initialisation du binding
        _binding = FragmentMapRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Utilisation du binding pour définir un gestionnaire de clic sur buttonNavigate2
        binding.buttonNavigate2.setOnClickListener {
            findNavController().navigate(R.id.action_mapRealEstatePropertyFragment_to_listRealEstatePropertyFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nettoyage du binding
        _binding = null
    }
}
