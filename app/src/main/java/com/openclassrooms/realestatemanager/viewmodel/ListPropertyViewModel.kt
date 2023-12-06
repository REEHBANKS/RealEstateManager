package com.openclassrooms.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository
import com.openclassrooms.realestatemanager.useCase.AddPropertyUseCase
import com.openclassrooms.realestatemanager.useCase.GetAllPropertiesWithMainPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListPropertyViewModel @Inject constructor(

    private val getAllPropertiesWithMainPictureUseCase: GetAllPropertiesWithMainPictureUseCase,
    private val addPropertyUseCase: AddPropertyUseCase
) : ViewModel() {

    val propertiesWithMainPicture = liveData(Dispatchers.IO) {
        val properties = getAllPropertiesWithMainPictureUseCase()
        emit(properties)
    }

    //TODO test


    fun maybeAddFakeProperties() {
        viewModelScope.launch {
            val properties = getAllPropertiesWithMainPictureUseCase()
            if (properties.isEmpty()) {
                addFakeProperties()
            }
        }
    }



    fun addFakeProperties() {
        viewModelScope.launch {
            fakePropertiesComplete.forEach { property ->
                addPropertyUseCase(property)
            }
        }
    }



    private val fakePropertiesComplete = listOf(
        PropertyModels(
            id = "1",
            type = "Maison",
            price = 500000,
            area = 200,
            numberOfRooms = 6,
            numberOfBathrooms = 2,
            fullDescription = "Grande maison avec un grand jardin et piscine.",
            address = "10 Rue de la Paix, 75007 Paris",
            neighborhood = "Paris 7ème ",
            nearbyPointsOfInterest = listOf("Parc du Champ de Mars", "École Militaire"),
            status = true, // Disponible
            marketEntryDate = Date(),
            saleDate = null,
            agentId = 3,
            latitude = 48.8566,
            longitude = 2.350987
        ),
        PropertyModels(
            id = "2",
            type = "Appartement",
            price = 350000,
            area = 85,
            numberOfRooms = 3,
            numberOfBathrooms = 1,
            fullDescription = "Appartement lumineux avec vue sur la ville.",
            address = "55 Avenue Montaigne, 75008 Paris",
            neighborhood = "Paris 8ème",
            nearbyPointsOfInterest = listOf("Champs-Élysées", "Théâtre des Champs-Élysées"),
            status = false, // Vendu
            marketEntryDate = Date(),
            saleDate = null,
            agentId = 4,
            latitude = 48.865938,
            longitude = 2.303718
        ),
        PropertyModels(
            id = "3",
            type = "Loft",
            price = 750000,
            area = 150,
            numberOfRooms = 4,
            numberOfBathrooms = 2,
            fullDescription = "Loft spacieux dans un quartier moderne.",
            address = "32 Rue de l'Université, 75007 Paris",
            neighborhood = "Paris 7ème",
            nearbyPointsOfInterest = listOf("Musée d'Orsay", "Université Paris Sciences et Lettres"),
            status = true, // Disponible
            marketEntryDate = Date(),
            saleDate = null,
            agentId = 5,
            latitude = 48.8602,
            longitude = 2.3284
        )
    )



}
