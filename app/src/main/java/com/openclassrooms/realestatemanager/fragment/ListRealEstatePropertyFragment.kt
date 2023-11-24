package com.openclassrooms.realestatemanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentListRealEstatePropertyBinding

class ListRealEstatePropertyFragment : Fragment() {

    // Déclaration du binding avec un type nullable et sans initialisation immédiate
    private var _binding: FragmentListRealEstatePropertyBinding? = null

    // Cette propriété est uniquement valide entre onCreateView et onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Initialisation du binding
        _binding = FragmentListRealEstatePropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Exemple d'utilisation du binding pour définir un gestionnaire de clic
        binding.buttonNavigate1.setOnClickListener {
            findNavController().navigate(R.id.action_listRealEstatePropertyFragment2_to_mapRealEstatePropertyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nettoyage du binding
        _binding = null
    }
}
