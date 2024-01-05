package com.openclassrooms.realestatemanager.view.useCase

import com.openclassrooms.realestatemanager.data.models.PropertyModels
import com.openclassrooms.realestatemanager.data.repository.PropertyRepository

class AddPropertyUseCase(private val propertyRepository: PropertyRepository) {

    suspend fun addPropertyUseCase(property: PropertyModels): String? {
        return try {
            // Appel au repository pour ajouter la propriété
            val newPropertyId = propertyRepository.addProperty(property)
            // Renvoie l'ID de la nouvelle propriété si l'ajout est réussi
            newPropertyId
        } catch (e: Exception) {
            // Gérer l'exception et renvoyer null en cas d'échec
            null
        }
    }
}
